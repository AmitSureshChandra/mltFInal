package mlt.ui.MyAlert;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import mlt.ui.support.GetStage;
import java.util.List;

public class MyAlert {

    public void createAlert(String msg,String title , Stage s){


        if (title == null)
            title = "Alert";

        BoxBlur boxBlur = new BoxBlur(4,4,2);
        BorderPane borderPane = (BorderPane) s.getScene().lookup("#mainborderpane");

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.getStyleClass().addAll("root","border");
        JFXButton button = new JFXButton("OK");
        button.setPrefSize(70,35);
        button.getStyleClass().add("acentbtn");
        button.getStyleClass().add("btn-danger");
        JFXDialog dialog = new JFXDialog((StackPane) s.getScene().getRoot(),dialogLayout,JFXDialog.DialogTransition.TOP);
        Label l = new Label(msg);


        MaterialDesignIconView icon2 = null;
        if (title.contains("rror") || title.contains("lert"))
            if (title.contains("db") || title.contains("DB") || title.contains("Db"))
                icon2 = new MaterialDesignIconView(MaterialDesignIcon.ALERT);
            else
                icon2 = new MaterialDesignIconView(MaterialDesignIcon.ALERT);

        else if (title.contains("Successful"))
            icon2 = new MaterialDesignIconView(MaterialDesignIcon.CHECK_CIRCLE_OUTLINE);
        else if (title.contains("?"))
            icon2 = new MaterialDesignIconView(MaterialDesignIcon.COMMENT_QUESTION_OUTLINE);
        else
            icon2 = new MaterialDesignIconView(MaterialDesignIcon.INFORMATION);
        icon2.setSize("30");

        l.setGraphic(icon2);
        dialogLayout.setHeading(l);

        dialogLayout.setPrefSize(300,100);
        dialogLayout.setActions(button);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
                dialog.close();

        });

        dialog.setOnDialogClosed(event -> {
            borderPane.setEffect(null);
        });

        borderPane.setEffect(boxBlur);
        dialog.show();

    }
    public void createAlert(String msg,String title , ActionEvent event){




        if (title == null)
            title = "Alert";

        Stage s = GetStage.getStage(event);
        BoxBlur boxBlur = new BoxBlur(4,4,2);
        BorderPane borderPane = (BorderPane) s.getScene().lookup("#mainborderpane");

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.getStyleClass().addAll("root","border");
        JFXButton button = new JFXButton("OK");
        button.setPrefSize(70,35);
        button.getStyleClass().add("acentbtn");
        JFXDialog dialog = new JFXDialog((StackPane) s.getScene().getRoot(),dialogLayout,JFXDialog.DialogTransition.TOP);
        Label l = new Label(msg);
//        l.getStyleClass().add("label_alert");



        MaterialDesignIconView icon2 = null;
        if (title.contains("rror") || title.contains("lert"))
            if (title.contains("db") || title.contains("DB") || title.contains("Db"))
                icon2 = new MaterialDesignIconView(MaterialDesignIcon.ALERT);
            else
                icon2 = new MaterialDesignIconView(MaterialDesignIcon.ALERT);

        else if (title.contains("Successful"))
            icon2 = new MaterialDesignIconView(MaterialDesignIcon.CHECK_CIRCLE_OUTLINE);
        else if (title.contains("?"))
            icon2 = new MaterialDesignIconView(MaterialDesignIcon.COMMENT_QUESTION_OUTLINE);
        else
            icon2  = new MaterialDesignIconView(MaterialDesignIcon.INFORMATION);
        icon2.setSize("30");

        l.setGraphic(icon2);
        dialogLayout.setHeading(l);
        dialogLayout.setPrefSize(300,100);
        dialogLayout.setActions(button);

        button.addEventHandler(MouseEvent.MOUSE_CLICKED,event2 -> {
            dialog.close();

        });

        dialog.setOnDialogClosed(event2 -> {
            borderPane.setEffect(null);
        });

        borderPane.setEffect(boxBlur);
        dialog.show();

    }

    public void confirm(String msg,String title , ActionEvent event,List<JFXButton> listcontrol){

        if (title == null)
            title = "Alert";

        Stage s = GetStage.getStage(event);
        BoxBlur boxBlur = new BoxBlur(4,4,2);
        BorderPane borderPane = (BorderPane) s.getScene().lookup("#mainborderpane");




        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.getStyleClass().addAll("root","border");
        JFXDialog dialog = new JFXDialog((StackPane) s.getScene().getRoot(),dialogLayout,JFXDialog.DialogTransition.TOP);
        Label l = new Label(msg);
//        l.getStyleClass().add("label_alert");



        MaterialDesignIconView icon2 = null;
        if (title.contains("rror") || title.contains("lert"))
            if (title.contains("db") || title.contains("DB") || title.contains("Db"))
                icon2 = new MaterialDesignIconView(MaterialDesignIcon.ALERT);
            else
                icon2 = new MaterialDesignIconView(MaterialDesignIcon.ALERT);

        else if (title.contains("Successful"))
            icon2 = new MaterialDesignIconView(MaterialDesignIcon.CHECK_CIRCLE_OUTLINE);
        else if (title.contains("?"))
            icon2 = new MaterialDesignIconView(MaterialDesignIcon.COMMENT_QUESTION_OUTLINE);
        else
            icon2 = new MaterialDesignIconView(MaterialDesignIcon.INFORMATION);
        icon2.setSize("30");

        l.setGraphic(icon2);
        dialogLayout.setHeading(l);
        dialogLayout.setPrefSize(300,100);
        dialogLayout.setActions(listcontrol);

        listcontrol.forEach(button ->{
            button.setPrefSize(70,35);
            button.getStyleClass().add("acentbtn");
            button.addEventHandler(MouseEvent.MOUSE_CLICKED,event2 -> {
                dialog.close();

            });
        } );

        dialog.setOnDialogClosed(event2 -> {
            borderPane.setEffect(null);
        });

        borderPane.setEffect(boxBlur);
        dialog.show();

    }
}
