package in.kay.edvora.Models;

import java.util.List;

public class HomeModel {
    PostedBy postedBy;
    String _id;
    String question;
    String subject;
    String topic;
    String createdAt;
    String imageUrl;
    List<Answers> answers;

    public HomeModel(PostedBy postedBy, String _id, String question, String subject, String topic, String createdAt, String imageUrl, List<Answers> answers) {
        this.postedBy = postedBy;
        this._id = _id;
        this.question = question;
        this.subject = subject;
        this.topic = topic;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
        this.answers = answers;
    }

    public PostedBy getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(PostedBy postedBy) {
        this.postedBy = postedBy;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Answers> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answers> answers) {
        this.answers = answers;
    }
}