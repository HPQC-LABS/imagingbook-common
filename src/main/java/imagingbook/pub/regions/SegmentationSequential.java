/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 *  image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2020 Wilhelm Burger, Mark J. Burge. All rights reserved. 
 * Visit http://imagingbook.com for additional details.
 *******************************************************************************/

package imagingbook.pub.regions;

import java.util.HashSet;

import ij.process.ByteProcessor;

/**
 * Binary region labeler based on a sequential labeling
 * algorithm. 
 * 
 * @author WB
 * @version 2020/12/20
 */
public class SegmentationSequential extends BinaryRegionSegmentation {

	private HashSet<LabelCollision> collisionMap = null;

	/**
	 * Creates a new sequential region labeling.
	 * TODO: needs REVISION!
	 * 
	 * @param ip the binary input image with 0 values for background pixels and values &gt; 0
	 * for foreground pixels.
	 */
	public SegmentationSequential(ByteProcessor ip) {
		this(ip, DEFAULT_NEIGHBORHOOD);
	}
	
	public SegmentationSequential(ByteProcessor ip, NeighborhoodType nh) {
		super(ip, nh);
	}

	@Override
	protected boolean applySegmentation() {
		//IJ.log("Sequential region labeling - Step 1 + neighborhood=" + neighborhood);
		collisionMap = new HashSet<>();
		
		// Step 1: assign initial labels:
		resetLabel();
		for (int v = 0; v < height; v++) {
			for (int u = 0; u < width; u++) {
				if (getLabel(u, v) == FOREGROUND) {
					int label = makeLabel(u, v);
						//(neighborhood == NeighborhoodType.N4) ? makeLabel4(u, v) : makeLabel8(u, v);
					//IJ.log(String.format("assigning label %d at (%d,%d), maxLabel=%d", label, u, v, maxLabel));
					setLabel(u, v, label);
				}
			}
		}
		
		// Step 2: resolve label collisions
		//IJ.log("Sequential region labeling - Step 2");
		int[] replacementTable = makeReplacementTable(getMaxLabel() + 1);
		
		// Step 3: relabel the image
		applyReplacementTable(replacementTable);
		//showLabelArray();
		return true;
	}
	
	private int makeLabel(int u, int v) {
		int newLabel = 0;
		int[] nh;
		//assemble the neighborhood nh (x is current position u,v):
		// 4-neighborhood:
		//       [1]
		//    [0][x]
		// 8-neighborhood:
		//    [1][2][3]
		//    [0][x]
		if (neighborhood == NeighborhoodType.N4) {
			nh = new int[2];
			nh[0] = getLabel(u - 1, v);  // gives 0 if no label is set or outside borders
			nh[1] = getLabel(u, v - 1);
		}
		else {
			nh = new int[4];
			nh[0] = getLabel(u - 1, v);  // gives 0 if no label is set or outside borders
			nh[1] = getLabel(u - 1, v - 1);
			nh[2] = getLabel(u, v - 1);
			nh[3] = getLabel(u + 1, v - 1);
		}

		if (allBackground(nh)) {
			//all neighbors in nh[] are empty, assign a new label:
			newLabel = this.getNextLabel(); 		
		} 
		else {	//at least one label in nh[] is not BACKGROUND
				//find minimum region label among neighbors
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < nh.length; i++) {
				int ni = nh[i];
				if (ni >= START_LABEL && ni < min)
					min = ni;
			}
			newLabel = min;

			//register label equivalence (collision)
			for (int i = 0; i < nh.length; i++) {
				int ni = nh[i];
				if (ni >= START_LABEL && ni != newLabel) {
					registerCollision(ni, newLabel);
				}
			}
		}
		return newLabel;
	}
	
	private boolean allBackground(int[] nh) {
		for (int i = 0; i < nh.length; i++) {
			if (nh[i] != BACKGROUND) {
				return false;
			}
		}
		return true;
	}

//	private int makeLabel8(int u, int v) {
//		int newLabel = 0;
//		//assemble the neighborhood n:
//		//
//		// [1][2][3]
//		// [0][x]
//		//
//		int[] nh = new int[4];
//		nh[0] = getLabel(u - 1, v);  // gives 0 if no label is set or outside borders
//		nh[1] = getLabel(u - 1, v - 1);
//		nh[2] = getLabel(u, v - 1);
//		nh[3] = getLabel(u + 1, v - 1);
//
//		if (   nh[0] == BACKGROUND
//			&& nh[1] == BACKGROUND
//			&& nh[2] == BACKGROUND
//			&& nh[3] == BACKGROUND) {
//			//all neighbors in n[] are empty, assign a new label:
//			newLabel = this.getNextLabel(); 		
//		} 
//		else {	//at least one label in n[] is not BACKGROUND
//				//find minimum region label among neighbors
//			int min = Integer.MAX_VALUE;
//			for (int i = 0; i < nh.length; i++) {
//				int ni = nh[i];
//				if (ni >= START_LABEL && ni < min)
//					min = ni;
//			}
//			newLabel = min;
//
//			//register label equivalence (collision)
//			for (int i = 0; i < nh.length; i++) {
//				int ni = nh[i];
//				if (ni >= START_LABEL && ni != newLabel) {
//					registerCollision(ni, newLabel);
//				}
//			}
//		}
//		return newLabel;
//	}
	
//	private int makeLabel4(int u, int v) {
//		int newLabel = 0;
//		//assemble the neighborhood n:
//		//
//		//    [1]
//		// [0][x]
//		//
//		int[] nh = new int[2];
//		nh[0] = getLabel(u - 1, v);  // gives 0 if no label is set or outside borders
//		nh[1] = getLabel(u, v - 1);
//
//		if (nh[0] == BACKGROUND && nh[1] == BACKGROUND) {
//			//all neighbors in n[] are empty, assign a new label:
//			newLabel = this.getNextLabel(); 		
//		} 
//		else {	//at least one label in n[] is not BACKGROUND
//				//find minimum region label among neighbors
//			int min = Integer.MAX_VALUE;
//			for (int i = 0; i < nh.length; i++) {
//				int ni = nh[i];
//				if (ni >= START_LABEL && ni < min)
//					min = ni;
//			}
//			newLabel = min;
//
//			//register label equivalence (collision)
//			for (int i = 0; i < nh.length; i++) {
//				int ni = nh[i];
//				if (ni >= START_LABEL && ni != newLabel) {
//					registerCollision(ni, newLabel);
//				}
//			}
//		}
//		return newLabel;
//	}

	private void registerCollision(int a, int b) {
		if (a != b) {
			LabelCollision c;
			if (a < b)
				c = new LabelCollision(a, b);
			else
				c = new LabelCollision(b, a);
			if (!collisionMap.contains(c))
				collisionMap.add(c);
		}
	}
	
	//---------------------------------------------------------------------------

	private int[] makeReplacementTable(int size) {
		int[] rTable = resolveCollisions(size);
		return cleanupReplacementTable(rTable);
	}
	
	/**
	 *  This is the core of the algorithm: The set of collisions (stored in map) 
	 *  is used to merge connected regions. Transitivity of collisions makes this 
	 *  a nontrivial task. The algorithm used here is a basic "Connected-Components 
	 *  Algorithm" as used for finding connected parts in undirected graphs 
	 *  (e.g. see Corman, Leiserson, Rivest: "Introduction to Algorithms", MIT Press, 
	 *  1995, p. 441). Here, the regions represent the nodes of the graph and the 
	 *  collisions are equivalent to the edges of the graph. The implementation is 
	 *  not particularly efficient, since the merging of sets is done by relabeling 
	 *  the entire replacement table for each pair of nodes. Still fast enough even 
	 *  for large and complex images.
	 *  
	 *  @param size size of the label set
	 *  @return replacement table
	 */
	private int[] resolveCollisions(int size) {
		
		// The table setNumber[i] indicates to which set the element i belongs:
		//   k == setNumber[e] means that e is in set k 
		int[] setNumber = new int[size];

		//Initially, we assign a separate set to each element e:
		for (int e = 0; e < size; e++) {
			setNumber[e] = e;
		}

		// Inspect all collisions c=(a,b) one by one [note that a<b]
		for (LabelCollision c: collisionMap) {	// collisionMap.keySet()
			int A = setNumber[c.a]; //element a is currently in set A
			int B = setNumber[c.b]; //element b is currently in set B
			//Merge sets A and B (unless they are the same) by
			//moving all elements of set B into set A
			if (A != B) {
				for (int i = 0; i < size; i++) {
					if (setNumber[i] == B)
						setNumber[i] = A;
				}
			}
		}
		return setNumber;
	}

	private int[] cleanupReplacementTable(int[] table) {
		if (table.length == 0) return table; // case of empty image
		// Assume the replacement table looks the following:
		// table = [0 1 4 4 4 6 6 8 3 3 ]
		//     i =  0 1 2 3 4 5 6 7 8 9
		// meaning that label 2 should be replaced by 4 etc.
		
		// First, figure out which of the original labels
		// are still used. For this we use an intermediate array "mark":
		int[] mark = new int[table.length];
		for (int i = 0; i < table.length; i++) {
			int k = table[i];
			if (k >= 0 && k < table.length)
				mark[k] = 1;
		}
		// Result:
		// mark = [1 1 0 1 1 0 1 0 1 0 ]
		//    i =  0 1 2 3 4 5 6 7 8 9
		
		// Now we assign new, contiguous labels in mark:
		int newLabel = START_LABEL;
		mark[BACKGROUND] = BACKGROUND;
		mark[FOREGROUND] = FOREGROUND;
		for (int i = START_LABEL; i < table.length; i++) {
			if (mark[i]>0) {
				mark[i] = newLabel;
				newLabel = newLabel + 1;
			}
		}
		// Result:
		// mark = [0 1 0 2 3 0 4 0 5 0 ]
		//    i =  0 1 2 3 4 5 6 7 8 9
		
		//Now modify the actual replacement table to reflect the new labels
		for (int i = 0; i < table.length; i++) {
			table[i] = mark[table[i]];
		}
        // table = [0 1 4 4 4 6 6 8 3 3 ]
        //              |             |
        //              V             V
		// table = [0 1 3 3 3 4 4 5 2 2 ]
		//     i =  0 1 2 3 4 5 6 7 8 9
		
		return table;
	}

	private void applyReplacementTable(int[] replacementTable){
		if (replacementTable != null && replacementTable.length > 0){
			for (int v = 0; v < height; v++) {
				for (int u = 0; u < width; u++) {
					int oldLb = getLabel(u, v);
					if (oldLb >= START_LABEL && oldLb < replacementTable.length){	
						setLabel(u, v, replacementTable[oldLb]);
					}
				}
			}
		}
	}
	
	/**
	 * This class represents a collision between two pixel labels a, b
	 */
	private class LabelCollision { 
		private final int a, b;

		LabelCollision(int label_a, int label_b) {
			a = label_a;
			b = label_b;
		}

		// This is important because hashCode determines how keys in the hashtable are
		// compared. It makes sure that the key is directly related to the values of a,b. 
		// Otherwise the key would be derived from the address of the Collision object.
		public int hashCode() {
			return a * b;	// TODO: change to 'long'?
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof LabelCollision) {
				LabelCollision coll = (LabelCollision) other;
				return (this.a == coll.a && this.b == coll.b);
			} else
				return false;
		}
	}

}


