package application;

public class Answer {
    private int id;
    private int questionId;
    private String content;
    private String owner;
    private boolean pinStatus;

    public Answer(int id, int questionId, String content, String owner) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.owner = owner;
        this.pinStatus = false;
    }

    public int getId() { return id; }
    public int getQuestionId() { return questionId; }
    public String getContent() { return content; }
    public String getOwner() { return owner; }
    public boolean getPin() { return pinStatus; }

    public void togglePin() {
        this.pinStatus = !this.pinStatus;
    }

    public void setContent(String content) { this.content = content; }

    public String getPinDisplayText() {
        String prefix = pinStatus ? "*" : " ";
        return prefix + "<" + this.owner + ">: " + this.content;
    }


}