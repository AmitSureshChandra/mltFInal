
package mlt.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amit
 */
public class DbHandler {

    private static Connection con = null;
    private String username,password;

    private DbHandler() {
    }

    public static Connection getConnection() throws SQLException {
        if(con==null)
        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            con = DriverManager.getConnection ("jdbc:mysql://localhost:3306","mlt","mlt");
                excAction("create database if not exists meritlist");
                excAction("use meritlist");
        }
        return con;
      }

    public static Connection getConnection(String username, String password) throws SQLException {
        if(con==null)
        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection ("jdbc:mysql://localhost:3306",username,password);
            excAction("create database if not exists meritlist");
            excAction("use meritlist");
            createAllRequiredTable();
        }
        return con;
    }

    public static Connection getConnection(String username, String password,String conString) throws SQLException {
        if(con==null)
        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection (conString,username,password);
            createAllRequiredTable();
        }
        return con;
    }

    public static boolean excAction(String sql) throws SQLException {

              Statement st = getConnection().createStatement();
              boolean flag = st.execute(sql);
              return flag;

      }
       public static ResultSet excQuery(String sql) throws SQLException {

              Statement st = getConnection().createStatement();
              ResultSet rs = st.executeQuery(sql);
              return rs;

      }
    public static void createLoginTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"login\" and table_schema = \"meritlist\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
             //   excAction("use meritlist");
                excAction("create table login(username varchar(20) primary key,password varchar(255) not null,mob varchar(15),email varchar(50),role varchar(30) not null ,signupdate date)");
                excAction("insert into login values('admin','66baa923b366da12d2f1ed6a2e971a146b650addf691c13f43b40d5d55d7968d','7218569842','','admin','2020-01-01')");
                excAction("insert into login values('teacher','07aad3a68a107160b43e8e62968ac837f97f980c9a757813d464ee9dcd37c6b5','7218569842','','user','2020-01-01')");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createAccountTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"account\" and table_schema = \"meritlist\";");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table account(username varchar(20) primary key,dob date, address varchar(255), about varchar(255),role varchar(40), foreign key(username) references login(username));");
                excAction("insert into account values('admin','2000-02-29','Virar,Maharashtra','Db Admin','admin')");
                excAction("insert into account values('teacher','2000-02-29','Virar,Maharashtra','Teacher Account','teacher')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createCourseTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"course\" and table_schema = \"meritlist\";");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
             //   excAction("use meritlist");
                excAction("create table course(id varchar(20) primary key ,cname varchar(30) unique key not null ,cduration int(2) not null,cdesc varchar(50) not null)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createOperationTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"operation\" and table_schema = \"meritlist\";");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //   excAction("use meritlist");
                excAction("create table operation(id int primary key auto_increment ,executor varchar(20) not null ,operation varchar(100) not null, optdate datetime)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void createTeacherTable(){
        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select count(*) from information_schema.tables where information_schema.tables.table_name = ? and table_schema = \"meritlist\"");
            ps.setString(1,"teacher");
            ResultSet rs = ps.executeQuery();
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
               // excAction("use meritlist");
                excAction("create table teacher(id varchar(30) primary key,tname varchar(30) unique key not null,post varchar(50) not null,exp int(2),course varchar(30) not null , foreign key(course) references course(cname))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPerSeatsTable(){
        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select count(*) from information_schema.tables where information_schema.tables.table_name = ? and table_schema = \"meritlist\"");
            ps.setString(1,"perseats");
            ResultSet rs = ps.executeQuery();
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table perseats(category varchar(70) primary key , seats float(3))");
                excAction("insert into perseats values(\"SC\",13)");
                excAction("insert into perseats values(\"ST\",7)");
                excAction("insert into perseats values(\"VJNT A\",3)");
                excAction("insert into perseats values(\"VJNT B\",2.5)");
                excAction("insert into perseats values(\"VJNT C\",3.5)");
                excAction("insert into perseats values(\"VJNT D\",2)");
                excAction("insert into perseats values(\"OPEN\",15)");
                excAction("insert into perseats values(\"SBC\",2)");
                excAction("insert into perseats values(\"OBC\",19)");
                excAction("insert into perseats values(\"SEBC\",16)");
                excAction("insert into perseats values(\"EBC\",10)");
                excAction("insert into perseats values(\"Management Cota\",15)");
                //excAction("insert into perseats values(\"Management Cota\",15)");
                excAction("insert into perseats values(\"Physical Handicape , Ex-Serviceman or Cultural\",3)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTotalSeatsTable(){
        try {
            PreparedStatement ps = DbHandler.getConnection().prepareStatement("select count(*) from information_schema.tables where information_schema.tables.table_name = ? and table_schema = \"meritlist\"");
            ps.setString(1,"totalseats");
            ResultSet rs = ps.executeQuery();
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                // excAction("use meritlist");
                excAction("create table totalseats(cid varchar(20) primary key , seats int(3))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createSubjectTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"subject\" and table_schema = \"meritlist\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //excAction("use meritlist");
                excAction("create table subject(id varchar(10) primary key,sname varchar(30) not null unique key,course varchar(30) not null, class varchar(20) not null, foreign key(course) references course(cname))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createSubjectTeacherTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"subteacher\" and table_schema = \"meritlist\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //excAction("use meritlist");
                excAction("create table subteacher(id int(4) primary key auto_increment,sname varchar(30) not null, tname varchar(30) not null,course varchar(30) not null, class varchar(20) not null, foreign key(course) references course(cname), foreign key(tname) references teacher(tname), foreign key(sname) references subject(sname))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTblTimeTable(String course,String classname) throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = 'timetable_"+course+"_"+classname+"' and table_schema = \"meritlist\"");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //excAction("use meritlist");
                excAction("create table timetable_"+course+"_"+classname+"(time varchar(50) not null, lecture varchar(3) primary key, monday varchar(50) , tuesday varchar(50), wednesday varchar(50) , thursday varchar(50), friday varchar(50), saturday varchar(50))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTimeScheduleTable(String course,String classname) throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = 'tt' and table_schema = \"meritlist\"");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //excAction("use meritlist");
                excAction("create table tt(id varchar(2) primary key auto_increment,day varchar(30) not null, lect1 varchar(30) ,lect2 varchar(30), lect3 varchar(30), lect4 varchar(30), lect5 varchar(30))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createStudentTable(String course,String classname) throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = '"+classname+"_"+course+"_"+"student' and table_schema = \"meritlist\"");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //excAction("use meritlist");
                excAction("create table "+classname+"_"+course+"_student(id int(6) primary key auto_increment,name varchar(40) not null, caste varchar(40) ,marks int(3), remark varchar(30),course varchar(30), class varchar(30) , foreign key(course) references course(cname))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createMeritListTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = 'meritlist' and table_schema = \"meritlist\"");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //excAction("use meritlist");
                excAction("create table meritlist(id varchar(10) primary key,sname varchar(30) not null unique key, mark int(3) not null ,course varchar(30) not null, class varchar(30) not null, caste varchar(30) not null,extra varchar(30))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createProfilePhotoTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = 'profilephoto' and table_schema = \"meritlist\"");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //excAction("use meritlist");
                excAction("create table profilephoto(id int(10) primary key auto_increment,username varchar(30) not null unique key,path varchar(100),foreign key(username) references login(username))");
                excAction("insert into profilephoto(username,path) values('admin','')");
                excAction("insert into profilephoto(username,path) values('teacher','')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createLectureScheduleTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = 'lec' and table_schema = \"meritlist\"");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //excAction("use meritlist");
                excAction("create table lec(id int(10) primary key auto_increment, tname varchar(30) not null, day , ltime time, ldur time  )");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createMltDataTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = 'mltdata' and table_schema = \"meritlist\"");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //excAction("use meritlist");
                excAction("create table mltdata(id int(4) primary key auto_increment,course varchar(30) , class varchar(30) not null, minmarks int(3) not null , reqsub varchar(30), totalseat int(3) not null, foreign key(course) references course(cname))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createThemeTable() throws SQLException {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = 'theme' and table_schema = \"meritlist\"");
        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                //excAction("use meritlist");
                excAction("create table theme(id int(4) primary key auto_increment,username varchar(30) , theme varchar(30), foreign key(username) references login(username))");
                excAction("INSERT into theme(username,theme) values('teacher','white')");
                excAction("INSERT into theme(username,theme) values('admin','white')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setUpDb() throws SQLException {
        DbHandler.excAction("USE meritlist ");
        DbHandler.createLoginTable();
        DbHandler.createAccountTable();
        DbHandler.createOperationTable();
        DbHandler.createCourseTable();
        DbHandler.createTeacherTable();
        DbHandler.createSubjectTable();
        DbHandler.createMeritListTable();
        DbHandler.createProfilePhotoTable();
        DbHandler.createPerSeatsTable();
        DbHandler.createTotalSeatsTable();
        DbHandler.createMltDataTable();
        DbHandler.createSubjectTeacherTable();
    }

    public static void createAllRequiredTable() throws SQLException {
        DbHandler.excAction("USE meritlist ");
        DbHandler.createLoginTable();
        DbHandler.createAccountTable();
        DbHandler.createThemeTable();

        DbHandler.createOperationTable();
        DbHandler.createCourseTable();
        DbHandler.createTeacherTable();
        DbHandler.createSubjectTable();
        DbHandler.createMeritListTable();
        DbHandler.createProfilePhotoTable();
        DbHandler.createPerSeatsTable();
        DbHandler.createTotalSeatsTable();
        DbHandler.createMltDataTable();
    }


    public static void main(String[] args) throws SQLException {
//        setUpDb();

        createStudentTable("BscIT","fy");


    }
}
