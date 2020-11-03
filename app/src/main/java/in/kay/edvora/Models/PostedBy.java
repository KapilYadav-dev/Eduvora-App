package in.kay.edvora.Models;

public class PostedBy {
     Id id;
     String userType;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public PostedBy(Id id, String userType) {
        this.id = id;
        this.userType = userType;
    }
}
