package mlt.ui.teacher;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mlt.db.DbHandler;
import mlt.ui.MyAlert.MyAlert;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {

    @FXML
    private AnchorPane anchorcenter;

    @FXML
    private JFXTextField txtsearchteacher;

    @FXML
    private JFXComboBox<String> cbsearch;

    @FXML
    private JFXComboBox<String> cbcourse;


    @FXML
    private JFXComboBox<String> cbexp;

    @FXML
    private JFXComboBox<String> cbcourse2;


    @FXML
    private TableView<Teacher> tblteacher;

    @FXML
    private TableColumn<Teacher, String> coltid;

    @FXML
    private TableColumn<Teacher, String> coltname;

    @FXML
    private TableColumn<Teacher, String> coltpost;


    @FXML
    private TableColumn<Teacher, String> colexp;

    @FXML
    private TableColumn<Teacher, String> colcourse;

    @FXML
    private JFXTextField txtteachername;

    @FXML
    private JFXTextField txtteacherpost;

    @FXML
    private JFXTextField txtteacherid;

    @FXML
    private JFXButton btnaddteacher;

    @FXML
    private JFXButton btnupdateteacher;

    @FXML
    private JFXButton btnremoveteacher;



    // Subject TAb


    @FXML
    private TableView<SubTeacher> tblSubjectTeacher;

    @FXML
    private TableColumn<SubTeacher, String> colcourse2;

    @FXML
    private TableColumn<SubTeacher, String> colclass2;

    @FXML
    private TableColumn<SubTeacher, String> colteachername2;

    @FXML
    private TableColumn<SubTeacher, String> colsubject2;

    @FXML
    private JFXComboBox<String> cbscourse2;


    @FXML
    private JFXComboBox<String> cbsubject2;

    @FXML
    private JFXComboBox<String> cbteacher2;

    @FXML
    private JFXComboBox<String> cbclass2;

    @FXML
    private JFXButton btndeletesubject;

    @FXML
    private JFXButton btnclearsubject;

    @FXML
    private JFXButton btnaddsubject;

//    @FXML
//    private JFXButton btnupdatesubject;

    @FXML
    private JFXComboBox<String> cbcourse3;

    @FXML
    private JFXComboBox<String> cbclass3;



    ObservableList<Teacher> tlist = FXCollections.observableArrayList();
    private String username;

    @FXML
    void EditTeacher(ActionEvent event) {
        try {
            if (!(txtteachername.getText().equals("") || txtteacherpost.getText().equals("") || txtteacherid.getText().equals("") || cbcourse.getSelectionModel().isEmpty() || cbexp.getSelectionModel().isEmpty()))
            {
                JFXButton yesbutton = new JFXButton("Yes");
                JFXButton nobutton = new JFXButton("No");
                List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

                yesbutton.setOnAction(event3 ->{
                    try {
                        PreparedStatement ps = DbHandler.getConnection().prepareStatement("update teacher set tname = ? , post = ? , course = ? , exp = ? where id = ?");
                        ps.setString(1,txtteachername.getText());
                        ps.setString(2,txtteacherpost.getText());
                        ps.setString(3,cbcourse.getValue());
                        ps.setString(4,cbexp.getValue());
                        ps.setString(5,txtteacherid.getText());

                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"Teacher : "+ txtteachername.getText()+" Data updated");
                        ps.execute();


                        new MyAlert().createAlert("Teacher Data Updated ","update",event);
                        searchAll(event);
                        resetInputControls();
                        btnupdateteacher.setVisible(false);
                        btnremoveteacher.setVisible(false);
                        btnaddteacher.setVisible(true);

                    } catch (SQLException e) {
                        e.printStackTrace();
                        new MyAlert().createAlert("Some Error happened ","update",event);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        new MyAlert().createAlert("Please specify data in proper format","update",event);
                    }

                });

                nobutton.setOnAction(event3 ->{
                    new MyAlert().createAlert(" Operation Cancelled ","update",event);

                });
                new MyAlert().confirm("Do you want to update the Teacher Data ?","Update ?",event,listcontrol);

            }
        } catch (Exception e) {
            e.printStackTrace();
            new MyAlert().createAlert("Some Error Occured ","Error",event);
        }

    }

    private void resetInputControls() {
        txtteacherid.setText("");
        txtteacherpost.setText("");
        txtteachername.setText("");
        txtteacherid.setEditable(true);
        cbexp.getSelectionModel().clearSelection();
    }



    @FXML
    void loadCourse(ActionEvent event) {
        loadCourse();
    }


    @FXML
    void addTeacher(ActionEvent event) {
        try {
            if (checkTeacherID()){
                if (!(txtteachername.getText().equals("") || txtteacherpost.getText().equals("") || txtteacherid.getText().equals("") || cbcourse.getSelectionModel().isEmpty()|| cbexp.getSelectionModel().isEmpty()))
                {

                   try{
                       PreparedStatement ps = DbHandler.getConnection().prepareStatement("insert into teacher value (?,?,?,?,?)");
                       ps.setString(1,txtteacherid.getText());
                       ps.setString(2,txtteachername.getText());
                       ps.setString(3,txtteacherpost.getText());
                       ps.setString(4,cbexp.getValue());
                       ps.setString(5,cbcourse.getValue());

                       ps.execute();

                       ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                       ps.setString(1,username);
                       ps.setString(2,"Teacher : "+ txtteachername.getText()+" Added");
                       ps.execute();

                       new MyAlert().createAlert("Teacher Added","Added",event);
                       searchAll(event);
                       cancel(event);
                   }catch (Exception e){
                       new MyAlert().createAlert("Specify Data in proper format","Info",event);
                   }
                }
                else
                    new MyAlert().createAlert("Specify All Required Data","Unspecified Data",event);
            }
        }
        catch (Exception e){
                e.printStackTrace();
                new MyAlert().createAlert("Some Error Occured ","Error",event);
        }
    }

    private boolean checkTeacherID()
    {
        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select count(*) from teacher where id = ?");
            ps.setString(1,txtteacherid.getText());
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getInt(1)==0)
                return true;
            else
            {
                new MyAlert().createAlert("Duplicate ID for Teacher","ID Duplicate",(Stage) btnaddteacher.getScene().getWindow());
                return false;
            }
        } catch (SQLException e) {
            new MyAlert().createAlert("Something Wrong happened ","DBError",(Stage) btnaddteacher.getScene().getWindow());
            e.printStackTrace();
            return false;
        }catch (NumberFormatException e)
        {
            new MyAlert().createAlert("Please enter data in proper format","Unspecified Format",(Stage) btnaddteacher.getScene().getWindow());
        }
        return false;
    }
    @FXML
    void cancel(ActionEvent event) {
        txtteacherid.setText("");
        txtteacherpost.setText("");
        txtteachername.setText("");
        cbexp.getSelectionModel().clearSelection();

        txtteacherid.setEditable(true);

        btnaddteacher.setVisible(true);
        btnupdateteacher.setVisible(false);
        btnremoveteacher.setVisible(false);

        txtteacherid.setDisable(false);
    }

    @FXML
    void print(ActionEvent event) {

    }


    @FXML
    void searchAll(ActionEvent event) {

       loadTeacherTable();
    }


    @FXML
    void removeTeacher(ActionEvent event) {
        try {
            if (!txtteacherid.getText().equals(""))
            {
                JFXButton yesbutton = new JFXButton("Yes");
                JFXButton nobutton = new JFXButton("No");
                List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

                yesbutton.setOnAction(event3 ->{
                    try {
                        PreparedStatement ps = DbHandler.getConnection().prepareStatement("delete from  teacher  where id = ?");
                        ps.setString(1,txtteacherid.getText());

                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"Teacher : "+ txtteachername.getText()+" Removed");
                        ps.execute();

                        new MyAlert().createAlert("Teacher Data Deleted ","Deleted",event);
                        searchAll(event);
                        resetInputControls();
                        btnupdateteacher.setVisible(false);
                        btnremoveteacher.setVisible(false);
                        btnaddteacher.setVisible(true);

                    } catch (SQLException e) {
                        e.printStackTrace();
                        new MyAlert().createAlert("Some Error happened ","Delete",event);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        new MyAlert().createAlert("Please specify data in proper format","Delete",event);
                    }

                });

                nobutton.setOnAction(event3 ->{
                    new MyAlert().createAlert(" Operation Cancelled ","Delete",event);

                });
                new MyAlert().confirm("Do you want to Delete the Teacher Data ?","Delete ?",event,listcontrol);


            }
        } catch (Exception e) {
            e.printStackTrace();
            new MyAlert().createAlert("Some Error Occured ","Error",event);
        }
    }

    @FXML
    void search(ActionEvent event) {
        if (!(txtsearchteacher.getText().equals("") || cbcourse2.getSelectionModel().isEmpty() || cbsearch.getSelectionModel().isEmpty()))
        {
            tlist.clear();
           try  {
               // Searching teacher by name
               if (cbsearch.getValue().equals("Name"))
               {
                   PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from teacher where tname = ? and course = ?");
                   ps.setString(1,txtsearchteacher.getText());
                   ps.setString(2,cbcourse2.getValue());
                   loadTeacherTableData(ps);
               }
               // Searching teacher by post
               else if (cbsearch.getValue().equals("Post"))
               {
                   PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from teacher where post = ? and course = ?");
                   ps.setString(1,txtsearchteacher.getText());
                   ps.setString(2,cbcourse2.getValue());
                   loadTeacherTableData(ps);
               }

               // Searching teacher by id
               else if (cbsearch.getValue().equals("ID"))
               {
                   PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from teacher where id = ? ");
                   ps.setString(1,txtsearchteacher.getText());
                   loadTeacherTableData(ps);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           catch (NumberFormatException e)
           {
               new MyAlert().createAlert("Please specify Proper ID Format","Wrong Format",event);
           }
        }
        else if (!(txtsearchteacher.getText().equals("") || !cbcourse2.getSelectionModel().isEmpty() || cbsearch.getSelectionModel().isEmpty()))
           {
               tlist.clear();
               try  {

                   if (cbsearch.getValue().equals("Name"))
                   {
                       PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from teacher where tname = ?");
                       ps.setString(1,txtteachername.getText());
                       loadTeacherTableData(ps);
                   }
                   else if (cbsearch.getValue().equals("Post"))
                   {
                       PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from teacher where post = ?");
                       ps.setString(1,txtteachername.getText());
                       loadTeacherTableData(ps);
                   }

                   else if (cbsearch.getValue().equals("ID"))
                   {
                       PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from teacher where id = ? ");
                       ps.setString(1,txtteachername.getText());
                       loadTeacherTableData(ps);
                   }
               } catch (SQLException e) {
                   e.printStackTrace();
               }
               catch (NumberFormatException e)
               {
                   new MyAlert().createAlert("Please specify in Proper Format","Wrong Format",event);
               }
           }
        else
            new MyAlert().createAlert("Please specify search criteria","Unspecified Search",event);
    }

    public void setUser(String username) {
        this.username = username;
    }

//    class CreateTeacherTable extends Task{
//
//        @Override
//        protected Object call() throws Exception {
//            DbHandler.createTeacherTable();
//            return null;
//        }
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        Thread t = new Thread(new CreateTeacherTable());
//        t.setDaemon(true);
//        t.start();

        cbsearch.getItems().addAll("Name","Post","ID");
        btnaddteacher.setVisible(true);
        btnremoveteacher.setVisible(false);
        btnupdateteacher.setVisible(false);

        coltid.setPrefWidth(tblteacher.getPrefWidth()/5);
        coltname.setPrefWidth(tblteacher.getPrefWidth()/5);
        coltpost.setPrefWidth(tblteacher.getPrefWidth()/5);
        colexp.setPrefWidth(tblteacher.getPrefWidth()/5);
        colcourse.setPrefWidth(tblteacher.getPrefWidth()/5);

        tblteacher.widthProperty().addListener((observable, oldValue, newValue) -> {
            coltid.setPrefWidth((double)newValue/5);
            coltname.setPrefWidth((double)newValue/5);
            coltpost.setPrefWidth((double)newValue/5);
            colexp.setPrefWidth((double)newValue/5);
            colcourse.setPrefWidth((double)newValue/5);
        });


        coltid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coltpost.setCellValueFactory(new PropertyValueFactory<>("post"));
        coltname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colexp.setCellValueFactory(new PropertyValueFactory<>("exp"));
        colcourse.setCellValueFactory(new PropertyValueFactory<>("course"));

        loadCourse();


        ObservableList<String> explist = FXCollections.observableArrayList();
        for (int i = 0; i < 30; i++) {
            explist.add(i+1+"");
        }
        cbexp.getItems().setAll(explist);

        tblteacher.getItems().setAll(tlist);

        // Subject Teacher TAb

        cbclass2.getItems().setAll("Semester1","Semester2","Semester3","Semester4","Semester5","Semester6");
        cbclass3.getItems().setAll("Semester1","Semester2","Semester3","Semester4","Semester5","Semester6");


        colcourse2.setCellValueFactory(new PropertyValueFactory<>("course"));
        colclass2.setCellValueFactory(new PropertyValueFactory<>("classname"));
        colteachername2.setCellValueFactory(new PropertyValueFactory<>("tname"));
        colsubject2.setCellValueFactory(new PropertyValueFactory<>("sname"));

        colclass2.setPrefWidth(tblSubjectTeacher.getPrefWidth()/4);
        colcourse2.setPrefWidth(tblSubjectTeacher.getPrefWidth()/4);
        colteachername2.setPrefWidth(tblSubjectTeacher.getPrefWidth()/4);
        colsubject2.setPrefWidth(tblSubjectTeacher.getPrefWidth()/4);

        tblSubjectTeacher.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                colclass2.setPrefWidth(newValue.intValue()/4);
                colcourse2.setPrefWidth(newValue.intValue()/4);
                colteachername2.setPrefWidth(newValue.intValue()/4);
                colsubject2.setPrefWidth(newValue.intValue()/4);
            }
        });

//        txtteachername.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                String id = newValue;
//                id = id.replace(" ","_");
//                if (!cbcourse.getSelectionModel().isEmpty())
//                {
//                    txtteacherid.setText("teacher_"+cbcourse.getValue().replace(" ","_")+ "_"+txtteachername.getText());
//                }
//            }
//        });
    }


    class  LoadTeacherTable extends  Task
    {
        @Override
        protected Integer call() throws Exception {
            tlist.clear();
            try {
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT  * from teacher ");
                loadTeacherTableData(ps);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void loadTeacherTableData(PreparedStatement ps) throws SQLException {

        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            tlist.add(new Teacher(rs.getString("id"),rs.getString("tname"),rs.getString("post"),rs.getInt("exp"),rs.getString("course")));
        }
        tblteacher.getItems().clear();
        tblteacher.getItems().setAll(tlist);
    }

    private void loadTeacherTable() {
      Thread t = new Thread(new LoadTeacherTable());
      t.setDaemon(true);
      t.start();
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

            cbscourse2.getItems().clear();
            cbscourse2.getItems().setAll(cname);

            cbcourse3.getItems().clear();
            cbcourse3.getItems().setAll(cname);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadTeacherRecord(MouseEvent event) {
            Teacher teacher = tblteacher.getSelectionModel().getSelectedItem();

            if (teacher!=null)
            {
                btnupdateteacher.setVisible(true);
                btnremoveteacher.setVisible(true);
                btnaddteacher.setVisible(false);

                txtteacherid.setText(teacher.getId()+"");
                txtteachername.setText(teacher.getName()+"");
                txtteacherpost.setText(teacher.getPost()+"");
                cbcourse.setValue(teacher.getCourse());
                cbexp.setValue(teacher.getExp()+"");

                txtteacherid.setEditable(false);
            }
    }

    public class Teacher
    {
        private String id;
        private int exp;
        private String name,post,course;

        public Teacher() {
        }

        public Teacher(String id, String name, String post, int exp, String course) {
            this.id = id;
            this.name = name;
            this.post = post;
            this.exp = exp;
            this.course = course;
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

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public String getCourse() {
            return course;
        }

        public int getExp() {
            return exp;
        }

        public void setExp(int exp) {
            this.exp = exp;
        }

        public void setCourse(String course) {
            this.course = course;
        }
    }



    // Subject TAb


    @FXML
    void addsubject(ActionEvent event) {
        if (!(cbscourse2.getSelectionModel().isEmpty() || cbclass2.getSelectionModel().isEmpty() || cbteacher2.getSelectionModel().isEmpty() || cbsubject2.getSelectionModel().isEmpty())){
            try{

                PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT  count(*) from subteacher where tname = ? and course = ? and class = ?");
                ps.setString(1,cbteacher2.getValue());
                ps.setString(2,cbscourse2.getValue());
                ps.setString(3,cbclass2.getValue());
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    if (rs.getInt(1)==0){

                        ps = DbHandler.getConnection().prepareStatement("SELECT  count(*) from subteacher where sname = ? and course = ? and class = ?");
                        ps.setString(1,cbsubject2.getValue());
                        ps.setString(2,cbscourse2.getValue());
                        ps.setString(3,cbclass2.getValue());
                        rs = ps.executeQuery();
                        if (rs.next()){
                            if (rs.getInt(1)==0){
                                ps = DbHandler.getConnection().prepareStatement("INSERT into subteacher(sname,tname,course,class) VALUES (?,?,?,?)");
                                ps.setString(1,cbsubject2.getValue());
                                ps.setString(2,cbteacher2.getValue());
                                ps.setString(3,cbscourse2.getValue());
                                ps.setString(4,cbclass2.getValue());
                                ps.execute();

                                ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                                ps.setString(1,username);
                                ps.setString(2, txtteachername.getText()+"added for teaching "+cbsubject2.getValue()+" subject in "+ cbcourse2.getValue()+ " "+ cbclass2.getValue() );
                                ps.execute();

                                new MyAlert().createAlert("Data Entered ","Success",event);
                                showAllSubTeacher(event);
                            }
                            else
                                new MyAlert().createAlert("Subject is Already taught by Someone else ","Error",event);
                        }
                    }
                    else
                        new MyAlert().createAlert("Teacher is already Engaged in "+ cbscourse2.getValue()+ " in "+ cbclass2.getValue(),"Error",event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new MyAlert().createAlert("Db Error ","Error",event);
            }
        }
        else
            new MyAlert().createAlert("Please specify all details","Unspecified Selectiom",event);

    }



    @FXML
    void clearSubject(ActionEvent event) {
        btnaddsubject.setVisible(true);
        btndeletesubject.setVisible(false);
        //btnupdatesubject.setVisible(false);

        cbscourse2.getSelectionModel().clearSelection();
        cbclass2.getSelectionModel().clearSelection();
        cbteacher2.getSelectionModel().clearSelection();
        cbsubject2.getSelectionModel().clearSelection();

    }

    @FXML
    void deleteSubject(ActionEvent event) {


        JFXButton yesbutton = new JFXButton("Yes");
        JFXButton nobutton = new JFXButton("No");
        List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

        yesbutton.setOnAction(event3 ->{
            try {
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("delete from  subteacher  where course = ? and class = ? and sname = ? and tname = ? ");

                ps.setString(1,cbscourse2.getValue());
                ps.setString(2,cbclass2.getValue());
                ps.setString(3,cbsubject2.getValue());
                ps.setString(4,cbteacher2.getValue());


                ps.execute();

                ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                ps.setString(1,username);
                ps.setString(2, txtteachername.getText()+" unselected  for teaching "+cbsubject2.getValue()+" subject in "+ cbcourse2.getValue()+ " "+ cbclass2.getValue() );
                ps.execute();


//                ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,current_date())");
//                ps.setString(1,username);
//                ps.setString(2,"Teacher : "+ cbteacher2.getValue()+" leave from subject "+ cbsubject2);
//                ps.execute();

                new MyAlert().createAlert(" Data Deleted ","Deleted",event);

                btnaddsubject.setVisible(true);
                btndeletesubject.setVisible(false);
                clearSubject(event);
                showAllSubTeacher(event);
            } catch (SQLException e) {
                e.printStackTrace();
                new MyAlert().createAlert("Some Error happened ","Delete",event);
            }catch (NumberFormatException e){
                e.printStackTrace();
                new MyAlert().createAlert("Please specify data in proper format","Delete",event);
            }

        });

        nobutton.setOnAction(event3 ->{
            new MyAlert().createAlert(" Operation Cancelled ","Delete",event);

        });

        new MyAlert().confirm("Do you want to Delete ?","Delete ?",event,listcontrol);

    }



//    @FXML
//    void updateSubject(ActionEvent event) {
//        if (!(cbscourse2.getSelectionModel().isEmpty() || cbclass2.getSelectionModel().isEmpty() || cbteacher2.getSelectionModel().isEmpty() || cbsubject2.getSelectionModel().isEmpty())){
//            try{
//
//                PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT  count(*) from subteacher where sname = ? and tname = ? and course = ? and class = ?");
//                ps.setString(1,cbsubject2.getValue());
//                ps.setString(2,cbteacher2.getValue());
//                ps.setString(3,cbscourse2.getValue());
//                ps.setString(4,cbclass2.getValue());
//                ResultSet rs = ps.executeQuery();
//                if (rs.next()){
//                    if (rs.getInt(1)==0){
//                        ps = DbHandler.getConnection().prepareStatement("UPDATE subteacher set sname = ? , tname = ? where course = ? and class = ?");
//
//                        ps.setString(1,cbsubject2.getValue());
//                        ps.setString(2,cbteacher2.getValue());
//                        ps.setString(3,cbscourse2.getValue());
//                        ps.setString(4,cbclass2.getValue());
//
//                        ps.execute();
//
//                        new MyAlert().createAlert("Data Updated ","Success",event);
//                        showAllSubTeacher(event);
//                    }
//                    else
//                        new MyAlert().createAlert("Duplicate Record ","Error",event);
//                }
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//                new MyAlert().createAlert("Db Error ","Error",event);
//            }
//        }
//        else
//            new MyAlert().createAlert("Please DO All Selection","Unspecified Selectiom",event);
//
//    }

    @FXML
    void loadTeacherName(ActionEvent event) {
        try {

            ObservableList tlist = FXCollections.observableArrayList();
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT tname from teacher where course = ?");
            ps.setString(1,cbscourse2.getValue());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                tlist.add(rs.getString("tname"));
            }

            cbteacher2.getItems().setAll(tlist);

            ObservableList slist = FXCollections.observableArrayList();
            ps = DbHandler.getConnection().prepareStatement("SELECT sname from subject where course = ? and class = ?");
            ps.setString(1,cbscourse2.getValue());
            ps.setString(2,cbclass2.getValue());
            rs = ps.executeQuery();
            while (rs.next()){
                slist.add(rs.getString("sname"));
            }

            cbsubject2.getItems().setAll(slist);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void searchSubjectTeacher(ActionEvent event) {
        if (!(cbcourse3.getSelectionModel().isEmpty() || cbclass3.getSelectionModel().isEmpty())){

          try {
              subTeachersList.clear();
              PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT * From subteacher where course = ? and class = ?");
              ps.setString(1,cbcourse3.getValue());
              ps.setString(2,cbclass3.getValue());
              loadSubjectTeacherTable(ps);
          } catch (SQLException e) {
              e.printStackTrace();
              new MyAlert().createAlert("System Error ","Error",event);
          }

        }else
            new MyAlert().createAlert("Select Course and Class ","Error",event);

    }

    ObservableList<SubTeacher> subTeachersList = FXCollections.observableArrayList();
    @FXML
    void showAllSubTeacher(ActionEvent event) {
        try{

            subTeachersList.clear();
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT * From subteacher");
            loadSubjectTeacherTable(ps);

        } catch (SQLException e) {
            e.printStackTrace();
            new MyAlert().createAlert("System Error ","Error",event);
        }
    }

    private void loadSubjectTeacherTable(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            subTeachersList.add(new SubTeacher(rs.getString("course"),rs.getString("sname"),rs.getString("tname"),rs.getString("class")));
        }
        tblSubjectTeacher.getItems().setAll(subTeachersList);
    }

    @FXML
    void loadTeacherSubjectRecord(MouseEvent event) {

        if (!tblSubjectTeacher.getSelectionModel().isEmpty()){

            SubTeacher subTeacher = tblSubjectTeacher.getSelectionModel().getSelectedItem();

            cbscourse2.setValue(subTeacher.getCourse());
            cbclass2.setValue(subTeacher.getClassname());
            cbteacher2.setValue(subTeacher.getTname());
            cbsubject2.setValue(subTeacher.getSname());

            btnaddsubject.setVisible(false);
            btndeletesubject.setVisible(true);
            //btnupdatesubject.setVisible(true);
        }

    }
    public class SubTeacher
    {
        private String course,sname,tname,classname;

        public SubTeacher(String course, String sname, String tname, String classname) {
            this.course = course;
            this.sname = sname;
            this.tname = tname;
            this.classname = classname;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }
    }
}
