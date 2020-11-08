package in.kay.edvora.Models;

public class User {
    String _id;
    String name;
    String college;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User(String _id, String name, String college, String imageUrl) {
        this._id = _id;
        this.name = name;
        this.college = college;
        this.imageUrl = imageUrl;
    }

    String imageUrl;
}
