package model.functionFilters;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class IdentityFunctionFilter extends AbstractFunctionFilter {
    @Override
    public Image filterImage(Image oldImage) {
        return oldImage;
    }

    @Override
    protected Color calculateColor(double red, double green, double blue) {
        return null;
    }
}
