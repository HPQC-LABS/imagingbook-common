/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 *  image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2020 Wilhelm Burger, Mark J. Burge. All rights reserved. 
 * Visit http://imagingbook.com for additional details.
 *******************************************************************************/
package imagingbook.pub.color.image;

import static imagingbook.lib.math.Arithmetic.max;
import static imagingbook.lib.math.Arithmetic.min;

import java.util.Arrays;
import java.util.Random;

/**
 * Methods for converting between RGB and HLS color spaces.
 * @author W. Burger
 * @version 2021/09/24
*/
public class HsvConverter {
	
	public HsvConverter() {	
	}

	public float[] RGBtoHSV (int[] RGB) {
		int R = RGB[0], G = RGB[1], B = RGB[2]; // R,G,B in [0,255]
//		int cHi = Math.max(R,Math.max(G,B));	// highest component value
//		int cLo = Math.min(R,Math.min(G,B));	// lowest component value		
		int cHi = max(R, G, B);		// highest component value
		int cLo = min(R, G, B);		// lowest component value

		int cRng = cHi - cLo;				    // component range
		float H = 0, S = 0, V = 0;
		float cMax = 255.0f;

		// compute value V
		V = cHi / cMax;
		
		// compute saturation S
		if (cHi > 0)
			S = (float) cRng / cHi;

		// compute hue H
		if (cRng > 0) {	// hue is defined only for color pixels
			float rr = (float)(cHi - R) / cRng;
			float gg = (float)(cHi - G) / cRng;
			float bb = (float)(cHi - B) / cRng;
			float hh;
			if (R == cHi)                   // R is greatest component value
				hh = bb - gg;
			else if (G == cHi)              // G is greatest component value
				hh = rr - bb + 2;
			else                            // B is greatest component value
				hh = gg - rr + 4;
			if (hh < 0)
				hh = hh + 6;
			H = hh / 6;
		}
		return new float[] {H, S, V};
	}
	
	public int[] HSVtoRGB (float[] HSV) {
		float H = HSV[0], S = HSV[1], V = HSV[2];  // H,S,V in [0,1]
		float r = 0, g = 0, b = 0;
		float hh = (6 * H) % 6;                 
		int   c1 = (int) hh;                     
		float c2 = hh - c1;
		float x = (1 - S) * V;
		float y = (1 - (S * c2)) * V;
		float z = (1 - (S * (1 - c2))) * V;	
		switch (c1) {
			case 0: r = V; g = z; b = x; break;
			case 1: r = y; g = V; b = x; break;
			case 2: r = x; g = V; b = z; break;
			case 3: r = x; g = y; b = V; break;
			case 4: r = z; g = x; b = V; break;
			case 5: r = V; g = x; b = y; break;
		}
		//System.out.format("rgb= %s\n", Arrays.toString(new double[] {r*255, g*255, b*255}));
		int R = min(Math.round(r * 255), 255);
		int G = min(Math.round(g * 255), 255);
		int B = min(Math.round(b * 255), 255);
		return new int[] {R, G, B};
	}
	
	// -------------------------------------------------------------
	
	public static void main(String[] args) {		// TODO: move to unit tests!
		doCheck(new int[] {0, 0, 0});
		doCheck(new int[] {255, 255, 255});
		doCheck(new int[] {177, 0, 0});
		doCheck(new int[] {0, 177, 0});
		doCheck(new int[] {0, 0, 177});
		doCheck(new int[] {19, 3, 174});
		Random rd = new Random();
		for (int i = 0; i < 10000; i++) {
			int r = rd.nextInt(256);
			int g = rd.nextInt(256);
			int b = rd.nextInt(256);
			doCheck(new int[] {r, g, b});
		}
	}
	
	private static void doCheck(int[] rgb1) {
		HsvConverter hsvC = new HsvConverter();
//		System.out.println();
//		System.out.println("rgb1 = " + Arrays.toString(rgb1));
		float[] hsv = hsvC.RGBtoHSV(rgb1);
//		System.out.println("hsv = " + Arrays.toString(hsv));
		int[] rgb2 = hsvC.HSVtoRGB(hsv);
//		System.out.println("rgb2 = " + Arrays.toString(rgb2));
		if (rgb1[0] != rgb2[0] || rgb1[1] != rgb2[1] || rgb1[1] != rgb2[1]) {
			System.out.println("** problem with " + Arrays.toString(rgb1) + " -> " + Arrays.toString(rgb2));
		}
	}
	


}
