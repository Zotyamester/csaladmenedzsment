package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class NiceController implements Initializable {
    @FXML
    private AnchorPane parent;

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeStageDraggable();
    }

    private void makeStageDraggable() {
        parent.setOnMousePressed((event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        parent.setOnMouseDragged((event) -> {
            FamilyManagement.stage.setX(event.getScreenX() - xOffset);
            FamilyManagement.stage.setY(event.getScreenY() - yOffset);
            FamilyManagement.stage.setOpacity(0.8f);
        });
        parent.setOnDragDone((event) -> {
            FamilyManagement.stage.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((event) -> {
            FamilyManagement.stage.setOpacity(1.0f);
        });
    }

    @FXML
    public void minimizeStage(MouseEvent event) {
        FamilyManagement.stage.setIconified(true);
    }

    @FXML
    public void closeApp(MouseEvent event) {
        FamilyManagement.stage.hide();
    }
}
