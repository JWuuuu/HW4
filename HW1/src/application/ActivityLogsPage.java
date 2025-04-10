// ActivityLogsPage.java
package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ActivityLogsPage displays a page with a list of user activity logs.
 */
public class ActivityLogsPage {

    private User currentUser;

    // Constructor that accepts the logged-in user
    public ActivityLogsPage(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Displays the Activity Logs page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Title label
        Label titleLabel = new Label("Activity Logs");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ListView to display logs
        ListView<String> logsListView = new ListView<>();
        logsListView.setPrefSize(600, 300);

        try {
        	ResultSet rs = DatabaseHelper.getInstance().getAllActivityLogs();
            while (rs.next()) {
                String time = rs.getTimestamp("timestamp").toString();
                String user = rs.getString("username");
                String action = rs.getString("action");
                logsListView.getItems().add("[" + time + "] " + user + ": " + action);
            }
        } catch (SQLException e) {
            logsListView.getItems().add("Error loading activity logs.");
            e.printStackTrace();
        }

        // Back button to return to StaffHomePage
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> new StaffHomePage(currentUser).show(primaryStage));

        layout.getChildren().addAll(titleLabel, logsListView, backButton);

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Activity Logs");
        primaryStage.show();
    }
}