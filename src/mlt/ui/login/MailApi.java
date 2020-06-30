package mlt.ui.login;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class MailApi {


    static int generateotp()
    {
        Random r = new Random();
        int otp = 100000 + Math.abs( r.nextInt()%900000);
        return otp;
    }
    public static void main(String[] args) {

        final String username = "akumar00029@gmail.com";
        final String password ="Amit@Google34.com";

        Properties prop = new Properties();
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port","587");
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.starttls.enable","true");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });
        try {
            Message m = new MimeMessage(session);
            m.setFrom(new InternetAddress("akumar00029@gmail.com","MLT Generator"));
            m.setRecipient(Message.RecipientType.TO,new InternetAddress("akumar00029@gmail.com"));
            m.setSubject("OTP verification for MLT Generator System");
            m.setText("OTP for verification : "+ generateotp() +" , valid for next 5 minutes");
            Transport.send(m);
            System.out.println("message send successful");
        }
        catch (Exception e){
           e.printStackTrace();
        }


    }

}
