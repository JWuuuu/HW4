package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

/**
 * ManageContentPage allows staff to view, delete, and edit questions, as well as manage reviews.
 */
public class ManageContentPage {

    private final QuestionsManager questionsManager = QuestionsManager.getInstance();
    private User currentUser;

    public ManageContentPage(User currentUser) {
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label titleLabel = new Label("Manage Questions, Answers, and Reviews");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<String> questionsListView = new ListView<>();
        List<Question> questions = questionsManager.getQuestions();
        for (Question q : questions) {
            questionsListView.getItems().add("Q" + q.getId() + ": " + q.getTitle());
        }

        Button deleteQuestionButton = new Button("Delete Selected Question");
        deleteQuestionButton.setOnAction(event -> {
            String selected = questionsListView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.startsWith("Q")) {
                int qId = Integer.parseInt(selected.split(":")[0].substring(1));
                questionsManager.deleteQuestion(qId);
                questionsListView.getItems().remove(selected);
                DatabaseHelper.getInstance().logActivity(currentUser.getUserName(), "Deleted question ID: " + qId);
            }
        });

        Button editQuestionButton = new Button("Edit Selected Question");
        editQuestionButton.setOnAction(event -> {
            String selected = questionsListView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.startsWith("Q")) {
                int qId = Integer.parseInt(selected.split(":")[0].substring(1));
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Edit Question");
                dialog.setHeaderText("Enter new question title:");
                dialog.setContentText("New Title:");
                dialog.showAndWait().ifPresent(newTitle -> {
                    questionsManager.updateQuestionTitle(qId, newTitle);
                    questionsListView.getItems().set(questionsListView.getSelectionModel().getSelectedIndex(), "Q" + qId + ": " + newTitle);
                    DatabaseHelper.getInstance().logActivity(currentUser.getUserName(), "Edited question ID: " + qId);
                });
            }
        });

        Button manageReviewsButton = new Button("Manage Reviews");
        manageReviewsButton.setOnAction(event -> {
            new ReviewManagerPage(currentUser).show(primaryStage);
            DatabaseHelper.getInstance().logActivity(currentUser.getUserName(), "Opened Review Manager");
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> new StaffHomePage(currentUser).show(primaryStage));

        HBox actions = new HBox(10, deleteQuestionButton, editQuestionButton, manageReviewsButton);
        actions.setStyle("-fx-alignment: center;");

        layout.getChildren().addAll(titleLabel, questionsListView, actions, backButton);

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Content");
        primaryStage.show();
    }
}
