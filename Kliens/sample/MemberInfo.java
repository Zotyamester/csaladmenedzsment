package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MemberInfo {
    private StringProperty email;
    private StringProperty uname;
    private StringProperty firstname;
    private StringProperty lastname;

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty unameProperty() {
        return uname;
    }

    public StringProperty firstnameProperty() {
        return firstname;
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    public MemberInfo(String email, String uname, String firstname, String lastname) {
        this.email = new SimpleStringProperty(email);
        this.uname = new SimpleStringProperty(uname);
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
    }
}
