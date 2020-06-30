package mlt.ui.login.logic;

import java.security.NoSuchAlgorithmException;

public class BcryptHashingExample
{
    public static void main(String[] args) throws NoSuchAlgorithmException
    {
        String  originalPassword = "admin";
        String generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
        System.out.println(generatedSecuredPasswordHash);
         generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
        System.out.println(generatedSecuredPasswordHash);
         generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
        System.out.println(generatedSecuredPasswordHash);
         generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
        System.out.println(generatedSecuredPasswordHash);

        boolean matched = BCrypt.checkpw(originalPassword, BCrypt.hashpw(originalPassword, BCrypt.gensalt(12)));
        System.out.println(matched);
    }
}
