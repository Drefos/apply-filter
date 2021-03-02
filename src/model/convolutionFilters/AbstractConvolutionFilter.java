package model.convolutionFilters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class AbstractConvolutionFilter {

    protected Kernel kernel;
    protected double off;
    protected double divisor;
    protected int imgWidth;
    protected int imgHeight;

    public Image filterImage(Image oldImage) {
        this.imgWidth = (int) oldImage.getWidth();
        this.imgHeight = (int) oldImage.getHeight();
        WritableImage newImage = new WritableImage(imgWidth, imgHeight);
        PixelWriter writer = newImage.getPixelWriter();
        PixelReader reader = oldImage.getPixelReader();
        for (int y = 0; y < oldImage.getHeight(); y++) {
            for (int x = 0; x < oldImage.getWidth(); x++) {
                writer.setColor(x, y, calculateColor(x, y, reader));
            }
        }
        return newImage;
    }

    protected Color calculateColor(int x, int y, PixelReader reader) {
        double k, red = 0, green = 0, blue = 0;
        Color old;
        for (int i = 0; i < kernel.coefficients.length; i++) {
            for (int j = 0; j < kernel.coefficients[i].length; j++) {
                old = reader.getColor(bound(x + i - kernel.anchorX, 0, imgWidth - 1), bound(y + j - kernel.anchorY, 0, imgHeight - 1));
                k = kernel.coefficients[i][j];
                red += k * old.getRed();
                green += k * old.getGreen();
                blue += k * old.getBlue();
            }
        }
        red = red / divisor + off;
        green = green / divisor + off;
        blue = blue / divisor + off;
        return Color.color(adjust(red), adjust(green), adjust(blue));
    }

    protected int bound(int n, int min, int max) {
        if (n < min) return min;
        if (n > max) return max;
        return n;
    }

    protected double adjust(double n) {
        if (n < 0) return 0.0;
        if (n > 1) return 1.0;
        return n;
    }
}
