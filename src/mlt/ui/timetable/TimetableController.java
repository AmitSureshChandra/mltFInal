package mlt.ui.timetable;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import mlt.db.DbHandler;
import mlt.ui.MyAlert.MyAlert;
import mlt.ui.support.GetStage;
import mlt.ui.timetable.model.Lecture;
import mlt.ui.timetable.model.Noteacher;
import mlt.ui.timetable.model.TimeTable;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

import java.net.URL;
public class TimetableController implements Initializable {

    @FXML
    private JFXComboBox<String> cbcourse;

    @FXML
    private JFXComboBox<String> cbclass;

    @FXML
    private JFXComboBox<String> cbhalf;

    @FXML
    private TableView<TimeTableStructure> tbltimetable;

    @FXML
    private TableColumn<TimeTableStructure, Integer> collec;

    @FXML
    private TableColumn<TimeTableStructure, String> colmon;

    @FXML
    private TableColumn<TimeTableStructure, String> coltue;

    @FXML
    private TableColumn<TimeTableStructure, String> colwed;

    @FXML
    private TableColumn<TimeTableStructure, String> colthurs;

    @FXML
    private TableColumn<TimeTableStructure, String> colfri;

    @FXML
    private TableColumn<TimeTableStructure, String> colsat;

    @FXML
    private TableColumn<TimeTableStructure, String> coltime;

    @FXML
    private AnchorPane panetimetable;

    private String username;

    //Create Time Table Tab

    @FXML
    private JFXComboBox<String> cbcourse2;

    @FXML
    private JFXComboBox<String> cbclass2;

    @FXML
    private JFXComboBox<String> cbhalf2;

    @FXML
    private JFXComboBox<String> cblecdur;

    @FXML
    private JFXComboBox<String> cbnolec;

    @FXML
    private JFXTimePicker tplecstart;

    @FXML
    private JFXComboBox<String> cbnoworkday;

    ObservableList<Lecture> lecturelist ;

    HashMap<String, Object> parameters ;
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

    Date d;
    String s;
    File dir;


    Vector<String> lecTime = new Vector<>(5);
    ObservableList<TimeTableStructure> fillLectureList = FXCollections.observableArrayList();


//    -----------------

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cbcourse.getItems().setAll("BScIT");
        //cbclass.getItems().setAll("Semester1","Semester2","Semester3","Semester4","Semester5","Semester6");
        cbclass.getItems().setAll("FY","SY","TY");

        collec.setCellValueFactory(new PropertyValueFactory<>("lecture"));
        coltime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colmon.setCellValueFactory(new PropertyValueFactory<>("dayMonday"));
        coltue.setCellValueFactory(new PropertyValueFactory<>("dayTuesday"));
        colwed.setCellValueFactory(new PropertyValueFactory<>("dayWednesday"));
        colthurs.setCellValueFactory(new PropertyValueFactory<>("dayThursday"));
        colfri.setCellValueFactory(new PropertyValueFactory<>("dayFriday"));
        colsat.setCellValueFactory(new PropertyValueFactory<>("daySaturday"));

        collec.setPrefWidth(tbltimetable.getPrefWidth()/8);
        colmon.setPrefWidth(tbltimetable.getPrefWidth()/8);
        colthurs.setPrefWidth(tbltimetable.getPrefWidth()/8);
        coltue.setPrefWidth(tbltimetable.getPrefWidth()/8);
        colwed.setPrefWidth(tbltimetable.getPrefWidth()/8);
        colsat.setPrefWidth(tbltimetable.getPrefWidth()/8);
        colfri.setPrefWidth(tbltimetable.getPrefWidth()/8);
        coltime.setPrefWidth(tbltimetable.getPrefWidth()/8);

        tbltimetable.widthProperty().addListener((observable, oldValue, newValue) -> {
            collec.setPrefWidth((double)newValue/8);
            colmon.setPrefWidth((double)newValue/8);
            colthurs.setPrefWidth((double)newValue/8);
            coltue.setPrefWidth((double)newValue/8);
            colwed.setPrefWidth((double)newValue/8);
            colsat.setPrefWidth((double)newValue/8);
            colfri.setPrefWidth((double)newValue/8);
            coltime.setPrefWidth((double)newValue/8);
        });

        lecTime.add("10:45 - 11:30 AM");
        lecTime.add("11:30 - 12:20 PM");
        lecTime.add("12:20 - 01:10 PM");
        lecTime.add("01:10 - 02:00 PM");
        lecTime.add("02:00 - 02:49 PM");

        cbhalf.getItems().setAll("first_half","second_half");
        cbhalf2.getItems().setAll("first_half","second_half");

        initTimeTableCreate();
    }

    private void initTimeTableCreate() {

        cbcourse2.getItems().setAll("BscIT");
      //  cbclass2.getItems().setAll("Semester1","Semester2","Semester3","Semester4","Semester5","Semester6");
        cbclass2.getItems().setAll("FY","SY","TY");
        cblecdur.getItems().setAll("50");
        cbnolec.getItems().setAll("5");
        cbnoworkday.getItems().setAll("6");

        cbcourse2.setValue("BscIT");
        cblecdur.setValue("50");
        cbnolec.setValue("5");
        cbnoworkday.setValue("6");
//        tplecstart.setValue(new Calendar().setTime(new Date()););
    }

    @FXML
    void remove(ActionEvent event) {
        if (!(cbcourse.getSelectionModel().isEmpty() || cbclass.getSelectionModel().isEmpty() || cbhalf.getSelectionModel().isEmpty())) {


            String half = cbhalf.getValue();
            String classname = cbclass.getValue()+"_"+half;

            //timetable_"+cbcourse.getValue()+"_"+classname


            try {
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("Select count(*) from timetable_" + cbcourse.getValue() + "_" + classname);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    if (rs.getInt(1)==0)
                        new MyAlert().createAlert("Time Table doesn't Exist ","Error",event);
                    else
                    {
                        ps = DbHandler.getConnection().prepareStatement("delete from timetable_" + cbcourse.getValue() + "_" + classname);
                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"Timetable for "+cbcourse.getValue()+ " "+cbclass.getValue()+" "+cbhalf.getValue()+" is deleted");
                        ps.execute();


                        tbltimetable.getItems().clear();
                        cbclass.getSelectionModel().clearSelection();
                        cbcourse.getSelectionModel().clearSelection();
                        new MyAlert().createAlert("Time Table Removed ","Removed",event);
                    }
                }
                else
                    new MyAlert().createAlert("Some System(DB) Error","Removed",event);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
            new MyAlert().createAlert("Provide Specified Details (Inc. course , class ,..etc) ","Data not Specified",event);
    }

    private String getClassName(String sem){

        if (sem != null){
            if (sem.contains("1"))
                return "FY_first_half";
            else if (sem.contains("2"))
                return "FY_second_half";
            else if (sem.contains("3"))
                return "SY_first_half";
            else if (sem.contains("4"))
                return "SY_second_half";
            else if (sem.contains("5"))
                return "TY_first_half";
            else if (sem.contains("6"))
                return "TY_second_half";
        }
        return null;
    }


    private String getSubjectName(String course, String classname, String half, String tname) throws SQLException {

        String sem = getSemester(classname,half);

        PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT sname from subteacher where course = ? and class = ? and tname = ?");
        ps.setString(1,course);
        ps.setString(2,sem);
        ps.setString(3,tname);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getString("sname");
        else
            return null;
    }

     @FXML
    void generate(ActionEvent event) {


      if (!(cbcourse2.getSelectionModel().isEmpty() || cbclass2.getSelectionModel().isEmpty() || cbhalf2.getSelectionModel().isEmpty() || cblecdur.getSelectionModel().isEmpty() || cbnolec.getSelectionModel().isEmpty() || cbnoworkday.getSelectionModel().isEmpty() || tplecstart.getValue()== null))
      {

      //    System.out.println(tplecstart.getValue(););
          String course = cbcourse2.getValue();
          String half = cbhalf2.getValue();
          String classname = cbclass2.getValue()+"_"+half;
          int lecDur = Integer.parseInt(cblecdur.getValue());
          int lecNoPerWeek = Integer.parseInt(cbnolec.getValue()) ;
          LocalTime time = tplecstart.getValue();
          int workingDay = Integer.parseInt(cbnoworkday.getValue());


          int noSubj = 5;
          try {
              DbHandler.createTblTimeTable(course,classname);
          } catch (SQLException e) {

              new MyAlert().createAlert("Some Error Occured while Accesing Server ","Error",event);
              e.printStackTrace();
          }


          System.out.println(tplecstart.getValue());
          try{
              PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT count(*) from timetable_"+course+"_"+classname);
              ResultSet rs = ps.executeQuery();
              if (rs.next()){
                  if (rs.getInt(1) == 0){

                      ps = DbHandler.getConnection().prepareStatement("SELECT count(*) from subject where course = ? and class = ?");
                      ps.setString(1,course);
                      ps.setString(2,getSemester(cbclass2.getValue(),half));
                      rs = ps.executeQuery();
                      if (rs.next())
                          noSubj = rs.getInt(1);
                      else
                          noSubj = 5;

                      int lecPerDay = lecNoPerWeek * noSubj / workingDay;
                      lecturelist = new TimeTable(course,classname,lecDur,lecNoPerWeek,time,workingDay,noSubj).createTimeTable();

                      for (int i = 0; i < lecPerDay+1; i++) {


                          ps = DbHandler.getConnection().prepareStatement(" insert into timetable_"+course+"_"+classname+" values(?,?,?,?,?,?,?,?)");
                          ps.setString(1,lecTime.get(i));
                          ps.setString(2, String.valueOf(i+1));


                          if (lecturelist.get(0).lec[i] != null)
                                ps.setString(3,lecturelist.get(0).lec[i]+"("+getSubjectName(course,cbclass2.getValue(),half,lecturelist.get(0).lec[i])+")");
                          else
                                ps.setString(3," ");


                          if (lecturelist.get(1).lec[i] != null)
                                ps.setString(4,lecturelist.get(1).lec[i]+"("+getSubjectName(course,cbclass2.getValue(),half,lecturelist.get(1).lec[i])+")");
                          else
                                ps.setString(4," ");

                          if (lecturelist.get(2).lec[i] != null)
                                ps.setString(5,lecturelist.get(2).lec[i]+"("+getSubjectName(course,cbclass2.getValue(),half,lecturelist.get(2).lec[i])+")");
                          else
                                ps.setString(5," ");

                          if (lecturelist.get(3).lec[i] != null)
                                ps.setString(6,lecturelist.get(3).lec[i]+"("+getSubjectName(course,cbclass2.getValue(),half,lecturelist.get(3).lec[i])+")");
                          else
                                ps.setString(6," ");

                          if (lecturelist.get(4).lec[i] != null)
                                ps.setString(7,lecturelist.get(4).lec[i]+"("+getSubjectName(course,cbclass2.getValue(),half,lecturelist.get(4).lec[i])+")");
                          else
                                ps.setString(7," ");

                          if (lecturelist.get(5).lec[i] != null)
                                ps.setString(8,lecturelist.get(5).lec[i]+"("+getSubjectName(course,cbclass2.getValue(),half,lecturelist.get(5).lec[i])+")");
                          else
                                ps.setString(8," ");

                          ps.execute();

                      }

                      ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                      ps.setString(1,username);
                      ps.setString(2,"Timetable for "+cbcourse.getValue()+ " "+cbclass.getValue()+" "+cbhalf.getValue()+" is generated");
                      ps.execute();

                      File dir = new File("C:/ProgramData/Mlt/TimeTable");

                      //method 'mkdir' to create directroy and result
                      //is getting stored in 'isDirectoryCreated'
                      //which will be either 'true' or 'false'

                      dir.mkdir();

                      //displaying results

                      Date d = Calendar.getInstance().getTime();
                      String s = d.getDate()+ "_"+ d.getMonth()+ "_"+ (d.getYear()+1900);



                        // For Storing in .csv file
//                      ps = DbHandler.getConnection().prepareStatement("SELECT * from timetable_"+course+"_"+classname+" into outfile 'C:/ProgramData/mlt/TimeTable/"+course+"_"+classname+"_timetable_"+s+".csv' FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'");
//                      ps.execute();

                      new MyAlert().createAlert("Time Table Created ","Data not Specified",event);

                  }
                  else
                      new MyAlert().createAlert("Time Table Already Generated ","Error",event);
              }
              else
                  new MyAlert().createAlert("Some Error occured","Error",event);
          } catch (SQLException e) {
              e.printStackTrace();
              new MyAlert().createAlert("Some Error occured","Error",event);
          } catch (Noteacher noteacher) {
              noteacher.printStackTrace();
              new MyAlert().createAlert("Teacher & Subject Data Not Available","Data not Specified",event);
          }
      }
      else
          new MyAlert().createAlert("Provide Specified Details (Inc. course , class ,..etc) ","Data not Specified",event);
    }

    @FXML
    void view(ActionEvent event) {


//        if (lecturelist != null){
//            ObservableList<TimeTableStructure> fillLectureList = FXCollections.observableArrayList(); ObservableList<TimeTableStructure> fillLectureList = FXCollections.observableArrayList();
//
//
//            for (int i = 0; i < 5; i++) {
//                fillLectureList.add(new TimeTableStructure(lecTime.get(i),i+1,lecturelist.get(0).lec[i],lecturelist.get(1).lec[i],lecturelist.get(2).lec[i],lecturelist.get(3).lec[i],lecturelist.get(4).lec[i],lecturelist.get(5).lec[i]));
//            }
//
//            tbltimetable.getItems().setAll(fillLectureList);
//        }
//        else
//            System.out.println("Generate First");

        if (!(cbcourse.getSelectionModel().isEmpty() || cbclass.getSelectionModel().isEmpty() || cbhalf.getSelectionModel().isEmpty())){


            String half = cbhalf.getValue();
            String classname = cbclass.getValue()+"_"+half;

            //timetable_"+cbcourse.getValue()+"_"+classname


            try{
                PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT count(*) from timetable_"+cbcourse.getValue()+"_"+classname);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    if (rs.getInt(1) != 0){
                        tbltimetable.getItems().clear();
                        fillLectureList.clear();
                        ps = DbHandler.getConnection().prepareStatement("Select * from timetable_"+cbcourse.getValue()+"_"+classname);
                        rs = ps.executeQuery();

                        list.clear();




                        while (rs.next()){

                            fillLectureList.add(new TimeTableStructure(rs.getString("time"), Integer.parseInt(rs.getString("lecture")),rs.getString("monday"),rs.getString("tuesday"),rs.getString("wednesday"),rs.getString("thursday"),rs.getString("friday"),rs.getString("saturday")));


                            parameters = new HashMap<String, Object>();

                            parameters.put("course", cbcourse.getValue());
                            parameters.put("class", classname);
                            parameters.put("time", rs.getString("time"));
                            parameters.put("lecture", rs.getString("lecture"));


                            String tname = null;
                            if (rs.getString("monday") != null)
                            {
                                tname =  rs.getString("monday");
                                parameters.put("monday", tname);
                            }
                            else
                                parameters.put("monday", " ");

                            if (rs.getString("tuesday") != null)
                            {

                                tname =  rs.getString("tuesday");
                                parameters.put("tuesday", tname);

                            }
                            else
                                parameters.put("tuesday", " ");

                            if (rs.getString("wednesday") != null)
                            {

                                tname =  rs.getString("wednesday");
                                parameters.put("wednesday", tname);

                            }
                            else
                                parameters.put("wednesday", " ");

                            if (rs.getString("thursday") != null)
                            {

                                tname =  rs.getString("thursday");
                                parameters.put("thursday", tname);

                            }
                            else
                                parameters.put("thursday", " ");

                            if (rs.getString("friday") != null)
                            {

                                tname =  rs.getString("friday");
                                parameters.put("friday", tname);

                            }
                            else
                                parameters.put("friday", " ");

                            if (rs.getString("saturday") != null)
                            {

                                tname =  rs.getString("saturday");
                                parameters.put("saturday", tname);

                            }
                            else
                                parameters.put("saturday", " ");

                            list.add(parameters);

                        }
                        tbltimetable.getItems().setAll(fillLectureList);
                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"Timetable for "+cbcourse.getValue()+ " "+cbclass.getValue()+" "+cbhalf.getValue()+" is viewed");
                        ps.execute();
                    }
                    else
                        new MyAlert().createAlert("Kindly Create First Time Table ","Data not Specified",event);
                }
                else
                    new MyAlert().createAlert("Some Error occured ","Data not Specified",event);

            } catch (SQLException e) {
                e.printStackTrace();
                new MyAlert().createAlert("Some Error occured","Db Error",event);
            }
        }
        else
            new MyAlert().createAlert("Provide Specified Details (Inc. course , class ,..etc) ","Data not Specified",event);
    }

    private String getSemester(String classname , String half){
        if (classname.equals("FY")){
            if (half.equals("first_half"))
                return "Semester1";
            else
                return "Semester2";
        }
        else if (classname.equals("SY")){
            if (half.equals("first_half"))
                return "Semester3";
            else
                return "Semester4";
        }
        else {
            if (half.equals("first_half"))
                return "Semester5";
            else
                return "Semester6";
        }
    }



    public void setUser(String username) {
        this.username = username;
    }

    @FXML
    void loadClass(ActionEvent event) {
        //  new SubjectController(). load(cbcourse,cbclass,null);;
    }

    @FXML
    void print(ActionEvent event) {

            if (!(cbcourse.getSelectionModel().isEmpty() || cbclass.getSelectionModel().isEmpty()))
            {
                try{

                    dir = new File("C:/ProgramData/Mlt");
                    dir.mkdir();

                    dir = new File("C:/ProgramData/Mlt/TimeTable");
                    dir.mkdir();

                    d = Calendar.getInstance().getTime();
                    s = d.getDate()+ "_"+ (d.getMonth()+1)+ "_"+ (d.getYear()+1900);
                    dir = new File("C:/ProgramData/Mlt/TimeTable/"+ s );
                    dir.mkdir();

               //     JasperReport jasperReport = JasperCompileManager.compileReport("jrxml/tt1.1.jrxml");
                    JasperReport jasperReport = JasperCompileManager.compileReport("jrxml/tt1.1.jrxml");
                    JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
                    JasperPrint print = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
                    JasperViewer.viewReport(print,false);
                    String date = String.valueOf(d.getHours()+d.getMinutes()+d.getSeconds());
                    JasperExportManager.exportReportToPdfFile(print, "C:/ProgramData/mlt/TimeTable/"+s + "/"+cbcourse.getValue()+"_"+cbclass.getValue()+"_mlt_"+date+".pdf");

                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                    ps.setString(1,username);
                    ps.setString(2,"Timetable for "+cbcourse.getValue()+ " "+cbclass.getValue()+" "+cbhalf.getValue()+" is printed");
                    ps.execute();
                }
                catch (Exception e){

                    e.printStackTrace();
                    new MyAlert().createAlert("Please ... first generate then print ", "Unspecified Data", event);
                }
            }
            else
            {
                new MyAlert().createAlert("Specify Course and Class ", "Unspecified Data", event);
            }
    }

    public class TimeTableStructure
    {
        private String time;
        private int lecture;
        private String dayMonday,dayTuesday,dayThursday,dayWednesday,dayFriday,daySaturday;

        public TimeTableStructure(String time, int lecture, String dayMonday, String dayTuesday, String dayWednesday, String dayThursday, String dayFriday, String daySaturday) {
            this.time = time;
            this.lecture = lecture;
            this.dayMonday = dayMonday;
            this.dayTuesday = dayTuesday;
            this.dayThursday = dayThursday;
            this.dayWednesday = dayWednesday;
            this.dayFriday = dayFriday;
            this.daySaturday = daySaturday;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getLecture() {
            return lecture;
        }

        public void setLecture(int lecture) {
            this.lecture = lecture;
        }

        public String getDayMonday() {
            return dayMonday;
        }

        public void setDayMonday(String dayMonday) {
            this.dayMonday = dayMonday;
        }

        public String getDayTuesday() {
            return dayTuesday;
        }

        public void setDayTuesday(String dayTuesday) {
            this.dayTuesday = dayTuesday;
        }

        public String getDayThursday() {
            return dayThursday;
        }

        public void setDayThursday(String dayThursday) {
            this.dayThursday = dayThursday;
        }

        public String getDayWednesday() {
            return dayWednesday;
        }

        public void setDayWednesday(String dayWednesday) {
            this.dayWednesday = dayWednesday;
        }

        public String getDayFriday() {
            return dayFriday;
        }

        public void setDayFriday(String dayFriday) {
            this.dayFriday = dayFriday;
        }

        public String getDaySaturday() {
            return daySaturday;
        }

        public void setDaySaturday(String daySaturday) {
            this.daySaturday = daySaturday;
        }
    }
}




