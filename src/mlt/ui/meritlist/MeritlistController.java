package mlt.ui.meritlist;

import animatefx.animation.SlideInUp;
import animatefx.animation.SlideOutDown;
import animatefx.animation.SlideOutUp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.protocol.Resultset;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import mlt.db.DbHandler;
import mlt.ui.MyAlert.MyAlert;
import mlt.ui.setting.SettingController;
import mlt.ui.support.GetStage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MeritlistController implements Initializable {

    @FXML
    private JFXComboBox<String> cbcourse;

    @FXML
    private JFXComboBox<String> cbclass;

    @FXML
    private JFXComboBox<String> cbcaste;

    @FXML
    private JFXComboBox<Integer> cbseats;

    @FXML
    private JFXComboBox<Integer> cbminmarks;

    @FXML
    private TableView<MeritList> tblmeritlist;

    @FXML
    private TableColumn<MeritList, String> colid;

    @FXML
    private TableColumn<MeritList, String> colname;

    @FXML
    private TableColumn<MeritList, String> colmark;

    @FXML
    private TableColumn<MeritList, String> colcaste;

    @FXML
    private TableColumn<MeritList, String> colextra;

    @FXML
    private TableColumn<MeritList, String> colrank;

    @FXML
    private CheckBox rbminboudary;

    @FXML
    private JFXButton btnprint;

    @FXML
    private JFXTabPane tabmerit;

    private String query ;

    JasperDesign jdesign = null;
    JRDesignQuery updateQuery = null;
    JasperReport jasperReport = null;
    JasperPrint jasperPrint = null;

    static String caste[] = {"SC", "ST", "OPEN", "NT", "SBC", "OBC", "SEBC", "EBC"};

    ObservableList<String> cname = FXCollections.observableArrayList();
    ObservableList<String> classlist = FXCollections.observableArrayList();
    ObservableList<String> courselist = FXCollections.observableArrayList();
    ObservableList<String> castelist = FXCollections.observableArrayList();
    ObservableList<Integer> seatlist = FXCollections.observableArrayList();
    ObservableList<Integer> mlist = FXCollections.observableArrayList();
    private String username;

    HashMap<String, Object> parameters ;
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

    Date d;
    String s;
    File dir;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cbseats.setEditable(false);
        cbseats.setDisable(true);
        //cbseats.setVisible(false);
        // classlist.addAll("FY","SY","TY");
        classlist.addAll("FY");
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colmark.setCellValueFactory(new PropertyValueFactory<>("mark"));
        // colcourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colcaste.setCellValueFactory(new PropertyValueFactory<>("caste"));
        colextra.setCellValueFactory(new PropertyValueFactory<>("extra"));

        //   colclass.setCellValueFactory(new PropertyValueFactory<>("classname"));
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colrank.setCellValueFactory(new PropertyValueFactory<>("rank"));

        cbseats.setEditable(false);

        castelist.clear();
        for (String str : caste)
            castelist.add(str);
        cbcaste.getItems().setAll(castelist);

        seatlist.clear();
        for (int i = 1; i <= 50; i++) {
            seatlist.add(i);
        }
        cbseats.getItems().setAll(seatlist);

        mlist.clear();
        for (int i = 40; i <= 100; i++) {
            mlist.add(i);
        }
        cbminmarks.getItems().setAll(mlist);
        cbclass.getItems().setAll("FY");

        colid.setPrefWidth(tblmeritlist.getPrefWidth() / 9);
        colmark.setPrefWidth(tblmeritlist.getPrefWidth() / 9);
        colname.setPrefWidth(2 * tblmeritlist.getPrefWidth() / 9);
        // colclass.setPrefWidth(tblmeritlist.getPrefWidth()/9);
        colcaste.setPrefWidth(tblmeritlist.getPrefWidth() / 9);
        //  colcourse.setPrefWidth(tblmeritlist.getPrefWidth()/9);
        colextra.setPrefWidth(3 * tblmeritlist.getPrefWidth() / 9);
        colrank.setPrefWidth(tblmeritlist.getPrefWidth() / 9);

        tblmeritlist.widthProperty().addListener((observable, oldValue, newValue) -> {
            colid.setPrefWidth((double) newValue / 9);
            colmark.setPrefWidth((double) newValue / 9);
            colname.setPrefWidth(2 * (double) newValue / 9);
            //   colclass.setPrefWidth((double)newValue/9);
            //   colcourse.setPrefWidth((double)newValue/9);
            colcaste.setPrefWidth((double) newValue / 9);
            colextra.setPrefWidth(3 * (double) newValue / 9);
            colrank.setPrefWidth((double) newValue / 9);
        });
        loadCourse();
        rbminboudary.setSelected(true);
        try {
            DbHandler.createMeritListTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        try {
//            jdesign = JRXmlLoader.load("src/mlt/jrxml/testmlt.jrxml");
//        } catch (JRException e) {
//            e.printStackTrace();
//        }

        updateQuery = new JRDesignQuery();



        // 2nd Tab


        colcategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colperseat.setCellValueFactory(new PropertyValueFactory<>("seat"));

        loadPercentageSeatTable();

        cbcat.getItems().setAll(castelist);
        tblseats.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                colcategory.setPrefWidth(newValue.intValue()/2);
                colperseat.setPrefWidth(newValue.intValue()/2);
            }
        });
    }

    public void loadCourse() {
        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("Select cname from course");
            ResultSet rs = ps.executeQuery();
            cname.clear();
            while (rs.next()) {
                cname.add(rs.getString("cname"));
            }
            cbcourse.getItems().clear();
            cbcourse.getItems().setAll(cname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadClass(ActionEvent event) {
//        try {
//            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select cduration from course where cname = ?");
//            ps.setString(1, cbcourse.getValue());
//            ResultSet rs = ps.executeQuery();
//            rs.next();
//            int duration = rs.getInt(1);
//
//            cbclass.getItems().setAll(classlist);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @FXML
    void calSeats(ActionEvent event) {
        String category = cbcaste.getValue();
        int totalSeats = 0, minmarks = 0;
        try {

            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select totalseat,minmarks from  mltdata where course = ? ");
            ps.setString(1, cbcourse.getValue());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSeats = rs.getInt("totalseat");
                minmarks = rs.getInt("minmarks");

                ps = DbHandler.getConnection().prepareStatement("SELECT seats from perseats where category = ?");

                if (category.contains("NT"))
                    category = "VJNT A";
                ps.setString(1, category);
                rs = ps.executeQuery();
                if (rs.next()) {
                    Float seatPer = rs.getFloat("seats");

                    int seat = (int) (totalSeats * seatPer / 100);
                    cbseats.getItems().add(seat);
                    cbseats.setValue(seat);
                    cbminmarks.getItems().add(minmarks);
                    cbminmarks.setValue(minmarks);
//                switch (category){
//                    case "SC":
//                        cbseats.setValue((int)(13*seatPer));
//                        break;
//                    case "ST":
//                        cbseats.setValue((int)(7*seatPer));
//                        break;
//                    case "OPEN":
//                        seatPer = 20.0F;
//                        cbseats.setValue((int)(20*seatPer));
//                        break;
//                    case "NT":
//                        cbseats.setValue((int)((2.75)*seatPer));
//                        break;
//                    case "SBC":
//                        cbseats.setValue((int)(2*seatPer));
//                        break;
//                    case "OBC":
//                        cbseats.setValue((int)(19*seatPer));
//                        break;
//                    case "SEBC":
//                        cbseats.setValue((int)(16*seatPer));
//                        break;
//                    case "EBC":
//                        cbseats.setValue((int)(10*seatPer));
//                        break;
//                }
                } else {

                }
            } else {
                new MyAlert().createAlert("Please specify All details about merit list in setting for particular course ", "Unspecified Data", event);
                cbminmarks.getSelectionModel().clearSelection();
                cbseats.getSelectionModel().clearSelection();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    int getClassIndex(String classname) {
        int index;
        String classes[] = {"fy", "sy", "ty"};
        for (int i = 0; i < classes.length; i++) {
            if (classes[i].equals(classname))
                return i + 1;
        }

        return -1;
    }

    @FXML
    void generate(ActionEvent event) throws JRException {


        if (!(cbclass.getSelectionModel().isEmpty() || cbcourse.getSelectionModel().isEmpty() || cbcaste.getSelectionModel().isEmpty() || cbclass.getSelectionModel().isEmpty() || cbseats.getSelectionModel().isEmpty())) {

            calSeats(event);
            tblmeritlist.getItems().clear();
            ObservableList meritlistdata = FXCollections.observableArrayList();

            dir = new File("C:/ProgramData/Mlt");
            dir.mkdir();

            dir = new File("C:/ProgramData/Mlt/MeritList");
            dir.mkdir();

            d = Calendar.getInstance().getTime();
            s = d.getDate()+ "_"+ (d.getMonth()+1)+ "_"+ (d.getYear()+1900);
            dir = new File("C:/ProgramData/Mlt/MeritList/"+ s );
            dir.mkdir();

            if (cbminmarks.getSelectionModel().isEmpty()) {
                int rank = 1;
                try {

                    query = "select * from " + cbclass.getValue() + "_" + cbcourse.getValue() + "_student where caste =?  order by marks desc limit " + cbseats.getValue();
                    PreparedStatement ps = DbHandler.getConnection().prepareStatement(query);
                    ps.setString(1, cbcaste.getValue());
                    ResultSet rs = ps.executeQuery();
                    meritlistdata.clear();
                    list.clear();
                    while (rs.next()) {
                        meritlistdata.add(new MeritList(rs.getString("name"), rs.getString("caste"), rs.getString("remark"), rs.getInt("marks"), rs.getString("id"), rank));

                        parameters = new HashMap<String, Object>();
                        parameters.put("course", cbcourse.getValue());
                        parameters.put("class", cbclass.getValue());
                        parameters.put("caste", cbcaste.getValue());
                        parameters.put("id", rs.getString("id"));
                        parameters.put("name", rs.getString("name"));
                        parameters.put("marks", rs.getString("marks"));
                        parameters.put("remark", rs.getString("remark"));

                        list.add(parameters);
                        rank++;
                    }

                    ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,current_date())");
                    ps.setString(1, username);
                    ps.setString(2, "Meritlist  for " + cbcourse.getValue() + " " + cbclass.getValue() + " for caste " + cbcaste.getValue() + " is generated");
                    ps.execute();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                tblmeritlist.getItems().clear();
                tblmeritlist.getItems().setAll(meritlistdata);



            } else {
                int rank = 1;
                try {

                    list.clear();
                    // loading data from Db and filtering according to constraint
                    query = "select * from  " + cbclass.getValue() + "_" + cbcourse.getValue() + "_student where  caste =? and marks > ? order by marks desc limit " + cbseats.getValue();
                    PreparedStatement ps = DbHandler.getConnection().prepareStatement(query);
                    ps.setString(1, cbcaste.getValue());
                    ps.setInt(2, cbminmarks.getValue());
                    ResultSet rs = ps.executeQuery();
                    meritlistdata.clear();

                    while (rs.next()) {

                        // loading data in meritlistdata array to pass to table to load
                        meritlistdata.add(new MeritList(rs.getString("name"), rs.getString("caste"), rs.getString("remark"), rs.getInt("marks"), rs.getString("id"), rank));

                        // mapping data to array of hashmap to pass data to JRData to prepare report
                        parameters = new HashMap<String, Object>();
                        parameters.put("course", cbcourse.getValue());
                        parameters.put("class", cbclass.getValue());
                        parameters.put("caste", cbcaste.getValue());
                        parameters.put("id", rs.getString("id"));
                        parameters.put("name", rs.getString("name"));
                        parameters.put("marks", rs.getString("marks"));
                        parameters.put("remark", rs.getString("remark"));

                        list.add(parameters);
                        rank++;
                    }

                    // storing record for admin as history log to maintain
                    ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,current_date())");
                    ps.setString(1, username);
                    ps.setString(2, "Meritlist  for " + cbcourse.getValue() + " " + cbclass.getValue() + " for caste " + cbcaste.getValue() + " is generated");
                    ps.execute();


                } catch (SQLException e) {

                    new MyAlert().createAlert("Student Data Doesn't Exists", "Unspecified Data", event);
                    e.printStackTrace();
                }

                // attaching data to table
                tblmeritlist.getItems().clear();
                tblmeritlist.getItems().setAll(meritlistdata);
            }
             btnprint.setDisable(false);


        } else
            new MyAlert().createAlert("Please specify Course, Class, Caste ", "Unspecified Data", event);
    }

    @FXML
    void checkMinMarksCriteria(ActionEvent event) {
        if (rbminboudary.isSelected()) {
            cbminmarks.setDisable(false);
        } else {
            cbminmarks.setDisable(true);
            cbminmarks.getSelectionModel().clearSelection();
        }
    }

    public void setUser(String username) {
        this.username = username;
    }

    public static class MeritList {
        public String name, caste, classname, course, extra, id;
        public int mark, rank;

        public MeritList(String name, String caste, String extra, int mark, String id, int rank) {
            this.name = name;
            this.caste = caste;
            this.extra = extra;
            this.mark = mark;
            this.id = id;
            this.rank = rank;
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

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public int getMark() {
            return mark;
        }

        public void setMark(int mark) {
            this.mark = mark;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    @FXML
    void printMeritList(ActionEvent event) throws SQLException, JRException {
        if (!(cbclass.getSelectionModel().isEmpty() || cbcourse.getSelectionModel().isEmpty() || cbcaste.getSelectionModel().isEmpty())) {
//            System.out.println("working");
//            updateQuery.setText(query);
//            jdesign.setQuery(updateQuery);
//            jasperReport = JasperCompileManager.compileReport(jdesign);
//            jasperPrint = JasperFillManager.fillReport(jasperReport,null,DbHandler.getConnection());
//            JasperViewer.viewReport(jasperPrint,false);
//
//            PreparedStatement ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,current_date())");
//            ps.setString(1, username);
//            ps.setString(2, "Meritlist  for " + cbcourse.getValue() + " " + cbclass.getValue() + " for caste " + cbcaste.getValue() + " is Prepared");
//            ps.execute();



//            jasperReport = JasperCompileManager.compileReport("src/mlt/jrxml/mlt1.5.jrxml");
//            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
//            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
//            JasperViewer.viewReport(print,false);
//            String path=System.getProperty("user.dir");
//            JasperExportManager.exportReportToPdfFile(print, "C:/ProgramData/mlt/Meritlist/"+s + "/"+cbcourse.getValue()+"_"+cbclass.getValue()+"_mlt_"+cbcaste.getValue()+".pdf");
//            JRViewer viewer = new JRViewer(print);
//            viewer.setOpaque(true);
//            viewer.setVisible(true);



           // 'C:/ProgramData/mlt/Meritlist/"+s+"/"+cbcourse.getValue()+"_"+cbclass.getValue()+"_mlt_"+cbcaste.getValue()+".csv'



//            jasperPrint = JasperFillManager.fillReport(jasperReport,null,DbHandler.getConnection());
//            JasperViewer.viewReport(jasperPrint,false);
//
//            PreparedStatement ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,current_date())");
//            ps.setString(1, username);
//            ps.setString(2, "Meritlist  for " + cbcourse.getValue() + " " + cbclass.getValue() + " for caste " + cbcaste.getValue() + " is Prepared");
//            ps.execute();



                try{


                    PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT reqsub from mltdata where course = ?");
                    ps.setString(1,cbcourse.getValue());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        if (rs.getString("reqsub").equals("Maths"))
                            jasperReport = JasperCompileManager.compileReport("jrxml/mlt1.1.jrxml");
                        else
                            jasperReport = JasperCompileManager.compileReport("jrxml/mlt1.1_2.jrxml");
                    }
                    else
                        jasperReport = JasperCompileManager.compileReport("jrxml/mlt1.1.jrxml");


//                    if (rs.next()){
//                        if (rs.getString("reqsub").equals("Maths"))
//                            jasperReport = JasperCompileManager.compileReport("jrxml/mlt1.1.jrxml");
//                        else
//                            jasperReport = JasperCompileManager.compileReport("jrxml/mlt1.1_2.jrxml");
//                    }
//                    else
//                        jasperReport = JasperCompileManager.compileReport("jrxml/mlt1.1.jrxml");


                    JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
                    JasperPrint print = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
                    JasperViewer.viewReport(print,false);
                    String path=System.getProperty("user.dir");
                    String date = String.valueOf(d.getHours()+d.getMinutes()+d.getSeconds());
                    JasperExportManager.exportReportToPdfFile(print, "C:/ProgramData/mlt/Meritlist/"+s + "/"+cbcourse.getValue()+"_"+cbclass.getValue()+"_mlt_"+cbcaste.getValue()+"_"+date+".pdf");
                }
                catch (Exception e){
                    e.printStackTrace();
                    new MyAlert().createAlert("Please ... first generate then print ", "Unspecified Data", event);
                }
                btnprint.setDisable(true);

        } else
            new MyAlert().createAlert("Please Select again Course, Class, Caste and No of seats", "Unspecified Data", event);
    }



//
//



//    Second Tab Category

    @FXML
    private TableView<Seats> tblseats;

    @FXML
    private TableColumn<Seats, String> colcategory;

    @FXML
    private TableColumn<Seats, String> colperseat;

    @FXML
    private JFXComboBox<String> cbcat;

    @FXML
    private TextField txtnewcat;

    @FXML
    private JFXTextField txtpseats;

    @FXML
    private JFXButton btnaddseats;

    @FXML
    private JFXButton btnremoveseats;

    @FXML
    private JFXButton btnupdateseats;

    @FXML
    private AnchorPane paneaddcat;

    @FXML
    void EditSeats(ActionEvent event) {
        try {
            if (!(txtpseats.getText().equals("")  || cbcat.getSelectionModel().isEmpty()))
            {
                JFXButton yesbutton = new JFXButton("Yes");
                JFXButton nobutton = new JFXButton("No");
                List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

                yesbutton.setOnAction(event3 ->{
                    try {
                        PreparedStatement ps = DbHandler.getConnection().prepareStatement("update perseats set seats = ?  where category = ?");
                        ps.setString(1,txtpseats.getText());
                        ps.setString(2,cbcat.getValue());
                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"Category : "+ cbcat.getValue()+" Data updated");
                        ps.execute();


                        loadPercentageSeatTable();
                        new MyAlert().createAlert("Category: "+ cbcat.getValue()+" Data Updated ","update",event);
                        clear(event);

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
                new MyAlert().confirm("Do you want to update the Data ?","Update ?",event,listcontrol);

            }
        } catch (Exception e) {
            e.printStackTrace();
            new MyAlert().createAlert("Some Error Occured ","Error",event);

        }

    }

    @FXML
    void addCategory(ActionEvent event) {

        tabmerit.setDisable(true);
        tabmerit.setEffect(new BoxBlur(2,2,1));
        new SlideOutDown(paneaddcat).play();

    }

    @FXML
    void cancelAddCategory(ActionEvent event) {
        tabmerit.setEffect(null);
        tabmerit.setDisable(false);
        new SlideInUp(paneaddcat).play();
    }

     @FXML
    void addItems(ActionEvent event) {
        if (!txtnewcat.getText().equals("")){
            if (!cbcat.getItems().contains(txtnewcat.getText())){
                cbcat.getItems().addAll(txtnewcat.getText());
                cancelAddCategory(event);
            }
            else
                new MyAlert().createAlert("Category Already Added","Error",event);
        }else
            new MyAlert().createAlert("Enter Category ","Error",event);
    }

    @FXML
    void addSeats(ActionEvent event) {
        if(!(cbcat.getSelectionModel().isEmpty() || txtpseats.getText().equals(""))){

           try{
               PreparedStatement ps = DbHandler.getConnection().prepareStatement("INSERT into perseats values(?,?)");
               Float.parseFloat(txtpseats.getText());
               ps.setString(1,cbcat.getValue());
               ps.setString(2,txtpseats.getText());
               ps.execute();
               loadPercentageSeatTable();
               new MyAlert().createAlert("Data inserted", "Unspecified Data", event);

               ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
               ps.setString(1,username);
               ps.setString(2,cbcat.getValue()+ " added with percentage "+ txtpseats.getText());
               ps.execute();


               clear(event);
           } catch (SQLException e) {
               e.printStackTrace();
               new MyAlert().createAlert("Some error Occured  ", "Db Error", event);
           }catch (NumberFormatException e){
               e.printStackTrace();
               new MyAlert().createAlert("Please Provide percentage in proper format ", "Unspecified Data", event);
           }
        }
        else
            new MyAlert().createAlert("Please Select category & percentage seats ", "Unspecified Data", event);
    }



    @FXML
    void loadSeatsSelectedRow(MouseEvent event) {
        if (!tblseats.getSelectionModel().isEmpty()){
            Seats seats = tblseats.getSelectionModel().getSelectedItem();
            txtpseats.setText(seats.getSeat().toString());
            cbcat.setValue(seats.getCategory());


            cbcat.setDisable(true);
            btnaddseats.setDisable(true);
            btnupdateseats.setDisable(false);
            btnremoveseats.setDisable(false);
        }
    }

    @FXML
    void reloadSeatData(ActionEvent event) {

        loadPercentageSeatTable();
    }

    @FXML
    void clear(ActionEvent event) {
        cbcat.getSelectionModel().clearSelection();
        txtpseats.setText("");
        btnupdateseats.setDisable(true);
        btnremoveseats.setDisable(true);
        btnaddseats.setDisable(false);
        cbcat.setDisable(false);
    }



//
//    @FXML
//    void editItems(ActionEvent event) {
//        cbcat.getSelectionModel().clearSelection();
//        cbcat.setEditable(true);
//    }



    @FXML
    void removeSeats(ActionEvent event) {
        try {
            if (!cbcat.getSelectionModel().isEmpty())
            {
                JFXButton yesbutton = new JFXButton("Yes");
                JFXButton nobutton = new JFXButton("No");
                List<JFXButton> listcontrol = Arrays.asList(yesbutton,nobutton);

                yesbutton.setOnAction(event3 ->{
                    try {
                        PreparedStatement ps = DbHandler.getConnection().prepareStatement("delete from  perseats  where category = ?");
                        ps.setString(1,cbcat.getValue());

                        ps.execute();

                        ps = DbHandler.getConnection().prepareStatement("INSERT INTO operation(executor,operation,optdate) values(?,?,now())");
                        ps.setString(1,username);
                        ps.setString(2,"Category "+cbcat.getValue()+" Removed");
                        ps.execute();
                        loadPercentageSeatTable();
                        new MyAlert().createAlert("Category : "+ cbcat.getValue() +" Data Deleted ","Deleted",event);
                        clear(event);

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
                new MyAlert().confirm("Do you want to Delete the Category ?","Delete ?",event,listcontrol);


            }
            else
                new MyAlert().createAlert("Please specify Category","Delete",event);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ObservableList<Seats> lseat = FXCollections.observableArrayList();

    private void loadPercentageSeatTable() {
        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select * from perseats");
            ResultSet rs = ps.executeQuery();

            lseat.clear();
            while (rs.next()){
                lseat.add(new Seats(rs.getString("category"),rs.getFloat("seats")));
            }
            tblseats.getItems().clear();
            tblseats.getItems().setAll(lseat);

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
}