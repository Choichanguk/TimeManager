package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class RoutinePlanItem implements Serializable {
    private int start_hour, start_minute, end_hour, end_minute;
    private int color;
    private boolean isFinish, isContainDay, isRecord  = false;
    private String day, att, title, Ipt;
    private ArrayList<String> array_day;
    private ArrayList<String> deleteDateArr = new ArrayList<>();
    private String isYES = "NO";
    private int iptDATE = 0;
    private int time = 0;
    private boolean isClick = false;
    private boolean isRegister = false;

    public boolean isRegister() {
        return this.isRegister;
    }

    public void setRegister(boolean register) {
        this.isRegister = register;
    }

    public boolean isClick() {
        return this.isClick;
    }

    public void setClick(boolean click) {
        this.isClick = click;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getIptDATE() {
        return this.iptDATE;
    }

    public void setIptDATE(int iptDATE) {
        this.iptDATE = iptDATE;
    }

    public String getIsYES() {
        return this.isYES;
    }

    public void setIsYES(String isYES) {
        this.isYES = isYES;
    }

    public boolean isRecord() {
        return this.isRecord;
    }

    public void setRecord(boolean record) {
        this.isRecord = record;
    }

    public ArrayList<String> getDeleteDate() {
        return this.deleteDateArr;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDateArr.add(deleteDate);
    }



    public boolean isContainDay() {
        return this.isContainDay;
    }

    public void setContainDay(boolean containDay) {
        this.isContainDay = containDay;
    }



    public boolean isFinish() {
        return this.isFinish;
    }

    public void setFinish(boolean finish) {
        this.isFinish = finish;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<String> getArray_day() {
        return this.array_day;
    }

    public void setArray_day(ArrayList<String> array_day) {
        this.array_day = array_day;
    }



    public String getIpt() {
        return this.Ipt;
    }

    public void setIpt(String ipt) {
        this.Ipt = ipt;
    }

    public String getAtt() {
        return this.att;
    }

    public void setAtt(String att) {
        this.att = att;
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStart_hour() {
        return this.start_hour;
    }

    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }

    public int getStart_minute() {
        return this.start_minute;
    }

    public void setStart_minute(int start_minute) {
        this.start_minute = start_minute;
    }

    public int getEnd_hour() {
        return this.end_hour;
    }

    public void setEnd_hour(int end_hour) {
        this.end_hour = end_hour;
    }

    public int getEnd_minute() {
        return this.end_minute;
    }

    public void setEnd_minute(int end_minute) {
        this.end_minute = end_minute;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
