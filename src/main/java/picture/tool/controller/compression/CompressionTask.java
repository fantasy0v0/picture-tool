package picture.tool.controller.compression;

import io.reactivex.rxjava3.core.Single;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

/**
 * 压缩任务
 */
public class CompressionTask {

  /**
   * 源文件
   */
  private final File originFile;

  private final BufferedImage originImage;

  private final FormatName formatName;

  /**
   * 任务状态
   */
  private ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.initial);

  private ObjectProperty<BufferedImage> compressedImage = new SimpleObjectProperty<>(null);

  public CompressionTask(File originFile, BufferedImage originImage, FormatName formatName) {
    this.originFile = originFile;
    this.originImage = originImage;
    this.formatName = formatName;
  }

  public static Single<CompressionTask> create(File file) {
    return Single.create(source -> {
      try {
        BufferedImage image = ImageIO.read(file);
        CompressionTask task = new CompressionTask(file, Objects.requireNonNull(image), FormatName.jpeg);
        source.onSuccess(task);
      } catch (Exception e) {
        source.onError(e);
      }
    });
  }
}
