package picture.tool.controller.compression;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Iterator;
import java.util.Objects;

/**
 * 压缩任务
 */
public class CompressionTask {

  /**
   * 源文件
   */
  public final File originFile;

  public final BufferedImage originImage;

  public final String formatName;

  /**
   * 任务状态
   */
  public final ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.compressing);

  public final ObjectProperty<BufferedImage> compressedImage = new SimpleObjectProperty<>(null);

  /**
   * 压缩后大小
   */
  public long compressedSize = 0;

  public CompressionTask(File originFile, BufferedImage originImage, String formatName) {
    this.originFile = originFile;
    this.originImage = originImage;
    this.formatName = formatName;
  }

  /**
   * @param file               源文件
   * @param compressionQuality a {@code float} between {@code 0} and {@code 1} indicating the desired quality level.
   */
  public static Single<CompressionTask> create(File file, float compressionQuality) {
    return Single.create(source -> {
      try {
        BufferedImage image = ImageIO.read(file);
        ImageInputStream iis = ImageIO.createImageInputStream(file);
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
        String formatName;
        if (imageReaders.hasNext()) {
          ImageReader reader = imageReaders.next();
          formatName = reader.getFormatName();
        } else {
          throw new RuntimeException("未知的图片类型");
        }
        CompressionTask task = new CompressionTask(file, Objects.requireNonNull(image), formatName);
        task.adjustQuality(compressionQuality);
        source.onSuccess(task);
      } catch (Exception e) {
        source.onError(e);
      }
    });
  }

  /**
   * @param quality a {@code float} between {@code 0} and {@code 1} indicating the desired quality level.
   */
  public void adjustQuality(float quality) {
    // 修改状态
    status.setValue(Status.compressing);
    Completable.create(emitter -> {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
      ImageWriter writer = writers.next();
      try (ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
        writer.setOutput(ios);
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(1 - quality);  // Change the quality value you prefer
        writer.write(null, new IIOImage(originImage, null, null), param);
        File tempFile = File.createTempFile(originFile.getName(), "_compression");
        try (ByteArrayInputStream bis = new ByteArrayInputStream(os.toByteArray())) {
          BufferedImage compressedImage = ImageIO.read(bis);
          if (!ImageIO.write(compressedImage, formatName, tempFile)) {
            throw new RuntimeException("写出压缩图片临时文件失败");
          }
          // fos.write(os.toByteArray());
          compressedSize = tempFile.length();
          this.compressedImage.setValue(compressedImage);
        } finally {
          tempFile.delete();
        }
      } catch (Exception e) {
        // 压缩失败
        status.setValue(Status.error);
        e.printStackTrace();
      } finally {
        os.close();
        writer.dispose();
      }
      status.setValue(Status.compressed);
    }).subscribeOn(Schedulers.io()).subscribe();
  }
}

