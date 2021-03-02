package model.convolutionFilters;

public class IdentityConvolutionFilter extends AbstractConvolutionFilter {

    public IdentityConvolutionFilter() {
        kernel = new Kernel();
        kernel.coefficients = new double[][]{
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}};
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        off = 0.0;
        divisor = 1.0;
    }
}

