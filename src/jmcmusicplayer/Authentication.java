package jmcmusicplayer;

public class Authentication {
    /**
     * Authenticates user and return boolean
     * @param username
     * @param rawPassword
     * @param admin
     * @return
     */
    public static boolean Authenticate(String username, String rawPassword, User admin) {
        String hashedPassword = HashUtil.getHashedPassword(rawPassword, username);
        if (admin.getPassword().equals(hashedPassword)) {
            return true;
        } else {
            return false;
        }
    }
}
