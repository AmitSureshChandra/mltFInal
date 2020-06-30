package mlt.ui.timetable.model;

import java.util.Calendar;

public class Lecture
{

    public int day;
    public String[]lec = new String[5];
    public String tname,sname;
    public Calendar ltime;

    public Lecture(int day) {
        this.day = day;
    }

    public int getDayname() {
        return day;
    }

    public void setDayname(int dayname) {
        this.day = dayname;
    }

    public String[] getLec() {
        return lec;
    }

    public void setLec(String[] lec) {
        this.lec = lec;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Calendar getLtime() {
        return ltime;
    }

    public void setLtime(Calendar ltime) {
        this.ltime = ltime;
    }
}
