package com.example.myapplication;

import java.io.Serializable;

public class PlanItem extends RoutinePlanItem implements Serializable {
//    int color;
//    int start_hour, start_minute, end_hour, end_minute;
//    String title
//    String ipt;
//    String att;

    @Override
    public String getAtt() {
        return super.getAtt();
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

    public String getIpt() {
        return super.getIpt();
    }

    public void setIpt(String ipt) {
        super.setIpt(ipt);
    }
}
