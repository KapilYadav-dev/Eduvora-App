package in.kay.edvora.Models;

public class Answers {
    String answer;
    String answeredAt;
    User user;

    public Answers(String answer, String answeredAt, User user) {
        this.answer = answer;
        this.answeredAt = answeredAt;
        this.user = user;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(String answeredAt) {
        this.answeredAt = answeredAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
