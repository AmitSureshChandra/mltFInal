package mlt.ui.Dashboard.teacher;

import animatefx.animation.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import mlt.ui.account.AccountController;
import mlt.ui.meritlist.MeritlistController;
import mlt.ui.setting.SettingController;
import mlt.ui.subject.SubjectController;
import mlt.ui.support.CloseStage;
import mlt.ui.support.CreateStage;
import mlt.ui.support.GetStage;
import mlt.ui.timetable.TimetableController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardTeacherController implements Initializable {



    @FXML
    private AnchorPane paneshortnav;

    @FXML
    private AnchorPane panecontent;

//    @FXML
//    private AnchorPane panecourse;
//
//    @FXML
//    private AnchorPane paneteacher;

    @FXML
    private AnchorPane panesubject;

    @FXML
    private AnchorPane panetimetable;

    @FXML
    private AnchorPane panemeritlist;


//    @FXML
//    private JFXButton btncourse;
//
//    @FXML
//    private JFXButton btnteacher;

    @FXML
    private JFXButton btnsubject;

    @FXML
    private JFXButton btnmeritlist;

    @FXML
    private JFXButton btntimetable;

    @FXML
    private JFXButton btnaccount;



    private FXMLLoader loader_sidepane;

    @FXML
    private ImageView imghome;

    @FXML
    private AnchorPane paneaccount;
    @FXML
    private AnchorPane panesetting;


    Scene sc ;

    String btnNavSelected = "btnaccount";

    private String username ;

    FXMLLoader accountSceneloader;

    private String theme;
    private FXMLLoader subjectSceneloader;
    private FXMLLoader timetableSceneloader;
    private FXMLLoader meritlistSceneloader;
    private FXMLLoader settingSceneloader;


    public void setUsername(String user){
        username = user;
        System.out.println(username);

        AccountController accountController = accountSceneloader.getController();
        accountController.setData(username);
        accountController.loadProfilePhoto();

    }

    @FXML
    private BorderPane mainborderpane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initPanes();
        paneaccount.toFront();
        sc = paneaccount.getScene();
        btnaccount.getStyleClass().add("onclickStyleOnNavButton");
    }

    public void setSelectedbtn(JFXButton btn)
    {
        sc = btn.getScene();
        JFXButton btn2 = (JFXButton) sc.lookup("#"+btnNavSelected);
        btn2.getStyleClass().removeAll("onclickStyleOnNavButton");
        btn.getStyleClass().add("onclickStyleOnNavButton");
    }



    private void initPanes() {
        try {
//            BorderPane borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("/mlt/ui/course/course.fxml"));
//            panecourse.getChildren().setAll(borderPane);
//            setAnchorConstraint(borderPane);

//            borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("/mlt/ui/teacher/teacher.fxml"));
//            paneteacher.getChildren().setAll(borderPane);
//            setAnchorConstraint(borderPane);


            subjectSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/subject/subject.fxml"));
            BorderPane borderPane = (BorderPane) subjectSceneloader.load();
            panesubject.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);


//            BorderPane borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("/mlt/ui/subject/subject.fxml"));
//            panesubject.getChildren().setAll(borderPane);
//            setAnchorConstraint(borderPane);

            timetableSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/timetable/timetable.fxml"));
            borderPane = (BorderPane) timetableSceneloader.load();
            panetimetable.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

//            borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("/mlt/ui/timetable/timetable.fxml"));
//            panetimetable.getChildren().setAll(borderPane);
//            setAnchorConstraint(borderPane);

            meritlistSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/meritlist/meritlist.fxml"));
            borderPane = (BorderPane) meritlistSceneloader.load();
            panemeritlist.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

//            borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("/mlt/ui/meritlist/meritlist.fxml"));
//            panemeritlist.getChildren().setAll(borderPane);
//            setAnchorConstraint(borderPane);

            accountSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/account/account.fxml"));
            borderPane = (BorderPane)accountSceneloader.load() ;
            paneaccount.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

//            accountSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/account/account.fxml"));
//            borderPane = (BorderPane)accountSceneloader.load() ;

            paneaccount.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

            settingSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/setting/setting.fxml"));
            borderPane = (BorderPane) settingSceneloader.load();
            panesetting.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAnchorConstraint(BorderPane borderPane) {

        Double v = 0.0;
        AnchorPane.setTopAnchor(borderPane,v);
        AnchorPane.setBottomAnchor(borderPane,v);
        AnchorPane.setLeftAnchor(borderPane,v);
        AnchorPane.setRightAnchor(borderPane,v);

    }



    boolean flag = false;
    @FXML
    void actionDrawer(ActionEvent event) throws FileNotFoundException {

        if (flag == false)
        {
            paneshortnav.setPrefWidth(250);
            panecontent.setEffect(new BoxBlur(2,2,1));
            panecontent.setDisable(true);
            flag = true;
        }
        else
        {
            paneshortnav.setPrefWidth(60);
            panecontent.setEffect(null);
            panecontent.setDisable(false);
            flag = false;


        }
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            JFXComboBox jfxComboBox= (JFXComboBox) ((Node)event.getSource()).getScene().lookup("#cbtheme");
            CloseStage.closeStage(event);
            if (jfxComboBox.getValue().equals("black"))
                new CreateStage().createStage("Teacher Login  ","/mlt/ui/login/Login.fxml", null,0,false,false,false,"black");
            else
                new CreateStage().createStage("Teacher Login  ","/mlt/ui/login/Login.fxml",null,0,false,false,false,"white");



        }
        catch (Exception e){
            new CreateStage().createStage("Teacher Login  ","/mlt/ui/login/Login.fxml", GetStage.getStage(event),0,false,false,false,"white");
        }
    }

//    @FXML
//    void openCourse(ActionEvent event) {
//        panecourse.toFront();
//        setSelectedbtn((JFXButton) event.getSource());
//        btnNavSelected = "btncourse";
//        toShortNavBar();
//        panecontent.setEffect(null);
//        panecontent.setDisable(false);
//        new FadeIn(panecourse).play();
//    }

    @FXML
    void openMeritList(ActionEvent event) {
        panemeritlist.toFront();
        setSelectedbtn((JFXButton) event.getSource());
        btnNavSelected = "btnmeritlist";

        MeritlistController meritlistController = meritlistSceneloader.getController();
        meritlistController.setUser(username);
        meritlistController.loadCourse();

        toShortNavBar();
        panecontent.setEffect(null);
        panecontent.setDisable(false);
        new FadeIn(panemeritlist).play();
    }

    @FXML
    void openSubject(ActionEvent event) {
        panesubject.toFront();
        setSelectedbtn((JFXButton) event.getSource());
        btnNavSelected = "btnsubject";

        SubjectController subjectController = subjectSceneloader.getController();
        subjectController.setUser(username);
        subjectController.loadCourse();

        toShortNavBar();
        panecontent.setEffect(null);
        panecontent.setDisable(false);
        new FadeIn(panesubject).play();
    }
//
//    @FXML
//    void openTeacher(ActionEvent event) {
//        paneteacher.toFront();
//        setSelectedbtn((JFXButton) event.getSource());
//        btnNavSelected = "btnteacher";
//        toShortNavBar();
//        panecontent.setEffect(null);
//        panecontent.setDisable(false);
//        new FadeIn(paneteacher).play();
//    }

    @FXML
    void openTimeTable(ActionEvent event) {
        panetimetable.toFront();
        setSelectedbtn((JFXButton) event.getSource());
        btnNavSelected = "btntimetable";

        TimetableController timetableController = timetableSceneloader.getController();
        timetableController.setUser(username);

        toShortNavBar();
        panecontent.setEffect(null);
        panecontent.setDisable(false);
        new FadeIn(panetimetable).play();
    }

    @FXML
    void openAccount(ActionEvent event) {
        paneaccount.toFront();
        setSelectedbtn((JFXButton) event.getSource());
        btnNavSelected = "btnaccount";
        toShortNavBar();
        panecontent.setEffect(null);
        panecontent.setDisable(false);

        AccountController accountController = accountSceneloader.getController();
        accountController.setData(username);
        accountController.loadProfilePhoto();

        new FadeIn(paneaccount).play();
    }

    @FXML
    void openSetting(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();
        btn.getStyleClass().add("onclickStyleOnNavButton");

        panesetting.toFront();
        setSelectedbtn((JFXButton) event.getSource());
        btnNavSelected = "btnsetting";

        SettingController settingController = settingSceneloader.getController();
        settingController.setUser(username);
        settingController.setTheme();
        settingController.loadCourse();
        settingController.loadStudentTableList();
        settingController.loadTableName();

        toShortNavBar();
        panecontent.setEffect(null);
        panecontent.setDisable(false);
        new FadeIn(panesetting).play();
    }


    private void toShortNavBar()
    {
        if (paneshortnav.getPrefWidth()== 250)
        {
            paneshortnav.setPrefWidth(60);

            flag = false;
        }
    }

    public void setTheme(String theme) {

    }
}


