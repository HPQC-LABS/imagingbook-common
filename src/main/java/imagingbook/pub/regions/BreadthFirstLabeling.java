/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 *  image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2020 Wilhelm Burger, Mark J. Burge. All rights reserved. 
 * Visit http://imagingbook.com for additional details.
 *******************************************************************************/

package imagingbook.pub.regions;

import java.util.LinkedList;
import java.util.Queue;

import ij.process.ByteProcessor;
import imagingbook.pub.geometry.basic.Point;

/**
 * Binary region labeler based on a breadth-first flood filling
 * algorithm using a stack.
 * Detected regions are 8-connected.
 * 
 * @author WB
 * @version 2020/04/01
 */
public class BreadthFirstLabeling extends RegionLabeling {
	
	/**
	 * Creates a new breadth-first (flood-fill) region labeling.
	 * 
	 * @param ip the binary input image with 0 values for background pixels and values &gt; 0
	 * for foreground pixels.
	 */
	public BreadthFirstLabeling(ByteProcessor ip) {
		super(ip);
	}
	
	@Override
	protected boolean applyLabeling() {
		resetLabel();
		for (int v = 0; v < height; v++) {
			for (int u = 0; u < width; u++) {
				if (getLabel(u, v) == FOREGROUND) {
					// start a new region
					int label = getNextLabel();
					//IJ.log(String.format("assigning label %d at (%d,%d), maxLabel=%d", label, u, v, maxLabel));
					floodFill(u, v, label);
				}
			}
		}
		return true;
	}

	private void floodFill(int u, int v, int label) {
		Queue<Point> Q = new LinkedList<Point>();	//queue contains pixel coordinates
		Q.add(Point.create(u, v));
		while (!Q.isEmpty()) {
			Point p = Q.remove();	// get the next point to process
			int x = (int) p.getX();
			int y = (int) p.getY();
			if ((x >= 0) && (x < width) && (y >= 0) && (y < height) && getLabel(x, y) == FOREGROUND) {
				setLabel(x, y, label);
				Q.add(Point.create(x + 1, y));
				Q.add(Point.create(x, y + 1));
				Q.add(Point.create(x, y - 1));
				Q.add(Point.create(x - 1, y));
				if (neighborhood == NeighborhoodType.N8) {
					Q.add(Point.create(x + 1, y + 1));
					Q.add(Point.create(x - 1, y + 1));
					Q.add(Point.create(x + 1, y - 1));
					Q.add(Point.create(x - 1, y - 1));
				}
			}
		}
	}

}
