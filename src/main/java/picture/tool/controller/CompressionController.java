package picture.tool.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.net.URL;
import java.util.ResourceBundle;

public class CompressionController implements Initializable {

  @FXML
  private TableView tableView;

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
        Observable.fromIterable(db.getFiles()).observeOn(Schedulers.io()).subscribe(file -> {
          System.out.println(Thread.currentThread());
          System.out.println(file.toString());
        });
      }
    });
  }
}
