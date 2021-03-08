package model.convolutionFilters;


public class Kernel {
    public double[][] coefficients;
    public int anchorX;
    public int anchorY;

    public static Kernel getBlurKernel() {
        Kernel kernel = new Kernel();
        kernel.coefficients = new double[][] {
                {1,1,1},
                {1,1,1},
                {1,1,1}
        };
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        return kernel;
    }

    public static Kernel getGaussianBlurKernel() {
        Kernel kernel = new Kernel();
        kernel.coefficients = new double[][] {
                {0,1,0},
                {1,4,1},
                {0,1,0}
        };
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        return kernel;
    }

    public static Kernel getSharpenKernel() {
        Kernel kernel = new Kernel();
        kernel.coefficients = new double[][] {
                {0,-1,0},
                {-1,5,-1},
                {0,-1,0}
        };
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        return kernel;
    }

    public static Kernel getEdgeKernel() {
        Kernel kernel = new Kernel();
        kernel.coefficients = new double[][] {
                {0,-1,0},
                {0,1,0},
                {0,0,0}
        };
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        return kernel;
    }

    public static Kernel getEmbossKernel() {
        Kernel kernel = new Kernel();
        kernel.coefficients = new double[][] {
                {-1,0,1},
                {-1,1,1},
                {-1,0,1}
        };
        kernel.anchorX = 1;
        kernel.anchorY = 1;
        return kernel;
    }
}
