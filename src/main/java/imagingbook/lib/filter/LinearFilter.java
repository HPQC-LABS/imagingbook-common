package imagingbook.lib.filter;

import imagingbook.lib.filter.kernel.Kernel2D;
import imagingbook.lib.image.access.PixelPack;
import imagingbook.lib.image.access.PixelPack.PixelSlice;

public class LinearFilter extends GenericFilterScalar {

	private final float[][] H;			// the kernel matrix, note H[y][x]!
	private final int xc, yc;			// 'hot spot' coordinates
	
	public LinearFilter(PixelPack pp, Kernel2D kernel) {
		super(pp);
		this.H = kernel.getH();
		this.xc = kernel.getXc();
		this.yc = kernel.getYc();
	}
	
	@Override
	protected float filterPixel(PixelSlice source, int u, int v) {
		double sum = 0;
		for (int j = 0; j < H.length; j++) {
			int vj = v + j - yc;
			for (int i = 0; i < H[j].length; i++) {
				int ui = u + i - xc;
				sum = sum + source.getVal(ui, vj) * H[j][i];
			}
		}
 		return (float)sum;
//		return convolve(source, H, u, v, xc, yc);
	}
	
	public static float convolve(PixelSlice source, float[][] H, int u, int v, int xc, int yc) {
		double sum = 0;
		for (int j = 0; j < H.length; j++) {
			int vj = v + j - yc;
			for (int i = 0; i < H[j].length; i++) {
				int ui = u + i - xc;
				sum = sum + source.getVal(ui, vj) * H[j][i];
			}
		}
 		return (float)sum;
	}

}
