package imagingbook.pub.geometry.fitting;

import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import imagingbook.pub.geometry.basic.Point;
import imagingbook.pub.geometry.mappings.linear.LinearMapping2D;

public class ProjectiveFit2D implements LinearFit2D {
	
	private final RealMatrix A;		// the calculated transformation matrix
	private final double err;		// the calculated error
	
	/**
	 * 
	 * @param P
	 * @param Q
	 */
	public ProjectiveFit2D(Point[] P, Point[] Q) {
		checkSize(P, Q);
		final int n = P.length;
		
		double[] ba = new double[2 * n];
		double[][] Ma = new double[2 * n][];
		for (int i = 0; i < n; i++) {
			double px = P[i].getX();
			double py = P[i].getY();
			double qx = Q[i].getX();
			double qy = Q[i].getY();
			ba[2 * i + 0] = qx;
			ba[2 * i + 1] = qy;
			Ma[2 * i + 0] = new double[] { px, py, 1, 0, 0, 0, -qx * px, -qx * py };
			Ma[2 * i + 1] = new double[] { 0, 0, 0, px, py, 1, -qy * px, -qy * py };
		}
		
		RealMatrix M = MatrixUtils.createRealMatrix(Ma);
		RealVector b = MatrixUtils.createRealVector(ba);
		DecompositionSolver solver = new SingularValueDecomposition(M).getSolver();
		RealVector h = solver.solve(b);
		A = MatrixUtils.createRealMatrix(3, 3);
		A.setEntry(0, 0, h.getEntry(0));
		A.setEntry(0, 1, h.getEntry(1));
		A.setEntry(0, 2, h.getEntry(2));
		A.setEntry(1, 0, h.getEntry(3));
		A.setEntry(1, 1, h.getEntry(4));
		A.setEntry(1, 2, h.getEntry(5));
		A.setEntry(2, 0, h.getEntry(6));
		A.setEntry(2, 1, h.getEntry(7));
		A.setEntry(2, 2, 1.0);
		
		err = this.calculateError(P, Q, A);
	}

	@Override
	public double[][] getTransformationMatrix() {
		return A.getData();
	}

	@Override
	public double getError() {
		return err;
	}
	
	// ------------------------------------------------------------------------------
	
	protected static void checkSize(Point[] P, Point[] Q) {
		if (P.length < 4 || Q.length < 4) {
			throw new IllegalArgumentException("At least 4 point pairs are required to calculate this fit");
		}
	}
	
	/**
	 * Calculates and returns the cumulative distance error
	 * between the two point sequences under the transformation A,
	 * i.e., {@code e = sum_i (||p_i * A - q_i||)}.
	 * @param P	the first point sequence
	 * @param Q the second point sequence
	 * @param A	a (3 x 3) projective transformation matrix
	 * @return the error {@code e}
	 */
	private double calculateError(Point[] P, Point[] Q, RealMatrix A) {
		int m = Math.min(P.length,  Q.length);
		LinearMapping2D map = new LinearMapping2D(A.getData());
		double errSum = 0;
		for (int i = 0; i < m; i++) {
			Point p = P[i];
			Point q = Q[i];
			Point pp = map.applyTo(p);
			errSum = errSum + Point.distance(q, pp);
		}
		return errSum;
	}

}
