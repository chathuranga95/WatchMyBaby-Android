package utill;

/**
 * Created by chathuranga on 2/2/2018.
 */

public class UserAppSettings {
    private int tel1;
    private int tel2;
    private boolean notification;
    private boolean SMS;

    public UserAppSettings(int tel1, int tel2) {
        this.tel1 = tel1;
        this.tel2 = tel2;
    }

    public int getTel1() {
        return tel1;
    }

    public void setTel1(int tel1) {
        this.tel1 = tel1;
    }

    public int getTel2() {
        return tel2;
    }

    public void setTel2(int tel2) {
        this.tel2 = tel2;
    }

    public boolean isNotificationON() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public boolean isSMSON() {
        return SMS;
    }

    public void setSMS(boolean SMS) {
        this.SMS = SMS;
    }
}
