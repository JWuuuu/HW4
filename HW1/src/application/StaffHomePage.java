package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * StaffHomePage displays the home screen for staff users.
 * Staff can view and moderate content such as questions and answers.
 */
public class StaffHomePage {

    private User currentUser;

    public StaffHomePage(User currentUser) {
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label staffLabel = new Label("Welcome, Staff!");
        staffLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button manageContentButton = new Button("Manage Content");
        manageContentButton.setOnAction(event -> {
            new ManageContentPage(currentUser).show(primaryStage);
        });

        Button activityLogsButton = new Button("View Activity Logs");
        activityLogsButton.setOnAction(event -> {
            new ActivityLogsPage(currentUser).show(primaryStage);
            DatabaseHelper.getInstance().logActivity(currentUser.getUserName(), "Viewed Activity Logs");
        });

        Button changeRoleButton = new Button("Change Role");
        changeRoleButton.setOnAction(event -> {
            DatabaseHelper dbHelper = new DatabaseHelper();
            new RoleSelectionPage(dbHelper, dbHelper.getUserRoles(currentUser.getUserName())).show(primaryStage, currentUser);
            DatabaseHelper.getInstance().logActivity(currentUser.getUserName(), "Switched Role");
        });

        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(event -> {
            DatabaseHelper.getInstance().logActivity(currentUser.getUserName(), "Logged Out");
            new StartCSE360().start(primaryStage);
        });

        layout.getChildren().addAll(staffLabel, manageContentButton, activityLogsButton, changeRoleButton, logoutButton);

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Home Page");
        primaryStage.show();
    }
}
