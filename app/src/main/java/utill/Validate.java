package utill;

/**
 * Created by chathuranga on 2/2/2018.
 */

public class Validate {

    public boolean validateUser(User user, String reTypePsw){
        return isEmailVaid(user.getEmail())&& isPswMatch(user.getPassword(),reTypePsw) && isTelValid(user.getTel()) &&isUsernameValid(user.getUserName());
    }

    public boolean isTelValid(int tel){
        String teltxt = String.valueOf(tel);
        return (teltxt.length() == 9);
    }

    public boolean isPswMatch(String psw, String reTypePsw){
        return (psw.equals(reTypePsw));
    }

    public boolean isEmailVaid(String email){
        return email.contains("@");
    }

    public boolean isUsernameValid(String username){
        //TODO: implement validation such that no two users can take same username.
        return true;
    }
}