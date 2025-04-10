package application;

import java.time.LocalDateTime;

/**
 * Represents a review of a question or an answer.
 */
public class Review {
    private int reviewId;
    private String reviewerUsername;
    private int questionId; // Set to -1 if not applicable
    private int answerId;   // Set to -1 if not applicable
    private String reviewText;
    private LocalDateTime timestamp;

    public Review(int reviewId, String reviewerUsername, int questionId, int answerId, String reviewText) {
        this.reviewId = reviewId;
        this.reviewerUsername = reviewerUsername;
        this.questionId = questionId;
        this.answerId = answerId;
        this.reviewText = reviewText;
        this.timestamp = LocalDateTime.now();
    }

    public int getReviewId() { return reviewId; }
    public String getReviewerUsername() { return reviewerUsername; }
    public int getQuestionId() { return questionId; }
    public int getAnswerId() { return answerId; }
    public String getReviewText() { return reviewText; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    @Override
    public String toString() {
        return "[" + reviewerUsername + "] " + reviewText + " (" + timestamp + ")";
    }
}