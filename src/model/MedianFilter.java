package model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedianFilter implements Filter {

    private int imgWidth;
    private int imgHeight;
    private static final int SIZE = 3;
    private static final int ANCHOR = 1;
    public static final String NAME = "Median";

    @Override
    public Image filterImage(Image oldImage) {
        this.imgWidth = (int) oldImage.getWidth();
        this.imgHeight = (int) oldImage.getHeight();
        WritableImage newImage = new WritableImage(imgWidth, imgHeight);
        PixelWriter writer = newImage.getPixelWriter();
        PixelReader reader = oldImage.getPixelReader();
        for (int y = 0; y < imgHeight; y++) {
            for (int x = 0; x < imgWidth; x++) {
                writer.setColor(x, y, calculateColor(x, y, reader));
            }
        }
        return newImage;
    }

    private Color calculateColor(int x, int y, PixelReader reader) {
        double color;
        Color old;
        List<Double> colors = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                old = reader.getColor(bound(x + i - ANCHOR, 0, imgWidth - 1), bound(y + j - ANCHOR, 0, imgHeight - 1));
                colors.add(old.getRed());
            }
        }
        Collections.sort(colors);
        color = colors.get(4);
        return Color.color(adjust(color), adjust(color), adjust(color));
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
