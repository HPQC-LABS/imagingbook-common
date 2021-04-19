package imagingbook.lib.math;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Eigensolver2x2Test {

	@Test
	public void testEigensolver2x2A() {
		double[][] M = {
				{3, -2},
				{-4, 1}};
		runTest(M);
	}
	
	@Test
	public void testEigensolver2x2B() {
		double[][] M = {
				{-0.004710, -0.006970},
				{-0.006970, -0.029195}};
		runTest(M);
	}
	
	@Test
	public void testEigensolver2x2C() {
		double[][] M = {
				{0, 0},
				{0, 1}};
		runTest(M);
	}
	
	@Test
	public void testEigensolver2x2D() {
		double[][] M = {
				{1, 0},
				{0, 0}};
		runTest(M);
	}
	
	@Test
	public void testEigensolver2x2E() {
		double[][] M = {
				{1, 0},
				{-2, 1}};
		runTest(M);
	}
	
	@Test
	public void testEigensolver2x2F() {
		double[][] M = {
				{1, -2},
				{0, 1}};
		runTest(M);
	}
	
	@Test
	public void testEigensolver2x2G() {
		double[][] M = {
				{1, 2},
				{2, 1}};
		runTest(M);
	}
	
	
	private void runTest(double[][] M) {
		Eigensolver2x2 solver = new Eigensolver2x2(M);	
		assertTrue(solver.isReal());
		double[] eigenvals = solver.getEigenvalues();
		
		for (int k = 1; k < eigenvals.length; k++) {
			assertTrue(eigenvals[k-1] >= eigenvals[k]);
		}
		
		assertTrue(Matrix.normL2(eigenvals) > 0.01);
		
		for (int k = 0; k < eigenvals.length; k++) {
			double lambda = eigenvals[k];
			double[] x = solver.getEigenvector(k);
			// check: M * x = λ * x
			assertArrayEquals(Matrix.multiply(M, x), Matrix.multiply(lambda, x), 1E-6);
		}
	}

}
