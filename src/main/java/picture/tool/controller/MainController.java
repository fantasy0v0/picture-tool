package picture.tool.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

  @FXML
  private HBox compressionBtn;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    compressionBtn.setOnMouseClicked(event -> {

    });
  }
}
