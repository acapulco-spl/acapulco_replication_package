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

package org.uma.mo_dagame.feature_models;

import java.util.Arrays;
import java.util.List;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntOpenHashSet;


public class Configuration implements Comparable<Configuration> {

	private boolean[] mConfiguration;
	private IntArrayList mConfigurationByID;
	private FeatureModel mFeatureModel;
	
	public Configuration(FeatureModel fm, boolean[] configuration) {
		mFeatureModel = fm;
		mConfiguration = new boolean[configuration.length]; // Copy, avoid sharing references
		System.arraycopy(configuration, 0, mConfiguration, 0, configuration.length);
		mConfigurationByID = _convertConfiguration();
	}
	
	public Configuration(FeatureModel fm) {
		mFeatureModel = fm;
		mConfiguration = new boolean[fm.size()];
		mConfigurationByID = new IntArrayList();
	}
	
	public Configuration(Configuration config) {
		this(config.getFeatureModel(), config.getConfiguration());
	}
	
	public void copy(Configuration config) {
		mFeatureModel = config.getFeatureModel();
		boolean[] configuration = config.getConfiguration();
		mConfiguration = new boolean[configuration.length]; // Copy, avoid sharing references
		System.arraycopy(configuration, 0, mConfiguration, 0, configuration.length);
		mConfigurationByID = _convertConfiguration();
	}
	
	public FeatureModel getFeatureModel() {
		return mFeatureModel;
	}
	
	public void set(int index, boolean value) {
		if (mConfiguration != null && index < mConfiguration.length) {
			mConfiguration[index] = value;
			int id = mFeatureModel.getFeatures().get(index);
			if (value) {
				mConfigurationByID.add(id);
			} else {
				mConfigurationByID.remove(mConfigurationByID.indexOf(id));
			}
		}
	}
	
	public void add(int id) {
		int index = mFeatureModel.getFeatures().indexOf(id);
		if (index != -1 && !mConfiguration[index]) {
			mConfiguration[index] = true;
			mConfigurationByID.add(id);
		}
	}
	
	public void clear() {
		Arrays.fill(mConfiguration, false);
		mConfigurationByID.clear();
	}
	
	public boolean disjoint(IntOpenHashSet features) {
		final int[] keys = features.keys;
		final boolean[] allocated = features.allocated;
		
		boolean disjoint = true;
		int i = 0;
		while (disjoint && i < allocated.length) {
			if (allocated[i]) {
				disjoint = !mConfigurationByID.contains(keys[i]);
			}
			i++;
		}
		
		return disjoint;
	}
		
	public boolean[] getConfiguration() {
		return mConfiguration;
	}
	
	public IntArrayList getConfigurationByID() {
		return mConfigurationByID;
	}
	
	public boolean contains(int id) {
		return mConfigurationByID.contains(id);
	}
	
	public int size() {
		return mConfiguration.length;
	}
	
	public int diff(Configuration configuration2) {
		int diff = 0;
		if (configuration2.size() != mConfiguration.length) {
			diff = -1;
		} else {
			boolean[] config2 = configuration2.getConfiguration();
			for (int i = 0; i < mConfiguration.length; i++) {
				if (mConfiguration[i] != config2[i]) {
					diff++;
				}
			}
		}
		
		return diff;
	}
	
	/* Methods for managing data representation */
	
	private IntArrayList _convertConfiguration() {
		IntArrayList configuration = new IntArrayList();
		IntArrayList features = mFeatureModel.getFeatures();
		for (int i = 0; i < mConfiguration.length; i++) {
			if (mConfiguration[i]) {
				configuration.add(features.get(i));
			}
		}
		return configuration;
	}
	
	/* Methods for calculating optimization data */
	
	/**
	 * Calculates the input for each one of the objectives
	 * @return
	 */
	public double[] getObjectivesValues() {
		double[] objectivesValues = new double[mFeatureModel.getNumberOfObjectives()];
		Arrays.fill(objectivesValues, 0);
		
		final int[] buffer = mConfigurationByID.buffer;
		final int size = mConfigurationByID.size();
		for (int i = 0; i < size; i++) {
			double[] featureObjectives = mFeatureModel.getObjectivesValues(buffer[i]);
			for (int j = 0; j < featureObjectives.length; j++) {
				objectivesValues[j] += featureObjectives[j];
			}
		}
		
		return objectivesValues;
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();

		final int[] buffer = mConfigurationByID.buffer;
		final int size = mConfigurationByID.size();
		
		for (int i = 0; i < size; i++) {
			output.append(mFeatureModel.getName(buffer[i]));
			output.append(" ");
		}

		// Delete last blank space
		if (output.length() > 0) {
			output.deleteCharAt(output.length() - 1);
		}
		
		return output.toString();
	}

	/**
	 * Compares two configurations. The higher the added objectives' input,
	 * the greater the configuration.
	 */
	@Override
	public int compareTo(Configuration o) {
		double[] myObjectives = getObjectivesValues();
		double[] oObjectives = o.getObjectivesValues();
		int result = 0;
		double myValue = 0;
		double oValue = 0;
		
		for (int i = 0; i < myObjectives.length; i++) {
			myValue += myObjectives[i];
		}
		
		for (int i = 0; i < oObjectives.length; i++) {
			oValue += oObjectives[i];
		}
		
		if (myValue < oValue) {
			result = -1;
		} else if (myValue > oValue) {
			result = 1;
		}
		
		return result;
	}
	
	public boolean checkTreeConstraints() {
		int rootFeature = mFeatureModel.getRootFeature();

		IntOpenHashSet checkedFeatures = new IntOpenHashSet();
		return mConfigurationByID.contains(rootFeature)
				&& checkFeature(rootFeature, checkedFeatures)
				&& checkedFeatures.size() == mConfigurationByID.size();		
	}
	
	// Top-down checking
	private boolean checkFeature(int feature, IntOpenHashSet checkedFeatures) {
		checkedFeatures.add(feature);
		IntArrayList children = mFeatureModel.getChildren(feature);
		
		// Check parent-child constraints
		final int[] buffer = children.buffer;
		final int size = children.size();
		for (int i = 0; i < size; i++) {
			int c = buffer[i];

			if (mFeatureModel.isMandatory(c) && !mConfigurationByID.contains(c)) {
				return false;
			}

			IntOpenHashSet xorMembers = mFeatureModel.getXORGroupMembers(c);
			if (xorMembers.size() > 0) {
				int included = 0;
				final int[] groupKeys = xorMembers.keys;
				final boolean[] groupAllocated = xorMembers.allocated;
				for (int j = 0; j < groupAllocated.length; j++) {
					if (groupAllocated[j] && mConfigurationByID.contains(groupKeys[j])) {
						included++;
					}
				}
				if (included != 1) {
					return false;
				}
			}

			IntOpenHashSet orMembers = mFeatureModel.getORGroupMembers(c);
			if (orMembers.size() > 0) {
				boolean included = false;
				final int[] groupKeys = orMembers.keys;
				final boolean[] groupAllocated = orMembers.allocated;
				int j = 0;
				while (!included && j < groupAllocated.length) {
					included = groupAllocated[j] &&
							mConfigurationByID.contains(groupKeys[j]);
					j++;
				}

				if (!included) {
					return false;
				}
			}				
		}
				
		// Check each included child
		for (int i = 0; i < size; i++) {
			if (mConfigurationByID.contains(buffer[i])
					&& !checkFeature(buffer[i], checkedFeatures)) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean checkCrossTreeConstraints() {
		boolean satisfied = true;
		
		List<CrossTreeConstraint> ctcs = mFeatureModel.getCrossTreeConstraints();
		
		final int size = ctcs.size();
		int i = 0;
		
		while (satisfied && i < size) {
			satisfied = ctcs.get(i).isSatisfied(this);
			i++;
		}
				
		return satisfied;
	}
}
