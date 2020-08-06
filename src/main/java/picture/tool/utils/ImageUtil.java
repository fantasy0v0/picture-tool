package picture.tool.utils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

  public static boolean isImage(File file) {
    try {
      ImageIO.read(file);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

}
