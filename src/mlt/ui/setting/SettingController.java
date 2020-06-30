package mlt.ui.setting;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import mlt.db.DbHandler;
import mlt.ui.MyAlert.MyAlert;
import mlt.ui.login.logic.SHAExample;
import mlt.ui.support.GetStage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    //Student Tab
    // student Table

    @FXML
    private TableView<Student> tblstudent;

    @FXML
    private TableColumn<Student, String> colid;

    @FXML
    private TableColumn<Student, String> colname;

    @FXML
    private TableColumn<Student, String> colcaste;

    @FXML
    private TableColumn<Student, String> colmarks;

    @FXML
    private TableColumn<Student, String> colremarks;

    @FXML
    private JFXComboBox<String> cbcourse;

    @FXML
    private JFXComboBox<String> cbclass;

    // Meritlist TAb

    @FXML
    private JFXComboBox<String> cbcourse2;

    @FXML
    private JFXComboBox<String> cbclass2;

    @FXML
    private JFXComboBox<String> cbminmarks;

    @FXML
    private JFXComboBox<String> cbrequired;

    @FXML
    private JFXComboBox<String> cbseats;


    @FXML
    private Label lblchoosefile;

    @FXML
    private JFXButton btnaddseats;

    @FXML
    private JFXButton btneditseats;

    @FXML
    private JFXButton btndeleteseats;

    @FXML
    private TableView<TotalSeat> tbltotalseats;

    @FXML
    private TableColumn<TotalSeat, String> colscourse;

    @FXML
    private TableColumn<TotalSeat, String> colsclass;

    @FXML
    private TableColumn<TotalSeat, String> colsrequired;

    @FXML
    private TableColumn<TotalSeat, String> colsminmarks;

    @FXML
    private TableColumn<TotalSeat, String> colstotalseats;


    // Database TAb

    @FXML
    private JFXComboBox<String> cbpath;

    @FXML
    private JFXTextField txtfilename;

    @FXML
    private JFXTextField txtuname;

    @FXML
    private JFXTextField txtuname2;

    @FXML
    private JFXPasswordField txtpassword;




    @FXML
    private JFXComboBox<String> cbtheme;

    @FXML
    private JFXComboBox<String> cbAscentColor;

    private String defaultTheme = "theme1";


    @FXML
    private AnchorPane panetheme;


    @FXML
    private JFXComboBox<String> cbtablename;

    @FXML
    private JFXComboBox<String> cbtablename2;

    //EXtra

    @FXML
    private Tab tab_student;

    @FXML
    private Tab tab_mlt;

    @FXML
    private Tab tab_db;

    @FXML
    private Tab tab_sys_setting;

    // username passed from login screen

    private String username;

    ObservableList lseat = FXCollections.observableArrayList();
    ObservableList lstudent = FXCollections.observableArrayList();

    File selectedFile;
    private String theme;


    public void setUser(String username) {

        this.username = username;
        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select role from login where username = ?");
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                if (rs.getString("role").equals("admin")){
                    enableImpTabs();
                    System.out.println("enabled");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadStudentTable() {
        if (!(cbcourse.getSelectionModel().isEmpty() || cbclass.getSelectionModel().isEmpty())){
            String course = cbcourse.getValue();
            System.out.println("idise");
            try {

                PreparedStatement ps = DbHandler.getConnection().prepareStatement("select count(*) from information_schema.tables where information_schema.tables.table_name = ? and table_schema = \"meritlist\"");
                ps.setString(1,"FY_"+course+"_student");
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    if (rs.getInt(1)>0){
                        ps = DbHandler.getConnection().prepareStatement("select * from FY_"+course+"_student");
                        rs = ps.executeQuery();
                        lstudent.clear();
                        if (rs.next())
                        {
                            lstudent.add(new Student(rs.getInt("id")+"",rs.getString("name"),rs.getString("caste"), rs.getString("marks"),rs.getString("remark")));
                            while (rs.next()){
                                lstudent.add(new Student(rs.getInt("id")+"",rs.getString("name"),rs.getString("caste"), rs.getString("marks"),rs.getString("remark")));
                            }
                            tblstudent.getItems().setAll(lstudent);
                        }
                        else
                            new MyAlert().createAlert("No Data Found ","Alert",GetStage.getStage(cbclass));
                    }
                    else
                        new MyAlert().createAlert("Please upload Student Data","Alert",GetStage.getStage(cbclass));
                }
                else
                    new MyAlert().createAlert("Data not Found","Alert",GetStage.getStage(cbclass));

            } catch (SQLException e) {
                e.printStackTrace();
                new MyAlert().createAlert("Data not Found","Alert",GetStage.getStage(cbclass));
            }
        }
    }

    @FXML
    void showStudentData(ActionEvent event) {
        loadStudentTable();
    }



    @FXML
    void updateTable(ActionEvent event) {

    }

    @FXML
    void importDb(ActionEvent event) throws IOException, SQLException {
        if (selectedFile != null) {
            DbHandler.excAction("create database if not exists meritlist");
            lblchoosefile.setText(selectedFile.getName()+"(path : "+selectedFile.getAbsolutePath()+")");
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"mysql -u root -p meritlist < "+ selectedFile.getAbsolutePath()+"\"");
        }
        else
            new MyAlert().createAlert("Choose File","Alert",event);
    }
    @FXML
    void exportDB(ActionEvent event) throws IOException {
        if (!cbpath.getSelectionModel().isEmpty()){
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"mysqldump -u root -p meritlist > "+cbpath.getValue()+txtfilename.getText()+".sql \"");
        }
        else
            new MyAlert().createAlert("Provide File name","Alert",event);
    }

    @FXML
    void dropDB(ActionEvent event) {
        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("DROP TABLE 'course','teacher','subject','meritlist','account','login'");
            ps.execute();
            DbHandler.getConnection();
            new MyAlert().createAlert("Older Database Dropped","Info",event);
        } catch (SQLException e) {
            e.printStackTrace();
            new MyAlert().createAlert("Some Error Occured ...","DB Error",event);
        }
    }

    @FXML
    void chooseSqlFile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
//        fileChooser.getExtensionFilters().addAll(
//                new ExtensionFilter("Text Files", "*.txt"),
//                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
//                new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
//                new ExtensionFilter("All Files", "*.*"));
        selectedFile = fileChooser.showOpenDialog(GetStage.getStage(event));
    }

    @FXML
    void chooseCsvFile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(GetStage.getStage(event));
        if (selectedFile != null) {
            lblchoosefile.setText(selectedFile.getName()+"(path : "+selectedFile.getAbsolutePath()+")");
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"mysqlimport -u root -p meritlist  "+ selectedFile.getAbsolutePath()+"\"");
        }
    }



    @FXML
    void addUser(ActionEvent event) {
        try {
            if (!(txtuname.getText().equals("") || txtpassword.getText().equals(""))){


                PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT count(username) from login  where username = ?");
                ps.setString(1,txtuname.getText());
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    if (rs.getInt(1)==0){
//                       ps = DbHandler.getConnection().prepareStatement("create user ? identified by ?");
//                       ps.setString(1,txtuname.getText());
//                       ps.setString(2,txtpassword.getText());
//                       ps.execute();
//
//                       ps = DbHandler.getConnection().prepareStatement("grant all on meritlist.* to ?");
//                       ps.setString(1,txtuname.getText());
//                       ps.execute();
//

//
//                       DbHandler.createLoginTable();
//                       DbHandler.createAccountTable();
                        ps = DbHandler.getConnection().prepareStatement("insert into login values(?,?,'','','user',curdate())");
                        ps.setString(1,txtuname.getText());
                        ps.setString(2, SHAExample.get_SHA_256_SecurePassword(txtpassword.getText(), "[B@6e2c634b3385185f8ac69287b02c7cc9e6c5385a4706c1fb7e5aaf44d38a33f2cd5bc193"));
                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"System User "+txtuname.getText()+ " added to System");
                        ps.execute();




                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO profilephoto(username,path) values(?,'jrxml/user.png')");
                        ps.setString(1,txtuname.getText());
                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO theme(username,theme) values(?,'black')");
                        ps.setString(1,txtuname.getText());
                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("insert into account values(?,NULL ,'','','teacher')");
                        ps.setString(1,txtuname.getText());
                        ps.execute();
                        new MyAlert().createAlert("User "+ txtuname.getText()+" added","Info",event);
                        txtuname.setText("");
                        txtpassword.setText("");
                    }
                    else
                        new MyAlert().createAlert("User Already exists","Unspecified Data",event);
                }
            }else
                new MyAlert().createAlert("Please specify Username & Password","Unspecified Data",event);
        } catch (SQLException e) {
            e.printStackTrace();
            new MyAlert().createAlert("System Error","Error",event);
        }

    }


    @FXML
    void removeUser(ActionEvent event) {
        try {
            if (!(txtuname2.getText().equals("") )){
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT count(username) from login  where username = ?");
                ps.setString(1,txtuname2.getText());
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    if (rs.getInt(1)==1){


                        JFXButton yesbutton = new JFXButton("Yes");
                        JFXButton nobutton = new JFXButton("No");
                        List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

                        yesbutton.setOnAction(event3 ->{

                            try{
                                PreparedStatement ps2 = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                                ps2.setString(1,username);
                                ps2.setString(2,"User "+txtuname2.getText()+ " removed");
                                ps2.execute();

                                ps2 = DbHandler.getConnection().prepareStatement("delete from  profilephoto where username = ?");
                                ps2.setString(1,txtuname2.getText());
                                ps2.execute();


                                ps2 = DbHandler.getConnection().prepareStatement("delete from  account where username = ?");
                                ps2.setString(1,txtuname2.getText());
                                ps2.execute();

                                ps2 = DbHandler.getConnection().prepareStatement("delete from  theme where username = ?");
                                ps2.setString(1,txtuname2.getText());
                                ps2.execute();


                                ps2 = DbHandler.getConnection().prepareStatement("delete from  login where username = ?");
                                ps2.setString(1,txtuname2.getText());
                                ps2.execute();

                                new MyAlert().createAlert("User "+ txtuname2.getText()+" removed","Info",event);
                                txtuname2.setText("");
                            } catch (SQLException e) {

                                new MyAlert().createAlert("System Error","Error",event);
                                e.printStackTrace();
                            }
                        });

                        nobutton.setOnAction(event3 ->{
                            new MyAlert().createAlert(" Operation Cancelled ","Delete",event);
                        });
                        new MyAlert().confirm("Do you want to remove user "+ txtuname2.getText(),"Delete ?",event,listcontrol);
                    }
                    else
                        new MyAlert().createAlert("User Doesn't exists","Unspecified Data",event);
                }
            }else
                new MyAlert().createAlert("Please specify Username ","Unspecified Data",event);
        } catch (SQLException e) {
            e.printStackTrace();
            new MyAlert().createAlert("System Error","Error",event);
        }

    }


    @FXML
    void loadClass(ActionEvent event) {

        if (!cbcourse.getSelectionModel().isEmpty()){

        }

    }

    @FXML
    void deleteTotalSeatsTable(ActionEvent event) throws SQLException {
        if (!(cbcourse2.getSelectionModel().isEmpty() || cbclass2.getSelectionModel().isEmpty())){
            try{
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT count(*) from mltdata where course = ? and class = ?");
                ps.setString(1,cbcourse2.getValue());
                ps.setString(2,cbclass2.getValue());
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                {
                    if (rs.getInt(1) == 1){
                        ps = DbHandler.getConnection().prepareStatement("DELETE from mltdata where course = ? and class = ?");
                        ps.setString(1,cbcourse2.getValue());
                        ps.setString(2,cbclass2.getValue());

                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"MeritList Settings for"+cbcourse2.getValue()+ " "+cbclass2.getValue()+" "+" is removed");
                        ps.execute();


                        new MyAlert().createAlert("Details Removed","Added",event);
                        cbcourse2.setDisable(false);
                        cbclass2.setDisable(false);
                        loadTotalSeatsTable();
                        clear(event);
                    }
                    else
                        new MyAlert().createAlert("Something Went Wrong ","Error",event);
                }
                else
                    new MyAlert().createAlert("Something Went Wrong ","Error",event);
            }catch(Exception e){
                new MyAlert().createAlert("Something Went Wrong ","Error",event);
            }
        }
        else
            new MyAlert().createAlert("Provide Course and Class","Error",event);
    }

    @FXML
    void editTotalSeatsTable(ActionEvent event){
        if (!(cbcourse2.getSelectionModel().isEmpty() || cbclass2.getSelectionModel().isEmpty() || cbminmarks.getSelectionModel().isEmpty() || cbrequired.getSelectionModel().isEmpty() || cbseats.getSelectionModel().isEmpty() ) ){


            try {

                PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT count(*) from mltdata where course = ? and class = ?");
                ps.setString(1,cbcourse2.getValue());
                ps.setString(2,cbclass2.getValue());
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                {
                    if (rs.getInt(1) == 1){
                        ps = DbHandler.getConnection().prepareStatement("UPDATE mltdata set minmarks = ? , reqsub =? , totalseat = ? where course = ? and class = ? ");

                        ps.setInt(1,Integer.parseInt(cbminmarks.getValue()));
                        ps.setString(2,cbrequired.getValue());
                        ps.setInt(3,Integer.parseInt(cbseats.getValue()));
                        ps.setString(4,cbcourse2.getValue());
                        ps.setString(5,cbclass2.getValue());
                        ps.execute();


                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"MeritList Setting for"+cbcourse2.getValue()+ " "+cbclass2.getValue()+" "+" is updated");
                        ps.execute();

                        new MyAlert().createAlert("Details Updated","Added",event);

                        loadTotalSeatsTable();

                    }
                    else
                        new MyAlert().createAlert("Something Went Wrong ","Error",event);
                }
                else
                    new MyAlert().createAlert("Something Went Wrong ","Error",event);
            }
            catch(Exception e){
                new MyAlert().createAlert("Something Went Wrong ","Error",event);
            }
        }

    }

    @FXML
    void loadTotalSeatsData(MouseEvent event) {

        if (!tbltotalseats.getSelectionModel().isEmpty()){

            TotalSeat totalSeat = tbltotalseats.getSelectionModel().getSelectedItem();
            cbcourse2.setValue(totalSeat.getCourse());
            cbclass2.setValue(totalSeat.getClassname());
            cbminmarks.setValue(totalSeat.getMinmarks());
            cbrequired.setValue(totalSeat.getRequired());
            cbseats.setValue(totalSeat.getTotalseats());

            cbcourse2.setDisable(true);
            cbclass2.setDisable(true);
            btneditseats.setDisable(false);
            btndeleteseats.setDisable(false);
            btnaddseats.setDisable(true);

        }
    }

     @FXML
    void addTotalSeatsTable(ActionEvent event) throws SQLException {
        if (!(cbcourse2.getSelectionModel().isEmpty() || cbclass2.getSelectionModel().isEmpty() || cbminmarks.getSelectionModel().isEmpty() || cbrequired.getSelectionModel().isEmpty() || cbseats.getSelectionModel().isEmpty() ) ){

                try{
                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT count(*) from mltdata where course = ? and class = ?");
                    ps.setString(1,cbcourse2.getValue());
                    ps.setString(2,cbclass2.getValue());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next())
                    {
                        if (rs.getInt(1) == 0){
                            ps = DbHandler.getConnection().prepareStatement("INSERT into mltdata(course,class,minmarks,reqsub,totalseat) values (?,?,?,?,?)");
                            ps.setString(1,cbcourse2.getValue());
                            ps.setString(2,cbclass2.getValue());
                            ps.setInt(3,Integer.parseInt(cbminmarks.getValue()));
                            ps.setString(4,cbrequired.getValue());
                            ps.setInt(5,Integer.parseInt(cbseats.getValue()));
                            ps.execute();
                            new MyAlert().createAlert("Details Added","Added",event);

                            ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                            ps.setString(1,username);
                            ps.setString(2,"MeritList Setting  for"+cbcourse2.getValue()+ " "+cbclass2.getValue()+" is added");
                            ps.execute();


                            loadTotalSeatsTable();
                        }
                        else
                            new MyAlert().createAlert("Already Added","Error",event);
                    }
                    else
                        new MyAlert().createAlert("Something Went Wrong ","Error",event);
                }catch(Exception e){
                    new MyAlert().createAlert("Something Went Wrong ","Error",event);
                }
        }
        else
            new MyAlert().createAlert("Provide all Details","Error",event);
    }

    private void disableImpTabs(){
        tab_student.setDisable(true);
        tab_db.setDisable(true);
        tab_mlt.setDisable(true);
    }

    private void enableImpTabs(){
        tab_student.setDisable(false);
        tab_db.setDisable(false);
        tab_mlt.setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        disableImpTabs();

        // Database Tab
        cbpath.getItems().setAll("d:\\","e:\\","f:\\","g:\\","h:\\");

        // Meritlist TAb

//
//        colperseat.setPrefWidth(tblseats.getPrefWidth()/4);
//        colcategory.setPrefWidth(3*tblseats.getPrefWidth()/4);
//
//        tblseats.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                colperseat.setPrefWidth(newValue.intValue()/2);
//                colcategory.setPrefWidth(newValue.intValue()/2);
//            }
//        });

        tbltotalseats.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                colsclass.setPrefWidth(newValue.intValue()/5);
                colscourse.setPrefWidth(newValue.intValue()/5);
                colsminmarks.setPrefWidth(newValue.intValue()/5);
                colsrequired.setPrefWidth(newValue.intValue()/5);
                colstotalseats.setPrefWidth(newValue.intValue()/5);
            }
        });



        // Student TAb

        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colcaste.setCellValueFactory(new PropertyValueFactory<>("caste"));
        colmarks.setCellValueFactory(new PropertyValueFactory<>("mark"));
        colremarks.setCellValueFactory(new PropertyValueFactory<>("remark"));

        colid.setPrefWidth(tblstudent.getPrefWidth()/6);
        colname.setPrefWidth(tblstudent.getPrefWidth()/3);
        colcaste.setPrefWidth(tblstudent.getPrefWidth()/6);
        colmarks.setPrefWidth(tblstudent.getPrefWidth()/6);
        colremarks.setPrefWidth(tblstudent.getPrefWidth()/6);

        tblstudent.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                colid.setPrefWidth(newValue.intValue()/6-2);
                colname.setPrefWidth(newValue.intValue()/3);
                colcaste.setPrefWidth(newValue.intValue()/6);
                colmarks.setPrefWidth(newValue.intValue()/6);
                colremarks.setPrefWidth(newValue.intValue()/6);
            }
        });

        cbcourse.getItems().setAll("BscIT","BscCS","Bsc");
        cbcourse.setValue("BscIT");
        cbclass.getItems().setAll("FY");
        cbclass.setValue("FY");
//        loadStudentTable();

//        cbtheme.setValue("white");

        // theme

//        try {
//
//            Parent themepane = FXMLLoader.load(getClass().getResource("/mlt/ui/setting/theme.fxml"));
//            panetheme.getChildren().setAll(themepane);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        loadMeritListConfig();

        loadTableName();


        //Theme

        cbtheme.getItems().setAll("black","white","white-emerald","white-teal","white-material-blue");
        cbAscentColor.getItems().setAll("#7b1cd7","#1dd724","#d7111e","#d79811","#0078d7");
        cbAscentColor.setValue("#0078d7");
//        cbAscentColor.getScene().getRoot().getStyleClass().add("theme1");
        defaultTheme = "theme1";

        cbAscentColor.setVisible(false);



    }

    public void setTheme(){
        try{
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select theme from theme where username = ?");
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                theme = rs.getString("theme");
                cbtheme.setValue(theme);
                System.out.println(" theme is set ");
                System.out.println(theme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ObservableList<String> tableList = FXCollections.observableArrayList();
    public void loadTableName() {
       tableList.clear();
        try{
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select table_name from information_schema.tables where table_schema = 'meritlist' and table_name like '%_student' ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                tableList.addAll(rs.getString("table_name"));
            }
            cbtablename.getItems().setAll(tableList);
            cbtablename2.getItems().setAll(tableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMeritListConfig() {

        loadCourse();

        //cbclass.getItems().setAll("FY",);
        cbclass2.getItems().setAll("FY");
        ObservableList<String> listminmarks = FXCollections.observableArrayList();

        for (int i = 30; i < 75 ; i++) {
            listminmarks.add(i+"");
        }


        cbminmarks.getItems().setAll(listminmarks);

        cbrequired.getItems().setAll("Maths","Average Aggrigate");

        listminmarks.clear();
        for (int i = 5; i < 150 ; i++) {
            listminmarks.add(i+"");
        }
        cbseats.getItems().setAll(listminmarks);


        btneditseats.setDisable(true);
        btndeleteseats.setDisable(true);


        colscourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colsclass.setCellValueFactory(new PropertyValueFactory<>("classname"));
        colsminmarks.setCellValueFactory(new PropertyValueFactory<>("minmarks"));
        colsrequired.setCellValueFactory(new PropertyValueFactory<>("required"));
        colstotalseats.setCellValueFactory(new PropertyValueFactory<>("totalseats"));


        loadTotalSeatsTable();

    }

    ObservableList<TotalSeat> listtotalSeats = FXCollections.observableArrayList();

    private void loadTotalSeatsTable() {

        listtotalSeats.clear();
        try
        {
            PreparedStatement ps= DbHandler.getConnection().prepareStatement("SELECT * from mltdata");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){

                listtotalSeats.add(new TotalSeat(rs.getString("course"),rs.getString("class"),rs.getString("reqsub"),rs.getString("minmarks"),rs.getString("totalseat")));
            }
            tbltotalseats.getItems().setAll(listtotalSeats);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void loadCourse() {

        ObservableList<String> cname = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("Select cname from course");
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                cname.add(rs.getString("cname"));
            }

            cbcourse.getItems().clear();
            cbcourse.getItems().setAll(cname);

            cbcourse2.getItems().clear();
            cbcourse2.getItems().setAll(cname);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public class Seats{

        private String category;
        private Float seat;

        public Seats(String category, Float seat) {

            this.category = category;
            this.seat = seat;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Float getSeat() {
            return seat;
        }

        public void setSeat(Float seat) {
            this.seat = seat;
        }
    }

    public class Student {
        private String id, name, caste, mark, remark;

        public Student(String id, String name, String caste, String mark, String remark) {
            this.id = id;
            this.name = name;
            this.caste = caste;
            this.mark = mark;
            this.remark = remark;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCaste() {
            return caste;
        }

        public void setCaste(String caste) {
            this.caste = caste;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    public class TotalSeat {

        private String course,classname,required;
        private String minmarks;
        private String totalseats;

        public TotalSeat(String course, String classname, String required, String minmarks, String totalseats) {
            this.course = course;
            this.classname = classname;
            this.required = required;
            this.minmarks = minmarks;
            this.totalseats = totalseats;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getRequired() {
            return required;
        }

        public void setRequired(String required) {
            this.required = required;
        }

        public String getMinmarks() {
            return minmarks;
        }

        public void setMinmarks(String minmarks) {
            this.minmarks = minmarks;
        }

        public String getTotalseats() {
            return totalseats;
        }

        public void setTotalseats(String totalseats) {
            this.totalseats = totalseats;
        }
    }


    @FXML
    void applyTheme(ActionEvent event) {
        if (!cbtheme.getSelectionModel().isEmpty()){

            GetStage.getStage(event).getScene().getStylesheets().removeAll("/mlt/ui/style/theme.css");

            if (cbtheme.getValue().equals("black")){


                GetStage.getStage(event).getScene().getStylesheets().setAll("/mlt/ui/style/newStyle.css","/mlt/ui/style/common.css","/mlt/ui/style/style.css");

                //cbAscentColor.setVisible(false);
            }
            else  {

               // cbAscentColor.setVisible(true);
//
//                GetStage.getStage(event).getScene().getStylesheets().setAll("/mlt/ui/style/new2.css");
//
//                //"white-emerald","white-teal","white-material-blue"
//                if (cbtheme.getValue().contains("emerald")){
//                    GetStage.getStage(event).getScene().getRoot().getStyleClass().removeAll("teal","blue");
//                    GetStage.getStage(event).getScene().getRoot().getStyleClass().add("emerald");
//
//                }
//                else if (cbtheme.getValue().contains("teal")){
//                    GetStage.getStage(event).getScene().getRoot().getStyleClass().removeAll("blue","emerald");
//                    GetStage.getStage(event).getScene().getRoot().getStyleClass().add("teal");
//                }
//                else
//                if (cbtheme.getValue().contains("blue")){
//                    GetStage.getStage(event).getScene().getRoot().getStyleClass().removeAll("teal","emerald");
//                    GetStage.getStage(event).getScene().getRoot().getStyleClass().add("blue");
//                }

                //"white-emerald","white-teal","white-material-blue"
                if (cbtheme.getValue().contains("emerald")){
                    GetStage.getStage(event).getScene().getStylesheets().setAll("/mlt/ui/style/new3.css");

                }
                else if (cbtheme.getValue().contains("teal")){
                    GetStage.getStage(event).getScene().getStylesheets().setAll("/mlt/ui/style/new4.css");
                }
                else
                if (cbtheme.getValue().contains("blue")){
                    GetStage.getStage(event).getScene().getStylesheets().setAll("/mlt/ui/style/new5.css");
                }
                else
                    GetStage.getStage(event).getScene().getStylesheets().setAll("/mlt/ui/style/new2.css");

            }

            try {
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("update theme set theme = ? where username = ?");
                ps.setString(1,cbtheme.getValue());
                ps.setString(2,username);
                ps.execute();
                //new MyAlert().createAlert("theme : "+ cbtheme.getValue()+ " is set", "Success", event);
            } catch (SQLException e) {
                new MyAlert().createAlert("Please try again", "Error", event);
                e.printStackTrace();
            }
        }
    }


    @FXML
    void applyAscentColor(ActionEvent event) {
        if (!cbAscentColor.getSelectionModel().isEmpty()){

            GetStage.getStage(event).getScene().getStylesheets().add("/mlt/ui/style/theme.css");

            Parent p = ((Node)event.getSource()).getScene().getRoot();

            if (cbAscentColor.getValue().equals("#7b1cd7"))
            {
                p.getStyleClass().remove(defaultTheme);
                p.getStyleClass().add("theme2");
                defaultTheme = "theme2";
            }
            else  if (cbAscentColor.getValue().equals("#1dd724"))
            {
                p.getStyleClass().remove(defaultTheme);
                p.getStyleClass().add("theme3");
                defaultTheme = "theme3";
            }
            else  if (cbAscentColor.getValue().equals("#d7111e"))
            {
                p.getStyleClass().remove(defaultTheme);
                p.getStyleClass().add("theme4");
                defaultTheme = "theme4";
            }
            else  if (cbAscentColor.getValue().equals("#d79811"))
            {
                p.getStyleClass().remove(defaultTheme);
                p.getStyleClass().add("theme5");
                defaultTheme = "theme5";
            }
            else  if (cbAscentColor.getValue().equals("#0078d7"))
            {
                p.getStyleClass().remove(defaultTheme);
                p.getStyleClass().add("theme1");
                defaultTheme = "theme1";
            }
            else
                System.out.println("------");
        }

    }


    @FXML
    void uploadStudentDataFile(ActionEvent event) {

            try{
                if (selectedFile != null) {
                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("LOAD DATA INFILE ? INTO TABLE "+ cbtablename.getValue()+ " FIELDS TERMINATED BY ';' LINES TERMINATED by  '\n'");
                    ps.setString(1,selectedFile.getAbsolutePath());
                    ps.execute();

                    ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                    ps.setString(1,username);
                    ps.setString(2,cbtablename.getValue()+" data is uploaded");
                    ps.execute();

                    new MyAlert().createAlert("Data Loaded into Table", "Error", event);
                }
                else
                    new MyAlert().createAlert("Choose file ", "Error", event);

            } catch (SQLException e) {
                e.printStackTrace();
                new MyAlert().createAlert("Failed ...", "Error", event);
            }catch (Exception e){
                new MyAlert().createAlert("Failed ...", "Error", event);
            }
    }

    @FXML
    void deleteTable(ActionEvent event) {
            if(!cbtablename2.getSelectionModel().isEmpty()){
                try{
                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("DELETE  from "+ cbtablename2.getValue() );
                    ps.execute();
                    cbtablename2.getSelectionModel().clearSelection();
                    loadTableName();

                    ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                    ps.setString(1,username);
                    ps.setString(2,cbtablename2.getValue()+" is deleted");
                    ps.execute();

                    new MyAlert().createAlert("Table "+ cbtablename2.getValue()+" Data deleted ", "Error", event);
                } catch (SQLException e) {
                    e.printStackTrace();
                    new MyAlert().createAlert("Some System(DB) Error Occured", "Error", event);
                }
            }else
                new MyAlert().createAlert("Please Choose Table", "Error", event);
    }

     @FXML
    void chooseStudentDataFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        selectedFile = fileChooser.showOpenDialog(GetStage.getStage(event));
    }

    @FXML
    void clear(ActionEvent event) {
        cbcourse2.getSelectionModel().clearSelection();
        cbclass2.getSelectionModel().clearSelection();
        cbminmarks.getSelectionModel().clearSelection();
        cbrequired.getSelectionModel().clearSelection();
        cbseats.getSelectionModel().clearSelection();

        cbcourse2.setDisable(false);
        cbclass2.setDisable(false);

        btnaddseats.setDisable(false);
        btndeleteseats.setDisable(true);
        btneditseats.setDisable(true);
    }

    public void loadStudentTableList(ActionEvent event){

       loadStudentTableList();
    }


    public void loadStudentTableList(){

        ObservableList<String> stutablelist = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT table_name from Information_schema.tables where table_name like '%_student'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if (!stutablelist.contains(rs.getString("table_name")))
                    stutablelist.addAll(rs.getString("table_name"));
            }
            cbtablename.getItems().setAll(stutablelist);
            cbtablename2.getItems().setAll(stutablelist);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
