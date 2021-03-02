package model.convolutionFilters;

public class HEdgeDetectionConvolutionFilter extends AbstractConvolutionFilter {

    public HEdgeDetectionConvolutionFilter() {
        kernel = new Kernel();
        kernel.coefficients = new double[][]{
                {0, -1, 0},
                {0, 1, 0},
                {0, 0, 0}};
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        off = 0.0;
        divisor = 1.0;
    }
}
