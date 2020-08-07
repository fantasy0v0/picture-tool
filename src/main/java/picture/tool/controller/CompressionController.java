package picture.tool.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import picture.tool.controller.compression.CompressionTask;

import java.net.URL;
import java.util.ResourceBundle;

public class CompressionController implements Initializable {

  @FXML
  private TableView<CompressionTask> tableView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
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
          // 判断是否是图片文件, 如果不是则跳过
          return CompressionTask.create(file).toObservable().onErrorResumeNext(error -> Observable.empty());
        }).toList().subscribe(tasks -> {
          System.out.println("subscribe:" + Thread.currentThread());
        });
      }
    });
  }
}
