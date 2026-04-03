package org.sdp.sdp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PlannerApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.getChildren().add(new Label("Planner"));

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Planner");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}