package in.kay.edvora.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class HomeModel {
    String _id;
    String question;
    String subject;
    String topic;
  //  JSONArray answers;
 //   JSONArray likes;
 //   JSONArray bookmarks;
    String createdAt;

    public JSONObject getPostedBy() {
        //  return postedBy;
          return null;
    }

    public void setPostedBy(JSONObject postedBy) {
        //  this.postedBy = postedBy;
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

    public JSONArray getAnswers() {
        //    return answers;
        return null;
    }

    public void setAnswers(JSONArray answers) {
        //     this.answers = answers;
    }

    public JSONArray getLikes() {
        //    return likes;
        return null;
    }

    public void setLikes(JSONArray likes) {
        //    this.likes = likes;
    }

    public JSONArray getBookmarks() {
        // return bookmarks;
        return null;
    }

    public void setBookmarks(JSONArray bookmarks) {
        //  this.bookmarks = bookmarks;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public HomeModel(JSONObject postedBy, String _id, String question, String subject, String topic, JSONArray answers, JSONArray likes, JSONArray bookmarks, String createdAt) {
        //   this.postedBy = postedBy;
        this._id = _id;
        this.question = question;
        this.subject = subject;
        this.topic = topic;
        //    this.answers = answers;
        //   this.likes = likes;
    //    this.bookmarks = bookmarks;
        this.createdAt = createdAt;
    }
}
