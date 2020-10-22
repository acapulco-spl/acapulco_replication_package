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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.ObjectArrayList;

/**
 * Parser of feature models in SXFM format.
 * The specifications of the format are available in:
 * http://gsd.uwaterloo.ca:8088/SPLOT/sxfm.html
 * 
 * @author Gustavo G. Pascual
 *
 */
public class SxfmParser {
	
	/*
	 * Start of XML document items
	 */
	private static final String ELEMENT_FEATURE_MODEL = "feature_model";
	private static final String ELEMENT_META = "meta";
	private static final String ELEMENT_DATA = "data";
	private static final String ELEMENT_FEATURE_TREE = "feature_tree";
	private static final String ELEMENT_CONSTRAINTS = "constraints";
	private static final String ATTRIBUTE_NAME = "name";
	private static final String DATA_DESCRIPTION = "description";
	private static final String DATA_CREATOR = "creator";
	private static final String DATA_EMAIL = "email";
	private static final String DATA_DATE = "date";
	private static final String DATA_DEPARTMENT = "department";
	private static final String DATA_ORGANIZATION = "organization";
	private static final String DATA_ADDRESS = "address";
	private static final String DATA_PHONE = "phone";
	private static final String DATA_WEBSITE = "website";
	private static final String DATA_REFERENCE = "reference";
	private static final String[] METADATA = {
		DATA_DESCRIPTION,
		DATA_CREATOR,
		DATA_EMAIL,
		DATA_DATE,
		DATA_DEPARTMENT,
		DATA_ORGANIZATION,
		DATA_ADDRESS,
		DATA_PHONE,
		DATA_WEBSITE,
		DATA_REFERENCE
	};
	/*
	 * End of XML document items 
	 */
	
	/*
	 * Start of parsing exceptions
	 */
	private static final String EXCEPTION_UNEXPECTED_ELEMENT = "Unexpected element: ";
	private static final String EXCEPTION_MANDATORY_ATTRIBUTE_NOT_FOUND = 
			"Mandatory attribute not found: ";
	private static final String EXCEPTION_REPEATED_DEFINITION = "Repeated definitions of " +
			"element: ";
	private static final String EXCEPTION_EMPTY_METADATA = "Empty metadata";
	private static final String EXCEPTION_UNKNOWN_METADATA = "Unknown metadata: ";
	private static final String EXCEPTION_MANDATORY_ELEMENT_NOT_FOUND = 
			"Mandatory element not found: ";
	private static final String EXCEPTION_EMPTY_FEATURE_TREE = "Empty feature tree";
	private static final String EXCEPTION_ROOT_FEATURE_NOT_FOUND = "Root feature not found";
	private static final String EXCEPTION_INVALID_FEATURE_TREE = "Invalid feature tree near ";
	private static final String EXCEPTION_INVALID_CONSTRAINT = "Invalid constraint near ";
	/*
	 * End of parsing exceptions
	 */
	
	/*
	 * Start of regular expression patterns
	 */
	private static final Pattern PATTERN_FEATURE =
			Pattern.compile("^(\\t*)(:[r|m|o])\\s+([^\\(]+)\\s*\\(([^\\(]+)\\)\\s*");
	private static final Pattern PATTERN_ROOT_FEATURE =
			Pattern.compile("^(\\t*):r\\s+([^\\(]+)\\s*\\(([^\\(]+)\\)\\s*");
	private static final Pattern PATTERN_MANDATORY_FEATURE =
			Pattern.compile("^(\\t*):m\\s+([^\\(]+)\\s*\\(([^\\(]+)\\)\\s*");
	private static final Pattern PATTERN_OPTIONAL_FEATURE =
			Pattern.compile("^(\\t*):o\\s+([^\\(]+)\\s*\\(([^\\(]+)\\)\\s*");
	private static final Pattern PATTERN_GROUP =
			Pattern.compile("^(\\t*):g.*\\[(\\d+),([\\d|\\*]+)\\]\\s*");
	private static final Pattern PATTERN_GROUP_FEATURE =
			Pattern.compile("^(\\t*):\\s+([^\\(]+)\\s*\\(([^\\(]+)\\)\\s*");
	private static final Pattern PATTERN_LINE = 
			Pattern.compile("^(\\t*).*");	
	/*
	 * End of regular expression patterns
	 */
	

	/**
	 * Parses an SXFM file
	 * @param xmlFile
	 * @return the feature model
	 * @throws javax.xml.parsers.ParserConfigurationException
	 * @throws org.xml.sax.SAXException
	 * @throws java.io.IOException
	 * @throws SxfmParserException
	 */
	public static FeatureModel parse(String xmlFile) throws ParserConfigurationException,
		SAXException, IOException, SxfmParserException  {
		
		FeatureModel fm = new FeatureModel();
		
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = db.parse(xmlFile);
		
		Element root = doc.getDocumentElement();
		
		// Check root
		if (!root.getNodeName().equals(ELEMENT_FEATURE_MODEL)) {
			throw new SxfmParserException(EXCEPTION_UNEXPECTED_ELEMENT + root.getNodeName());
		}
		
		// Get name attribute, which is mandatory
		if (!root.hasAttribute(ATTRIBUTE_NAME)) {
			throw new SxfmParserException(EXCEPTION_MANDATORY_ATTRIBUTE_NOT_FOUND +
					ATTRIBUTE_NAME);
		}
		fm.setName(root.getAttribute(ATTRIBUTE_NAME));
		
		// Get metadata, which is optional
		NodeList nl = root.getElementsByTagName(ELEMENT_META);
		if (nl.getLength() > 1) {
			throw new SxfmParserException(EXCEPTION_REPEATED_DEFINITION + ELEMENT_META); 
		} else if (nl.getLength() == 1) {
			// Generate list of allowed data
			ObjectArrayList<String> validData = new ObjectArrayList<String>();
			validData.add(METADATA);
			
			nl = ((Element) nl.item(0)).getElementsByTagName(ELEMENT_DATA);
			int length = nl.getLength();
			if (length == 0) {
				throw new SxfmParserException(EXCEPTION_EMPTY_METADATA);
			}
			for (int i = 0; i < length; i++) {
				Element dataElement = (Element) nl.item(i);
				if (!dataElement.hasAttribute(ATTRIBUTE_NAME)) {
					throw new SxfmParserException(EXCEPTION_MANDATORY_ATTRIBUTE_NOT_FOUND +
							ATTRIBUTE_NAME); 
				}
				String name = dataElement.getAttribute(ATTRIBUTE_NAME);
				
				// Check validity
				if (!validData.contains(name)) {
					throw new SxfmParserException(EXCEPTION_UNKNOWN_METADATA + name); 
				}
				
				// Add data to the FM
				String value = dataElement.getTextContent();
				if (name.equalsIgnoreCase(DATA_DESCRIPTION)) {
					fm.setDescription(value);
				} else if (name.equalsIgnoreCase(DATA_CREATOR)) {
					fm.setCreator(value);
				} else if (name.equalsIgnoreCase(DATA_EMAIL)) {
					fm.setEmail(value);
				} else if (name.equalsIgnoreCase(DATA_DATE)) {
					fm.setDate(value);
				} else if (name.equalsIgnoreCase(DATA_DEPARTMENT)) {
					fm.setDepartment(value);
				} else if (name.equalsIgnoreCase(DATA_ORGANIZATION)) {
					fm.setOrganization(value);
				} else if (name.equalsIgnoreCase(DATA_ADDRESS)) {
					fm.setAddress(value);
				} else if (name.equalsIgnoreCase(DATA_PHONE)) {
					fm.setPhone(value);
				} else if (name.equalsIgnoreCase(DATA_WEBSITE)) {
					fm.setWebsite(value);
				} else if (name.equalsIgnoreCase(DATA_REFERENCE)) {
					fm.setReference(value);
				}				
			}
		} // End of metadata
		
		// Parse feature tree
		nl = root.getElementsByTagName(ELEMENT_FEATURE_TREE);
		if (nl.getLength() == 0) {
			throw new SxfmParserException(EXCEPTION_MANDATORY_ELEMENT_NOT_FOUND +
					ELEMENT_FEATURE_TREE);
		} else if (nl.getLength() > 1) {
			throw new SxfmParserException(EXCEPTION_REPEATED_DEFINITION +
					ELEMENT_FEATURE_TREE);
		}
		
		if (!nl.item(0).hasChildNodes()) {
			throw new SxfmParserException(EXCEPTION_EMPTY_FEATURE_TREE);
		}
		
		nl = nl.item(0).getChildNodes();
		StringBuffer featureTree = new StringBuffer();
		int length = nl.getLength();
		for (int i = 0; i < length; i++) {
			if (nl.item(i).getNodeType() == Node.TEXT_NODE) {
				featureTree.append(nl.item(i).getNodeValue() + "\n");
			}
		}
		
		parseFeatureTree(featureTree.toString(), fm);
		
		// Parse cross-tree constraints (the tag is mandatory)
		nl = root.getElementsByTagName(ELEMENT_CONSTRAINTS);
		if (nl.getLength() == 0) {
			throw new SxfmParserException(EXCEPTION_MANDATORY_ELEMENT_NOT_FOUND +
					ELEMENT_CONSTRAINTS);
		} else if (nl.getLength() > 1) {
			throw new SxfmParserException(EXCEPTION_REPEATED_DEFINITION +
					ELEMENT_CONSTRAINTS);
		}
		
		if (nl.item(0).hasChildNodes()) {
			nl = nl.item(0).getChildNodes();
			StringBuffer constraints = new StringBuffer();
			length = nl.getLength();
			for (int i = 0; i < length; i++) {
				if (nl.item(i).getNodeType() == Node.TEXT_NODE) {
					constraints.append(nl.item(i).getNodeValue() + "\n");
				}
			}
			parseConstraints(constraints.toString(), fm);
		}
		
		return fm;
	}
	
	private static String getNextLine(BufferedReader reader) {
		String line = null;
		try {
			do {
				line = reader.readLine();
			} while (line != null && line.length() == 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return line;
	}
	
	private static String[] splitTextBlock(String block) {
		BufferedReader reader = new BufferedReader(new StringReader(block));
		ObjectArrayList<String> lines = new ObjectArrayList<String>();
		String line;
		do {
			line = getNextLine(reader);
			if (line != null) {
				lines.add(line);
			}
		} while (line != null);
	
		return (String[]) (lines.toArray(String.class));
	}
	
	private static void parseConstraints(String constraintsList, FeatureModel fm)
			throws SxfmParserException {
		String[] constraints = splitTextBlock(constraintsList);
		for (String c : constraints) {
			c = c.toLowerCase();
			String[] cParts = c.split(":\\s*");
			if (cParts.length != 2) {
				throw new SxfmParserException(EXCEPTION_INVALID_CONSTRAINT + c);
			}
			IntArrayList positive = new IntArrayList();
			IntArrayList negative = new IntArrayList();
			String[] features = cParts[1].split("\\s+or\\s+");
			for (int i = 0; i < features.length; i++) {
				features[i] = features[i].trim();
				boolean neg = features[i].startsWith("~");
				
				String name =  neg ? features[i].substring(1) : features[i];
				int id = fm.getID(name);
				if (id == 0) {
					throw new SxfmParserException(EXCEPTION_INVALID_CONSTRAINT + c);
				}
				if (neg) {
					negative.add(id);
				} else {
					positive.add(id);
				}
			}
			fm.addCrossTreeConstraint(positive, negative);
		}
	}
	
	private static void parseFeatureTree(String featureTree, FeatureModel fm)
			throws SxfmParserException {
		String[] tree = splitTextBlock(featureTree);
		try {
			parseFeatureTreeLine(tree, 0, fm, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses a line of the feature tree and updates the feature model
	 * @param tree
	 * @param lineIndex
	 * @param fm
	 * @param parentFeature
	 * @return next line to parse
	 * @throws java.io.IOException
	 * @throws SxfmParserException
	 */
	private static int parseFeatureTreeLine(String[] tree, int lineIndex,
			FeatureModel fm, int parentFeature) throws IOException, SxfmParserException {
		if (parentFeature == 0) {
			Matcher m = PATTERN_ROOT_FEATURE.matcher(tree[lineIndex]);
			if (!m.matches()) {
				throw new SxfmParserException(EXCEPTION_ROOT_FEATURE_NOT_FOUND);
			}
			
			String sxfmID = m.group(3);
			int id = fm.addFeature(sxfmID, 0, true);
			return parseFeatureTreeLine(tree, lineIndex + 1, fm, id); 
		} else {
			int depth = getDepth(tree[lineIndex]);
			do {
				
				// Process elements in the current depth and add them as children of the
				// parent feature
				Matcher m = PATTERN_FEATURE.matcher(tree[lineIndex]);
				if (m.matches()) {
					Matcher mMandatory = PATTERN_MANDATORY_FEATURE.matcher(tree[lineIndex]);
					Matcher mOptional = PATTERN_OPTIONAL_FEATURE.matcher(tree[lineIndex]);
					boolean mandatory = mMandatory.matches();
					boolean optional = mOptional.matches();
					if (!mandatory && !optional) {
						throw new SxfmParserException(EXCEPTION_INVALID_FEATURE_TREE);
					}
					String sfxmID = mandatory ? mMandatory.group(3) : mOptional.group(3);
					int id = fm.addFeature(sfxmID, parentFeature, mandatory);
					
					// If next lines are children of this feature, parse them
					if (lineIndex + 1 < tree.length &&
							getDepth(tree[lineIndex + 1]) > depth) {
						lineIndex = parseFeatureTreeLine(tree, lineIndex + 1, fm, id);
					} else {
						lineIndex++;
					}				
				} else {
					m = PATTERN_GROUP.matcher(tree[lineIndex]);
					if (m.matches()) {
						// Get the type of group
						// Assuming that the lower bound is 1
						String upperBound = m.group(3);
						int type = upperBound.equals("1") ? FeatureModel.TYPE_XOR :
							FeatureModel.TYPE_OR;
						
						// Get list of members of the group
						ObjectArrayList<String> members = new ObjectArrayList<String>();
						int groupDepth = depth + 1;
						lineIndex++;
						int i = lineIndex;
						do {
							m = PATTERN_GROUP_FEATURE.matcher(tree[i]);
							if (getDepth(tree[i]) == groupDepth && !m.matches()) {
								throw new SxfmParserException(EXCEPTION_INVALID_FEATURE_TREE + tree[lineIndex]);
							} else if (getDepth(tree[i]) == groupDepth) {
								members.add(m.group(3));
							}
							i++;
						} while (i < tree.length && getDepth(tree[i]) >= groupDepth);
						
						// Create the group of features in the FM
						String[] names = (String[]) members.toArray(String.class);
						int[] ids = fm.addFeatureGroup(names, parentFeature, type);
						
						// Parse each feature in the group
						i = 0;
						do {
							if ((lineIndex + 1) < tree.length &&
									getDepth(tree[lineIndex + 1]) > groupDepth) {
								lineIndex = parseFeatureTreeLine(tree, lineIndex + 1, fm, ids[i]);
							} else {
								lineIndex++;
							}
							i++;
						} while (lineIndex < tree.length && getDepth(tree[lineIndex]) > depth);
					} else {
						throw new SxfmParserException(EXCEPTION_INVALID_FEATURE_TREE  + tree[lineIndex]);
					}
				}
			} while (lineIndex < tree.length && getDepth(tree[lineIndex]) == depth);
		}
		
		return lineIndex;
	}
	
	private static int getDepth(String line) {
		Matcher m = PATTERN_LINE.matcher(line);
		m.matches();
		return m.group(1).length();
	}
}