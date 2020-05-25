package sample;

import org.json.simple.JSONObject;

public class UserInfo {

    private int id = 0;
    private String email = null;
    private String uname = null;
    private String firstname = null;
    private String lastname = null;
    private int fid = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public UserInfo() {
    }

    public UserInfo(int id, String email, String uname, String firstname, String lastname, int fid) {
        this.id = id;
        this.email = email;
        this.uname = uname;
        this.firstname = firstname;
        this.lastname = lastname;
        this.fid = fid;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("email", email);
        obj.put("uname", uname);
        obj.put("firstname", firstname);
        obj.put("lastname", lastname);
        obj.put("fid", fid);
        return obj;
    }
}
