package model.convolutionFilters;

public class EmbossConvolutionFilter extends AbstractConvolutionFilter {

    public EmbossConvolutionFilter() {
        kernel = new Kernel();
        kernel.coefficients = new double[][]{
                {-1, 0, 1},
                {-1, 1, 1},
                {-1, 0, 1}};
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        off = 0.0;
        divisor = 1.0;
    }
}
