package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TrustedReviewerPage {
    private final User currentUser;

    public TrustedReviewerPage(User currentUser) {
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label label = new Label("Manage Trusted Reviewers for " + currentUser.getUserName());
        TextField reviewerField = new TextField();
        reviewerField.setPromptText("Enter reviewer username");

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String reviewer = reviewerField.getText().trim();
            TrustedReviewerManager.addTrustedReviewer(currentUser.getUserName(), reviewer);
            reviewerField.clear();
        });

        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> {
            String reviewer = reviewerField.getText().trim();
            TrustedReviewerManager.removeTrustedReviewer(currentUser.getUserName(), reviewer);
            reviewerField.clear();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new UserHomePage(currentUser).show(primaryStage));

        layout.getChildren().addAll(label, reviewerField, addButton, removeButton, backButton);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Trusted Reviewer Management");
        primaryStage.show();
    }
}
