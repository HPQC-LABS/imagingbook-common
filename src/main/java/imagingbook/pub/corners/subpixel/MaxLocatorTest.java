package imagingbook.pub.corners.subpixel;

import imagingbook.lib.math.Matrix;
import imagingbook.lib.settings.PrintPrecision;
import imagingbook.pub.corners.subpixel.MaxLocator.Method;

public class MaxLocatorTest {
	
	public static void main(String[] args) {
		
		PrintPrecision.set(8);
		float[] samples = {16,9,7,11,8,15,14,12,10}; // = s_0,...,s_8, result xyz = {-0.37500000, 0.05555556, 16.57638931}
//		float[] samples = {40229.785156f, 33941.535156f, 25963.150391f, 39558.175781f, 39078.843750f, 33857.863281f, 39861.664063f, 38746.250000f, 33652.839844f};

		for (Method m : MaxLocator.Method.values()) {
			MaxLocator interp = MaxLocator.getInstance(m); 
		
			if (interp != null) {
				System.out.println("interpolator = " + interp.getClass().getName());
				
				float[] xyz = interp.getInterpolatedMax(samples);
				if (xyz != null) {
					System.out.println("xyz = " + Matrix.toString(xyz));
				}
				else {
					System.out.println("*** Max could not be located! ***");
				}			
				System.out.println();
			}
		}
	}

}


// Results:
//interpolator = imagingbook.pub.corners.subpixel.MaxLocator$Quadratic1
//xyz = {-0.37500000, 0.05555556, 16.57638931}
//
//interpolator = imagingbook.pub.corners.subpixel.MaxLocator$Quadratic2
//xyz = {-0.38320211, 0.08748906, 16.59667587}
//
//interpolator = imagingbook.pub.corners.subpixel.MaxLocator$Quartic
//xyz = {-0.40573445, 0.11285823, 16.62036324}
//
//----------------------------------------------------------------------------
//
//interpolator = imagingbook.pub.corners.subpixel.MaxLocator$Quadratic1
//xyz = {0.00330453, -0.18836921, 40268.08984375}
//
//interpolator = imagingbook.pub.corners.subpixel.MaxLocator$Quadratic2
//xyz = {-0.02513363, -0.20850648, 40271.58203125}
//
//interpolator = imagingbook.pub.corners.subpixel.MaxLocator$Quartic
//*** Max could not be located! ***
