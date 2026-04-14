package org.sdp.sdp.gui;

import domein.TeamController;
import domein.WerknemerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MainController {

    private final Map<ToggleButton, Supplier<Node>> navButtons = new HashMap<>();

    @FXML
    public ToggleButton btnGebruikers;

    @FXML
    public ToggleButton btnLogs;

    @FXML
    public ToggleButton btnMeldingen;

    @FXML
    public ToggleButton btnTeams;

    @FXML
    public StackPane popupContent;

    @FXML
    private ToggleGroup navGroup;

    @FXML
    private StackPane contentPane;

    @FXML
    private void initialize() {
        WerknemerController werknemerController = new WerknemerController();
        TeamController teamController = new TeamController();
        navButtons.put(btnGebruikers, () -> {
            GebruikersController c = new GebruikersController(this, werknemerController);
            return c;
        });
        navButtons.put(btnLogs, () -> loadFxml("/org.sdp.sdp/gui/Logs.fxml"));
        navButtons.put(btnMeldingen, () -> loadFxml("/org.sdp.sdp/gui/meldingen.fxml"));
        navButtons.put(btnTeams, () -> {
            TeamControllerGUI teamControllerGUI = new TeamControllerGUI(this, teamController);
            return teamControllerGUI;
        });
        navGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                Supplier<Node> factory = navButtons.get((ToggleButton) newToggle);
                if (factory != null) {
                    setContent(factory);
                }
            }
        });

        setDefaultBtn(btnGebruikers);
    }

    private void setContent(Supplier<Node> factory) {
        contentPane.getChildren().clear();
        Node newContent = factory.get();
        contentPane.getChildren().add(newContent);
    }

    // Hulpmethode voor gewone FXML's (zonder fx:root patroon)
    private Node loadFxml(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Node node = loader.load();

            Object controller = loader.getController();
            if (controller instanceof CanPopup p) {
                p.setMainController(this);
            }

            return node;
        } catch (IOException e) {

            throw new RuntimeException(e);
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