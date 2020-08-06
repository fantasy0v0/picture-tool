package picture.tool.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.net.URL;
import java.util.List;
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
        List<File> files = db.getFiles();
        System.out.println(files.toString());
      }
    });
    tableView.setOnMouseClicked(event -> {
      System.out.println("tableView clicked.");
    });
  }
}
