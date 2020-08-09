package picture.tool.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

public class ImageUtil {

  public static Image bufferedImageToImage(BufferedImage bufferedImage) {
    WritableImage wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
    PixelWriter pw = wr.getPixelWriter();
    for (int x = 0; x < bufferedImage.getWidth(); x++) {
      for (int y = 0; y < bufferedImage.getHeight(); y++) {
        pw.setArgb(x, y, bufferedImage.getRGB(x, y));
      }
    }
    return new ImageView(wr).getImage();
  }

}
