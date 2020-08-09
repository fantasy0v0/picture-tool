package picture.tool.controller;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import picture.tool.controller.compression.CompressionTask;
import picture.tool.controller.compression.Status;
import picture.tool.utils.FileSizeHelper;
import picture.tool.utils.ImageUtil;
import picture.tool.utils.RxJavaFx;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class CompressionController implements Initializable {

  private final ObservableList<CompressionTask> data = FXCollections.observableArrayList();

  @FXML
  private TableView<CompressionTask> tableView;

  @FXML
  private TableColumn<CompressionTask, String> fileNameColumn;

  @FXML
  private TableColumn<CompressionTask, String> sizeColumn;

  @FXML
  private TableColumn<CompressionTask, String> expectedSizeColumn;

  @FXML
  private ImageView previewImageView;

  @FXML
  private Slider slider;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    tableView.setItems(data);
    // 文件名
    fileNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().originFile.getName()));
    // 文件大小
    sizeColumn.setCellValueFactory(p -> {
      String sizeStr = FileSizeHelper.format(p.getValue().originFile.length());
      return new SimpleStringProperty(sizeStr);
    });
    // 压缩后大小
    expectedSizeColumn.setCellValueFactory(p -> {
      return Bindings.createStringBinding(() -> {
        CompressionTask task = p.getValue();
        switch (task.status.get()) {
          case compressing:
            return "计算中";
          case compressed:
            return FileSizeHelper.format(task.compressedSize);
          case error:
            return "处理失败";
          default:
            return "未知错误";
        }
      }, p.getValue().status);
    });
    // 图片拖拽
    tableView.setOnDragOver(event -> {
      Dragboard db = event.getDragboard();
      if (db.hasFiles()) {
        event.acceptTransferModes(TransferMode.COPY);
      } else {
        event.consume();
      }
    });
    tableView.setOnDragDropped(event -> {
      Dragboard db = event.getDragboard();
      if (db.hasFiles()) {
        Observable.fromIterable(db.getFiles()).subscribeOn(Schedulers.io()).flatMap(file -> {
          // 压缩等级
          float compressionQuality = slider.valueProperty().floatValue() / 10;
          // 判断是否是图片文件, 如果不是则跳过
          return CompressionTask.create(file, compressionQuality).toObservable().onErrorResumeNext(error -> Observable.empty());
        }).toList().subscribe(tasks -> data.addAll(tasks));
      }
    });
    // 滑动条
    RxJavaFx.fromObservableValue(slider.valueProperty())
      .subscribeOn(Schedulers.computation())
      .debounce(300, TimeUnit.MILLISECONDS)
      .subscribe(value -> {
        data.forEach(task -> task.adjustQuality(value.floatValue() / 10));
        CompressionTask selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (null != selectedItem) {
          RxJavaFx.fromObservableValue(selectedItem.status)
            .takeUntil((Predicate<Status>) Status.compressed::equals)
            .subscribe(status -> previewImage(selectedItem));
        }
      });
    // 图片选中
    RxJavaFx.fromObservableValue(tableView.getSelectionModel().selectedItemProperty())
      .subscribeOn(Schedulers.computation())
      .debounce(150, TimeUnit.MILLISECONDS)
      .subscribe(this::previewImage);
  }

  /**
   * 展示图片
   *
   * @param task 任务
   */
  private void previewImage(CompressionTask task) {
    Maybe.<Image>create(emitter -> {
      if (Status.compressed.equals(task.status.get())) {
        Image image = ImageUtil.bufferedImageToImage(task.compressedImage.get());
        emitter.onSuccess(image);
      } else {
        emitter.onComplete();
      }
    }).subscribe(image -> Platform.runLater(() -> previewImageView.setImage(image)));
  }
}
