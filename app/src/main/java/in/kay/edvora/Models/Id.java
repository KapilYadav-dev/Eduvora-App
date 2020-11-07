package in.kay.edvora.Models;

public class Id {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Id(String name, String college, String _id, String imageUrl) {
        this.name = name;
        this.college = college;
        this._id=_id;
        this.imageUrl=imageUrl;
    }

    String name;
     String college;
     String _id;
     String imageUrl;
}
