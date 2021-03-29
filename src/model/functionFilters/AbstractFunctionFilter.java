package model.functionFilters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import model.Filter;

import java.util.Arrays;
import java.util.Collection;


public abstract class AbstractFunctionFilter implements Filter {
    public static final String INVERSE = "Inversion";
    public static final String BRIGHTNESS = "Brightness correction";
    public static final String CONTRAST = "Contrast enhancement";
    public static final String GAMMA = "Gamma correction";

    public static Collection<String> getFiltersNames() {
        return Arrays.asList(INVERSE, BRIGHTNESS, CONTRAST, GAMMA);
    }

    @Override
    public Image filterImage(Image oldImage) {
        WritableImage newImage = new WritableImage((int)oldImage.getWidth(), (int)oldImage.getHeight());
        PixelWriter writer = newImage.getPixelWriter();
        PixelReader reader =  oldImage.getPixelReader();
        for(int y=0; y< oldImage.getHeight(); y++) {
            for(int x=0; x< oldImage.getWidth(); x++) {
                Color old = reader.getColor(x,y);
                writer.setColor(x,y, calculateColor(old.getRed(), old.getGreen(), old.getBlue()));
            }
        }
        return newImage;
    }

    protected abstract Color calculateColor(double red, double green, double blue);

    protected double adjust(double n) {
        if(n<0) return 0.0;
        if(n>1) return 1.0;
        return n;
    }
}
