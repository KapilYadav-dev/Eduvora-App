package in.kay.edvora.Models;

import java.util.List;

import okhttp3.ResponseBody;

public class HomeModel {
    PostedBy postedBy;

    public PostedBy getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(PostedBy postedBy) {
        this.postedBy = postedBy;
    }

    public HomeModel(PostedBy postedBy) {
        this.postedBy = postedBy;
    }

    String _id;
    String question;
    String subject;
    String topic;
    String createdAt;


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

    public HomeModel(ResponseBody postedBy, String _id, String question, String subject, String topic, String createdAt) {
        this._id = _id;
        this.question = question;
        this.subject = subject;
        this.topic = topic;
        this.createdAt = createdAt;
    }
}