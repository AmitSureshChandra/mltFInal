package mlt.ui.Dashboard;

import animatefx.animation.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.StackPane;
import mlt.db.DbHandler;
import mlt.ui.account.AccountController;
import mlt.ui.adminpanel.AdminpageController;
import mlt.ui.course.CourseController;
import mlt.ui.login.LoginController;
import mlt.ui.meritlist.MeritlistController;
import mlt.ui.setting.SettingController;
import mlt.ui.subject.SubjectController;
import mlt.ui.support.CloseStage;
import mlt.ui.support.CreateStage;
import mlt.ui.support.GetStage;
import mlt.ui.teacher.TeacherController;
import mlt.ui.timetable.TimetableController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private JFXDrawer drawer;

    @FXML
    private AnchorPane paneshortnav;

    @FXML
    private AnchorPane panecontent;

    @FXML
    private AnchorPane panecourse;

    @FXML
    private AnchorPane paneteacher;

    @FXML
    private AnchorPane panesubject;

    @FXML
    private AnchorPane panetimetable;

    @FXML
    private AnchorPane panemeritlist;

    @FXML
    private AnchorPane panehome;


    @FXML
    private JFXButton btnhome;

    @FXML
    private JFXButton btncourse;

    @FXML
    private JFXButton btnteacher;

    @FXML
    private JFXButton btnsubject;

    @FXML
    private JFXButton btnmeritlist;

    @FXML
    private JFXButton btntimetable;

    @FXML
    private JFXButton btnaccount;

    @FXML
    private JFXButton btnsetting;

    private FXMLLoader loader_sidepane;

    @FXML
    private ImageView imghome;

    @FXML
    private AnchorPane paneaccount;

    @FXML
    private AnchorPane panesetting;

    Scene sc ;

    String btnNavSelected = "btnhome";

    private String username ;

    private String theme;

    @FXML
    private StackPane mainStackpane;

    private FXMLLoader accountSceneloader;
    private FXMLLoader courseSceneloader;
    private FXMLLoader teacherSceneloader;
    private FXMLLoader subjectSceneloader;
    private FXMLLoader homeSceneloader;
    private FXMLLoader meritlistSceneloader;
    private FXMLLoader timetableSceneloader;
    private FXMLLoader settingSceneloader;

    public void setUsername(String user){


        username = user;
        System.out.println(username);
        AccountController accountController = accountSceneloader.getController();
        accountController.setData(username);


        GetStage.getStage(mainStackpane).setMinHeight(700);
        GetStage.getStage(mainStackpane).setMinWidth(1160);
        GetStage.getStage(mainStackpane).setMaximized(true);
    }

    @FXML
    private BorderPane mainborderpane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initPanes();
        try {
            DbHandler.createOperationTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        panehome.toFront();
        sc = paneaccount.getScene();
        btnhome.getStyleClass().add("onclickStyleOnNavButton");
        try {
            DbHandler.createAccountTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        File dir = new File("C:/ProgramData/Mlt");
        dir.mkdir();


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
            courseSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/course/course.fxml"));
            BorderPane borderPane = (BorderPane) courseSceneloader.load();
            panecourse.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

            teacherSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/teacher/teacher.fxml"));
            borderPane = (BorderPane) teacherSceneloader.load();
            TeacherController teacherController = teacherSceneloader.getController();
            teacherController.setUser(username);
            paneteacher.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

            subjectSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/subject/subject.fxml"));
            borderPane = (BorderPane) subjectSceneloader.load();
            panesubject.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

            timetableSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/timetable/timetable.fxml"));
            borderPane = (BorderPane) timetableSceneloader.load();
            panetimetable.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

            meritlistSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/meritlist/meritlist.fxml"));
            borderPane = (BorderPane) meritlistSceneloader.load();
            panemeritlist.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

            homeSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/adminpanel/adminpage2.fxml"));
            borderPane = (BorderPane) homeSceneloader.load();
            AdminpageController adminpageController = homeSceneloader.getController();
            adminpageController.loadOperationTable();
            panehome.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);

            accountSceneloader = new FXMLLoader(getClass().getResource("/mlt/ui/account/account.fxml"));
            borderPane = (BorderPane)accountSceneloader.load() ;
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

    public void setTheme(String theme){
        JFXComboBox jfxComboBox= (JFXComboBox) (paneshortnav).getScene().lookup("#cbtheme");
        this.theme = theme;
        if (theme.equals("black"))
            jfxComboBox.setValue("black");
        else
            jfxComboBox.setValue("white");
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

            CloseStage.closeStage(event);
            JFXComboBox jfxComboBox= (JFXComboBox) ((Node)event.getSource()).getScene().lookup("#cbtheme");
            if (jfxComboBox.getValue().equals("black"))
            {
                CreateStage createStage=  new CreateStage();
                FXMLLoader loader  = createStage.createStage2("Teacher Login  ","/mlt/ui/login/Login.fxml", null,0,false,false,false,"black");
                LoginController loginController = loader.getController();
                loginController.setTheme("black");
            }

            else
            {
                CreateStage createStage=  new CreateStage();
                FXMLLoader loader  = createStage.createStage2("User Login  ","/mlt/ui/login/Login.fxml", null,0,false,false,false,"white");
                LoginController loginController = loader.getController();
                loginController.setTheme("white");
            }

        }
        catch (Exception e){
            new CreateStage().createStage("Teacher Login  ","/mlt/ui/login/Login.fxml",null,0,false,false,false,"white");
        }
    }

    @FXML
    void openCourse(ActionEvent event) {
       panecourse.toFront();
       setSelectedbtn((JFXButton) event.getSource());
        btnNavSelected = "btncourse";
        toShortNavBar();
        panecontent.setEffect(null);
        panecontent.setDisable(false);

        CourseController courseController = courseSceneloader.getController();
        courseController.setUser(username);

        new FadeIn(panecourse).play();
    }

    @FXML
    void openHome(ActionEvent event) {
        panehome.toFront();
        setSelectedbtn((JFXButton) event.getSource());
        btnNavSelected = "btnhome";
        toShortNavBar();
        panecontent.setEffect(null);
        panecontent.setDisable(false);

        AdminpageController adminpageController = homeSceneloader.getController();
        adminpageController.loadOperationTable();
        adminpageController.setUser(username);

        new FadeIn(panehome).play();

        Label lblmlt = (Label) sc.lookup("#lblmlt");
        new Bounce(lblmlt).play();
    }

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

    @FXML
    void openTeacher(ActionEvent event) {
        paneteacher.toFront();
        setSelectedbtn((JFXButton) event.getSource());
        btnNavSelected = "btnteacher";

        TeacherController teacherController = teacherSceneloader.getController();
        teacherController.setUser(username);
        teacherController.loadCourse();

        toShortNavBar();
        panecontent.setEffect(null);
        panecontent.setDisable(false);
        new FadeIn(paneteacher).play();
    }

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
}


