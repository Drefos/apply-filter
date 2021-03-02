package model.convolutionFilters;

public class SharpenConvolutionFilter extends AbstractConvolutionFilter {

    public SharpenConvolutionFilter() {
        kernel = new Kernel();
        kernel.coefficients = new double[][]{
                {0, -1, 0},
                {-1, 5, -1},
                {0, -1, 0}};
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        off = 0.0;
        divisor = 9.0;
    }
}
