package mlt.ui.login.logic;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class SHAExample {

    public static void main(String[] args) throws NoSuchAlgorithmException {
//        String passwordToHash = "password";
//        String salt = getSalt();
//
//        System.out.printf(getSalt());
////        String securePassword = get_SHA_1_SecurePassword(passwordToHash, salt);
////        System.out.println(securePassword);
//
//        String securePassword = get_SHA_256_SecurePassword(passwordToHash, "[B@6e2c634b3385185f8ac69287b02c7cc9e6c5385a4706c1fb7e5aaf44d38a33f2cd5bc193");
//        System.out.println(securePassword);
//
//        securePassword = get_SHA_256_SecurePassword(passwordToHash, salt);
//        System.out.println(securePassword);
//
//        securePassword = get_SHA_256_SecurePassword(passwordToHash, salt);
//        System.out.println(securePassword);
//
//        securePassword = get_SHA_256_SecurePassword(passwordToHash, salt);
//        System.out.println(securePassword);
//        System.out.println(securePassword.equals(get_SHA_256_SecurePassword(passwordToHash, salt)));

        System.out.println(SHAExample.get_SHA_256_SecurePassword("teacher", "[B@6e2c634b3385185f8ac69287b02c7cc9e6c5385a4706c1fb7e5aaf44d38a33f2cd5bc193"));
//
//        securePassword = get_SHA_384_SecurePassword(passwordToHash, salt);
//        System.out.println(securePassword);
//
//        securePassword = get_SHA_512_SecurePassword(passwordToHash, salt);
//        System.out.println(securePassword);
    }

    private static String get_SHA_1_SecurePassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static String get_SHA_256_SecurePassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private static String get_SHA_384_SecurePassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private static String get_SHA_512_SecurePassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    private static String getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
}
