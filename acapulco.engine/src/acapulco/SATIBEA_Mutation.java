/*
 * Author : Christopher Henard (christopher.henard@uni.lu)
 * Date : 01/03/14
 * Copyright 2013 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
 * All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package acapulco;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.minisat.core.IOrder;
import org.sat4j.minisat.core.Solver;
import org.sat4j.minisat.orders.NegativeLiteralSelectionStrategy;
import org.sat4j.minisat.orders.PositiveLiteralSelectionStrategy;
import org.sat4j.minisat.orders.RandomLiteralSelectionStrategy;
import org.sat4j.minisat.orders.RandomWalkDecorator;
import org.sat4j.minisat.orders.VarOrderHeap;
import org.sat4j.reader.DimacsReader;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVecInt;
import org.sat4j.tools.ModelIterator;

import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;

/**
 *
 * @author chris
 */
public class SATIBEA_Mutation {

	private static Random r = new Random();
	private String fm;
	private int nFeat;
	private List<List<Integer>> constraints;

	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays.asList(BinarySolutionType.class, BinaryRealSolutionType.class,
			IntSolutionType.class, aCaPulCO_BinarySolution.class);

	private Double mutationProbability_ = null;

	private static final int SATtimeout = 1000;
	private static final long iteratorTimeout = 150000;

	/**
	 * Constructor Creates a new instance of the Bit Flip mutation operator
	 */
	public SATIBEA_Mutation(HashMap<String, Object> parameters, String fm, int nFeat,
			List<List<Integer>> constraints) {
		this.fm = fm;
		this.nFeat = nFeat;
		this.constraints = constraints;

	}

	/**
	 * Perform the mutation operation
	 *
	 * @param probability Mutation probability
	 * @param solution    The solution to mutate
	 * @throws JMException
	 */
	public void doMutation(Binary bin) throws JMException {

//            long t = System.currentTimeMillis();

		if (r.nextBoolean()) {

			boolean[] prod = randomProduct();

			for (int j = 0; j < prod.length; j++) {
				bin.setIth(j, prod[j]);

			}

//                System.out.println("Replace (ms) " + (System.currentTimeMillis() - t));
		} 

			HashSet<Integer> blacklist = new HashSet<Integer>();
			int violated = numViolatedConstraints(bin, blacklist);
			if (violated > 0) {
				IVecInt iv = new VecInt();

				for (int j = 0; j < aCaPulCO_Problem.numFeatures; j++) {
					int feat = j + 1;

					if (!blacklist.contains(feat)) {
						iv.push(bin.bits_.get(j) ? feat : -feat);
					}

				}

				boolean[] prod = randomProductAssume(iv);
				for (int j = 0; j < prod.length; j++) {
					bin.setIth(j, prod[j]);
				}
			}

//                System.out.println("Fix (ms) " + (System.currentTimeMillis() - t));
//		}

	} // doMutation

	public int numViolatedConstraints(Binary b) {

		// IVecInt v = bitSetToVecInt(b);
		int s = 0;
		for (List<Integer> constraint : constraints) {
			boolean sat = false;

			for (Integer i : constraint) {
				int abs = (i < 0) ? -i : i;
				boolean sign = i > 0;
				if (b.getIth(abs - 1) == sign) {
					sat = true;
					break;
				}
			}
			if (!sat) {
				s++;
			}

		}

		return s;
	}

	public int numViolatedConstraints(Binary b, HashSet<Integer> blacklist) {

		// IVecInt v = bitSetToVecInt(b);
		int s = 0;
		for (List<Integer> constraint : constraints) {
			boolean sat = false;

			for (Integer i : constraint) {
				int abs = (i < 0) ? -i : i;
				boolean sign = i > 0;
				if (b.getIth(abs - 1) == sign) {
					sat = true;
				} else {
					blacklist.add(abs);
				}
			}
			if (!sat) {
				s++;
			}

		}

		return s;
	}

	public int numViolatedConstraints(boolean[] b) {

		// IVecInt v = bitSetToVecInt(b);
		int s = 0;
		for (List<Integer> constraint : constraints) {

			boolean sat = false;

			for (Integer i : constraint) {
				int abs = (i < 0) ? -i : i;
				boolean sign = i > 0;
				if (b[abs - 1] == sign) {
					sat = true;
					break;
				}
			}
			if (!sat) {
				s++;
			}

		}

		return s;
	}

	public boolean[] randomProduct() {

		boolean[] prod = new boolean[nFeat];
		for (int i = 0; i < prod.length; i++) {
			prod[i] = r.nextBoolean();

		}

		int rand = r.nextInt(3);

		try {
			IOrder order;
			if (rand == 0) {
				order = new RandomWalkDecorator(new VarOrderHeap(new NegativeLiteralSelectionStrategy()), 1);
			} else if (rand == 1) {
				order = new RandomWalkDecorator(new VarOrderHeap(new PositiveLiteralSelectionStrategy()), 1);
			} else {
				order = new RandomWalkDecorator(new VarOrderHeap(new RandomLiteralSelectionStrategy()), 1);
			}

			// dimacsSolver.reset();
			ISolver dimacsSolver2 = SolverFactory.newMiniSATHeap();
			dimacsSolver2.setTimeout(SATtimeout);

			DimacsReader dr = new DimacsReader(dimacsSolver2);
			dr.parseInstance(new FileInputStream(new File(fm)));
			((Solver) dimacsSolver2).setOrder(order);

			ISolver solverIterator = new ModelIterator(dimacsSolver2);
			solverIterator.setTimeoutMs(iteratorTimeout);

			if (solverIterator.isSatisfiable()) {
				int[] i = solverIterator.findModel();

				for (int j = 0; j < i.length; j++) {
					int feat = i[j];

					int posFeat = feat > 0 ? feat : -feat;

					if (posFeat > 0) {
						prod[posFeat - 1] = feat > 0;
					}

//                    else
//                    {
//                         prod[nFeat-1] = r.nextBoolean();
//                    }
				}

			}

			// solverIterator = null;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		return prod;
	}

	public boolean[] randomProductAssume(IVecInt ivi) {

		boolean[] prod = new boolean[nFeat];
		for (int i = 0; i < prod.length; i++) {
			prod[i] = r.nextBoolean();

		}

		int rand = r.nextInt(3);

		try {
			IOrder order;
			if (rand == 0) {
				order = new RandomWalkDecorator(new VarOrderHeap(new NegativeLiteralSelectionStrategy()), 1);
			} else if (rand == 1) {
				order = new RandomWalkDecorator(new VarOrderHeap(new PositiveLiteralSelectionStrategy()), 1);
			} else {
				order = new RandomWalkDecorator(new VarOrderHeap(new RandomLiteralSelectionStrategy()), 1);
			}

			// dimacsSolver.reset();
			ISolver dimacsSolver2 = SolverFactory.newMiniSATHeap();
			dimacsSolver2.setTimeout(SATtimeout);

			DimacsReader dr = new DimacsReader(dimacsSolver2);
			dr.parseInstance(new FileInputStream(new File(fm)));
			((Solver) dimacsSolver2).setOrder(order);

			ISolver solverIterator = new ModelIterator(dimacsSolver2);
			solverIterator.setTimeoutMs(iteratorTimeout);

			if (solverIterator.isSatisfiable()) {
				int[] i = solverIterator.findModel(ivi);
				for (int j = 0; j < i.length; j++) {
					int feat = i[j];

					int posFeat = feat > 0 ? feat : -feat;

					if (posFeat > 0) {
						prod[posFeat - 1] = feat > 0;
					}

//                    else
//                    {
//                         prod[nFeat-1] = r.nextBoolean();
//                    }
				}

			}

			// solverIterator = null;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		return prod;
	}

} // BitFlipMutation
