package picture.tool.controller.cell;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import picture.tool.controller.compression.CompressionTask;

public class ActionTableCell extends TableCell<CompressionTask, CompressionTask> {

  private final ObservableList<CompressionTask> data;

  private CompressionTask compressionTask;

  private HBox hbox;

  public ActionTableCell(ObservableList<CompressionTask> data) {
    this.data = data;
    hbox = new HBox();
    hbox.setAlignment(Pos.CENTER);
    Hyperlink hyperlink = new Hyperlink("移除");
    hyperlink.setOnAction(event -> {
      if (null != compressionTask) {
        data.remove(compressionTask);
      }
    });
    hbox.getChildren().add(hyperlink);
  }

  @Override
  protected void updateItem(CompressionTask item, boolean empty) {
    super.updateItem(item, empty);
    this.compressionTask = item;
    if (empty || null == item) {
      setGraphic(null);
    } else {
      setGraphic(hbox);
    }
  }
}
