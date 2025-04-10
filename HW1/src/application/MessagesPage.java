package application;

import java.util.List;
import java.util.Optional;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * MessagesPage allows a user to see sent or received messages.
 */

public class MessagesPage {
	private final MessageManager messageManager = MessageManager.getInstance();
	private final ObservableList<String> messageList = FXCollections.observableArrayList();
	private ListView<String> messageListView;
	
	private User currentUser;
	
	public MessagesPage(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public void Show(Stage primaryStage) {
		VBox layout = new VBox(10);
		layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

		Label titleLabel = new Label("Messages (Logged in as: " + currentUser.getUserName() + ")");
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		
		messageListView = new ListView<>(messageList);
		updateMessageList(); // load user’s messages
		
		TextField toField = new TextField();
        toField.setPromptText("Enter username of recipient");

        TextArea messageContentField = new TextArea();
        messageContentField.setPromptText("Enter message content");
		
        DatabaseHelper dbHelper = new DatabaseHelper();
        
		Button sendButton = new Button("Send");
		sendButton.setOnAction(event -> {
			String content = messageContentField.getText().trim();
			String fromUser = currentUser.getUserName();
			String toUser = toField.getText().trim();
			if (content.isEmpty()) {
				showAlert("Error", "Cannot send a blank message.", Alert.AlertType.ERROR);
                return;
			}
			if (!dbHelper.doesUserExist(toUser)) {
				showAlert("Error", "Username is not recognized.", Alert.AlertType.ERROR);
                return;
			}
			messageManager.addMessage(content, fromUser, toUser);
			updateMessageList();
			toField.clear();
			messageContentField.clear();
			showAlert("Success", "Message successfully sent to " + toUser + ".", Alert.AlertType.CONFIRMATION);
		});
		
		
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> new UserHomePage(currentUser).show(primaryStage));

		layout.getChildren().addAll(titleLabel, toField, messageContentField, messageListView, sendButton, backButton);

		Scene scene = new Scene(layout, 800, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Messages");
		primaryStage.show();
	}
	
	private void updateMessageList() {
		messageList.clear();
		for (Message m : messageManager.getMessages()) {
			// only show messages owned by currentUser or sent to currentUser
			if (m.getSender().equalsIgnoreCase(currentUser.getUserName())) {
				messageList.add(m.getId() + ": " + m.toString());
			}
			if (m.getReceiver().equalsIgnoreCase(currentUser.getUserName())) {
				messageList.add(m.getId() + ": " + m.toString());
			}
		}
		messageListView.setItems(messageList);
	}
	
	private void showAlert(String title, String msg, Alert.AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
}


