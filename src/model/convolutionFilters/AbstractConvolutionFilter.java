package model.convolutionFilters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class AbstractConvolutionFilter {

    protected double[][] kernel;
    protected int anchorX;
    protected int anchorY;
    protected double off;
    protected double divisor;

    public Image filterImage(Image oldImage) {
        WritableImage newImage = new WritableImage((int) oldImage.getWidth(), (int) oldImage.getHeight());
        PixelWriter writer = newImage.getPixelWriter();
        PixelReader reader = oldImage.getPixelReader();
        for (int y = 0; y < oldImage.getHeight(); y++) {
            for (int x = 0; x < oldImage.getWidth(); x++) {
                writer.setColor(x, y, calculateColor(x, y, reader));
            }
        }
        return newImage;
    }

    protected abstract Color calculateColor(int x, int y, PixelReader reader);

    protected double adjust(double n) {
        if (n < 0) return 0.0;
        if (n > 1) return 1.0;
        return n;
    }
}
