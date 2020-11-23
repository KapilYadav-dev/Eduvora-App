package in.kay.edvora.Models;

import android.content.Intent;

public class FindLibraryBody {
    String type,subject;
    Intent year;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Intent getYear() {
        return year;
    }

    public void setYear(Intent year) {
        this.year = year;
    }
}
