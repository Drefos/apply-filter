package model.functionFilters;

import javafx.scene.paint.Color;

public class GammaFunctionFilter extends AbstractFunctionFilter {

    public static double GAMMA_FACTOR = 1.1;

    protected Color calculateColor(double red, double green, double blue) {
        return Color.color(adjust(Math.pow(red, GAMMA_FACTOR)), adjust(Math.pow(green, GAMMA_FACTOR)), adjust(Math.pow(blue, GAMMA_FACTOR)));
    }
}
