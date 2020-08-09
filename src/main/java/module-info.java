
module picture.tool {
  requires javafx.graphics;
  requires javafx.fxml;
  requires javafx.controls;
  requires io.reactivex.rxjava3;
  requires java.desktop;

  exports picture.tool to javafx.graphics;
  opens picture.tool.controller to javafx.fxml;
}