package picture.tool.controller.compression;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 压缩任务
 */
public class CompressionTask {

  /**
   * 源文件
   */
  private final File orginFile;

  /**
   * 任务状态
   */
  private ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.initial);

  private ObjectProperty<BufferedImage> originImage = new SimpleObjectProperty<>(null);

  private ObjectProperty<BufferedImage> compressedImage = new SimpleObjectProperty<>(null);

  public CompressionTask(File orginFile) {
    this.orginFile = orginFile;
  }
}
