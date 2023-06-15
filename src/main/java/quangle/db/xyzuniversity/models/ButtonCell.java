package quangle.db.xyzuniversity.models;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class ButtonCell<S, T> extends TableCell<S, T> {

    private final Button button;

    public ButtonCell(String text, EventHandler<ActionEvent> eventHandler) {
        button = new Button(text);
        button.setOnAction(eventHandler);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            setGraphic(button);
        }
    }

}
