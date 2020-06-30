package mlt.ui.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import mlt.db.DbHandler;
import mlt.ui.MyAlert.MyAlert;
import mlt.ui.support.CloseStage;
import mlt.ui.support.CreateStage;
import mlt.ui.support.GetStage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminpageController implements Initializable {

//
//
//    @FXML
//    private Label lblusername;
//
//    @FXML
//    private Label lbluserdata;
//
//    @FXML
//    void ManageCourse(ActionEvent event) {
//        new CreateStage().createStage("Manage Courses","/mlt/ui/course/course.fxml",null,0,true,false,false);
//    }
//
//    @FXML
//    void logout(ActionEvent event) {
//
//        new CreateStage().createStage("Login Page","/mlt/ui/login/Login.fxml",null,0,true,false,false);
//        CloseStage.closeStage(event);
//    }
//
//    @FXML
//    void manageSubject(ActionEvent event) {
//        new CreateStage().createStage("Manage Subject","/mlt/ui/subject/subject.fxml",null,0,true,false,false);
//    }
//
//    @FXML
//    void manageTeacher(ActionEvent event) {
//        new CreateStage().createStage("Manage Teacher","/mlt/ui/teacher/teacher.fxml",null,0,true,false,false);
//          }
//
//    @FXML
//    void meritList(ActionEvent event) {
//        new CreateStage().createStage("Generate MeritList","/mlt/ui/meritlist/meritlist.fxml",null,0,true,false,false);
//
//    }
//
//    @FXML
//    void settings(ActionEvent event) {
//
//    }
//
//    @FXML
//    void timeTable(ActionEvent event) {
//        new CreateStage().createStage("Generate Time Table","/mlt/ui/timetable/timetable.fxml",null,0,true,false,false);
//
//    }


    @FXML
    private BorderPane mainborderpane;

    @FXML
    Label lblmlt;

    @FXML
    private TableView<History> tblhistory;

    @FXML
    private TableColumn<History, String > colexecutor;

    @FXML
    private TableColumn<History, String > colopt;

    @FXML
    private TableColumn<History, String> coldate;


    ObservableList<History> hlist = FXCollections.observableArrayList();
    private String username;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        try {
            DbHandler.createOperationTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        colexecutor.setCellValueFactory(new PropertyValueFactory<>("executor"));
        colopt.setCellValueFactory(new PropertyValueFactory<>("operation"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date"));


        colexecutor.setPrefWidth(tblhistory.getPrefWidth()/3);
        coldate.setPrefWidth(tblhistory.getPrefWidth()/3);
        colopt.setPrefWidth(tblhistory.getPrefWidth()/3);


        tblhistory.widthProperty().addListener((observable, oldValue, newValue) -> {
            colopt.setPrefWidth((double)newValue/3);
            coldate.setPrefWidth((double)newValue/3);
            colexecutor.setPrefWidth((double)newValue/3);
        });



        loadOperationTable();

    }

    @FXML
    void clear(ActionEvent event)  {

//        PrinterJob job = PrinterJob.createPrinterJob();
//
//        boolean proceed = job.showPageSetupDialog(GetStage.getStage(event));
//
//        if (proceed)
//        {
//            if(job != null){
////                   job.showPrintDialog(GetStage.getStage(event)); // Window must be your main Stage
////
//                boolean printed = job.printPage(tblhistory);
//
//
//                if (printed)
//                {
//                    job.endJob();
//                }
//            }
//        }
//

        try {
          PreparedStatement ps = DbHandler.getConnection().prepareStatement("DELETE FROM operation");
          ps.execute();

          loadOperationTable();
      } catch (SQLException e) {
          e.printStackTrace();
          new MyAlert().createAlert("Error Please Check Connectivity and try ..","Info",event);
      }
    }

    public void loadOperationTable() {
        try{
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from operation");
            ResultSet rs = ps.executeQuery();
            hlist.clear();
            while (rs.next()){
                hlist.add(new History(rs.getString("executor"),rs.getString("operation"), rs.getTimestamp("optdate")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tblhistory.getItems().clear();
        tblhistory.getItems().setAll(hlist);
    }

    public void setUser(String username) {
        this.username = username;
    }


    public class History
    {
        private String executor,operation;
        private Date date;

        public History(String executor, String operation, Date date) {
            this.executor = executor;
            this.operation = operation;
            this.date = date;
        }

        public String getExecutor() {
            return executor;
        }

        public void setExecutor(String executor) {
            this.executor = executor;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
