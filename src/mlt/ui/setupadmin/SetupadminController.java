package mlt.ui.setupadmin;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import mlt.ui.MyAlert.MyAlert;
import mlt.ui.support.GetStage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SetupadminController implements Initializable {

    @FXML
    private BorderPane mainborderpane;

    @FXML
    private JFXTextField txtuname;

    @FXML
    void setup(ActionEvent event) throws IOException {
        try {
            if (!txtuname.getText().equals(""))
            {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");

                File selectedFile = fileChooser.showOpenDialog(GetStage.getStage(event));
                if (selectedFile != null) {
                    Process p= Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"mysql -u root -p meritlist < "+ selectedFile.getAbsolutePath()+"\"");
                    p.waitFor();
                    new MyAlert().createAlert("Setup Done","Info",event);
                    GetStage.getStage(event).close();
                }
            }
            else
                new MyAlert().createAlert("Enter the Database Username ","Alert",event);
        }
        catch (Exception e){
            new MyAlert().createAlert("Something Wrong Happened ... try Again .. ","Alert",event);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
