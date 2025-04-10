package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class RoleSelectionPage {

    private final DatabaseHelper databaseHelper;
    private final List<String> roles;

    public RoleSelectionPage(DatabaseHelper databaseHelper, List<String> roles) {
        this.databaseHelper = databaseHelper;
        this.roles = roles;
    }

    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        Label headerLabel = new Label("Role Management");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        if (roles.isEmpty()) {
            showAlert("Error", "No roles available. Please contact an administrator.");
            Button backButton = new Button("Back");
            backButton.setOnAction(e -> new WelcomeLoginPage(databaseHelper).show(primaryStage, user));
            layout.getChildren().addAll(headerLabel, backButton);

            Scene scene = new Scene(layout, 800, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Role Management");
            primaryStage.show();
            return;
        }

        Label currentLabel = new Label("Your current roles: " + String.join(", ", roles));

        ObservableList<String> roleOptions = FXCollections.observableArrayList(roles);
        ListView<String> roleListView = new ListView<>(roleOptions);
        roleListView.setPrefSize(300, 150);

        Button selectButton = new Button("Select Role");
        selectButton.setOnAction(event -> {
            String selectedRole = roleListView.getSelectionModel().getSelectedItem();
            if (selectedRole != null) {
                user.setRole(selectedRole);
                if ("admin".equalsIgnoreCase(selectedRole)) {
                    new AdminHomePage(user).show(primaryStage);
                } else if ("staff".equalsIgnoreCase(selectedRole)) {
                    new StaffHomePage(user).show(primaryStage);
                } else {
                    new UserHomePage(user).show(primaryStage);
                }
            } else {
                showAlert("Warning", "Please select a role to continue.");
            }
        });

        ComboBox<String> requestDropdown = new ComboBox<>(FXCollections.observableArrayList("admin", "reviewer", "user", "guest", "staff"));
        requestDropdown.setPromptText("Select a role to request");

        Button requestButton = new Button("Request New Role");
        requestButton.setOnAction(event -> {
            String requestedRole = requestDropdown.getValue();
            if (requestedRole != null && !requestedRole.trim().isEmpty()) {
                boolean success = databaseHelper.sendRoleRequest(user.getUserName(), requestedRole);
                Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                alert.setTitle(success ? "Request Sent" : "Request Failed");
                alert.setContentText(success ? "Your request for role '" + requestedRole + "' has been sent to the admin." : "Something went wrong. Please try again.");
                alert.showAndWait();
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> new WelcomeLoginPage(databaseHelper).show(primaryStage, user));

        layout.getChildren().addAll(headerLabel, currentLabel, roleListView, selectButton, requestDropdown, requestButton, backBtn);

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Role Management");
        primaryStage.show();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
