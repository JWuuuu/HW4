package application;

public class Message {
	
    private int id;
    private String messageContent;
    private String fromUser;
    private String toUser;

    public Message(int id, String messageContent, String fromUser, String toUser) {
        this.id = id;
        this.messageContent = messageContent;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public int getId() { return id; }
    public String getContent() { return messageContent; }
    public String getSender() { return fromUser; }
    public String getReceiver() { return toUser; }


    public void setContent(String content) { this.messageContent = content; }

    public String toString() {
        return this.fromUser + " -> " + this.toUser + ": " + this.messageContent;
    }


}