package model.functionFilters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import model.Filter;

public class InverseFilter implements Filter {

    @Override
    public Image filterImage(Image oldImage) {
        WritableImage newImage = new WritableImage((int)oldImage.getWidth(), (int)oldImage.getHeight());
        PixelWriter writer = newImage.getPixelWriter();
        PixelReader reader =  oldImage.getPixelReader();
       for(int y=0; y< oldImage.getHeight(); y++) {
           for(int x=0; x< oldImage.getWidth(); x++) {
               Color old = reader.getColor(x,y);
               writer.setColor(x,y, Color.color(1-old.getRed(), 1-old.getGreen(), 1-old.getBlue()));
           }
       }
        return newImage;
    }
}
