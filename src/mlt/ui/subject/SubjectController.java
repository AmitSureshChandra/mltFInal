package mlt.ui.subject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import mlt.db.DbHandler;
import mlt.ui.MyAlert.MyAlert;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class SubjectController implements Initializable {

    @FXML
    private AnchorPane anchorcenter;

    @FXML
    private JFXTextField txtsearchsubject;

    @FXML
    private JFXComboBox<String> cbsearch;

    @FXML
    private JFXComboBox<String> cbcourse;

    @FXML
    private JFXComboBox<String> cbcourse2;

    @FXML
    private JFXComboBox<String> cbclass2;

    @FXML
    private TableView<Subject> tblsubject;

    @FXML
    private TableColumn<Subject, String> colsid;

    @FXML
    private TableColumn<Subject, String> colsname;

    @FXML
    private TableColumn<Subject, String> colclass;

    @FXML
    private TableColumn<Subject, String> colcourse;

    @FXML
    private JFXButton btnaddSubject;

    @FXML
    private JFXTextField txtsubjectid;


    @FXML
    private JFXTextField txtsubjectname;

    @FXML
    private JFXComboBox<String> cbclass;

    @FXML
    private JFXButton btnremoveSubject;

    @FXML
    private JFXButton btnupdatesubject;

    ObservableList<String> classlist = FXCollections.observableArrayList();
    ObservableList<Subject> slist = FXCollections.observableArrayList();
    private String username;


    @FXML
    void EditSubject(ActionEvent event) {
        try {
            if (!(txtsubjectname.getText().equals("") || txtsubjectid.getText().equals("") ||  cbcourse.getSelectionModel().isEmpty() ||  cbclass.getSelectionModel().isEmpty()))
            {
                JFXButton yesbutton = new JFXButton("Yes");
                JFXButton nobutton = new JFXButton("No");
                List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

                yesbutton.setOnAction(event3 ->{
                    try {
                        PreparedStatement ps = DbHandler.getConnection().prepareStatement("update subject set  sname = ? , course = ? , class = ? where id = ?");

                        ps.setString(1,txtsubjectname.getText());
                        ps.setString(2,cbcourse.getValue());
                        ps.setString(3,cbclass.getValue());
                        ps.setString(4,txtsubjectid.getText());
                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"Subject : "+ txtsubjectname.getText()+" Updated");
                        ps.execute();

                        new MyAlert().createAlert("Subject Data Updated ","update",event);
                        cancel(event);
                        tblsubject.getItems().clear();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new MyAlert().createAlert("Some Error happened ","update",event);
                    }
                });
                nobutton.setOnAction(event3 ->{
                    new MyAlert().createAlert(" Operation Cancelled ","update",event);
                });
                new MyAlert().confirm("Do you want to update the Subject Data ?","Update ?",event,listcontrol);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void addSubject(ActionEvent event) throws SQLException {

        if (!(cbcourse.getSelectionModel().isEmpty() || cbclass.getSelectionModel().isEmpty() || txtsubjectid.getText().equals("") || txtsubjectname.getText().equals("")))
        {

                try {
                  //  DbHandler.createSubjectTable();
                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("insert into subject values(?,?,?,?)");
                    ps.setString(1,txtsubjectid.getText());
                    ps.setString(2,txtsubjectname.getText());
                    ps.setString(3,cbcourse.getValue());
                    ps.setString(4,cbclass.getValue());
                    ps.execute();

                    ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                    ps.setString(1,username);
                    ps.setString(2,"Subject : "+ txtsubjectname.getText()+" Added");
                    ps.execute();

                    new MyAlert().createAlert("Subject Added","Info",event);

                    if (!(cbcourse2.getSelectionModel().isEmpty() || cbclass2.getSelectionModel().isEmpty()))
                    {
                        listSubject(event);
                    }

                    txtsubjectname.setText("");
                    txtsubjectid.setText("");
                    cbcourse.getSelectionModel().clearSelection();
                    cbclass.getSelectionModel().clearSelection();
                }
                catch(SQLIntegrityConstraintViolationException e){
                    e.printStackTrace();
                    new MyAlert().createAlert("Check Integrity of Data","Alert",event);
                }

                catch (SQLException e) {
                    e.printStackTrace();
                    new MyAlert().createAlert("Database Server Error","Alert",event);
                }


        }
        else
            new MyAlert().createAlert("Please Fill all Specified Data","Alert",event);

    }

    @FXML
    void removeSubject(ActionEvent event) {
        try {
            if (!txtsubjectid.getText().equals(""))
            {

                // setup button and action listener
                JFXButton yesbutton = new JFXButton("Yes");
                JFXButton nobutton = new JFXButton("No");
                List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

                yesbutton.setOnAction(event3 ->{
                    try {
                        // firing query to delete the subject after confirmation
                        PreparedStatement ps = DbHandler.getConnection().prepareStatement("delete from  subject  where id = ?");
                        ps.setString(1,txtsubjectid.getText());

                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"Subject : "+ txtsubjectname.getText()+" Removed");
                        ps.execute();


                        new MyAlert().createAlert("Subject Deleted ","Deleted",event);
                        cancel(event);
                        tblsubject.getItems().clear();

                    } catch (SQLException e) {
                        e.printStackTrace();
                        new MyAlert().createAlert("Some Error happened ","Delete",event);
                    }

                });

                nobutton.setOnAction(event3 ->{
                    // to cancel operation
                    new MyAlert().createAlert(" Operation Cancelled ","Delete",event);

                });
                // asking for confirmation
                new MyAlert().confirm("Do you want to Delete the Subject ?","Delete ?",event,listcontrol);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new MyAlert().createAlert("Delete Operation failed with some Issue ","Error",event);
        }
    }

    @FXML
    void search(ActionEvent event) throws SQLException {

        //cbsearch.getItems().setAll("Teacher Name","Subject Name","Class","Course","ID");
        if (!(txtsearchsubject.getText().equals("") || cbsearch.getSelectionModel().isEmpty())){

            System.out.println( cbsearch.getValue());
            if (cbsearch.getValue().equals("Teacher Name")){
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from subject where tname = ?");
                ps.setString(1,txtsearchsubject.getText());
                loadSubjectTable(ps);


            }else   if (cbsearch.getValue().equals("ID")){
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from subject where id = ? ");
                ps.setString(1,txtsearchsubject.getText());
                loadSubjectTable(ps);

            }else   if (cbsearch.getValue().equals("Subject Name")){
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from subject where sname = ? ");
                ps.setString(1,txtsearchsubject.getText());
                loadSubjectTable(ps);

            }else   if (cbsearch.getValue().equals("Course")){
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from subject where course = ?");
                ps.setString(1,txtsearchsubject.getText());
                loadSubjectTable(ps);

            }
            else  if (cbsearch.getValue().equals("Class")){
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from subject where class = ?");
                ps.setString(1,txtsearchsubject.getText());
                loadSubjectTable(ps);
            }
            else
                new MyAlert().createAlert("System Error Please try Again ...","Alert",event);
        }
        else
            new MyAlert().createAlert("Please enter search tect and criteria","Alert",event);
    }

    @FXML
    void listSubject(ActionEvent event) throws SQLException {
        if (!(cbcourse2.getSelectionModel().isEmpty() || cbclass2.getSelectionModel().isEmpty()))
        {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from subject where course = ? and class = ?");
            ps.setString(1,cbcourse2.getValue());
            ps.setString(2,cbclass2.getValue());
            loadSubjectTable(ps);
        }
        else
            new MyAlert().createAlert("Please Specify Course and Class ","Data not Specified",event);
    }

    private void loadSubjectTable(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        slist.clear();
        while (rs.next()){
            slist.add(new Subject(rs.getString("id"),rs.getString("sname"),rs.getString("course"),rs.getString("class")));
        }
        tblsubject.getItems().clear();
        tblsubject.getItems().setAll(slist);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

       // classlist.addAll("First Year","Second Year","Third Year","Fourth year","Fifth Year","Sixth Year");
    //    classlist.addAll("First","Second","Third","Fourth","Fifth","Sixth");
       // classlist.addAll("Sem 1","Sem 2","Sem 3","Sem 4","Sem 5","Sem 6");
        classlist.addAll("Semester1","Semester2","Semester3","Semester4","Semester5","Semester6");
        cbclass.getItems().setAll(classlist);
        cbclass2.getItems().setAll(classlist);

//        DbHandler.createSubjectTable();

        colsid.setPrefWidth(tblsubject.getPrefWidth()/5);
        colsname.setPrefWidth(2*tblsubject.getPrefWidth()/5);

        colclass.setPrefWidth(tblsubject.getPrefWidth()/5);
        colcourse.setPrefWidth(tblsubject.getPrefWidth()/5);

        tblsubject.widthProperty().addListener((observable, oldValue, newValue) -> {
            colsid.setPrefWidth((double)newValue/5);
            colsname.setPrefWidth(2*(double)newValue/5);
            colclass.setPrefWidth((double)newValue/5);
            colcourse.setPrefWidth((double)newValue/5);
        });

        btnremoveSubject.setVisible(false);
        btnupdatesubject.setVisible(false);
        txtsubjectid.setDisable(false);

        colsid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colsname.setCellValueFactory(new PropertyValueFactory<>("sname"));
        colclass.setCellValueFactory(new PropertyValueFactory<>("classname"));
        colcourse.setCellValueFactory(new PropertyValueFactory<>("course"));

        cbsearch.getItems().setAll("Subject Name","Class","Course","ID");
        loadCourse();
    }

    public void loadCourse() {

        ObservableList<String> cname = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("Select cname from course");
            ResultSet rs = ps.executeQuery();
            cname.clear();
            while (rs.next())
            {
                cname.add(rs.getString("cname"));
            }

            cbcourse.getItems().clear();
            cbcourse.getItems().setAll(cname);

            cbcourse2.getItems().clear();
            cbcourse2.getItems().setAll(cname);
            cname.clear();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void cancel(ActionEvent event) {
        btnremoveSubject.setVisible(false);
        btnupdatesubject.setVisible(false);
        btnaddSubject.setVisible(true);
        txtsubjectid.setDisable(false);
        txtsubjectname.setText("");
        txtsubjectid.setText("");
        cbcourse.getSelectionModel().clearSelection();
        cbclass.getSelectionModel().clearSelection();
    }

    @FXML
    void loadSubjectRecord(MouseEvent event) {
        if (!tblsubject.getSelectionModel().isEmpty()){

            txtsubjectid.setDisable(true);
            Subject sub = tblsubject.getSelectionModel().getSelectedItem();

            txtsubjectid.setText(sub.getId());
            txtsubjectname.setText(sub.getSname());

            cbcourse.setValue(sub.getCourse());
            cbclass.setValue(sub.getClassname());

            btnaddSubject.setVisible(false);
            btnupdatesubject.setVisible(true);
            btnremoveSubject.setVisible(true);
        }
    }

    public void setUser(String username) {
        this.username = username;
    }

    public class Subject{
        private String id;
        private String sname,course,classname;

        public Subject() {
        }

        public Subject(String id, String sname, String course, String classname) {
            this.id = id;
            this.sname = sname;
            this.course = course;
            this.classname = classname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
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
    }
}
