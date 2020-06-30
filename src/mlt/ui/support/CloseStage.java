package mlt.ui.support;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class CloseStage {

    // to close the stage on event
    public  static void closeStage(ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    //to close stage on nodes
    // overloaded version of above
    public  static void closeStage(Node node){
        Stage stage = (Stage) (node).getScene().getWindow();
        stage.close();
    }
}
