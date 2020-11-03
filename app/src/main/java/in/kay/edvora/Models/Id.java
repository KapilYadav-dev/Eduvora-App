package in.kay.edvora.Models;

public class Id {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Id(String name, String id, String college) {
        this.name = name;
        this.id = id;
        this.college = college;
    }

    String name;
     String  id;;
     String college;
}
