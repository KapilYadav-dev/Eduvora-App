package in.kay.edvora.Models;

public class Answers {
    String answer;
    String answeredAt;
  //  String user;

    public Answers(String answer, String answeredAt, String user) {
        this.answer = answer;
        this.answeredAt = answeredAt;
      //  this.user = user;
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

  /*  public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

   */
}
