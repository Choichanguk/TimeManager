package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivitySetPlan extends AppCompatActivity {
    ImageButton imageButton, calenderButton;
    Button registerPlan;

    TextView todayDate;

    String planTitle, planIpt;
    String pickedDate = "";
    String pickedDay = "";
    int DateInt;
    int planColor;

    RecyclerView mRecyclerView, rRecyclerView = null;
    PlanAdapter mRecyclerAdapter = null;
    RoutineAdapter2 rRecyclerAdapter = null;
    int position1;
    private int mYear =0, mMonth=0, mDay=0;


    ArrayList<RoutinePlanItem> planList = new ArrayList<RoutinePlanItem>();
    ArrayList<RoutinePlanItem> routineList, iptPlan = new ArrayList<>();

    /**
     *  pieChart Data List
     */

    ArrayList<String> pieChartTitle = new ArrayList<>();
    ArrayList<Float> pieChartTime = new ArrayList<>();
    ArrayList<Integer> pieChartColor = new ArrayList<>();


    /**
     * 수정 중
     */
    ArrayList<Integer> RPTime = new ArrayList<Integer>();
    ArrayList<Integer> RPTime2 = new ArrayList<Integer>();
    ArrayList<RoutinePlanItem> RPItem, newRPItem, RPItem2, newRPItem2 = new ArrayList<RoutinePlanItem>();


    ArrayList<String> arrayDay;

    // 요일 가져오기
    Calendar calendar = Calendar.getInstance();
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    String korDayOfWeek = "";

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    String DateString = simpleDateFormat.format(date);
    int DateInt2 = Integer.parseInt(DateString);


    static final int REQUEST_CODE = 5678;
    static final int REQUEST_CODE_EDIT = 2222;
    static final int REQUEST_CODE_DatePicker = 1234;
    private static final String TAG= "ActivitySetPlan";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_plan);

        pickedDate = SharedClass.loadPickedDate(this, "pickedDate");
        pickedDay = SharedClass.loadPickedDay(this, "pickedDay");
        routineList = SharedClass.loadRoutine(this, "routine list");
        DateInt = SharedClass.loadIntDay(this, "DateInt");
        planList = SharedClass.loadOneDayPlan(this, DateInt);




        todayDate = (TextView) findViewById(R.id.date);
        todayDate.setText(pickedDate);


        // routine 리스트 요일 flag 초기화, 등록된 루틴인지 여부 초기화
        for(int i=0; i<routineList.size(); i++){
            routineList.get(i).setContainDay(false);
            routineList.get(i).setRegister(false);
        }


        // 해당 요일에 맞는 루틴에 요일 flag 달아줌
        for(int i=0; i<routineList.size(); i++){
            if(routineList.get(i).getArray_day().contains(pickedDay)){
                routineList.get(i).setContainDay(true);
            }
        }

        int a = 0;
        for(int i=0; i<routineList.size(); i++){
            if(!routineList.get(i).isContainDay() || routineList.get(i).getDeleteDate().contains(pickedDate)){
                routineList.get(i).setRegister(false);
            }
            else{
                routineList.get(i).setRegister(true);
                a++;
            }

        }
        Log.e("count: ", String.valueOf(a));

        // routine + plan list 만듬
        addTime(routineList, planList);

        makeList(routineList, planList);

        renewList(RPTime, RPItem);

        // 등록된 plan + routine list 만듬
        addTime2(routineList, planList);

        makeList2(routineList, planList);

        renewList2(RPTime2, RPItem2);

        Log.e("new: ", String.valueOf(newRPItem2.size()));

        pieChartData(newRPItem2);
        Log.e("title count: ", String.valueOf(pieChartTitle.size()));
        Log.e("color count: ", String.valueOf(pieChartColor.size()));
        Log.e("time count: ", String.valueOf(pieChartTime.size()));

        // pieChart 생성
        setupPiechart();

        // 달력 다이얼로그 세팅
        calenderButton = (ImageButton) findViewById(R.id.calenderButton);
        calenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CustomDatePicker.class);
                startActivityForResult(intent, REQUEST_CODE_DatePicker);
            }
        });


        rRecyclerView = findViewById(R.id.routineRecycler);
        rRecyclerAdapter = new RoutineAdapter2(routineList, pickedDate, DateInt);
        rRecyclerAdapter.setOnItemClickListener(new RoutineAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, View v, int position) {
                if(v.getId() == R.id.delete){
                    Toast.makeText(getApplicationContext(), "routine deleted", Toast.LENGTH_SHORT).show();
                    routineList.get(position).setDeleteDate(pickedDate);
                    routineList.get(position).setRegister(false);

                    Intent intent = new Intent();
                    intent.setAction(MyService.ACTION_STOP_THREAD2);
                    sendBroadcast(intent);
                    rRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });

        rRecyclerView.setAdapter(rRecyclerAdapter);
        rRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        mRecyclerView  = findViewById(R.id.planRecycler);
        mRecyclerAdapter = new PlanAdapter(this, planList, DateInt);
        mRecyclerAdapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if(v.getId() == R.id.edit){
                    position1 = position;
                    Intent intent = new Intent(getApplicationContext(), ActivityEditPlan.class);
                    intent.putExtra("제목", planList.get(position).getTitle());
                    intent.putExtra("색상", planList.get(position).getColor());
                    intent.putExtra("start_hour", planList.get(position).getStart_hour());
                    intent.putExtra("start_minute", planList.get(position).getStart_minute());
                    intent.putExtra("end_hour", planList.get(position).getEnd_hour());
                    intent.putExtra("end_minute", planList.get(position).getEnd_minute());
                    intent.putExtra("우선순위", planList.get(position).getIpt());
                    intent.putExtra("루틴 리스트", routineList);
                    intent.putExtra("일과 리스트", planList);
                    intent.putExtra("position", position);
                    intent.putExtra("삭제날짜", pickedDate);
                    intent.putExtra("중요일정 등록여부", planList.get(position).getIsYES());
                    startActivityForResult(intent, REQUEST_CODE_EDIT);
                }

                else if(v.getId() == R.id.delete){

                    Log.e(TAG, "plan Deleted");
                    Intent intent = new Intent();
                    intent.setAction(MyService.ACTION_STOP_THREAD2);
                    sendBroadcast(intent);

                    planList.remove(position);
                    mRecyclerAdapter.notifyDataSetChanged();

                }
            }
        });
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));




            // plan 추가 버튼
        imageButton = (ImageButton) findViewById(R.id.plusPlan);
        if(DateInt < DateInt2){
            imageButton.setVisibility(View.GONE);
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityOnedayPlan.class);
                intent.putExtra("루틴 리스트", routineList);
                intent.putExtra("일과 리스트", planList);
                intent.putExtra("삭제날짜", pickedDate);
                startActivityForResult(intent, REQUEST_CODE);
            }



        });

        ImageButton button = findViewById(R.id.b2);
        button.setBackgroundColor(Color.BLUE);
        ImageButton button1 = findViewById(R.id.b1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.BLUE);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton button3 = findViewById(R.id.b3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.BLUE);
                Intent intent = new Intent(getApplicationContext(), ActivityTimeRecord.class);

                startActivity(intent);
                finish();
            }
        });

        ImageButton button4 = findViewById(R.id.b4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.BLUE);
                Intent intent = new Intent(getApplicationContext(), ActivityStatisticOneday.class);
                startActivity(intent);
                finish();
            }
        });


        ImageButton button5 = findViewById(R.id.b5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.BLUE);
                Intent intent = new Intent(getApplicationContext(), ActivityStatisticMonth.class);
                startActivity(intent);
                finish();
            }
        });

        // Plan 등록 버튼
        registerPlan = (Button) findViewById(R.id.registerPlan);
        registerPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void addItem(String title, int start_hour, int start_minute, int end_hour, int end_minute, String ipt, int color, String att, String isYES){
        RoutinePlanItem item = new RoutinePlanItem();

        item.setTitle(title);
        item.setStart_hour(start_hour);
        item.setStart_minute(start_minute);
        item.setEnd_hour(end_hour);
        item.setEnd_minute(end_minute);
        item.setIpt(ipt);
        item.setColor(color);
        item.setAtt(att);
        item.setRegister(true);
        item.setIsYES(isYES);
        if(isYES.equals("YES")){
            item.setIptDATE(DateInt);
        }

        planList.add(item);
    }

    public void editItem(String title, int start_hour, int start_minute, int end_hour, int end_minute, String ipt, int color, String att, int position, String isYES){
        RoutinePlanItem item = new RoutinePlanItem();

        item.setTitle(title);
        item.setStart_hour(start_hour);
        item.setStart_minute(start_minute);
        item.setEnd_hour(end_hour);
        item.setEnd_minute(end_minute);
        item.setIpt(ipt);
        item.setColor(color);
        item.setAtt(att);
        item.setIsYES(isYES);
        if(isYES.equals("YES")){
            item.setIptDATE(DateInt);
        }

        planList.set(position, item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        int start_hour, start_minute, end_hour, end_minute;
        String isYES;
        if(resultCode == REQUEST_CODE){

            planTitle = data.getExtras().getString("제목");
            planIpt = data.getExtras().getString("우선순위");
            planColor = data.getExtras().getInt("색상");

            start_hour = data.getExtras().getInt("start_hour");
            start_minute = data.getExtras().getInt("start_minute");
            end_hour = data.getExtras().getInt("end_hour");
            end_minute = data.getExtras().getInt("end_minute");
            isYES = data.getExtras().getString("중요일정 등록여부");

            addItem(planTitle, start_hour, start_minute, end_hour, end_minute, planIpt, planColor, planIpt, isYES);

            mRecyclerAdapter.notifyDataSetChanged();
        } else if (resultCode == REQUEST_CODE_EDIT) {

            planTitle = data.getExtras().getString("제목");
            planIpt = data.getExtras().getString("우선순위");
            planColor = data.getExtras().getInt("색상");

            start_hour = data.getExtras().getInt("start_hour");
            start_minute = data.getExtras().getInt("start_minute");
            end_hour = data.getExtras().getInt("end_hour");
            end_minute = data.getExtras().getInt("end_minute");
            isYES = data.getExtras().getString("중요일정 등록여부");

            editItem(planTitle, start_hour, start_minute, end_hour, end_minute, planIpt, planColor, planIpt, position1, isYES);
            mRecyclerAdapter.notifyDataSetChanged();
        }

        else if(resultCode == REQUEST_CODE_DatePicker){
            mYear = data.getExtras().getInt("mYear");
            mMonth = data.getExtras().getInt("mMonth");
            mDay = data.getExtras().getInt("mDay");

            String YY, MM, DD, Date;
            YY = Integer.toString(mYear);
            MM = String.format("%02d", mMonth+1);
            DD = String.format("%02d", mDay);
            Date = YY + MM + DD;
            DateInt = Integer.parseInt(Date);


            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear, (mMonth), mDay);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String korDay = null;
            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    korDay = "일";
                    break;
                case Calendar.MONDAY:
                    korDay = "월";
                    break;
                case Calendar.TUESDAY:
                    korDay = "화";
                    break;
                case Calendar.WEDNESDAY:
                    korDay = "수";
                    break;
                case Calendar.THURSDAY:
                    korDay = "목";
                    break;
                case Calendar.FRIDAY:
                    korDay = "금";
                    break;
                case Calendar.SATURDAY:
                    korDay = "토";
                    break;
                default:
                    break;
            }
            pickedDay = korDay;
            pickedDate = mYear + "." + (mMonth+1) + "." + mDay + "." + korDay;
            todayDate = (TextView) findViewById(R.id.date);
            todayDate.setText(pickedDate);
            routineList = SharedClass.loadRoutine(this, "routine list");

            if(DateInt < DateInt2){
                imageButton.setVisibility(View.GONE);
            }
            else{
                imageButton.setVisibility(View.VISIBLE);
            }

            // routine 리스트 요일 flag 초기화
            for(int i=0; i<routineList.size(); i++){
                routineList.get(i).setContainDay(false);
            }

            // 해당 요일에 맞는 루틴에 요일 flag 달아줌
            for(int i=0; i<routineList.size(); i++){
                if(routineList.get(i).getArray_day().contains(pickedDay)){
                    routineList.get(i).setContainDay(true);
                }
            }


            rRecyclerView = findViewById(R.id.routineRecycler);
            rRecyclerAdapter = new RoutineAdapter2(routineList, pickedDate, DateInt);
            rRecyclerAdapter.setOnItemClickListener(new RoutineAdapter2.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, View v, int position) {
                    if(v.getId() == R.id.delete){
                        Toast.makeText(getApplicationContext(), "routine deleted", Toast.LENGTH_SHORT).show();
                        routineList.get(position).setDeleteDate(pickedDate);
                        routineList.get(position).setRegister(false);

                        Intent intent = new Intent();
                        intent.setAction(MyService.ACTION_STOP_THREAD2);
                        sendBroadcast(intent);
                        rRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
            rRecyclerView.setAdapter(rRecyclerAdapter);
            rRecyclerView.setLayoutManager(new LinearLayoutManager(this));




            planList = SharedClass.loadOneDayPlan(this, DateInt);
            mRecyclerView  = findViewById(R.id.planRecycler);
            mRecyclerAdapter = new PlanAdapter(this, planList, DateInt);
            mRecyclerAdapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    if(v.getId() == R.id.edit){
                        position1 = position;
                        Intent intent = new Intent(getApplicationContext(), ActivityEditPlan.class);
                        intent.putExtra("제목", planList.get(position).getTitle());
                        intent.putExtra("색상", planList.get(position).getColor());
                        intent.putExtra("start_hour", planList.get(position).getStart_hour());
                        intent.putExtra("start_minute", planList.get(position).getStart_minute());
                        intent.putExtra("end_hour", planList.get(position).getEnd_hour());
                        intent.putExtra("end_minute", planList.get(position).getEnd_minute());
                        intent.putExtra("우선순위", planList.get(position).getIpt());
                        intent.putExtra("루틴 리스트", routineList);
                        intent.putExtra("일과 리스트", planList);
                        intent.putExtra("position", position);
                        intent.putExtra("중요일정 등록여부", planList.get(position).getIsYES());
                        startActivityForResult(intent, REQUEST_CODE_EDIT);
                    }
                    else if(v.getId() == R.id.delete){

                        Log.e(TAG, "plan Deleted");
                        Intent intent = new Intent();
                        intent.setAction(MyService.ACTION_STOP_THREAD2);
                        sendBroadcast(intent);

                        planList.remove(position);
                    }


                }
            });
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        }
    }

    // 로그 기록
    @Override
    protected void onStart() {
        Log.e(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();

    }

    @Override
    protected void onDestroy() {



        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedClass.saveStatisticData(getApplicationContext(), newRPItem, DateInt);
        SharedClass.saveRoutine(getApplicationContext(), routineList, "routine list");
        SharedClass.saveOneDayPlan(getApplicationContext(), planList, DateInt);
        SharedClass.savePickedDate(getApplicationContext(), pickedDate, "pickedDate");
        SharedClass.savePickedDay(getApplicationContext(), pickedDay, "pickedDay");
        SharedClass.saveIntDate(getApplicationContext(), DateInt, "DateInt");



        Log.e(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {

        Log.e(TAG, "onRestart");
        super.onRestart();
    }


    public void addTime(ArrayList<RoutinePlanItem> rList, ArrayList<RoutinePlanItem> mList){
        if(rList.size() > 0){
            for(int i=0; i<rList.size(); i++){
                int time=0;
                time = rList.get(i).getStart_hour()*60 + rList.get(i).getStart_minute();
                if(!RPTime.contains(time)){
                    RPTime.add(time);
                }

            }
        }

        if(mList.size() > 0){
            for(int i=0; i<mList.size(); i++){
                int time=0;
                time = mList.get(i).getStart_hour()*60 + mList.get(i).getStart_minute();
                if(!RPTime.contains(time)){
                    RPTime.add(time);
                }
            }
        }
    }

    public void addTime2(ArrayList<RoutinePlanItem> routineList, ArrayList<RoutinePlanItem> planList){
        if(routineList.size() > 0){
            for(int i=0; i<routineList.size(); i++){
                if(routineList.get(i).isRegister()){
                    int time=0;
                    time = routineList.get(i).getStart_hour()*60 + routineList.get(i).getStart_minute();
                    if(!RPTime2.contains(time)){
                        RPTime2.add(time);
                    }
                }
            }
        }

        if(planList.size() > 0){
            for(int i=0; i<planList.size(); i++){
                int time=0;
                time = planList.get(i).getStart_hour()*60 + planList.get(i).getStart_minute();
                if(!RPTime2.contains(time)){
                    RPTime2.add(time);
                }
            }
        }
    }

    public void makeList(ArrayList<RoutinePlanItem> rList, ArrayList<RoutinePlanItem> mList){
        RPItem = new ArrayList<RoutinePlanItem>();
        if (rList.size() > 0) {
            for(int i=0; i<rList.size(); i++){
                RPItem.add(rList.get(i));
            }
        }

        if (mList.size() > 0) {
            for(int i=0; i<mList.size(); i++){
                RPItem.add(mList.get(i));
            }
        }
    }

    public void makeList2(ArrayList<RoutinePlanItem> rList, ArrayList<RoutinePlanItem> mList){
        RPItem2 = new ArrayList<RoutinePlanItem>();
        if (rList.size() > 0) {
            for(int i=0; i<rList.size(); i++){
                if(rList.get(i).isRegister()){
                    RPItem2.add(rList.get(i));
                }

            }
        }

        if (mList.size() > 0) {
            for(int i=0; i<mList.size(); i++){
                RPItem2.add(mList.get(i));
            }
        }
    }

    public void renewList(ArrayList<Integer> RPTime, ArrayList<RoutinePlanItem> RPItem){
        Collections.sort(RPTime);
        newRPItem = new ArrayList<>();
        for(int i=0; i<RPTime.size(); i++){
            for(int j=0; j<RPItem.size(); j++){
                if(RPTime.get(i) == (RPItem.get(j).getStart_hour()*60) + RPItem.get(j).getStart_minute()){
                    newRPItem.add(RPItem.get(j));
                }
            }
        }
    }

    public void renewList2(ArrayList<Integer> RPTime, ArrayList<RoutinePlanItem> RPItem){
        Collections.sort(RPTime2);
        newRPItem2 = new ArrayList<>();
        for(int i=0; i<RPTime2.size(); i++){
            for(int j=0; j<RPItem2.size(); j++){
                if(RPTime2.get(i) == (RPItem2.get(j).getStart_hour()*60) + RPItem2.get(j).getStart_minute()){
                    newRPItem2.add(RPItem.get(j));
                }
            }
        }
    }

    // pie chart data list 를 만들어주는 method
    public void pieChartData(ArrayList<RoutinePlanItem> newRPItem2){
        for(int i=0; i<newRPItem2.size(); i++){
            if(i==0){
                // 첫번째 plan - 00:00 angle
                int time = newRPItem2.get(i).getStart_hour()*60 +  newRPItem2.get(i).getStart_minute();
                float chartAngle = (float) (2.5 * time/10);
                pieChartTime.add(chartAngle);
                pieChartColor.add(Color.WHITE);
                pieChartTitle.add(" ");

                // 첫번째 plan angle
                int time2 = (newRPItem2.get(i).getEnd_hour()*60 + newRPItem2.get(i).getEnd_minute()) - (newRPItem2.get(i).getStart_hour()*60 + newRPItem2.get(i).getStart_minute());
                float chartAngle2 = (float) (2.5*time2/10);
                pieChartTime.add(chartAngle2);
                pieChartColor.add(newRPItem2.get(i).getColor());
                pieChartTitle.add(newRPItem2.get(i).getTitle());

            }

            else {
                if(i < newRPItem2.size()-1){
                    int time = (newRPItem2.get(i).getStart_hour()*60 + newRPItem2.get(i).getStart_minute()) - (newRPItem2.get(i-1).getEnd_hour()*60 + newRPItem2.get(i).getEnd_minute());
                    float chartAngle = (float) (2.5 * time/10);
                    pieChartTime.add(chartAngle);
                    pieChartColor.add(Color.WHITE);
                    pieChartTitle.add(" ");

                    int time2 = (newRPItem2.get(i).getEnd_hour()*60 + newRPItem2.get(i).getEnd_minute()) - (newRPItem2.get(i).getStart_hour()*60 + newRPItem2.get(i).getStart_minute());
                    float chartAngle2 = (float) (2.5*time2/10);
                    pieChartTime.add(chartAngle2);
                    pieChartColor.add(newRPItem2.get(i).getColor());
                    pieChartTitle.add(newRPItem2.get(i).getTitle());
                }

                else{
                    int time = (newRPItem2.get(i).getStart_hour()*60 + newRPItem2.get(i).getStart_minute()) - (newRPItem2.get(i-1).getEnd_hour()*60 + newRPItem2.get(i).getEnd_minute());
                    float chartAngle = (float) (2.5 * time/10);
                    pieChartTime.add(chartAngle);
                    pieChartColor.add(Color.WHITE);
                    pieChartTitle.add(" ");

                    int time2 = (newRPItem2.get(i).getEnd_hour()*60 + newRPItem2.get(i).getEnd_minute()) - (newRPItem2.get(i).getStart_hour()*60 + newRPItem2.get(i).getStart_minute());
                    float chartAngle2 = (float) (2.5*time2/10);
                    pieChartTime.add(chartAngle2);
                    pieChartColor.add(newRPItem2.get(i).getColor());
                    pieChartTitle.add(newRPItem2.get(i).getTitle());

                    int time3 = 24*60 - (newRPItem2.get(i).getEnd_hour()*60 - newRPItem2.get(i).getEnd_minute());
                    float chartAngle3 = (float) (2.5*time3/10);
                    pieChartTime.add(chartAngle3);
                    pieChartColor.add(Color.WHITE);
                    pieChartTitle.add(" ");
                }

            }

        }
    }


    private void setupPiechart(){
        //Populating a list of PieEntries;
        List<PieEntry> pieEntries = new ArrayList<>();

        for(int i=0; i<pieChartTitle.size(); i++){

            pieEntries.add(new PieEntry(pieChartTime.get(i), pieChartTitle.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "원형 시간표");
//        dataSet.setColor(new int[]);

        // data 1
//        for(int i=0; i < rainfall.length; i++){
//            pieEntries.add(new PieEntry(rainfall[i], monthNames[i]));
//        }
//
//        PieDataSet dataSet = new PieDataSet(pieEntries, "Rainfall for Vancouver");



        // 각 데이터마다 색깔 지정 가능
//        dataSet.setColors(new int[]{R.color.colorAccent, Color.WHITE});
        dataSet.setColors(pieChartColor);
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(0); // value 의 글씨를 조정하는 method
//        getDataSetByIndex(int index) // 해당 index의 data value 를 return한다.

        // Get the Chart
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.invalidate();
        chart.setCenterText("시간표"); //
//        chart.setCenterTextRadiusPercent(100f);
        chart.setHoleRadius(30f); // 가운데 원의 반지름 길이를 정해줌
        chart.setTransparentCircleRadius(0);
//        chart.setMaxAngle(270f);
//        chart.setDrawBorders(true);
        chart.setRotationAngle(270f);  // 12시 부터 시작
    }




}
