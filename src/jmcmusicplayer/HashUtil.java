package jmcmusicplayer;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class HashUtil {
    // hash algorithm
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    // stretching number
    private static final int ITERATION_COUNT = 10000;
    // length of key
    private static final int KEY_LENGTH = 256;

    /**
     * Creates hashed password and return it
     * @param password
     * @param salt
     * @return
     */
    public static String getHashedPassword(String password, String salt) {
        // create prepare variables in order to create PBEKey
        char[] passCharAry = password.toCharArray();
        byte[] hashedSalt = getSalt(salt);

        // Create PBEKey
        PBEKeySpec keySpec = new PBEKeySpec(passCharAry, hashedSalt, ITERATION_COUNT, KEY_LENGTH);
        // to manage secret key
        SecretKeyFactory skf = null;

        try {
            // create an object to convert secretkey of algorithm
            skf = SecretKeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // interface of secret key
        // it can manage all of the secret keys
        SecretKey secretKey = null;
        try {
            // create secret key
            secretKey = skf.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        // encode secret key
        byte[] passByteAry = secretKey.getEncoded();

        // convert bytes to Hexadecimal
        StringBuffer sf = new StringBuffer(64);
        for (byte b : passByteAry) {
            sf.append(String.format("%02x", b & 0xff));
        }
        return sf.toString();
    }

    /**
     * Creates salt
     * @param salt
     * @return
     */
    private static byte[] getSalt(String salt) {
        byte[] saltBytes = null;
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(messageDigest != null) {
            messageDigest.update(salt.getBytes());
            saltBytes = messageDigest.digest();
        }
        return saltBytes;
    }
}
