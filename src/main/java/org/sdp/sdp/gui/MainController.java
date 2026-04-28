package org.sdp.sdp.gui;

import domein.MeldingenController;
import domein.SiteController;
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

    private MeldingenController meldingenController;

    @FXML
    public ToggleButton btnGebruikers;

    @FXML
    public ToggleButton btnLogs;

    @FXML
    public ToggleButton btnMeldingen;

    @FXML
    public ToggleButton btnTeams;

    @FXML
    public ToggleButton btnSites;

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
        SiteController siteController = new SiteController();
        navButtons.put(btnGebruikers, () -> {
            GebruikersController c = new GebruikersController(this, werknemerController, teamController, siteController);
            return c;
        });
        navButtons.put(btnLogs, () -> loadFxml("/org.sdp.sdp/gui/Logs.fxml"));
        navButtons.put(btnMeldingen, () -> {
            if (meldingenController == null) {
                meldingenController = new MeldingenController(
                        teamController.getAllTeams(),
                        werknemerController.getWerknemers());
            }
            return loadMeldingen(meldingenController);
        });
        navButtons.put(btnTeams, () -> {
            TeamControllerGUI teamControllerGUI = new TeamControllerGUI(this, teamController, werknemerController, siteController);
            return teamControllerGUI;
        });
        navButtons.put(btnSites, () -> {
            SiteControllerGUI siteControllerGUI = new SiteControllerGUI(this, siteController);
            return siteControllerGUI;
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

    private Node loadMeldingen(MeldingenController domeinMeldingenController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/Meldingen.fxml"));
            Node node = loader.load();
            MeldingenControllerGUI controller = loader.getController();
            controller.setMainController(this);
            controller.setDomeinController(domeinMeldingenController);
            return node;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper for plain FXML screens (no fx:root pattern)
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