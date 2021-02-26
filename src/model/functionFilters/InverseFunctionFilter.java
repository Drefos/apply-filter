package model.functionFilters;

import javafx.scene.paint.Color;

public class InverseFunctionFilter extends AbstractFunctionFilter {

    @Override
    protected Color calculateColor(double red, double green, double blue) {
        return Color.color(1-red, 1-green, 1-blue);
    }
}
