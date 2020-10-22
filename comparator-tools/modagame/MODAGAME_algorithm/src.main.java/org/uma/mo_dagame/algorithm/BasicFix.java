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

import java.util.HashMap;
import java.util.List;

import jmetal.core.Operator;
import jmetal.util.JMException;

public class BasicFix extends Operator {

    private static final long serialVersionUID = 1L;

    private FeatureModel mFeatureModel;

    private IntOpenHashSet mExcludedFeatures;

    private int[][] mPrebuiltArrays;
    private static final int PREBUILT_MAX = 50;

    public static final String PARAMETER_FEATURE_MODEL = "featureModel";

    private XSRandom mRandom = new XSRandom(System.currentTimeMillis());

    public BasicFix(HashMap<String, Object> parameters) {
        super(parameters);

        if (parameters.get(PARAMETER_FEATURE_MODEL) != null) {
            mFeatureModel = (FeatureModel) parameters.get(PARAMETER_FEATURE_MODEL);
        }

        generatePrebuilts();

    }

    @Override
    public Object execute(Object object) throws JMException {
        Configuration configuration = (Configuration) object;
        Configuration fixedConfiguration = new Configuration(mFeatureModel);
        boolean success = fix(configuration, fixedConfiguration);
        return success ? fixedConfiguration : null;
    }


    private void generatePrebuilts() {
        mPrebuiltArrays = new int[PREBUILT_MAX][];
        for (int i = 0; i < PREBUILT_MAX; i++) {
            mPrebuiltArrays[i] = new int[i + 1];
            for (int j = 0; j < i + 1; j++) {
                mPrebuiltArrays[i][j] = j;
            }
        }
    }

    public boolean fix(Configuration configuration, Configuration fixedConfiguration) {

        mFeatureModel = configuration.getFeatureModel();

        fixedConfiguration.clear();
        mExcludedFeatures = new IntOpenHashSet();

        // Traverse features in random order.
        // This improves the probability of traversing different paths each time.
        //Collections.shuffle(configuration.getConfigurationByID());

        fixTreeConstraints(configuration, fixedConfiguration);
        return fixCrossTreeConstraints(configuration, fixedConfiguration);
    }

    private boolean fixCrossTreeConstraints(Configuration initialConfiguration,
                                            Configuration fixedConfiguration) {
        List<CrossTreeConstraint> ctcs = mFeatureModel.getCrossTreeConstraints();

        boolean fixable = true;
        int index = 0;
        int size = ctcs.size();

        if (size > 0) {
            int[] order = getRandomIterationOrder(size);

            while (index < size && fixable) {
                CrossTreeConstraint ctc = ctcs.get(order[index]);
                if (!ctc.isSatisfied(fixedConfiguration)) {
                    IntArrayList positive = ctc.getPositiveFeatures();

                    final int[] buffer = positive.buffer;
                    int ctcSize = positive.size();
                    if (ctcSize > 0) {
                        int[] iterationOrder = getRandomIterationOrder(ctcSize);
                        int i = 0;
                        boolean fixed = false;

                        while (!fixed && i < ctcSize) {
                            int i_ = iterationOrder[i];
                            if (featureCanBeIncluded(buffer[i_], fixedConfiguration)) {
                                includeFeature(buffer[i_], initialConfiguration,
                                        fixedConfiguration);
                                fixed = true;
                            } else {
                                i++;
                            }
                        }
                        fixable = fixed; //&& mRemainingResources >= 0;
                    } else {
                        fixable = false;
                    }
                    index = 0; // We may have affected previous CTCs
                } else {
                    index++;
                }
            }
        }

        return fixable;
    }

    private boolean featureCanBeIncluded(int feature, Configuration configuration) {
        int parentFeature = mFeatureModel.getParent(feature);
        return !mExcludedFeatures.contains(feature) && (parentFeature == 0 ||
                configuration.contains(parentFeature) ||
                featureCanBeIncluded(parentFeature, configuration));
    }

    /**
     * Transforms a configuration in order to satisfy the tree constraints.
     *
     * @param configuration Configuration to fix
     */
    private void fixTreeConstraints(Configuration configuration, Configuration fixedConfiguration) {

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

            // Always try different paths
            IntArrayList children = mFeatureModel.getChildren(feature);
            if (children.size() > 0) {
                final int[] buffer = children.buffer;
                final int size = children.size();
                int[] iterationOrder = getRandomIterationOrder(size);
                for (int i = 0; i < size; i++) {
                    int i_ = iterationOrder[i];
                    IntOpenHashSet groupMembers;
                    if (mFeatureModel.getORGroupMembers(buffer[i_]).size() > 0) {
                        groupMembers = mFeatureModel.getORGroupMembers(buffer[i_]);
                    } else {
                        groupMembers = mFeatureModel.getXORGroupMembers(buffer[i_]);
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
                            includeFeature(buffer[i_], initialConfiguration,
                                    transformedConfiguration);
                        }
                    } else if (mFeatureModel.isMandatory(buffer[i_])) {
                        includeFeature(buffer[i_], initialConfiguration, transformedConfiguration);
                    }
                }
            }
        }
    }

    private int[] getRandomIterationOrder(int size) {
        if (size <= PREBUILT_MAX) {
            shuffleArray(mPrebuiltArrays[size - 1]);
            return mPrebuiltArrays[size - 1];
        } else {
            int[] order = new int[size];
            for (int i = 0; i < size; i++) {
                order[i] = i;
            }
            shuffleArray(order);
            return order;
        }
    }

    // Modern Fisher-Yates shuffle
    private void shuffleArray(int[] ar) {

        for (int i = ar.length - 1; i > 0; i--) {
            int index = mRandom.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

}
