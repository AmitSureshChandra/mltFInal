package mlt.ui.login;

import mlt.db.DbHandler;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

public class sendMail {

    public void send() throws UnsupportedEncodingException, MessagingException {
        final String username = "akumaro*****@gmail.com";
        final String password ="*********";

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


        int otp = generateotp();
        Message m = new MimeMessage(session);
        m.setFrom(new InternetAddress("ak*******@gmail.com","MLT Generator"));
        m.setRecipient(Message.RecipientType.TO,new InternetAddress("a******@gmail.com"));
        m.setSubject("OTP verification for MLT Generator System");
        m.setText("OTP for verification : "+  otp +" , valid for next 5 minutes");
        Transport.send(m);
    }

    int generateotp()
    {
        Random r = new Random();
        int otp = 100000 + Math.abs( r.nextInt()%900000);
        return otp;
    }

    public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
        new sendMail().send();
    }
}
