package org.sdp.sdp.gui;

import domein.MeldingType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MeldingenController implements CanPopup {

    private MainController mainController;
    private final domein.MeldingenController domeinController = new domein.MeldingenController();
    private final ObservableList<ObservableMelding> alleMeldingen = FXCollections.observableArrayList();

    // Cached images – loaded once to avoid repeated I/O on every card render
    private Image fileImage;
    private Image trashImage;

    @FXML private HBox ongelezeBanner;
    @FXML private Label bannerLabel;
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> typeFilter;
    @FXML private Label meldingenHeader;
    @FXML private VBox meldingenContainer;

    @FXML
    private void initialize() {
        fileImage  = new Image(getClass().getResourceAsStream("/images/file-solid.png"));
        trashImage = new Image(getClass().getResourceAsStream("/images/16201366061606130516-128.png"));

        domeinController.getMeldingen().stream()
                .map(ObservableMelding::new)
                .forEach(alleMeldingen::add);

        statusFilter.setItems(FXCollections.observableArrayList("Ongelezen", "Gelezen", "Alle"));
        statusFilter.getSelectionModel().selectFirst();

        typeFilter.setItems(FXCollections.observableArrayList(
                "Alle types",
                MeldingType.TAAK_TOEGEWEZEN.getDisplay(),
                MeldingType.TAAK_GEWIJZIGD.getDisplay(),
                MeldingType.SYSTEEM.getDisplay()));
        typeFilter.getSelectionModel().selectFirst();

        statusFilter.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, newVal) -> refreshMeldingen());
        typeFilter.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, newVal) -> refreshMeldingen());

        refreshMeldingen();
    }

    @FXML
    private void markeerAllesAlsGelezen() {
        domeinController.markeerAllesAlsGelezen();
        alleMeldingen.forEach(m -> m.setGelezen(true));
        refreshMeldingen();
    }

    private void refreshMeldingen() {
        String statusSel = statusFilter.getSelectionModel().getSelectedItem();
        String typeSel = typeFilter.getSelectionModel().getSelectedItem();

        List<ObservableMelding> gefilterd = alleMeldingen.stream()
                .filter(m -> switch (statusSel) {
                    case "Ongelezen" -> !m.isGelezen();
                    case "Gelezen"   -> m.isGelezen();
                    default          -> true;
                })
                .filter(m -> "Alle types".equals(typeSel)
                        || m.getType().getDisplay().equals(typeSel))
                .toList();

        meldingenContainer.getChildren().clear();
        for (ObservableMelding melding : gefilterd) {
            meldingenContainer.getChildren().add(buildMeldingCard(melding));
        }

        long aantalOngelezen = alleMeldingen.stream().filter(m -> !m.isGelezen()).count();
        bannerLabel.setText("Je hebt " + aantalOngelezen
                + " ongelezen melding" + (aantalOngelezen == 1 ? "" : "en"));
        meldingenHeader.setText("Meldingen (" + gefilterd.size() + ")");
        ongelezeBanner.setVisible(aantalOngelezen > 0);
        ongelezeBanner.setManaged(aantalOngelezen > 0);
    }

    private Node buildMeldingCard(ObservableMelding melding) {
        boolean ongelezen = !melding.isGelezen();

        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle(ongelezen
                ? "-fx-background-color: #FEE2E2; -fx-border-color: #FECACA; -fx-border-radius: 8; -fx-background-radius: 8;"
                : "-fx-background-color: white; -fx-border-color: #E5E5E5; -fx-border-radius: 8; -fx-background-radius: 8;");

        // ── Top row: icon + title + (optional) mark-as-read ──────────────────
        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);

        ImageView fileIcon = new ImageView(fileImage);
        fileIcon.setFitWidth(18);
        fileIcon.setFitHeight(18);
        fileIcon.setPreserveRatio(true);
        if (ongelezen) {
            fileIcon.setStyle("-fx-effect: innershadow(gaussian, #EF4444, 0, 0, 0, 0);");
            fileIcon.setOpacity(0.85);
        } else {
            fileIcon.setOpacity(0.45);
        }

        Label titelLabel = new Label(melding.getTitel());
        titelLabel.setStyle(ongelezen
                ? "-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #111;"
                : "-fx-font-size: 14; -fx-text-fill: #333;");

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        topRow.getChildren().addAll(fileIcon, titelLabel, topSpacer);

        if (ongelezen) {
            Button markeerBtn = new Button("✓  Markeren als gelezen");
            markeerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #666;"
                    + "-fx-cursor: hand; -fx-padding: 3 8; -fx-font-size: 12;");
            markeerBtn.setOnAction(e -> {
                melding.setGelezen(true);
                domeinController.markeerAlsGelezen(melding.getId());
                refreshMeldingen();
            });
            topRow.getChildren().add(markeerBtn);
        }

        // ── Detail row ────────────────────────────────────────────────────────
        Label detailLabel = new Label(melding.getDetail());
        detailLabel.setStyle("-fx-text-fill: #333; -fx-font-size: 13;");
        detailLabel.setPadding(new Insets(0, 0, 0, 28));

        // ── Bottom row: time + delete + bekijk ────────────────────────────────
        HBox bottomRow = new HBox(8);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        Label tijdLabel = new Label(formatTijdslot(melding.getBeginTijd(), melding.getEindTijd()));
        tijdLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12;");
        tijdLabel.setPadding(new Insets(0, 0, 0, 28));

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        Button deleteBtn = new Button();
        ImageView trashIcon = new ImageView(trashImage);
        trashIcon.setFitWidth(16);
        trashIcon.setFitHeight(16);
        trashIcon.setPreserveRatio(true);
        deleteBtn.setGraphic(trashIcon);
        deleteBtn.setStyle("-fx-background-color: white; -fx-border-color: #E5E5E5;"
                + "-fx-border-radius: 5; -fx-cursor: hand; -fx-padding: 5 8;");
        deleteBtn.setOnAction(e -> {
            domeinController.verwijderMelding(melding.getId());
            alleMeldingen.remove(melding);
            refreshMeldingen();
        });

        Button bekijkBtn = new Button("Bekijk  →");
        bekijkBtn.setStyle("-fx-background-color: white; -fx-border-color: #E5E5E5;"
                + "-fx-border-radius: 5; -fx-cursor: hand; -fx-padding: 5 14;");
        bekijkBtn.setOnAction(e -> toonMeldingDetails(melding));

        bottomRow.getChildren().addAll(tijdLabel, bottomSpacer, deleteBtn, bekijkBtn);

        card.getChildren().addAll(topRow, detailLabel, bottomRow);
        return card;
    }

    private void toonMeldingDetails(ObservableMelding melding) {
        if (mainController == null) return;

        VBox popup = new VBox(12);
        popup.setMaxWidth(420);
        popup.setStyle("-fx-background-color: white; -fx-border-radius: 15; -fx-background-radius: 15;");
        popup.setPadding(new Insets(24));

        Label titelLabel = new Label("Melding Details");
        titelLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label typeLabel = new Label("Type: " + melding.getType().getDisplay());
        typeLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #555;");

        Label naamLabel = new Label(melding.getDetail());
        naamLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Label tijdLabel = new Label("Tijd: " + formatTijdslot(melding.getBeginTijd(), melding.getEindTijd()));
        tijdLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #555;");

        Label statusLabel = new Label("Status: " + (melding.isGelezen() ? "Gelezen" : "Ongelezen"));
        statusLabel.setStyle("-fx-font-size: 13; -fx-text-fill: "
                + (melding.isGelezen() ? "#555;" : "#EF4444;"));

        Button sluitenBtn = new Button("Sluiten");
        sluitenBtn.setStyle("-fx-background-color: #E3E3E3; -fx-padding: 6 14; -fx-border-radius: 5;"
                + "-fx-background-radius: 5; -fx-cursor: hand;");
        sluitenBtn.setOnAction(e -> mainController.closePopup());

        popup.getChildren().addAll(titelLabel, typeLabel, naamLabel, tijdLabel, statusLabel, sluitenBtn);
        mainController.showPopup(popup);
    }

    private String formatTijdslot(LocalTime begin, LocalTime eind) {
        long uren = begin.until(eind, ChronoUnit.HOURS);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("H:mm");
        return String.format("%s - %s (%d uur)", begin.format(fmt), eind.format(fmt), uren);
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
