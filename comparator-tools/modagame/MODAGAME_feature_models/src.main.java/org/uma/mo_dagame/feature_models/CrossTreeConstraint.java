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

import com.carrotsearch.hppc.IntArrayList;

/**
 * Represents an FM cross-tree constraint in CNF notation
 * @author Gustavo G. Pascual
 *
 */
public class CrossTreeConstraint {

	/**
	 * Length of the positive an negative features.
	 * It's just an slight optimization.
	 */
	private int mPositiveFeaturesSize, mNegativeFeaturesSize;
	
	/**
	 * Positive and negative features.
	 * Stored in arrays and lists in order to improve access performance
	 */
	private IntArrayList mPositiveFeatures, mNegativeFeatures;
	
	// Copy constructor to prevent external modifications of mutable properties
	public CrossTreeConstraint(CrossTreeConstraint c) {
		mPositiveFeaturesSize = c.mPositiveFeaturesSize;
		mNegativeFeaturesSize = c.mNegativeFeaturesSize;
		mPositiveFeatures = c.mPositiveFeatures.clone();
		mNegativeFeatures = c.mNegativeFeatures.clone();
	}
	
	/**
	 * Generates the constraint straight from the lists of features
	 * @param positiveFeatures
	 * @param negativeFeatures
	 */
	public CrossTreeConstraint(IntArrayList positiveFeatures, IntArrayList negativeFeatures) {
		mPositiveFeatures = positiveFeatures.clone();
		mNegativeFeatures = negativeFeatures.clone();
		mPositiveFeaturesSize = mPositiveFeatures.size();
		mNegativeFeaturesSize = mNegativeFeatures.size();
	}

	/**
	 * Checks whether a configuration satisfies this constraints
	 * @param configuration the configuration 
	 * @return true if the constraint is satisfied
	 */
	public boolean isSatisfied(Configuration configuration) {
		// If one feature of the configuration is contained in the positive features, the constraint is satisfied.
		
		int[] buffer = mPositiveFeatures.buffer;
		for (int i = 0; i < mPositiveFeaturesSize; i++) {
			if (configuration.contains(buffer[i])) {
				return true;
			}
		}
				
		// If it is not satisfied, we will check if any of the negative features is not found in configuration.
		buffer = mNegativeFeatures.buffer;
		for (int i = 0; i < mNegativeFeaturesSize; i++) {
			if (!configuration.contains(buffer[i])) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Returns the positive features of the constraint.
	 * If one of these features is included in the configuration,
	 * then the constraint is satisfied.
	 * @return the positive features
	 */
	public IntArrayList getPositiveFeatures() {
		return mPositiveFeatures;
	}
		
	/**
	 * Returns the negative features of the constraint.
	 * If at least one of these features is not included in the configuration,
	 * then the constraint is satisfied.
	 * @return the negative features
	 */
	public IntArrayList getNegativeFeatures() {
		return mNegativeFeatures;
	}
	
	public String toString() {
		return "+ " + mPositiveFeatures.toString() + " ; - " + mNegativeFeatures.toString();
	}
}
