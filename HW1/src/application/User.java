package application;

import java.util.Random;

/**
 * The User class represents a user entity in the system.
 */
public class User {
    private String userName;
    private String password;
    private String role;
    private int permID;  // Add permID field
    private boolean reviewerRequest;  // Indicates if user has requested reviewer role
    private int reviewerWeight;       // Weightage value for the reviewer (higher means more preferred)

    // Default constructor
    public User() {
        this.userName = "Guest";
        this.password = "";
        this.role = "user";
        this.permID = generateRandomPermID();  // Generate random permID
        this.reviewerRequest = false;  // Default: no reviewer request
        this.reviewerWeight = 0;       // Default weightage
    }

    // Overloaded constructor
    public User(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.permID = generateRandomPermID();  // Generate random permID
        this.reviewerRequest = false;  // Default: no reviewer request
        this.reviewerWeight = 0;       // Default weightage
    }

    // Generate a random permID between 1 and 500,000,000
    private int generateRandomPermID() {
        Random random = new Random();
        return random.nextInt(500000000) + 1; // Random number between 1 and 500M
    }

    // Getter methods
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public int getPermID() {
        return permID;
    }
    
    /**
     * Returns whether the user has requested reviewer status.
     * @return true if reviewer request is active, false otherwise.
     */
    public boolean hasRequestedReviewer() {
        return reviewerRequest;
    }
    
    /**
     * Returns the reviewer weightage value.
     * @return the reviewer weight.
     */
    public int getReviewerWeight() {
        return reviewerWeight;
    }

    // Setter methods
    public void setRole(String role) {
        this.role = role;
    }
    
    /**
     * Sets the reviewer request status.
     * @param requested true to request reviewer status, false otherwise.
     */
    public void setReviewerRequest(boolean requested) {
        this.reviewerRequest = requested;
    }
    
    /**
     * Sets the reviewer weightage value.
     * @param weight the new weight value.
     */
    public void setReviewerWeight(int weight) {
        this.reviewerWeight = weight;
    }
}