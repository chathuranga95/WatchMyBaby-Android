package utill;

/**
 * Created by chathuranga on 2/2/2018.
 */

public class User {
    private String Name;
    private String userName;
    private String password;
    private String email;
    private int tel;
    private UserAppSettings settings;

    public User() {
    }

    public User(String name, String userName, String password, String email, int tel) {
        Name = name;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.settings = null;
    }

    public String getDetailString(){
        String details;
        details = Name + " " + userName + " " + password + " " + email + " " + tel;
        return details;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public UserAppSettings getSettings() {
        return settings;
    }

    public void setSettings(int tel1, int tel2) {
        this.settings = new UserAppSettings(tel1, tel2);
    }
}