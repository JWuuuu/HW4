package application;

import java.util.ArrayList;
import java.util.List;

/**
 * MessageManager handles storing and retrieving private messages.
 */
public class MessageManager {
    private static MessageManager instance;
    private final List<Message> messages; // Stores all messages
    
    private int totalMessages; // stores the total number of messages sent by anyone, used to order messages by how recent they are.


    private MessageManager() {
        this.messages = new ArrayList<>();
    }

    public static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager();
        }
        return instance;
    }
    
    // addMessage adds a new message to the messages list.
    public void addMessage(String content, String sender, String receiver) {
        if (content == null || content.isEmpty()) {
            System.out.println("Error: Message must have content.");
            return;
        }
        if (sender == null || receiver == null || receiver.isEmpty()) {
        	System.out.println("Error: User was not found.");
        	return;
        }
        Message newMessage = new Message(totalMessages + 1, content, sender, receiver);
        messages.add(newMessage);
        totalMessages++;
        System.out.println("New message sent by " + sender + ": to " + receiver + ".");
    }
    
    public List<Message> getMessages() {
        return messages;
    }
}