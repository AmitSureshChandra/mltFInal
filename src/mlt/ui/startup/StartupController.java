package mlt.ui.startup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import mlt.ui.support.CloseStage;
import mlt.ui.support.CreateStage;

import java.net.URL;
import java.util.ResourceBundle;

public class StartupController implements Initializable {

    @FXML
    private BorderPane mainborderpane;

    @FXML
    private Label lblmltheader;

    @FXML
    private Label lblopening;

    @FXML
    void loginAdmin(ActionEvent event) {
        new CreateStage().createStage("Admin Login","/mlt/dbadmin/dbadmin.fxml",null,0,false,false,false);
        CloseStage.closeStage(event);
    }

    @FXML
    void loginTeacher(ActionEvent event) {
        new CreateStage().createStage("Teacher Login","/mlt/ui/login/Login.fxml",null,0,false,false,false);
        CloseStage.closeStage(event);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
