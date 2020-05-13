package jmcmusicplayer;

public class User {
    private String username;

    public String getPassword() {
        return password;
    }

    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}
