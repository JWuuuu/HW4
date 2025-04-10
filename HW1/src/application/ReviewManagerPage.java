package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class ReviewManagerPage {

    private final QuestionsManager questionsManager = QuestionsManager.getInstance();
    private final User currentUser;

    public ReviewManagerPage(User currentUser) {
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label title = new Label("Manage Reviews");

        ComboBox<String> questionSelector = new ComboBox<>();
        ObservableList<String> questionTitles = FXCollections.observableArrayList();
        List<Question> allQuestions = questionsManager.getQuestions();
        for (Question q : allQuestions) {
            questionTitles.add("Q" + q.getId() + ": " + q.getTitle());
        }
        questionSelector.setItems(questionTitles);
        questionSelector.setPromptText("Select a Question");

        ComboBox<String> answerSelector = new ComboBox<>();
        ObservableList<String> answerTexts = FXCollections.observableArrayList();
        answerSelector.setItems(answerTexts);
        answerSelector.setPromptText("< No Answer >");

        ListView<String> reviewListView = new ListView<>();
        reviewListView.setPrefSize(600, 200);

        TextField reviewInput = new TextField();
        reviewInput.setPromptText("Enter new review text");

        // Update answers and reviews when a question is selected
        questionSelector.setOnAction(e -> {
            answerTexts.clear();
            reviewListView.getItems().clear();
            int selectedIndex = questionSelector.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Question selectedQuestion = allQuestions.get(selectedIndex);
                List<Answer> answers = questionsManager.getAnswersForQuestion(selectedQuestion.getId());
                answerTexts.add("< No Answer >");
                for (Answer a : answers) {
                    answerTexts.add("A" + a.getId() + ": " + a.getContent());
                }
                answerSelector.getSelectionModel().select(0);
                populateReviews(reviewListView, selectedQuestion.getId(), -1);  // Only question reviews
            }
        });

        // Update review list when an answer is selected
        answerSelector.setOnAction(e -> {
            int qIndex = questionSelector.getSelectionModel().getSelectedIndex();
            int aIndex = answerSelector.getSelectionModel().getSelectedIndex() - 1;
            if (qIndex >= 0) {
                int qId = allQuestions.get(qIndex).getId();
                int aId = (aIndex >= 0) ? questionsManager.getAnswersForQuestion(qId).get(aIndex).getId() : -1;
                populateReviews(reviewListView, qId, aId);
            }
        });

        Button addButton = new Button("Add Review");
        addButton.setOnAction(e -> {
            int selectedIndex = questionSelector.getSelectionModel().getSelectedIndex();
            int aIndex = answerSelector.getSelectionModel().getSelectedIndex() - 1;
            if (selectedIndex >= 0 && !reviewInput.getText().isEmpty()) {
                int qId = allQuestions.get(selectedIndex).getId();
                int aId = (aIndex >= 0) ? questionsManager.getAnswersForQuestion(qId).get(aIndex).getId() : -1;
                questionsManager.addReview(currentUser.getUserName(), qId, aId, reviewInput.getText());
                reviewInput.clear();
                populateReviews(reviewListView, qId, aId);
            }
        });

        Button updateButton = new Button("Update Selected Review");
        updateButton.setOnAction(e -> {
            String selected = reviewListView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.contains("[")) {
                int reviewId = extractReviewId(selected);
                questionsManager.updateReview(reviewId, reviewInput.getText());
                reviewInput.clear();
                int qIndex = questionSelector.getSelectionModel().getSelectedIndex();
                int aIndex = answerSelector.getSelectionModel().getSelectedIndex() - 1;
                if (qIndex >= 0) {
                    int qId = allQuestions.get(qIndex).getId();
                    int aId = (aIndex >= 0) ? questionsManager.getAnswersForQuestion(qId).get(aIndex).getId() : -1;
                    populateReviews(reviewListView, qId, aId);
                }
            }
        });

        Button deleteButton = new Button("Delete Selected Review");
        deleteButton.setOnAction(e -> {
            String selected = reviewListView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.contains("[")) {
                int reviewId = extractReviewId(selected);
                questionsManager.deleteReview(reviewId);
                int qIndex = questionSelector.getSelectionModel().getSelectedIndex();
                int aIndex = answerSelector.getSelectionModel().getSelectedIndex() - 1;
                if (qIndex >= 0) {
                    int qId = allQuestions.get(qIndex).getId();
                    int aId = (aIndex >= 0) ? questionsManager.getAnswersForQuestion(qId).get(aIndex).getId() : -1;
                    populateReviews(reviewListView, qId, aId);
                }
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new UserHomePage(currentUser).show(primaryStage));

        layout.getChildren().addAll(
            new Label("Manage Reviews"),
            questionSelector,
            answerSelector,
            reviewListView,
            reviewInput,
            addButton,
            updateButton,
            deleteButton,
            backButton
        );

        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Review Manager");
        primaryStage.show();
    }

    private void populateReviews(ListView<String> reviewListView, int qId, int aId) {
        reviewListView.getItems().clear();
        List<Review> reviews = (aId == -1)
                ? questionsManager.getReviewsForQuestion(qId)
                : questionsManager.getReviewsForAnswer(aId);
        for (Review r : reviews) {
            String prefix = aId == -1 ? "Q" : "A";
            // Use the actual review ID from the Review object.
            reviewListView.getItems().add("[" + r.getReviewId() + "] " + prefix + " (" + r.getReviewerUsername() + "): " + r.getReviewText());
        }
    }

    private int extractReviewId(String text) {
        // Assumes text starts with [<reviewId>] 
        try {
            int start = text.indexOf('[') + 1;
            int end = text.indexOf(']');
            return Integer.parseInt(text.substring(start, end));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
