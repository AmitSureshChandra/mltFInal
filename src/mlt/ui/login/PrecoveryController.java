package mlt.ui.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import mlt.db.DbHandler;
import mlt.ui.MyAlert.MyAlert;
import mlt.ui.login.logic.BCrypt;
import mlt.ui.login.logic.SHAExample;
import mlt.ui.support.GetStage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

public class PrecoveryController implements Initializable {

    @FXML
    private JFXButton btnsendotp;

    @FXML
    private JFXButton btnverifyotp;

    @FXML
    private JFXButton btnsave;

    @FXML
    private JFXPasswordField txtpassword;

    @FXML
    private JFXTextField txtotp;

    @FXML
    private JFXTextField txtuid;

    @FXML
    private BorderPane mainborderpane;

    private  String uname,passwd,email;

    private long sendTime, receiveTime;

    private int otp;

    @FXML
    void savePassword(ActionEvent event) {
        if (!txtpassword.getText().equals("")){
            try {
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("update login set password = ? where username = ?");
                ps.setString(1, SHAExample.get_SHA_256_SecurePassword(txtpassword.getText(), "[B@6e2c634b3385185f8ac69287b02c7cc9e6c5385a4706c1fb7e5aaf44d38a33f2cd5bc193"));
                ps.setString(2,uname);
                ps.execute();
                new MyAlert().createAlert("New Password Updated","Info",event);

                GetStage.getStage(event).close();
            } catch (SQLException e) {
                e.printStackTrace();
                new MyAlert().createAlert("Database Error... try Again","Alert",event);
            }
        }else
            new MyAlert().createAlert("please enter Password  ","Alert",event);

    }

    @FXML
    void sendOtp(ActionEvent event) {

        try {
            if (!txtuid.getText().equals("") && verifyUser())
            {


                PreparedStatement ps = DbHandler.getConnection().prepareStatement("select email from login where username = ?");
                ps.setString(1,uname);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    if (!(rs.getString("email").equals(null)|| rs.getString("email").equals("") )){
//                        try {
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

                            ps = DbHandler.getConnection().prepareStatement("SELECT email from login where username = ?");
                            ps.setString(1,txtuid.getText());
                            rs = ps.executeQuery();
                            while (rs.next()){
                                email = rs.getString("email");
                            }

                        System.out.println(email);


                            otp = generateotp();
                            Message m = new MimeMessage(session);
                            m.setFrom(new InternetAddress("akumar00029@gmail.com","MLT Generator"));
                            m.setRecipient(Message.RecipientType.TO,new InternetAddress(email));
                            m.setSubject("OTP verification for MLT Generator System");
                            m.setText("OTP for verification : "+  otp +" , valid for next 5 minutes");
                            Transport.send(m);

                            sendTime = Calendar.getInstance().getTimeInMillis();
                            txtotp.setDisable(false);
                            btnverifyotp.setDisable(false);
//                        }
//                        catch(MessagingException e){
//                         //   e.printStackTrace();
//                            new MyAlert().createAlert("Check your Internet Connectivity ...","Alert",event);
//                        }
//                        catch (Exception e){
//
//                            e.printStackTrace();
//                        }
                    }
                    else
                        new MyAlert().createAlert("Email unavailable to send OTP ","Alert",event);
                }
                else
                    new MyAlert().createAlert("Some Error Occured  ","Alert",event);
            }
            else
                new MyAlert().createAlert("please enter Correct UserID  ","Alert",event);
        } catch (SQLException e) {
            e.printStackTrace();
            new MyAlert().createAlert("Some Error Occured ","Alert",event);
        }
        catch (Exception exc){
            new MyAlert().createAlert("Please try Again","Alert",event);
        }
    }

    static int generateotp()
    {
        Random r = new Random();
        int otp = 100000 + Math.abs( r.nextInt()%900000);
        return otp;
    }

    @FXML
    void verifyOtp(ActionEvent event) {
        if (!txtotp.getText().equals("")){
            receiveTime = Calendar.getInstance().getTimeInMillis();
            if (receiveTime - sendTime < 300000){

//                System.out.println(txtotp.getText());
//                System.out.println(otp);

               if (txtotp.getText().equals(otp+"")){
                   btnsave.setDisable(false);
                   txtpassword.setDisable(false);
               }
               else
                   new MyAlert().createAlert("Wrong OTP ","Alert",event);
            }
            else
                new MyAlert().createAlert("OTP Expired ","Alert",event);
        }
        else
            new MyAlert().createAlert("Please enter OTP ","Alert",event);
    }


    boolean verifyUser() throws SQLException {
        if (!txtuid.getText().equals("")){
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select count(*) from login where username = ?");
            ps.setString(1,txtuid.getText());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if (rs.getInt(1)==1){
                    {
                        uname = txtuid.getText();
                        return true;
                    }
                }
                else
                    return false;
            }
        }
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
