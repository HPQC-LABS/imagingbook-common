package imagingbook.lib.ij;

import java.awt.Polygon;

import ij.gui.Roi;
import ij.gui.PolygonRoi;
import ij.process.FloatPolygon;
import imagingbook.pub.geometry.basic.Pnt2d;

/**
 * This class defines static ROI-related utility methods
 * to interface with ImageJ.
 * 
 * @author WB
 *
 */
public class RoiUtils {
	
	/**
	 * Retrieves the outline of the specified ROI as an
	 * array of {@link Pnt2d} points with {@code float}
	 * coordinates. Note that unless the ROI is of type
	 * {@link PolygonRoi} only the corner points of the
	 * bounding box are returned.
	 * 
	 * @param roi the ROI
	 * @return the ROI's polygon coordinates
	 */
	public static Pnt2d[] getPolygonPointsFloat(Roi roi) {
		FloatPolygon pgn = roi.getFloatPolygon();
		Pnt2d[] pts = new Pnt2d[pgn.npoints];
		for (int i = 0; i < pgn.npoints; i++) {
			pts[i] = Pnt2d.from(pgn.xpoints[i], pgn.ypoints[i]);
		}
		return pts;
	}
	
	/**
	 * Retrieves the outline of the specified ROI as an
	 * array of {@link Pnt2d} points with {@code int}
	 * coordinates. Note that unless the ROI is of type
	 * {@link PolygonRoi} only the corner points of the
	 * bounding box are returned.
	 * 
	 * @param roi the ROI
	 * @return the ROI's polygon coordinates
	 */
	public static Pnt2d[] getPolygonPointsInts(Roi roi) {
		Polygon pgn = roi.getPolygon();
		Pnt2d[] pts = new Pnt2d[pgn.npoints];
		for (int i = 0; i < pgn.npoints; i++) {
			pts[i] = Pnt2d.from(pgn.xpoints[i], pgn.ypoints[i]);
		}
		return pts;
	}

}
