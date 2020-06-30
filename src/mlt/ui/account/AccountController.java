package mlt.ui.account;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import mlt.db.DbHandler;
import mlt.ui.MyAlert.MyAlert;
import mlt.ui.support.GetStage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountController implements Initializable {

    @FXML
    private JFXTextField txtuname;

    @FXML
    private JFXTextField txtemail;

//    @FXML
//    private JFXPasswordField txtpassword;

    @FXML
    private JFXTextField txtmob;

    @FXML
    private JFXTextField txtabout;

    @FXML
    private JFXTextArea txtadd;

    @FXML
    private JFXDatePicker dpdob;


    @FXML
    private JFXButton btnedit;

    @FXML
    private JFXButton btnupdate;

    @FXML
    private JFXButton btncancel;

    @FXML
    private JFXButton btnedita;

    @FXML
    private JFXButton btnupdatea;

    @FXML
    private JFXButton btncancela;

//    @FXML
//    private JFXButton btnshowpassword;
//
//    @FXML
//    private JFXButton btnshowpassword1;
//    @FXML
//    private Label lblshowpassword;

//    @FXML
//    private ImageView imageprofile;

    private boolean showpass = false;

    private String uname;


    @FXML
    private Circle circleProfile;



    @FXML
    private WebView webview;

    public void setData(String uname)
    {
        this.uname = uname;
        try{
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from login where username = ?");
            ps.setString(1,uname);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                txtuname.setText(rs.getString("username"));
                txtemail.setText(rs.getString("email"));
            //    txtpassword.setText(rs.getString("password"));
                txtmob.setText(rs.getString("mob"));
            }
            ps = DbHandler.getConnection().prepareStatement("select * from account where username = ?");
            ps.setString(1,uname);
            rs = ps.executeQuery();
            while (rs.next()) {
                Date date = rs.getDate("dob");
                if (rs.getDate("dob") != null)
                     dpdob.setValue(rs.getDate("dob").toLocalDate());
                if (!rs.getString("about").equals(""))
                     txtabout.setText(rs.getString("about"));
                if (!rs.getString("address").equals(""))
                    txtadd.setText(rs.getString("address"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    @FXML
//    void showpassword(ActionEvent event) {
//
//
//        if (!showpass)
//        {
//
//            showpass = true;
//            btnshowpassword.setVisible(false);
//            btnshowpassword1.setVisible(true);
//            lblshowpassword.setVisible(true);
//        }
//        else
//        {
//
//            lblshowpassword.setVisible(false);
//            btnshowpassword1.setVisible(false);
//            btnshowpassword.setVisible(true);
//            showpass = false;
//        }
//    }
        @FXML
    void cancel(ActionEvent event) {
        btnedit.setVisible(true);
        btncancel.setVisible(false);
        btnupdate.setVisible(false);

        txtemail.setEditable(false);
        txtmob.setEditable(false);
        txtabout.setEditable(false);
        //txtpassword.setEditable(false);

        setData(uname);
    }

    @FXML
    void canceladditional(ActionEvent event) {
        dpdob.setDisable(true);
        txtadd.setEditable(false);
        txtabout.setEditable(false);

        btnedita.setVisible(true);
        btncancela.setVisible(false);
        btnupdatea.setVisible(false);
    }

    @FXML
    void edit(ActionEvent event) {
        txtemail.setEditable(true);
        txtmob.setEditable(true);
        txtabout.setEditable(true);
  //      txtpassword.setEditable(true);

        btnedit.setVisible(false);
        btncancel.setVisible(true);
        btnupdate.setVisible(true);

    }

    @FXML
    void editadditional(ActionEvent event) {
            dpdob.setDisable(false);
            txtadd.setEditable(true);
            txtabout.setEditable(true);

            btnedita.setVisible(false);
            btncancela.setVisible(true);
            btnupdatea.setVisible(true);
    }

    public boolean checkUserProfile(String uname) throws SQLException {

        PreparedStatement ps = DbHandler.getConnection().prepareStatement("select count(*) from profilephoto where username = ?");
        ps.setString(1,uname);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            if (rs.getInt(1) == 1)
                return true;
            return false;
        }
        return true;
    }

    @FXML
    void chooseProfilePhoto(ActionEvent event) throws IOException {
       try  {

           // to choose file from file browser
           FileChooser fileChooser = new FileChooser();
           fileChooser.setTitle("Open Resource File");
           File selectedFile = fileChooser.showOpenDialog(GetStage.getStage(event));
           if (selectedFile != null) {

               //filling image in circleAvatar
               circleProfile.setFill(new ImagePattern(new Image(new FileInputStream(selectedFile))));
                if (!checkUserProfile(uname)){
                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("INSERT INTO profilephoto(username,path) values(?,?)");
                    ps.setString(1,uname);
                    ps.setString(2,selectedFile.getAbsolutePath());
                    ps.execute();
                }
                else
                {
                    // updating path of profile photo in Database
                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("update profilephoto set path = ? where username = ?");
                    ps.setString(2,uname);
                    ps.setString(1,selectedFile.getAbsolutePath());
                    ps.execute();
                }

                // storing reference of work done
                   PreparedStatement ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,current_date())");
                   ps.setString(1,uname);
                   ps.setString(2,uname+ " updated profile");
                   ps.execute();
           }
           else{
               new MyAlert().createAlert(" Some thing Went Wrong Choose File Again ","Info",event);
           }
       } catch (SQLException e) {
           e.printStackTrace();
           new MyAlert().createAlert("Failed with some Issue ","Error",event);
       }
    }
    @FXML
    void update(ActionEvent event) {

        JFXButton yesbutton = new JFXButton("Yes");
        JFXButton nobutton = new JFXButton("No");
        List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

        yesbutton.setOnAction(event3 ->{
            try {


                String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

                Pattern pattern = Pattern.compile(regex);


                Matcher matcher = pattern.matcher(txtemail.getText());

                if (matcher.matches()){


                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("UPDATE login set mob =? , email = ? where username = ?");
                    ps.setString(1,txtmob.getText());
                    ps.setString(2,txtemail.getText());
                    //  ps.setString(3,txtpassword.getText());
                    ps.setString(3,txtuname.getText());
                    ps.execute();

                    ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                    ps.setString(1,uname);
                    ps.setString(2,uname+ " updated profile");
                    ps.execute();

                    setData(uname);
                    cancel(event);
                }
                else
                    new MyAlert().createAlert(" Provide email in formate (eg abc@mail.com) ","update",event);
            } catch (SQLException e) {
                e.printStackTrace();
                new MyAlert().createAlert("Some Error happened ","update",event);

            }
        });

        nobutton.setOnAction(event3 ->{
            new MyAlert().createAlert(" Operation Cancelled ","update",event);

        });

        new MyAlert().confirm("Do you want to update Profile ?","Update ?",event,listcontrol);


    }

//    @FXML
//    void test(ActionEvent event) {
//        PrinterJob job = PrinterJob.createPrinterJob();
//
//        boolean proceed = job.showPageSetupDialog(GetStage.getStage(event));
//
//        if (proceed)
//        {
//            if(job != null){
////                   job.showPrintDialog(GetStage.getStage(event)); // Window must be your main Stage
////
//                boolean printed = job.printPage(webview);
//
//
//                if (printed)
//                {
//                    job.endJob();
//                }
//            }
//        }
//    }
   @FXML
    void updateadditional(ActionEvent event) {
        JFXButton yesbutton = new JFXButton("Yes");
        JFXButton nobutton = new JFXButton("No");
        List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

        yesbutton.setOnAction(event3 ->{
            try {

                PreparedStatement ps = DbHandler.getConnection().prepareStatement("UPDATE account set dob =?, address = ? , about = ? where username = ?");


                Date d = Date.valueOf(dpdob.getValue());
                ps.setDate(1,d);
                ps.setString(2,txtadd.getText());
                ps.setString(3,txtabout.getText());
                ps.setString(4,txtuname.getText());
                ps.execute();


                ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                ps.setString(1,uname);
                ps.setString(2,uname+ " updated profile");
                ps.execute();

                setData(uname);

                canceladditional(event);
            } catch (SQLException e) {
                e.printStackTrace();
                new MyAlert().createAlert("Some Error happened ","update",event);
            }
        });

        nobutton.setOnAction(event3 ->{
            new MyAlert().createAlert(" Operation Cancelled ","update",event);

        });

        new MyAlert().confirm("Do you want to update Profile ?","Update ?",event,listcontrol);

    }

    public void loadProfilePhoto(){
        boolean isSet = false;
        System.out.println("running");
        try{
            System.out.println("pro : "+ uname);
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT path from profilephoto where username =?");
            ps.setString(1,uname);
//            System.out.println("uname " +uname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
//                System.out.println("in");
                if (!rs.getString("path").equals("")){
//                    System.out.println("working");
                    {
                        circleProfile.setFill(new ImagePattern(new Image(new FileInputStream(rs.getString("path")))));
                        //imageprofile.setImage(new Image(new FileInputStream(rs.getString("path"))));
                        isSet = true;
                        System.out.println("profile img set");
                    }
                }

            }
            if (!isSet) {
                circleProfile.setFill(new ImagePattern(new Image(new FileInputStream("jrxml/user.png"))));
                //   imageprofile.setImage(new Image(new FileInputStream("jrxml/user.png")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        DbHandler.createLoginTable();
//        DbHandler.createAccountTable();


        txtuname.setEditable(false);
        txtemail.setEditable(false);
        txtmob.setEditable(false);
        txtabout.setEditable(false);
        //txtpassword.setEditable(false);

        dpdob.setDisable(true);
        txtadd.setEditable(false);


        // lblshowpassword.setVisible(false);


//        txtpassword.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    lblshowpassword.setText(newValue);
//            }
//        });

//        WebEngine webEngine = webview.getEngine();
//        File file = new File("C:\\Users\\Amit\\IdeaProjects\\mlt2.2\\mlt2.2\\src\\mlt\\web\\mlt_help_page\\index.html");
//        URL url= null;
//        try {
//            url = file.toURI().toURL();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        webEngine.load(url.toString());


      //  imageprofile.getStyleClass().add("circleImage");
//
//        circleProfile.setFill(new ImagePattern(new Image("/mlt/ui/account/user.png")));
        circleProfile.setStyle("-fx-padding: 20px;");
        circleProfile.getStyleClass().add("circleBorder");
        circleProfile.setEffect(new DropShadow(+10d,0d,+2d, Color.valueOf("#3E65FF")));
    }
}
