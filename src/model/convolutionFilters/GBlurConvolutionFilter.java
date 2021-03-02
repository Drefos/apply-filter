package model.convolutionFilters;

public class GBlurConvolutionFilter extends AbstractConvolutionFilter{

    public GBlurConvolutionFilter() {
        kernel = new Kernel();
        kernel.coefficients = new double[][]{
                {0, 1, 0},
                {1, 4, 1},
                {0, 1, 0}};
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        off = 0.0;
        divisor = 8.0;
    }
}
