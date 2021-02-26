package model.functionFilters;

import javafx.scene.paint.Color;

public class BrightnessFunctionFilter extends AbstractFunctionFilter {
    public static double BRIGHTNESS_FACTOR = 0.1;

    protected Color calculateColor(double red, double green, double blue) {
        return Color.color(adjust(red + BRIGHTNESS_FACTOR), adjust(green + BRIGHTNESS_FACTOR), adjust(blue + BRIGHTNESS_FACTOR));
    }
}
