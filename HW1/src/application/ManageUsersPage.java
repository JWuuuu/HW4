package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ManageUsersPage displays a page with a list of users.
 */
public class ManageUsersPage {

    private final DatabaseHelper databaseHelper = new DatabaseHelper();
    private User currentUser;
	
	// Constructor that accepts the logged-in user
	public ManageUsersPage(User currentUser) {
		this.currentUser = currentUser;
	}

    /**
     * Displays the Manage Users page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Title label
        Label titleLabel = new Label("Manage Users");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ListView to display users
        ListView<String> usersListView = new ListView<>();
        usersListView.getItems().addAll(
            "User 1: John Smith",
            "User 2: Amy Birch",
            "User 3: Francy Prose",
            "User 4: Katie Rose"
        );
        usersListView.setPrefSize(600, 300);

        // Input fields for role assignment
        Label assignLabel = new Label("Assign a role to user:");
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Enter username");

        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("admin", "user", "guest", "reviewer");
        roleDropdown.setPromptText("Select role");

        Label statusLabel = new Label();

        Button assignButton = new Button("Assign Role");
        assignButton.setOnAction(event -> {
            String username = usernameInput.getText().trim();
            String selectedRole = roleDropdown.getValue();

            if (username.isEmpty() || selectedRole == null) {
                statusLabel.setText("Please enter a username and select a role.");
                return;
            }

            boolean success = databaseHelper.addRoleToUser(username, selectedRole);
            if (success) {
                statusLabel.setText("Assigned role '" + selectedRole + "' to " + username);
            } else {
                statusLabel.setText("Failed to assign role.");
            }
        });

        // Back button to return to the AdminHomePage
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            new AdminHomePage(currentUser).show(primaryStage);
        });

        layout.getChildren().addAll(
            titleLabel,
            usersListView,
            assignLabel,
            usernameInput,
            roleDropdown,
            assignButton,
            statusLabel,
            backButton
        );

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Users");
        primaryStage.show();

        // Automated Test: Scene Loaded
        System.out.println("Test: InvitationPage Loaded - Scene Title: " + primaryStage.getTitle());
    }
}
