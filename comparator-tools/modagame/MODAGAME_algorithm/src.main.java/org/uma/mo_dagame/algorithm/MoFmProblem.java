/*
 * Copyright 2014 Gustavo García Pascual, Mónica Pinto and Lidia Fuentes
 *
 * This file is part of MO-DAGAME
 * *
 * MO-DAGAME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MO-DAGAME is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MO-DAGAME.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.uma.mo_dagame.algorithm;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntOpenHashSet;

import org.uma.mo_dagame.feature_models.Configuration;
import org.uma.mo_dagame.feature_models.CrossTreeConstraint;
import org.uma.mo_dagame.feature_models.FeatureModel;

import java.util.Collection;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;

/**
 * Multiobjective Feature Model Problem Model
 * Supports constraints violation measurement for standard MOEAs
 *
 * @author Gustavo G. Pascual
 */
public class MoFmProblem extends Problem {

    private static final long serialVersionUID = 1L;

    private FeatureModel mFeatureModel;
    private IntOpenHashSet mExcludedFeatures;
    private boolean[] mMinimize;
    private boolean mEvaluateConstraints;
    private Configuration mSeed;

    public MoFmProblem(FeatureModel fm, boolean[] minimize, boolean evaluateConstraints,
                       Configuration seed) {
        // Each feature is modelled as a boolean variable
        numberOfVariables_ = fm.size();
        length_ = new int[numberOfVariables_];
        for (int i = 0; i < numberOfVariables_; i++) {
            length_[i] = 1;
        }
        numberOfObjectives_ = fm.getNumberOfObjectives();
        numberOfConstraints_ = 2 * numberOfVariables_; // Not sure whether it's necessary
        problemName_ = "MO_FM";
        mFeatureModel = fm;
        mMinimize = minimize;
        mEvaluateConstraints = evaluateConstraints;
        solutionType_ = new BinarySolutionType(this);
        mSeed = seed;
    }

    public FeatureModel getFeatureModel() {
        return mFeatureModel;
    }

    public Configuration getSeed() {
        return mSeed;
    }

    /**
     * Converts a jMetal solution into an FM configuration
     *
     * @param solution jMetal solution
     * @return FM configuration
     */
    public Configuration getConfiguration(Solution solution) {
        // Map the configuration to an array of booleans
        Variable[] genes = solution.getDecisionVariables();
        boolean[] configuration = new boolean[genes.length];
        for (int i = 0; i < genes.length; i++) {
            configuration[i] = ((Binary) genes[i]).getIth(0);
        }

        return new Configuration(mFeatureModel, configuration);
    }

    /**
     * Converts an FM configuration into a jMetal solution
     *
     * @param configuration
     * @return jMetal solution
     */
    public Solution getSolution(Configuration configuration) {
        Solution s = new Solution(mFeatureModel.getNumberOfObjectives());
        s.setType(new BinarySolutionType(this));
        boolean[] config = configuration.getConfiguration();
        Binary[] vars = new Binary[config.length];
        for (int i = 0; i < config.length; i++) {
            vars[i] = new Binary(1);
            vars[i].setIth(0, config[i]);
        }
        s.setDecisionVariables(vars);
        return s;
    }

    private boolean featureCanBeIncluded(Integer feature, Configuration configuration) {
        boolean option1 = mFeatureModel.getXORGroupMembers(feature).size() == 0;
        boolean option2 = configuration.disjoint(mFeatureModel.getXORGroupMembers(feature));
        Integer parentFeature = mFeatureModel.getParent(feature);
        boolean parentSatisfied = parentFeature == 0 ||
                configuration.contains(parentFeature) ||
                featureCanBeIncluded(parentFeature, configuration);

        return (option1 || option2) && parentSatisfied;
    }

    /**
     * Transforms a configuration in order to satisfy the tree constraints.
     *
     * @param configuration Configuration to fix
     * @return A fixed configuration
     */
    protected void fixTreeConstraints(Configuration configuration,
                                      Configuration fixedConfiguration) {

        // If the initial configuration is empty, we have to add the root to trigger
        // the transformation process
        IntArrayList features = configuration.getConfigurationByID();
        final int size = features.size();
        if (size > 0) {
            final int[] buffer = features.buffer;
            int i = 0;
            do {
                if (!fixedConfiguration.contains(buffer[i])) {
                    includeFeature(buffer[i], configuration, fixedConfiguration);
                }
                i++;
            } while (i < size);
        } else {
            includeFeature(mFeatureModel.getRootFeature(), configuration, fixedConfiguration);
        }
    }

    private void excludeFeature(int feature) {
        mExcludedFeatures.add(feature);
        IntArrayList children = mFeatureModel.getChildren(feature);
        final int[] buffer = children.buffer;
        final int size = children.size();
        for (int i = 0; i < size; i++) {
            excludeFeature(buffer[i]);
        }
    }

    private void includeFeature(int feature, Configuration initialConfiguration,
                                Configuration transformedConfiguration) {

        if (!transformedConfiguration.contains(feature) &&
                featureCanBeIncluded(feature, transformedConfiguration)) {
            // Include the feature in the configuration
            transformedConfiguration.add(feature);

            // If the feature is in an XOR group, exclude the rest of the members
            // (traversing also their branches)
            IntOpenHashSet xorMembers = mFeatureModel.getXORGroupMembers(feature);
            final int[] keys = xorMembers.keys;
            final boolean[] allocated = xorMembers.allocated;
            for (int i = 0; i < allocated.length; i++) {
                if (allocated[i] && keys[i] != feature) {
                    excludeFeature(keys[i]);
                }
            }

            // Include the parent
            int parent = mFeatureModel.getParent(feature);
            if (parent != 0 && !transformedConfiguration.contains(parent)) {
                includeFeature(parent, initialConfiguration, transformedConfiguration);
            }

            IntArrayList children = mFeatureModel.getChildren(feature);
            if (children.size() > 0) {
                final int[] buffer = children.buffer;
                final int size = children.size();
                for (int i = 0; i < size; i++) {
                    IntOpenHashSet groupMembers;
                    if (mFeatureModel.getORGroupMembers(buffer[i]).size() > 0) {
                        groupMembers = mFeatureModel.getORGroupMembers(buffer[i]);
                    } else {
                        groupMembers = mFeatureModel.getXORGroupMembers(buffer[i]);
                    }

                    if (groupMembers.size() > 0 &&
                            transformedConfiguration.disjoint(groupMembers)) {
                        // The child is in a group and it is not included.
                        // We need to assure that, at least, one group's feature
                        // is added to the configuration.
                        // We have to select a feature in the group.
                        // We try to add one in the initial configuration to minimize
                        // the number of steps.
                        final int[] groupKeys = groupMembers.keys;
                        final boolean[] groupAllocated = groupMembers.allocated;
                        boolean included = false;
                        int j = 0;
                        do {
                            if (groupAllocated[j] && initialConfiguration.contains(groupKeys[j])) {
                                included = true;
                                includeFeature(groupKeys[j], initialConfiguration,
                                        transformedConfiguration);
                            }
                            j++;
                        } while (!included && j < groupAllocated.length);

                        if (!included) {
                            includeFeature(buffer[i], initialConfiguration,
                                    transformedConfiguration);
                        }
                    } else if (mFeatureModel.isMandatory(buffer[i])) {
                        includeFeature(buffer[i], initialConfiguration, transformedConfiguration);
                    }
                }
            }
        }
    }

    @Override
    public void evaluate(Solution solution) throws JMException {
        // We expect Variable[] to be an array of 1-sized binary variables
        if (!(solution.getType() instanceof BinarySolutionType)) {
            throw new JMException("Unexpected solution type");
        }

        Configuration configuration = getConfiguration(solution);
        double[] objectivesValues = configuration.getObjectivesValues();
        for (int i = 0; i < objectivesValues.length; i++) {
            // Note that algorithms minimize objective function
            solution.setObjective(i, mMinimize[i] ? objectivesValues[i] : 0 - objectivesValues[i]);
        }
    }

    @Override
    public void evaluateConstraints(Solution solution) throws JMException {
        if (!mEvaluateConstraints) {
            solution.setOverallConstraintViolation(0.0);
        } else {
            // Note that constraint violation is <= 0
            double constraintViolation = 0.0;

            // 1. Tree constraints violation
            mExcludedFeatures = new IntOpenHashSet();
            Configuration configuration = getConfiguration(solution);
            Configuration fixedConfiguration = new Configuration(mFeatureModel);
            fixTreeConstraints(configuration, fixedConfiguration);
            constraintViolation -= configuration.diff(fixedConfiguration);

            // 2. Cross-tree constraints violation
            Collection<CrossTreeConstraint> ctc = mFeatureModel.getCrossTreeConstraints();

            if (ctc.size() > 0) {
                int notSatisfiedCtc = 0;
                for (CrossTreeConstraint c : ctc) {
                    if (!c.isSatisfied(configuration)) {
                        notSatisfiedCtc++;
                    }
                }
                // Criterion: violating all CTCs is equivalent to violating all TCs
                constraintViolation -= (double) numberOfVariables_ * (double) notSatisfiedCtc /
                        (double) ctc.size();
            }

            solution.setOverallConstraintViolation(constraintViolation);
        }
    }
}
