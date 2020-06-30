package mlt.ui.MyAlert;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertController implements Initializable {

    @FXML
    private Label lblmsg;

    @FXML
    private Button btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMsg(String msg){

        lblmsg.setText(msg);
    }

}
