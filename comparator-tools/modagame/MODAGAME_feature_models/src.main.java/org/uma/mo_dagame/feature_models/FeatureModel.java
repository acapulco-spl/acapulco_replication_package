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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.ObjectIntOpenHashMap;

public class FeatureModel {
	
	public static final int TYPE_OR = 1;
	public static final int TYPE_XOR = 2;

	/*
	 * Start of FM's metadata
	 * Based on SXFM specification
	 */
	
	/**
	 * Name of the feature model
	 */
	private String mName;
	
	private String mDescription;
	
	private String mCreator;
	
	private String mEmail;
	
	private String mDate;
	
	private String mDepartment;
	
	private String mOrganization;
	
	private String mAddress;
	
	private String mPhone;
	
	private String mWebsite;
	
	private String mReference;

	/*
	 * End of FM's metadata
	 */
	
	/**
	 * List of feature names
	 */	
	private IntObjectOpenHashMap<String> mFeatureNames;
		
	/**
	 * Inverted list of feature names
	 */
	private ObjectIntOpenHashMap<String> mNamesToID;
	
	/**
	 * List of features identifiers
	 */
	private IntArrayList mFeatures;
		
	/**
	 * The parent of each feature, 0 if it is the root feature
	 */
	private IntIntOpenHashMap mParents;
	
	/**
	 * Members of the OR group in which a feature is included (if applicable)
	 */
	private IntObjectOpenHashMap<IntOpenHashSet> mORGroupMembers;
	
	/**
	 * Members of the XOR group in which a feature is included (if applicable)
	 */	
	private IntObjectOpenHashMap<IntOpenHashSet> mXORGroupMembers;
	
	/**
	 * Mandatory features
	 */
	private IntOpenHashSet mMandatoryFeatures;
	
	/**
	 * Cross-tree constraints
	 */
	private List<CrossTreeConstraint> mCrossTreeConstraints;
		
	/**
	 * Objectives input of each feature
	 */
	private IntObjectOpenHashMap<double[]> mObjectivesValues;
	
	/**
	 * Number of objectives
	 */
	private int mNumberOfObjectives;
	
	private AtomicInteger mIDFactory = new AtomicInteger(1);
	
	/*
	 * The following fields are redundant but optimize the execution time
	 */
	private IntObjectOpenHashMap<IntArrayList> mChildren;
	
	/**
	 * Feature model constructor
	 * @param numberOfObjectives Number of objectives, or 0 in the case that no objectives are defined
	 */
	public FeatureModel() {
		mFeatures = new IntArrayList();
		mFeatureNames = new IntObjectOpenHashMap<String>();
		mNamesToID = new ObjectIntOpenHashMap<String>();
		mParents = new IntIntOpenHashMap();
		mNumberOfObjectives = 0;
		mMandatoryFeatures = new IntOpenHashSet();
		mORGroupMembers = new IntObjectOpenHashMap<IntOpenHashSet>();
		mXORGroupMembers = new IntObjectOpenHashMap<IntOpenHashSet>();
		mCrossTreeConstraints = new ArrayList<CrossTreeConstraint>();
		mChildren = new IntObjectOpenHashMap<IntArrayList>();
	}
	
	private int getNextID() {
		return mIDFactory.getAndIncrement();
	}

	/* Methods for modifying the feature model */

	/**
	 * Adds a feature to the feature model
	 * @param name Name of the feature
	 * @param parent ID of the parent, or 0 if it is the root feature
	 * @param mandatory True if the relationship is mandatory, false if it is optional
	 * @return the ID of the new feature
	 */
	public int addFeature(String name, int parent, boolean mandatory) {
		int id = getNextID();
		name = name.toLowerCase();
		mFeatures.add(id);
		mFeatureNames.put(id, name);
		mNamesToID.put(name, id);
		mParents.put(id, parent);
		if (parent != 0 && mandatory) {
			mMandatoryFeatures.add(id);
		}
		
		mChildren.put(id, new IntArrayList());
		if (parent != 0) {
			mChildren.get(parent).add(id);
		}
		
		mORGroupMembers.put(id, new IntOpenHashSet());
		mXORGroupMembers.put(id, new IntOpenHashSet());
		
		// Initialize objectives to 0
		if (mNumberOfObjectives > 0) {
			double[] objectivesInput = new double[mNumberOfObjectives];
			Arrays.fill(objectivesInput, 0);
			mObjectivesValues.put(id, objectivesInput);
		}
		
		return id;
	}
	
	public int[] addFeatureGroup(String[] names, int parent, int type) {
		int[] ids = new int[names.length];
		IntOpenHashSet groupMembers = new IntOpenHashSet();

		for (int i = 0; i < names.length; i++) {
			ids[i] = getNextID();
			String name = names[i].toLowerCase();
			groupMembers.add(ids[i]);
			mFeatures.add(ids[i]);
			mFeatureNames.put(ids[i], name);
			mNamesToID.put(name, ids[i]);
			mParents.put(ids[i], parent);
			
			// Initialize objectives to 0
			if (mNumberOfObjectives > 0) {
				double[] objectivesInput = new double[mNumberOfObjectives];
				Arrays.fill(objectivesInput, 0);
				mObjectivesValues.put(ids[i], objectivesInput);
			}
			
			mChildren.put(ids[i], new IntArrayList());
			mChildren.get(parent).add(ids[i]);
		}
		
		for (int id : ids) {
			if (type == TYPE_OR) {
				mORGroupMembers.put(id, groupMembers);
				mXORGroupMembers.put(id, new IntOpenHashSet());
			} else {
				mORGroupMembers.put(id, new IntOpenHashSet());
				mXORGroupMembers.put(id, groupMembers);
			}
		}
		
		return ids;
	}
	
	/**
	 * Adds a constraint in CNF notation
	 * @param positive positive features
	 * @param negative negative features
	 */
	public void addCrossTreeConstraint(IntArrayList positive, IntArrayList negative) {
		mCrossTreeConstraints.add(new CrossTreeConstraint(positive, negative));
	}
	
	/**
	 * Sets the objectives values of a feature in particular
	 * @param id ID of the feature
	 * @param objectivesValues objective values
	 */
	public void setObjectivesValues(int id, double[] objectivesValues) {
		if (mNumberOfObjectives != objectivesValues.length) {
			setNumberOfObjectives(objectivesValues.length);
		}
		
		mObjectivesValues.put(id, objectivesValues);
	}	
	
	/* 
	 * Start of methods to manage the FM's metadata
	 */
	
	/**
	 * Returns the name of the feature model
	 * @return
	 */
	public String getName() {
		return mName;
	}
	
	/**
	 * Sets the name of the feature model
	 * @param name
	 */
	public void setName(String name) {
		mName = name;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.mDescription = description;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return mCreator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.mCreator = creator;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return mEmail;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.mEmail = email;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return mDate;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.mDate = date;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return mDepartment;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.mDepartment = department;
	}

	/**
	 * @return the organization
	 */
	public String getOrganization() {
		return mOrganization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization) {
		this.mOrganization = organization;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return mAddress;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.mAddress = address;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return mPhone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.mPhone = phone;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return mWebsite;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.mWebsite = website;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return mReference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.mReference = reference;
	}

	/*
	 * Ends of methods to manage the FM's metadata 
	 */
	
	/**
	 * Gets the name of a feature
	 * @param id ID of the feature
	 * @return
	 */
	public String getName(int id) {
		return mFeatureNames.get(id);
	}
	
	public IntArrayList getFeatures() {
		return mFeatures;
	}
	
	public int getParent(int id) {
		return mParents.get(id);
	}
	
	public IntOpenHashSet getORGroupMembers(int id) {
		return mORGroupMembers.get(id);
	}
	
	public IntOpenHashSet getXORGroupMembers(int id) {
		return mXORGroupMembers.get(id);
	}
	
	public IntOpenHashSet getGroupMembers(int id) {
		return mORGroupMembers.get(id).size() > 0 ? mORGroupMembers.get(id) : mXORGroupMembers.get(id);
	}
	
	public boolean isMandatory(int id) {
		return mMandatoryFeatures.contains(id);
	}
	
	public List<CrossTreeConstraint> getCrossTreeConstraints() {
		return mCrossTreeConstraints;
	}
	
	public int size() {
		return mFeatures.size();
	}
	
	public int getRootFeature() {
		int rootFeature = 0;
		boolean rootFound = false;
		
		final int[] keys = mParents.keys;
		final int[] values = mParents.values;
		final boolean[] allocated = mParents.allocated;
		int i = 0;
		while (!rootFound && i < allocated.length) {
			if (allocated[i] && values[i] == 0) {
				rootFound = true;
				rootFeature = keys[i];
			} else {
				i++;
			}
		}
		
		return rootFeature;
	}
	
	public int getID(String name) {
		return mNamesToID.get(name);
	}
	
	public IntArrayList getChildren(int id) {
		return mChildren.get(id);
	}
		
	public double[] getObjectivesValues(int id) {
		return mNumberOfObjectives == 0 ? null : (double[]) mObjectivesValues.get(id);
	}
	
	public int getNumberOfObjectives() {
		return mNumberOfObjectives;
	}
	
	public void setNumberOfObjectives(int objectives) {
		mNumberOfObjectives = objectives;
		if (objectives > 0) {
			mObjectivesValues = new IntObjectOpenHashMap<double[]>();
			int[] buffer = mFeatures.buffer;
			int size = mFeatures.size();
			for (int i = 0; i < size; i++) {
				double[] objectivesInput = new double[mNumberOfObjectives];
				Arrays.fill(objectivesInput, 0);
				mObjectivesValues.put(buffer[i], objectivesInput);
			}
		} else {
			mObjectivesValues = null;
		}
	}
}
