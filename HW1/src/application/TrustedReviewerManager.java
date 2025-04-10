package application;

import java.util.*;

public class TrustedReviewerManager {
    private static final Map<String, Set<String>> trustedReviewers = new HashMap<>();

    public static void addTrustedReviewer(String studentUserName, String reviewerUserName) {
        trustedReviewers.computeIfAbsent(studentUserName, k -> new HashSet<>()).add(reviewerUserName);
    }

    public static void removeTrustedReviewer(String studentUserName, String reviewerUserName) {
        if (trustedReviewers.containsKey(studentUserName)) {
            trustedReviewers.get(studentUserName).remove(reviewerUserName);
        }
    }

    public static Set<String> getTrustedReviewers(String studentUserName) {
        return trustedReviewers.getOrDefault(studentUserName, new HashSet<>());
    }

    public static boolean isTrustedReviewer(String studentUserName, String reviewerUserName) {
        return getTrustedReviewers(studentUserName).contains(reviewerUserName);
    }
}

