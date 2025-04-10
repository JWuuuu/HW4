package application;

import static org.junit.jupiter.api.Assertions.*; 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
class StaffFunctionsTest {
    private QuestionsManager manager;

    @BeforeEach
    public void setUp() {
        manager = QuestionsManager.getInstance();
        manager.getQuestions().clear(); // Reset for isolation
    }

    @Test
    public void testAddAndDeleteQuestion() {
        manager.addQuestion("Staff Test Title", "Content for testing");
        List<Question> questions = manager.getQuestions();
        assertEquals(1, questions.size());

        int questionId = questions.get(0).getId();
        manager.deleteQuestion(questionId);

        questions = manager.getQuestions();
        assertEquals(0, questions.size());
    }

    @Test
    public void testUpdateQuestionTitle() {
        manager.addQuestion("Old Title", "Content");
        int questionId = manager.getQuestions().get(0).getId();

        manager.updateQuestionTitle(questionId, "New Updated Title");
        assertEquals("New Updated Title", manager.getQuestions().get(0).getTitle());
    }

}
