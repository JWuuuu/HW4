package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Comparator;
import java.util.List;

/**
 * TrustedReviewersPage allows students to manage their trusted reviewers by assigning
 * weightage values and also provides a filter field (integrated with the add/update section)
 * to search for specific trusted reviewers.
 */
public class TrustedReviewersPage {

    private User currentUser;
    private DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
    
    // List for managing trusted reviewers
    private ObservableList<User> trustedReviewersList;
    // ListView to display trusted reviewers
    private ListView<User> reviewersListView;

    /**
     * Constructor that accepts the current user.
     * @param currentUser The logged-in student.
     */
    public TrustedReviewersPage(User currentUser) {
        this.currentUser = currentUser;
        trustedReviewersList = FXCollections.observableArrayList();
    }

    /**
     * Displays the TrustedReviewersPage, integrating add/update and search functionality on one page.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        Label titleLabel = new Label("Manage Trusted Reviewers");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Section for adding/updating trusted reviewers.
        TextField reviewerNameField = new TextField();
        reviewerNameField.setPromptText("Enter reviewer username");
        
        TextField weightField = new TextField();
        weightField.setPromptText("Enter weightage (1-100)");
        
        Button addUpdateButton = new Button("Add/Update Reviewer");
        addUpdateButton.setOnAction(event -> {
            String reviewerName = reviewerNameField.getText().trim();
            String weightText = weightField.getText().trim();
            if (reviewerName.isEmpty() || weightText.isEmpty()) {
                showAlert("Error", "Both reviewer name and weightage are required.");
                return;
            }
            int weight;
            try {
                weight = Integer.parseInt(weightText);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Weightage must be a numeric value.");
                return;
            }
            if (weight < 1 || weight > 100) {
                showAlert("Error", "Weightage must be between 1 and 100.");
                return;
            }
            // Check if reviewer already exists in the list.
            User existingReviewer = null;
            for (User reviewer : trustedReviewersList) {
                if (reviewer.getUserName().equalsIgnoreCase(reviewerName)) {
                    existingReviewer = reviewer;
                    break;
                }
            }
            if (existingReviewer != null) {
                existingReviewer.setReviewerWeight(weight);
            } else {
                User newReviewer = new User(reviewerName, "", "reviewer");
                newReviewer.setReviewerWeight(weight);
                newReviewer.setReviewerRequest(true); // Mark as trusted request (for reference)
                trustedReviewersList.add(newReviewer);
            }
            FXCollections.sort(trustedReviewersList, Comparator.comparingInt(User::getReviewerWeight).reversed());
            refreshReviewersListView();
            showAlert("Success", "Trusted reviewer updated.");
            reviewerNameField.clear();
            weightField.clear();
        });
        
        // Inline search field to filter the trusted reviewers list.
        HBox searchBox = new HBox(10);
        Label searchLabel = new Label("Search:");
        TextField filterField = new TextField();
        filterField.setPromptText("Type to Search");
        Button filterButton = new Button("Apply");
        filterButton.setOnAction(e -> refreshReviewersListView(filterField.getText().trim()));
        searchBox.getChildren().addAll(searchLabel, filterField, filterButton);
        
        // ListView for displaying trusted reviewers.
        reviewersListView = new ListView<>(trustedReviewersList);
        reviewersListView.setPrefSize(300, 150);
        reviewersListView.setCellFactory(list -> new ListCell<User>() {
            @Override
            protected void updateItem(User reviewer, boolean empty) {
                super.updateItem(reviewer, empty);
                if (empty || reviewer == null) {
                    setText(null);
                } else {
                    setText(reviewer.getUserName() + " - Weight: " + reviewer.getReviewerWeight());
                }
            }
        });
        
        // Back button to return to the UserHomePage.
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> new UserHomePage(currentUser).show(primaryStage));
        
        layout.getChildren().addAll(titleLabel, reviewerNameField, weightField, addUpdateButton, searchBox, reviewersListView, backButton);
        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trusted Reviewers");
        primaryStage.show();
    }
    
    /**
     * Refreshes the ListView to show all trusted reviewers.
     */
    private void refreshReviewersListView() {
        refreshReviewersListView("");
    }
    
    /**
     * Refreshes the ListView to show only trusted reviewers matching the filter.
     * @param filter the filter string; if empty, shows all reviewers.
     */
    private void refreshReviewersListView(String filter) {
        ObservableList<User> filteredList = FXCollections.observableArrayList();
        for (User reviewer : trustedReviewersList) {
            if (filter.isEmpty() || reviewer.getUserName().toLowerCase().contains(filter.toLowerCase())) {
                filteredList.add(reviewer);
            }
        }
        reviewersListView.setItems(filteredList);
    }
    
    /**
     * Utility method to display an alert dialog.
     * @param title The title of the alert.
     * @param message The content text of the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
