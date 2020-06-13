package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class RoutineItem extends RoutinePlanItem implements Serializable {

//    private ArrayList<String> array_day;
//    private String day;


    @Override
    public String getAtt() {
        return super.getAtt();
    }

    @Override
    public void setAtt(String att) {
        super.setAtt(att);
    }

    public ArrayList<String> getArray_day() {
        return super.getArray_day();
    }

    public void setArray_day(ArrayList<String> array_day) {
        super.setArray_day(array_day);
    }


    public int getColor() {
        return super.getColor();
    }

    public void setColor(int color) {
        super.setColor(color);
    }

    public String getTitle() {
        return super.getTitle();
    }

    public void setTitle(String title) {
        super.setTitle(title);
    }

    public String getDay() {
        return super.getDay();
    }

    public void setDay(String day) {
        super.setDay(day);
    }

    public int getStart_hour() {
        return super.getStart_hour();
    }

    public void setStart_hour(int start_hour) {
        super.setStart_hour(start_hour);
    }

    public int getStart_minute() {
        return super.getStart_minute();
    }

    public void setStart_minute(int start_minute) {
        super.setStart_minute(start_minute);
    }

    public int getEnd_hour() {
        return super.getEnd_hour();
    }

    public void setEnd_hour(int end_hour) {
        super.setEnd_hour(end_hour);
    }

    public int getEnd_minute() {
        return super.getEnd_minute();
    }

    public void setEnd_minute(int end_minute) {
        super.setEnd_minute(end_minute);
    }
}
