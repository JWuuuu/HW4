package application;

import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * MyQuestionsPage allows a user (student) to see and delete their own
 * questions.
 */
public class MyQuestionsPage {
	private final QuestionsManager questionsManager = QuestionsManager.getInstance();
	private final ObservableList<String> questionList = FXCollections.observableArrayList();
	private ListView<String> questionListView;

	private User currentUser;

	public MyQuestionsPage(User currentUser) {
		this.currentUser = currentUser;
	}

	public void show(Stage primaryStage) {
		VBox layout = new VBox(10);
		layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

		Label titleLabel = new Label("My Questions (Logged in as: " + currentUser.getUserName() + ")");
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		questionListView = new ListView<>(questionList);
		updateQuestionList();

		Button deleteQuestionButton = new Button("Delete Selected Question");
		deleteQuestionButton.setOnAction(e -> {
			String selected = questionListView.getSelectionModel().getSelectedItem();
			if (selected != null) {
				int questionId = Integer.parseInt(selected.split(": ")[0]);
				boolean success = questionsManager.deleteQuestion(questionId, currentUser);
				if (success) {
					updateQuestionList();
					showAlert("Success", "Question deleted successfully.", Alert.AlertType.INFORMATION);
				} else {
					showAlert("Error", "You are not allowed to delete this question.", Alert.AlertType.ERROR);
				}
			} else {
				showAlert("Error", "No question selected.", Alert.AlertType.ERROR);
			}
		});

		Button updateQuestionButton = new Button("Update Question Title");
		updateQuestionButton.setOnAction(e -> {
			String selected = questionListView.getSelectionModel().getSelectedItem();
			if (selected != null) {
				int questionId = Integer.parseInt(selected.split(": ")[0]);
				boolean canUpdate = questionsManager.checkQuestionUpdate(questionId, currentUser);
				if (!canUpdate) {
					TextInputDialog inputDialog = new TextInputDialog();
					inputDialog.setTitle("Update Question Title");
					inputDialog.setHeaderText("Enter a new title for the question:");
					inputDialog.setContentText("New Title:");
					Optional<String> newTitle = inputDialog.showAndWait();
					newTitle.ifPresent(title -> {
						questionsManager.updateQuestionTitle(questionId, title);
						updateQuestionList();
						showAlert("Success", "Question title updated successfully.", Alert.AlertType.INFORMATION);
					});
				} else {
					showAlert("Error", "You are not allowed to update this question.", Alert.AlertType.ERROR);
				}
			} else {
				showAlert("Error", "No question selected.", Alert.AlertType.ERROR);
			}
		});

		Button updateQuestionInfoButton = new Button("Update Question info");
		updateQuestionInfoButton.setOnAction(e -> {
			showAlert("Error", "You are not allowed to update this question.", Alert.AlertType.ERROR);
		});

		Button viewAnswersButton = new Button("View Answers");
		viewAnswersButton.setOnAction(event -> {
			String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
			if (selectedQuestion == null) {
				showAlert("Error", "Please select a question to view answers.", Alert.AlertType.ERROR);
				return;
			}
			int questionId = Integer.parseInt(selectedQuestion.split(": ")[0]);
			List<Answer> answers = questionsManager.getAnswersForQuestion(questionId);
			if (answers.isEmpty()) {
				showAlert("Info", "No answers available for this question.", Alert.AlertType.ERROR);
			} else {
				showAnswersPopup(answers, questionId);
			}
		});

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> new UserHomePage(currentUser).show(primaryStage));

		layout.getChildren().addAll(titleLabel, questionListView, deleteQuestionButton, updateQuestionButton,
				updateQuestionInfoButton, viewAnswersButton, backButton);

		Scene scene = new Scene(layout, 800, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("My Questions");
		primaryStage.show();
	}

	private void updateQuestionList() {
		questionList.clear();
		for (Question q : questionsManager.getQuestions()) {
			if (q.getOwner().equalsIgnoreCase(currentUser.getUserName())) {
				questionList.add(q.getId() + ": " + q.getTitle());
			}
		}
		questionListView.setItems(questionList);
	}

	private void showAnswersPopup(List<Answer> answers, int questionId) {
		Stage answerStage = new Stage();
		VBox layout = new VBox(10);
		layout.setPadding(new javafx.geometry.Insets(10));
		Label titleLabel = new Label("Answers for the Selected Question");
		layout.getChildren().add(titleLabel);

		for (Answer a : answers) {
			HBox row = new HBox(10);
			Label label = new Label(a.getId() + ": " + a.getPinDisplayText());
			Button reviewButton = new Button("Review");
			Button seeReviewsButton = new Button("See Reviews");

			reviewButton.setOnAction(e -> {
				TextInputDialog dialog = new TextInputDialog();
				dialog.setTitle("Review Answer");
				dialog.setHeaderText("Enter your review for this answer:");
				dialog.setContentText("Review:");
				Optional<String> result = dialog.showAndWait();
				result.ifPresent(reviewText -> {
					questionsManager.addReview(currentUser.getUserName(), questionId, a.getId(), reviewText);
					showAlert("Success", "Review submitted.", Alert.AlertType.INFORMATION);
				});
			});

			seeReviewsButton.setOnAction(e -> {
				List<Review> reviews = questionsManager.getReviewsForAnswer(a.getId());
				Stage popup = new Stage();
				VBox reviewsLayout = new VBox(10);
				reviewsLayout.setPadding(new javafx.geometry.Insets(10));
				Label header = new Label("Reviews for Answer ID " + a.getId());
				reviewsLayout.getChildren().add(header);

				if (reviews.isEmpty()) {
					reviewsLayout.getChildren().add(new Label("No reviews available."));
				} else {
					for (Review r : reviews) {
						HBox reviewRow = new HBox(10);
						Label reviewLabel = new Label(r.getReviewerUsername() + ": " + r.getReviewText());
						Button dmButton = new Button("Private Message");

						dmButton.setOnAction(dmEvent -> {
							TextInputDialog dmDialog = new TextInputDialog();
							dmDialog.setTitle("Direct Message");
							dmDialog.setHeaderText("Send a message to " + r.getReviewerUsername());
							dmDialog.setContentText("Message:");
							Optional<String> msgResult = dmDialog.showAndWait();
							msgResult.ifPresent(msg -> {
								MessageManager.getInstance().addMessage(msg, currentUser.getUserName(), r.getReviewerUsername());
								showAlert("Success", "Message sent.", Alert.AlertType.INFORMATION);
							});
						});

						reviewRow.getChildren().addAll(reviewLabel, dmButton);
						reviewsLayout.getChildren().add(reviewRow);
					}
				}

				Button close = new Button("Close");
				close.setOnAction(ev -> popup.close());
				reviewsLayout.getChildren().add(close);
				popup.setScene(new Scene(reviewsLayout, 450, 300));
				popup.setTitle("Reviews");
				popup.show();
			});

			row.getChildren().addAll(label, reviewButton, seeReviewsButton);
			layout.getChildren().add(row);
		}

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> answerStage.close());
		layout.getChildren().add(closeButton);
		Scene scene = new Scene(layout, 500, 400);
		answerStage.setScene(scene);
		answerStage.setTitle("Answers");
		answerStage.show();
	}

	private void showAlert(String title, String msg, Alert.AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
}
