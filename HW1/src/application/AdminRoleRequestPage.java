package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRoleRequestPage {

    private final DatabaseHelper databaseHelper = new DatabaseHelper();
    private User currentUser;
	
	// Constructor that accepts the logged-in user
	public AdminRoleRequestPage(User currentUser) {
		this.currentUser = currentUser;
	}

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label titleLabel = new Label("Pending Role Requests");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<String> requestList = new ListView<>();
        List<RoleRequest> pendingRequests = getPendingRoleRequests();
        for (RoleRequest request : pendingRequests) {
            requestList.getItems().add(request.userName + " → " + request.requestedRole);
        }

        Button approveButton = new Button("Approve Selected Request");
        approveButton.setOnAction(e -> {
            int selectedIndex = requestList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                RoleRequest selected = pendingRequests.get(selectedIndex);
                databaseHelper.addRoleToUser(selected.userName, selected.requestedRole);
                updateRequestStatus(selected.userName, selected.requestedRole, "approved");
                requestList.getItems().remove(selectedIndex);
            }
        });

        Button rejectButton = new Button("Reject Selected Request");
        rejectButton.setOnAction(e -> {
            int selectedIndex = requestList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                RoleRequest selected = pendingRequests.get(selectedIndex);
                updateRequestStatus(selected.userName, selected.requestedRole, "rejected");
                requestList.getItems().remove(selectedIndex);
            }
        });

        Button backButton = new Button("Back to Admin Home");
        backButton.setOnAction(e -> new AdminHomePage(currentUser).show(primaryStage));

        layout.getChildren().addAll(titleLabel, requestList, approveButton, rejectButton, backButton);
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Role Request Review");
        primaryStage.show();
    }

    private List<RoleRequest> getPendingRoleRequests() {
        List<RoleRequest> requests = new ArrayList<>();
        String query = "SELECT userName, requestedRole FROM RoleRequests WHERE status = 'pending'";
        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/FoundationDatabase", "sa", "")) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                requests.add(new RoleRequest(rs.getString("userName"), rs.getString("requestedRole")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    private void updateRequestStatus(String userName, String role, String status) {
        String query = "UPDATE RoleRequests SET status = ? WHERE userName = ? AND requestedRole = ? AND status = 'pending'";
        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/FoundationDatabase", "sa", "")) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, status);
            pstmt.setString(2, userName);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class RoleRequest {
        String userName;
        String requestedRole;

        RoleRequest(String userName, String requestedRole) {
            this.userName = userName;
            this.requestedRole = requestedRole;
        }
    }
}
