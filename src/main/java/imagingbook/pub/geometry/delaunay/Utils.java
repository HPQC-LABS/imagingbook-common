package imagingbook.pub.geometry.delaunay;

import java.util.Collection;

import imagingbook.pub.geometry.basic.Point;

/**
 * This class defines static utility methods used by the Delaunay implementations.
 */
public abstract class Utils {
	
	/**
	 * Creates a 2D triangle that is sufficiently large to be used as 
	 * an outer triangle for the Delaunay triangulation of the given
	 * point set.
	 * @param points the 2D point set
	 * @return a triangle as an array of 3 points
	 */
	public static Point[] makeOuterTriangle(Collection<Point> points) {
		double xmin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymin = xmin;
		double ymax = xmax;
		
		for (Point p : points) {
			double x = p.getX();
			double y = p.getY();
			xmin = Math.min(x, xmin);
			xmax = Math.max(x, xmax);
			ymin = Math.min(y, ymin);
			ymax = Math.max(y, ymax);
		}
		return makeOuterTriangle(xmin, xmax, ymin, ymax);
	}
	
	/**
	 * Creates a 2D triangle that is sufficiently large to be used as 
	 * an outer triangle for the Delaunay triangulation of points 
	 * inside the given bounding rectangle.
	 * @param xmin minimum x-coordinate of the bounding rectangle
	 * @param xmax maximum x-coordinate of the bounding rectangle
	 * @param ymin minimum y-coordinate of the bounding rectangle
	 * @param ymax maximum y-coordinate of the bounding rectangle
	 * @return a triangle as an array of 3 points
	 */
	public static Point[] makeOuterTriangle(double xmin, double xmax, double ymin, double ymax) {
		double width = xmax - xmin;
		double height = ymax - ymin;
		double diam = Math.max(width,  height);
		double xc = xmin + width / 2;
		double yc = ymin + height / 2;
		double s = 50;
		return new Point[] {
				Point.create(xc, yc + s * diam),
				Point.create(xc + s * diam, yc),
				Point.create(xc - s * diam, yc - s * diam)
		};
	}
	
	/**
	 * Creates a 2D triangle that is sufficiently large to be used as 
	 * an outer triangle for the Delaunay triangulation of points 
	 * inside the given bounding rectangle, anchored at (0,0).
	 * @param width the width of the bounding rectangle
	 * @param height the height of the bounding rectangle
	 * @return a triangle as an array of 3 points
	 */
	public static Point[] makeOuterTriangle(double width, double height) {
		return makeOuterTriangle(0, width, 0, height);
	}

}
