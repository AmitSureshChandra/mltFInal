package mlt.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import mlt.db.DbHandler;


import mlt.ui.MyAlert.MyAlert;
import mlt.ui.support.CloseStage;
import mlt.ui.support.CreateStage;
import mlt.ui.support.GetStage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private JFXTextField txtuname;

    @FXML
    private JFXTextField txtemail;

    @FXML
    private JFXTextField txtmob;

    @FXML
    private JFXPasswordField txtpassword;
    @FXML
    private JFXPasswordField txtcpassword;

    @FXML
    private JFXTextField txtemail2;

    @FXML
    private JFXTextField txtotp;

    @FXML
    private Tab tabuserinfo;

    @FXML
    private Tab tabcreateuser;

    @FXML
    private TabPane tabpane;

    @FXML
    private BorderPane mainborderpane;

    private String username="",email="",mob="";


    @FXML
    void backToInfo(ActionEvent event) {

        tabpane.getSelectionModel().select(tabuserinfo);
    }

    @FXML
    void backToLogin(ActionEvent event) {
        new CreateStage().createStage("Login","/mlt/ui/login/login.fxml",null,0,false,false,false);
        CloseStage.closeStage(event);
    }

    @FXML
    void login(ActionEvent event) {

        backToLogin(event);
    }

    @FXML
    void next(ActionEvent event) {
        if (!(txtuname.getText().equals("") || txtemail.getText().equals("") || txtmob.getText().equals(""))){
            tabpane.getSelectionModel().select(tabcreateuser);
            username = txtuname.getText();
            email = txtemail.getText();
            mob = txtemail.getText();
        }
        else
            new MyAlert().createAlert("Please specify Username, Email and Contact number","Unspecified Data", GetStage.getStage(event));
    }

    @FXML
    void sendOtp(ActionEvent event) {

    }

    @FXML
    void signup(ActionEvent event) {

    }

    @FXML
    void verifyOtp(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            DbHandler.createLoginTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tabpane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {

              System.out.println("first");
              if (newValue.equals(tabcreateuser))
              {
                  if (!(txtuname.getText().equals("") || txtemail.getText().equals("") || txtmob.getText().equals(""))){
                      tabpane.getSelectionModel().select(tabcreateuser);
                      username = txtuname.getText();
                      email = txtemail.getText();
                      mob = txtemail.getText();
                  }
                  else
                  {
                      tabpane.getSelectionModel().select(tabuserinfo);
                      new MyAlert().createAlert("Please specify Username, Email and Contact number","Unspecified Data", GetStage.getStage(txtuname));
                  }
              }
            }
        });
    }
}
