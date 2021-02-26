package model.functionFilters;

import javafx.scene.image.Image;
import model.Filter;

public class IdentityFilter implements Filter {
    @Override
    public Image filterImage(Image oldImage) {
        return oldImage;
    }
}
