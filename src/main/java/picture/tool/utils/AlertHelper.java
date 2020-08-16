package picture.tool.utils;

import javafx.scene.control.Alert;

public class AlertHelper {

  public static Alert error(String headerText) {
    return of(Alert.AlertType.ERROR, headerText, null);
  }

  public static Alert info(String headerText) {
    return of(Alert.AlertType.INFORMATION, headerText, null);
  }

  private static Alert of(Alert.AlertType alertType, String headerText, String contentText) {
    Alert alert = new Alert(alertType);
    alert.setTitle("提示");
    if (null == contentText) {
      alert.setContentText(contentText);
    }
    alert.setHeaderText(headerText);
    return alert;
  }
}
