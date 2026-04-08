package org.sdp.sdp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {

    private final Map<ToggleButton, String> navButtons = new HashMap<>();

    @FXML
    public ToggleButton btnGebruikers;

    @FXML
    public ToggleButton btnLogs;

    @FXML
    public ToggleButton btnMeldingen;

    @FXML
    public StackPane popupContent;

    @FXML
    private ToggleGroup navGroup;

    @FXML
    private StackPane contentPane;

    @FXML
    private void initialize() {
        navButtons.put(btnGebruikers, "/org.sdp.sdp/gui/Gebruikers.fxml");
        navButtons.put(btnLogs, "/org.sdp.sdp/gui/Logs.fxml");
        navButtons.put(btnMeldingen, "/org.sdp.sdp/gui/meldingen.fxml");

        navGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                Node selected = (Node) newToggle;
                String path = navButtons.get(selected);
                if (path != null) {setContent(path);
                }
            }
        });

        setDefaultBtn(btnGebruikers);
    }

    private void setContent(String fxmlPath) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node newContent = loader.load();

            Object controller = loader.getController();
            if (controller instanceof CanPopup p) {
                p.setMainController(this);
            }

            contentPane.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultBtn(ToggleButton defaultBtn) {
        if (defaultBtn != null) {
            defaultBtn.setSelected(true);
        }
    }

    public void showPopup(Node popup) {
        popupContent.setMouseTransparent(false);

        Pane background = new Pane();
        background.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        background.prefWidthProperty().bind(popupContent.widthProperty());
        background.prefHeightProperty().bind(popupContent.heightProperty());

        StackPane wrapper = new StackPane(background, popup);
        StackPane.setAlignment(popup, Pos.CENTER);

        background.setOnMouseClicked(e -> closePopup());

        popupContent.getChildren().add(wrapper);
    }

    public void closePopup() {
        popupContent.getChildren().clear();
        popupContent.setMouseTransparent(true);
    }

}