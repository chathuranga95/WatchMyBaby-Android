package utill;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chathuranga on 2/2/2018.
 */

public class UserAppSettings {
    private int tel1;
    private int tel2;
    private boolean notification;
    private boolean SMS;
    private HashMap<String, Integer> fileList = new HashMap();


    public UserAppSettings() {

    }

    public HashMap<String, Integer> getFileList() {
        return fileList;
    }

    public UserAppSettings(int tel1, int tel2) {
        this.tel1 = tel1;
        this.tel2 = tel2;
    }

    public void addFile(String fileName, int time) {

        for (String file: fileList.keySet()) {
            System.out.println(file);
        }
        fileList.put(fileName,time);
        for (String file: fileList.keySet()) {
            System.out.println(file);
        }
        System.out.println();
    }

    public void editFileTime(String fileName, int time){
        fileList.remove(fileName);
        fileList.put(fileName, time);
    }

    public void removeFile(String fileName){
        fileList.remove(fileName);
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
