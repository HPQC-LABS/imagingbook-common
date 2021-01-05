/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 *  image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2020 Wilhelm Burger, Mark J. Burge. All rights reserved. 
 * Visit http://imagingbook.com for additional details.
 *******************************************************************************/

package imagingbook.pub.edgepreservingfilters;

import static imagingbook.pub.edgepreservingfilters.BilateralF.gauss;
import static imagingbook.lib.math.Arithmetic.sqr;

import ij.process.ImageProcessor;
import imagingbook.lib.filter.GenericFilterScalar;
import imagingbook.lib.filter.linear.GaussianKernel2D;
import imagingbook.lib.image.access.PixelPack;
import imagingbook.lib.image.access.PixelPack.PixelSlice;
import imagingbook.pub.edgepreservingfilters.BilateralF.Parameters;

/**
 * Scalar version, applicable to all image types.
 * On color images, this filter is applied separately to each color component.
 * This class implements a bilateral filter as proposed in
 * C. Tomasi and R. Manduchi, "Bilateral Filtering for Gray and Color Images",
 * Proceedings of the 1998 IEEE International Conference on Computer Vision,
 * Bombay, India.
 * The filter uses Gaussian domain and range kernels and can be applied to all 
 * image types.
 * 
 * @author W. Burger
 * @version 2021/01/01
 */
public class BilateralFilterScalar extends GenericFilterScalar {
	
	private final float[][] Hd;	// the domain kernel
	private final int K;		// the domain kernel size [-K,...,K]
	private final double sigmaR2;
	
	public BilateralFilterScalar(ImageProcessor ip) {
		this(ip, new Parameters());
	}
	
	public BilateralFilterScalar(ImageProcessor ip, Parameters params) {
		super(PixelPack.fromImageProcessor(ip, params.obs));
		GaussianKernel2D kernel = new GaussianKernel2D(params.sigmaD);
		this.Hd = kernel.getH();
		this.K = kernel.getXc();
		this.sigmaR2 = sqr(params.sigmaR);
	}
	
	@Override
	protected float filterPixel(PixelSlice source, int u, int v) {
		float S = 0;			// sum of weighted pixel values
		float W = 0;			// sum of weights		
		float a = source.getVal(u, v); // value of the current center pixel
		
		for (int m = -K; m <= K; m++) {
			for (int n = -K; n <= K; n++) {
				float b = source.getVal(u + m, v + n);
				float wd = Hd[m + K][n + K];		// domain weight
				float wr = gauss(a - b, sigmaR2);	// range weight
				float w = wd * wr;
				S = S + w * b;
				W = W + w;
			}
		}
		return S / W;
	}

}
