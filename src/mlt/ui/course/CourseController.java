package mlt.ui.course;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import mlt.db.DbHandler;
import mlt.ui.MyAlert.MyAlert;
import mlt.ui.support.GetStage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CourseController implements Initializable {

    @FXML
    private AnchorPane anchorcenter;

    @FXML
    private JFXTextField txtcourse;

    @FXML
    private JFXComboBox<String> cbsearch;

    @FXML
    private TableView<Course> tblcourse;

    @FXML
    private TableColumn<Course, Integer> colcourseid;

    @FXML
    private TableColumn<Course, String> colcoursename;

    @FXML
    private TableColumn<Course, String> colcourseinfo;

    @FXML
    private TableColumn<Course,Integer> colcoursedur;

    @FXML
    private JFXTextField txtcoursename;

    @FXML
    private JFXTextField txtcourseduration;

    @FXML
    private JFXTextField txtcourseinfo;

    @FXML
    private JFXTextField txtcourseid;

    @FXML
    private JFXButton btncheckid;

    @FXML
    private FontAwesomeIconView glyidverify;

    @FXML
    private JFXButton btnupdatecourse;

    @FXML
    private JFXButton btnremovecourse;

    @FXML
    private JFXButton btnaddcourse;

    private StackPane mainStakepane ;

    Stage stage;

    Course tempcourse = new Course();

    ObservableList<Course> clist = FXCollections.observableArrayList();
    private String username;

//
//    JasperDesign jdesign = null;
//    JRDesignQuery updateQuery = null;
//    JasperReport jasperReport = null;
//    JasperPrint jasperPrint = null;


    private void resetInputControls()
    {
        txtcourseid.setText("");
        txtcourseduration.setText("");
        txtcourseinfo.setText("");
        txtcoursename.setText("");
        txtcourse.setText("");
    }
    @FXML
    void EditCourse(ActionEvent event) {
        if (!(txtcoursename.getText().equals("") | txtcourseinfo.getText().equals("") | txtcourseduration.getText().equals("") | txtcourseid.getText().equals(""))){
            JFXButton yesbutton = new JFXButton("Yes");
            JFXButton nobutton = new JFXButton("No");
            List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

            yesbutton.setOnAction(event3 ->{
                try {
                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("update course set cname = ? , cduration = ?, cdesc = ? where id = ?");
                    ps.setString(1,txtcoursename.getText());

                    System.out.println(txtcourseduration.getText().equals(""));
                    ps.setInt(2,Integer.parseInt(txtcourseduration.getText()));
                    ps.setString(3,txtcourseinfo.getText());
                    ps.setString(4,txtcourseid.getText());

                    ps.execute();
                    new MyAlert().createAlert("Course Updated ","update",event);


                    ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                    ps.setString(1,username);
                    ps.setString(2,"Course "+ txtcoursename.getText()+" updated");
                    ps.execute();


                    btnupdatecourse.setVisible(false);
                    btnremovecourse.setVisible(false);
                    btnaddcourse.setVisible(true);
                    txtcourseid.setEditable(true);

                    btncheckid.setDisable(false);

                    showAll(event);
                    resetInputControls();

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



            new MyAlert().confirm("Do you want to update the course ?","Update ?",event,listcontrol);




        }
            else
                new MyAlert().createAlert("Pleaese fill all details","Fill Details",event);
    }

    @FXML
    void addCourse(ActionEvent event)  {

        if (!(txtcoursename.getText().equals("") || txtcourseinfo.getText().equals("") || txtcourseduration.getText().equals("") || txtcourseid.getText().equals(""))) {

            try {
                tempcourse.setName(txtcoursename.getText());
                tempcourse.setInfo(txtcourseinfo.getText());
                tempcourse.setDur(Integer.parseInt(txtcourseduration.getText()));

                if (checkCourse(event)) {

                    try {
                        PreparedStatement ps = DbHandler.getConnection().prepareStatement("insert into course values(?,?,?,?)");
                        ps.setString(1, tempcourse.getId());
                        ps.setString(2, tempcourse.getName());
                        ps.setInt(3, tempcourse.getDur());
                        ps.setString(4, tempcourse.getInfo());

                        Boolean result = ps.execute();

                        String course = txtcoursename.getText();
                       // boolean flag = course.contains("[A-Za-z|]")
                        //String classname =
                        DbHandler.createStudentTable(course,"FY");

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1, username);
                        ps.setString(2, "Course " + txtcoursename.getText() + " added");
                        ps.execute();


                        new MyAlert().createAlert("Course Inserted Successfully", "Course Added", event);


                        showAll(event);
                        txtcourseid.setText("");
                        txtcourseduration.setText("");
                        txtcourseinfo.setText("");
                        txtcoursename.setText("");
                        glyidverify.setVisible(false);

                        System.out.println(result);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new MyAlert().createAlert("Enter Proper Details", "Enter All Data", event);
                    }
                }
            } catch (Exception e) {
                new MyAlert().createAlert("Enter All Specified Details", "Enter All Data", event);
            }
        }
        else
            new MyAlert().createAlert("Enter All Apecified Details","Enter All Data",event);
    }

    @FXML
    void removeCourse(ActionEvent event) {
        if (!txtcourseid.getText().equals("")){
            JFXButton yesbutton = new JFXButton("Yes");
            JFXButton nobutton = new JFXButton("No");
            List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

            yesbutton.setOnAction(event3 ->{

                try {



                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("delete from course where id = ?");

                    ps.setString(1,txtcourseid.getText());
                    ps.execute();

                    ps = DbHandler.getConnection().prepareStatement("drop table fy_"+ txtcoursename.getText() + "_student");
                    ps.execute();

                    ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                    ps.setString(1,username);
                    ps.setString(2,"Course "+ txtcoursename.getText()+" removed");
                    ps.execute();



                    btnupdatecourse.setVisible(false);
                    btnremovecourse.setVisible(false);
                    btnaddcourse.setVisible(true);
                    txtcourseid.setEditable(true);

                    btncheckid.setDisable(false);

                    new MyAlert().createAlert("Course Deleted ","Delete",event);
                    resetInputControls();
                    showAll(event);
                } catch (SQLException e) {
                    e.printStackTrace();
                    new MyAlert().createAlert("First Remove Course Associated Details","Delete",event);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    new MyAlert().createAlert("Please specify data in proper format","Delete",event);
                }

            });

            nobutton.setOnAction(event3 ->{
                new MyAlert().createAlert(" Operation Cancelled ","Delete",event);

            });



            new MyAlert().confirm("Do you want to Delete the course ?","Delete ?",event,listcontrol);




        }else
            new MyAlert().createAlert("Please provide the ID","ID Error",event);
    }

    public void setUser(String username) {
        this.username = username;
    }

    @FXML
    void search(ActionEvent event) {
       if (!txtcourse.getText().equals("")){

           stage = GetStage.getStage(event);
           try  {
               PreparedStatement ps = null;
               if (cbsearch.getValue().equals("ID"))
               {
                   ps = DbHandler.getConnection().prepareStatement("select * from course where id = ?");
                   ps.setString(1,txtcourse.getText());
                   loadSearchData(ps);
               }
               else  if (cbsearch.getValue().equals("Name"))
               {
                   ps = DbHandler.getConnection().prepareStatement("select * from course where cname like ?");
                   ps.setString(1,"%"+txtcourse.getText()+"%");
                   loadSearchData(ps);
               }
               else  if (cbsearch.getValue().equals("Duration"))
               {
                   ps = DbHandler.getConnection().prepareStatement(" select * from course where cduration = ?");

                   try{
                       int no = Integer.parseInt(txtcourse.getText());
                       ps.setInt(1,no);
                       loadSearchData(ps);
                   }
                   catch (NumberFormatException e)
                   {
                       new MyAlert().createAlert("Please Provide data in proper format ","Warning ",stage);
                   }


               }
               else  if (cbsearch.getValue().equals("Description"))
               {
                   ps = DbHandler.getConnection().prepareStatement(" select * from course where cdesc like ?");
                   ps.setString(1,"%"+txtcourse.getText()+"%");
                   loadSearchData(ps);
               }
               else
               {
                   new MyAlert().createAlert("Please Specify Search Criteria of Course ","Warning ",stage);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
       else
           new MyAlert().createAlert("Please Specify "+ cbsearch.getValue()+" of Course ","Warning ",event);

       txtcourse.setText("");
    }

    private void loadSearchData(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();

        clist.clear();
        while (rs.next())
        {
            clist.add(new Course(rs.getString("id"),rs.getInt("cduration"),rs.getString("cname"),rs.getString("cdesc")));
        }
        tblcourse.getItems().clear();
        tblcourse.getItems().setAll(clist);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        cbsearch.getItems().addAll("ID","Name","Duration","Description");
        cbsearch.setValue("Name");

        colcourseid.setPrefWidth(tblcourse.getPrefWidth()/4);
        colcoursedur.setPrefWidth(tblcourse.getPrefWidth()/4);
        colcourseinfo.setPrefWidth(tblcourse.getPrefWidth()/4);
        colcoursename.setPrefWidth(tblcourse.getPrefWidth()/4);

        tblcourse.widthProperty().addListener((observable, oldValue, newValue) -> {
            colcourseid.setPrefWidth((double)newValue/4);
            colcoursedur.setPrefWidth((double)newValue/4);
            colcourseinfo.setPrefWidth((double)newValue/4);
            colcoursename.setPrefWidth((double)newValue/4);
        });

        initCourseTable();

        glyidverify.setVisible(false);

        btnaddcourse.setVisible(true);
        btnremovecourse.setVisible(false);
        btnupdatecourse.setVisible(false);

//        try {
//            jdesign = JRXmlLoader.load("src/mlt/jrxml/courseList.jrxml");
//        } catch (JRException e) {
//            e.printStackTrace();
//        }
//
//        updateQuery = new JRDesignQuery();
//
//        tblcourse.setRowFactory(new Callback<TableView<Course>, TableRow<Course>>() {
//            @Override
//            public TableRow<Course> call(TableView<Course> tableView) {
//                final TableRow<Course> row = new TableRow<Course>() {
//                    @Override
//                    protected void updateItem(Course row, boolean empty) {
//                        super.updateItem(row, empty);
//                        if (!empty)
//                            styleProperty().bind(Bindings.when(BooleanProperty(row.getDur()==3))
//                                    .then("-fx-font-weight: bold; -fx-font-size: 16;")
//                                    .otherwise(""));
//                    }
//                };
//                return row;
//            }
//        });

//        txtcoursename.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                String id = newValue;
//                id = id.replace(" ","_");
//                txtcourseid.setText("UG_"+id);
//            }
//        });
    }


    private void initCourseTable() {
        colcoursename.setCellValueFactory(new PropertyValueFactory<>("name"));
        colcourseinfo.setCellValueFactory(new PropertyValueFactory<>("info"));
        colcoursedur.setCellValueFactory(new PropertyValueFactory<>("dur"));
        colcourseid.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//
//        clist.add(new Course(1,3,"IT","Information Technology"));
//        clist.add(new Course(1,3,"IT","Information Technology"));
//        clist.add(new Course(1,3,"IT","Information Technology"));
//        clist.add(new Course(1,3,"IT","Information Technology"));
//        clist.add(new Course(1,3,"IT","Information Technology"));
//        clist.add(new Course(1,3,"IT","Information Technology"));
//        tblcourse.getItems().setAll(clist);
    }

    @FXML
    boolean checkCourse(ActionEvent event) {

        if (!txtcourseid.getText().equals(""))
        {
            try {
                tempcourse.setId(txtcourseid.getText());
                glyidverify.setVisible(false);
                try {
                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("select count(id) from course where id = ? ");
                    ps.setString(1,tempcourse.getId());
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    int result = rs.getInt(1);
                    if (result == 0){
                        glyidverify.setVisible(true);
                        return true;
                    }
                    else
                    {
                        new MyAlert().createAlert("ID already used ","Duplicate ID ",event);
                        return false;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }catch (NumberFormatException e)
            {
                new MyAlert().createAlert("Provide ID in proper formate ","Info..",event);
            }
        }
        else
        {
           new MyAlert().createAlert("Enter the ID ","ID Unspecified", GetStage.getStage(event));
        }
        return false;
    }


    public class Course{

        private int dur;
        private String name;
        private String id;
        private String info;

        public Course(String id, int dur, String name, String info) {
            this.id = id;
            this.dur = dur;
            this.name = name;
            this.info = info;
        }

        public Course() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getDur() {
            return dur;
        }

        public void setDur(int dur) {
            this.dur = dur;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }


    @FXML
    void showAll(ActionEvent event) throws SQLException {
        stage = GetStage.getStage(event);
        Thread t = new Thread(new ShowAllCourses());
        t.setDaemon(true);
        t.start();


    }

    class ShowAllCourses extends Task
    {

        @Override
        protected Integer call()  {

            try {
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("Select * from course");
                ResultSet rs = ps.executeQuery();

                clist.clear();
                while (rs.next())
                {
                    clist.add(new Course(rs.getString("id"),rs.getInt("cduration"),rs.getString("cname"),rs.getString("cdesc")));
                }
                tblcourse.getItems().clear();
                tblcourse.getItems().setAll(clist);


                txtcourse.setText("");

            } catch (SQLException e) {
                e.printStackTrace();
                new MyAlert().createAlert("Something Wrong Happened","Error",stage);

            }

            return null;
        }
    }
    @FXML
    void loadRowData(MouseEvent event) {
               Course course = tblcourse.getSelectionModel().getSelectedItem();
               if (course!= null)
               {
                   txtcoursename.setText(course.getName());
                   txtcourseinfo.setText(course.getInfo());
                   txtcourseduration.setText(course.getDur()+"");
                   txtcourseid.setText(course.getId()+"");
                   btncheckid.setDisable(true);

                   btnupdatecourse.toFront();
                   btnupdatecourse.setVisible(true);
                   txtcourseid.setEditable(false);
                   btnaddcourse.setVisible(false);
                   btnremovecourse.setVisible(true);
               }
    }

    @FXML
    void clear(ActionEvent event) {


        txtcourseid.setEditable(true);
        resetInputControls();
        btnaddcourse.setVisible(true);
        btncheckid.setDisable(false);
        btnremovecourse.setVisible(false);
        btnupdatecourse.setVisible(false);
        glyidverify.setVisible(false);


    }

    @FXML
    void print(ActionEvent event) {


//      //  String sql = "select * from course where caste ='"+cbcaste.getValue()+"' order by marks desc limit "+ cbseats.getValue();
//
//        try{
//            updateQuery.setText("select * from course");
//            jdesign.setQuery(updateQuery);
//            jasperReport = JasperCompileManager.compileReport(jdesign);
//            jasperPrint = JasperFillManager.fillReport(jasperReport,null,DbHandler.getConnection());
//            JasperViewer.viewReport(jasperPrint,false);
//        } catch (JRException e) {
//            e.printStackTrace();
//        }

//       try{
//           PrinterJob job = PrinterJob.createPrinterJob();
//
//           boolean proceed = job.showPageSetupDialog(GetStage.getStage(event));
//
//           if (proceed)
//           {
//               if(job != null){
////                   job.showPrintDialog(GetStage.getStage(event)); // Window must be your main Stage
////
//                   boolean printed = job.printPage(tblcourse);
//
//
//                   if (printed)
//                   {
//                       job.endJob();
//                   }
//               }
//           }
//
//
//           // Create the Printer Job
//           PrinterJob printerJob = PrinterJob.createPrinterJob();
//
//           // Get The Printer Job Settings
//           JobSettings jobSettings = printerJob.getJobSettings();
//
//           // Get the Page Layout
//           PageLayout pageLayout = jobSettings.getPageLayout();
//
//           // Get the Page Attributes
//           double pgW = pageLayout.getPrintableWidth();
//           double pgH = pageLayout.getPrintableHeight();
//           double pgLM = pageLayout.getLeftMargin();
//           double pgRM = pageLayout.getRightMargin();
//           double pgTM = pageLayout.getTopMargin();
//           double pgBM = pageLayout.getBottomMargin();
//
//           // Show the Page Attributes
//           System.out.println("Printable Width: " + pgW + "\n");
//           System.out.println("Printable Height: " + pgH + "\n");
//           System.out.println("Page Left Margin: " + pgLM + "\n");
//           System.out.println("Page Right Margin: " + pgRM + "\n");
//           System.out.println("Page Top Margin: " + pgTM + "\n");
//           System.out.println("Page Bottom Margin: " + pgBM + "\n");
//
//           // Get The Printer
//           Printer printer = printerJob.getPrinter();
//           // Create the Page Layout of the Printer
//           pageLayout = printer.createPageLayout(Paper.A4,
//                   PageOrientation.PORTRAIT,Printer.MarginType.EQUAL);
//
//           jobSettings.setPageLayout(pageLayout);
//
//           // Get the Page Attributes
//           pgW = pageLayout.getPrintableWidth();
//           pgH = pageLayout.getPrintableHeight();
//           pgLM = pageLayout.getLeftMargin();
//           pgRM = pageLayout.getRightMargin();
//           pgTM = pageLayout.getTopMargin();
//           pgBM = pageLayout.getBottomMargin();
//
//           // Show the Page Attributes
//           System.out.println("Printable Width: " + pgW + "\n");
//           System.out.println("Printable Height: " + pgH + "\n");
//           System.out.println("Page Left Margin: " + pgLM + "\n");
//           System.out.println("Page Right Margin: " + pgRM + "\n");
//           System.out.println("Page Top Margin: " + pgTM + "\n");
//           System.out.println("Page Bottom Margin: " + pgBM + "\n");
//       }
//       catch(Exception e){
//           new MyAlert().createAlert("Something Wrong Happened","Error",stage);
//       }
    }

}


