package model.convolutionFilters;

public class BlurConvolutionFilter extends AbstractConvolutionFilter {

    public BlurConvolutionFilter() {
        kernel = new Kernel();
        kernel.coefficients = new double[][]{
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}};
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        off = 0.0;
        divisor = 9.0;
    }
}
