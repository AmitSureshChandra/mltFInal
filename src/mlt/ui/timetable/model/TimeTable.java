package mlt.ui.timetable.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mlt.db.DbHandler;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class TimeTable {

    private ObservableList<Lecture> listlecture = FXCollections.observableArrayList();
    private ArrayList<String> tlist = new ArrayList<>(10);
    private ArrayList<String> tlist2 = new ArrayList<>(10);
    private ArrayList<String> tlist3 = new ArrayList<>(10);
    private int[] lecCount = new int[5];
    private Random r = new Random();

    private String course;
    private String classname;
    private int lecDur;
    private int lecNoPerDay;
    private LocalTime time;
    private int workingDay ;
    private int noSubject ;

    private int lecNoPerWeek;

    public TimeTable(String course, String classname, int lecDur, int lecNoPerWeek, LocalTime time, int workingDay, int noSubject) {

       this.course = course;
       this.classname = classname;

        System.out.println(classname);
        System.out.println(getSem(classname));
       this.lecDur = lecDur;
       this.lecNoPerWeek = lecNoPerWeek;
       this.time = time;
       this.workingDay = workingDay;
       this.noSubject = noSubject;
    }


    public ObservableList<Lecture> createTimeTable() throws Noteacher{

        try{
            // sets lecture count
            setLecCount();

            // loads teachers data
            loadTeacherList();


            lecNoPerDay = (int) ((lecNoPerWeek * noSubject )/workingDay);

            for (int i = 0; i < workingDay; i++) {
                Lecture lecture = new Lecture(i);

                loadAlternateTeacherList(i);

                int match = 0;
                for (int j = 0; j < lecNoPerDay; j++) {

                    int bound = lecNoPerDay -j-match;

                    if (bound<=0){
                        TimeTable timeTable = new TimeTable(course,  classname,  lecDur,  lecNoPerWeek,  time,  workingDay,noSubject);
                        timeTable.createTimeTable();
                    }

                    int pos = Math.abs(r.nextInt(bound));
                    int origPos = tlist.indexOf(tlist2.get(pos));

                    while ((lecCount[origPos] == lecNoPerDay+ 1)){

                        tlist2.remove(pos);
                        match++;
                        bound = (lecNoPerDay+1)-j-match;
                        pos = Math.abs(r.nextInt(bound));
                        origPos = tlist.indexOf(tlist2.get(pos));
                    }
                    lecture.lec[j] = tlist2.get(pos);

                    lecCount[origPos]++;
                    tlist2.remove(pos);

                    int count = 0;
                    for (int k = 0; k < lecCount.length; k++) {
                        if (lecCount[k]==(lecNoPerDay+1))
                            count++;
                        if (count>1 && i!=(lecNoPerDay+1)){
                            TimeTable timeTable = new TimeTable(course,  classname,  lecDur,  lecNoPerWeek,  time,  workingDay,noSubject);
                            timeTable.createTimeTable();
                        }
                    }
                }
                // add one lecture to list
                listlecture.add(lecture);
            }


            int pos = remainLecture();

            if (pos!=-1){
                for (Lecture lecture : listlecture){
                    boolean flag = false;
                    for (String teacherName : lecture.lec)
                        if (teacherName!=null)
                            if (teacherName.equals(tlist.get(pos))){
                                flag = true;
                            }
                    if (!flag)
                    {
                        lecture.lec[lecNoPerDay] = tlist.get(pos);
                        lecCount[pos]++;
                        break;
                    }
                }
            }
            printTimeTable(course, classname);
        }
        catch (Exception e){
            e.printStackTrace();
            throw  new Noteacher();
        }
        return listlecture;
    }

    private void loadAlternateTeacherList(int status) {

        if (status==(lecNoPerDay+1))
        {

           tlist3.clear();
        }

        int pos = Math.abs(r.nextInt(lecNoPerDay+1));
        String str = tlist.get(pos);
        tlist2 = (ArrayList<String>) tlist.clone();
        while (tlist3.contains(str))
        {
            pos = Math.abs(r.nextInt(lecNoPerDay+1));
            str = tlist.get(pos);
        }
        tlist2.remove(str);
        tlist3.add(str);


    }

    private int remainLecture() {
        for (int i = 0; i < (lecNoPerDay+1); i++) {
            if (lecCount[i]!=(lecNoPerDay+1)){
                return i;
            }
        }
        return -1;
    }

    private void setLecCount() {
        for (int i = 0; i < 10; i++) {
            lecCount[0]=0;
        }
    }



    private boolean checkTeacherAvailable(String teacherLecture) {

        try {
            PreparedStatement ps;
        }
        catch (Exception e){

        }
        return true;
    }

    private void printTimeTable(String course, String classname ) {

        for (Lecture l : listlecture){

            switch (l.day){
                case 0:
                    System.out.print("Monday : ");
                    break;
                case 1:
                    System.out.print("Tuesday : ");
                    break;
                case 2:
                    System.out.print("Wednesday : ");
                    break;
                case 3:
                    System.out.print("Thursday : ");
                    break;
                case 4:
                    System.out.print("Friday : ");
                    break;
                case 5:
                    System.out.print("Saturday : ");
                    break;
            }
            for (String s : l.lec){
                if (s==null)
                    ;
                else
                    System.out.print(s+" ");
            }
            System.out.println();
        }
    }

    private void loadTeacherList() throws Noteacher {

//        tlist.add("DG");
//        tlist.add("PN");
//        tlist.add("SVM");
//        tlist.add("SB");
//        tlist.add("GG");

        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("SELECT  count(*) from subteacher where course = ? and class = ? ");
            ps.setString(1,course);
            ps.setString(2,getSem(classname));
            System.out.println(getSem(classname));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
               if (rs.getInt(1)== noSubject){
                   int count = 0;
                   ps = DbHandler.getConnection().prepareStatement("SELECT  tname from subteacher where course = ? and class = ? ");
                   ps.setString(1,course);
                   ps.setString(2,getSem(classname));
                   rs = ps.executeQuery();
                   while (rs.next()){
                       count++;
                       tlist.add(rs.getString("tname"));
                       if (count >= noSubject)
                           break;
                   }
               }
               else
               {
                   throw new Noteacher();
               }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getSem(String classname){
        if (classname.contains("FY"))
        {
            if (classname.contains("first"))
                return "Semester1";
            else
                return "Semester2";
        }
        else  if (classname.contains("SY"))
        {
            if (classname.contains("first"))
                return "Semester3";
            else
                return "Semester4";
        }
        else  if (classname.contains("TY"))
        {
            if (classname.contains("first"))
                return "Semester5";
            else
                return "Semester6";
        }

        return null;
    }

    public static void main(String[] args) {
       TimeTable tt =  new TimeTable("BscIT","FY",50,5,null,6,5);
        try {
            tt.createTimeTable();
        } catch (Noteacher noteacher) {
            System.out.println(noteacher.getLocalizedMessage());
            System.out.println("No teacher Available");
        }
        tt.printTimeTable("CS","IT");
    }
}

