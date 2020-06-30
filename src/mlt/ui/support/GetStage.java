package mlt.ui.support;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;


public class GetStage {

    // Get Stage from event object
    public  static  Stage getStage(ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        return stage;
    }

    //get stage from node object
    // overloaded version of above
    public  static  Stage getStage(Node node){
        Stage stage = (Stage) (node.getScene().getWindow());
        return stage;
    }
}
