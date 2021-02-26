package model.functionFilters;

import javafx.scene.paint.Color;

public class ContrastFunctionFilter extends AbstractFunctionFilter {

    public static double CONTRAST_FACTOR = 1.1;

    protected Color calculateColor(double red, double green, double blue) {
        return Color.color(adjust((red-0.5)*CONTRAST_FACTOR + 0.5), adjust((green-0.5)*CONTRAST_FACTOR + 0.5), adjust((blue-0.5)*CONTRAST_FACTOR + 0.5));
    }
}
