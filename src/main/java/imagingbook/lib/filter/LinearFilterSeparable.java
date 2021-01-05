package imagingbook.lib.filter;

import imagingbook.lib.filter.kernel.Kernel1D;
import imagingbook.lib.image.access.PixelPack;
import imagingbook.lib.image.access.PixelPack.PixelSlice;

public class LinearFilterSeparable extends GenericFilterScalarSeparable {

	private final float[] hX, hY;			// the horizontal/vertical kernel arrays
	private final int width, height;		// width/height of the kernel
	private final int xc, yc;				// 'hot spot' coordinates
	
	public LinearFilterSeparable(PixelPack pp, Kernel1D kernelXY) {
		this(pp, kernelXY, kernelXY);
	}
	
	public LinearFilterSeparable(PixelPack pp, Kernel1D kernelX, Kernel1D kernelY) {
		super(pp);
		this.hX = kernelX.getH();
		this.hY = kernelY.getH();
		this.width = kernelX.getWidth();
		this.height = kernelY.getWidth();
		this.xc = kernelX.getXc();
		this.yc = kernelY.getXc();
	}

	// ------------------------------------------------------------------------

	// 1D convolution in x-direction
	@Override
	protected float filterPixelX(PixelSlice source, int u, int v) {
		final int vj = v; // - yc;
		double sum = 0;
		for (int i = 0; i < width; i++) {
			int ui = u + i - xc;
			sum = sum + source.getVal(ui, vj) * hX[i];
		}
		return (float)sum;
	}
	
	@Override
	// 1D convolution in y-direction
	protected float filterPixelY(PixelSlice source, int u, int v) {
		final int ui = u; // - xc;
		double sum = 0;
		for (int j = 0; j < height; j++) {
			int vj = v + j - yc;
			sum = sum + source.getVal(ui, vj) * hY[j];
		}
		return (float) sum;
	}

}
