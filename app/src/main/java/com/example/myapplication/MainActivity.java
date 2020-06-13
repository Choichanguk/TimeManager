package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView, iptRecyclerView = null;
    RoutineAdapter mRecyclerAdapter  = null;
    planIPTAdapter iptRecyclerAdapter = null;

    ArrayList<String> receive_day;

    ImageButton routinePlus, L1, L2, L3, L4, L5;
    Button allMonth, theseMonth;
    ArrayList<RoutinePlanItem> routineList;
    ArrayList<RoutinePlanItem> planIPT  = new ArrayList<>();
    ArrayList<RoutinePlanItem> planMonth = new ArrayList<>();
    ArrayList<String> keyArray;
    ArrayList<Integer> keyInt = new ArrayList<>();
    static final int REQUEST_CODE = 1234;
    static final int REQUEST_CODE_EDIT = 1111;
    private static final String TAG= "MainActivity";
    String routineTitle, routineDay;
    int routineColor;
    int position1;
    int todayDateInt;
    String pickedDate;

    // 오늘 날짜 구하기
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    String todayDate = simpleDateFormat.format(date);


    int DateInt2 = Integer.parseInt(todayDate);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todayDateInt = Integer.parseInt(todayDate);
        keyArray = SharedClass.getAll(this);

        // 전체 중요일정을 담는 리스트를 만드는 로직
        for(int i=0; i<keyArray.size(); i++) {
            if(Integer.parseInt(keyArray.get(i)) != 0){
            int keyint = Integer.parseInt(keyArray.get(i));
            keyInt.add(keyint);
            }
        }

        Collections.sort(keyInt);
        Log.e(TAG, String.valueOf(DateInt2));
        for (int i=0; i<keyInt.size(); i++){
            if(DateInt2 <= keyInt.get(i)){
                ArrayList<RoutinePlanItem> tempArray = SharedClass.loadOneDayPlan(this, keyInt.get(i));
                if(tempArray.size() != 0){
                    for(int j=0; j < tempArray.size(); j++){

                        if(tempArray.get(j).getIsYES().equals("YES")){
                            planIPT.add(tempArray.get(j));
                        }

                    }
                }
            }

        }

        for (int i=0; i<keyInt.size(); i++){
            if(DateInt2 <= keyInt.get(i)){
            if(String.valueOf(keyInt.get(i)).substring(5, 6).equals("6")){
                ArrayList<RoutinePlanItem> tempArray = SharedClass.loadOneDayPlan(this, keyInt.get(i));
                if(tempArray.size() != 0){
                    for(int j=0; j < tempArray.size(); j++){
                        if(tempArray.get(j).getIsYES().equals("YES")){
                            planMonth.add(tempArray.get(j));
                        }

                    }
                }
            }
            }

        }

        allMonth = (Button) findViewById(R.id.allMonth);
        theseMonth = (Button) findViewById(R.id.theseMonth);
        allMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iptRecyclerView = findViewById(R.id.planIptRecycler);
                iptRecyclerAdapter = new planIPTAdapter(planIPT, todayDateInt);
                iptRecyclerView.setAdapter(iptRecyclerAdapter);
                iptRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                iptRecyclerAdapter.notifyDataSetChanged();
            }
        });

        theseMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, String.valueOf(planMonth.size()));
                iptRecyclerView = findViewById(R.id.planIptRecycler);
                iptRecyclerAdapter = new planIPTAdapter(planMonth, todayDateInt);
                iptRecyclerView.setAdapter(iptRecyclerAdapter);
                iptRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                iptRecyclerAdapter.notifyDataSetChanged();
            }
        });



        Log.e(TAG, String.valueOf(planIPT.size()));


        iptRecyclerView = findViewById(R.id.planIptRecycler);
        iptRecyclerAdapter = new planIPTAdapter(planMonth, todayDateInt);
        iptRecyclerView.setAdapter(iptRecyclerAdapter);
        iptRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        iptRecyclerAdapter.notifyDataSetChanged();



        pickedDate = SharedClass.loadPickedDate(this, "pickedDate");

        routineList = SharedClass.loadRoutine(this, "routine list");

        mRecyclerView  = findViewById(R.id.routineRecycler);
        mRecyclerAdapter = new RoutineAdapter(this, routineList);
        mRecyclerAdapter.setOnItemClickListener(new RoutineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Boolean isCheck = false;
//                if(routineList.get(position).isRecord()){
//
//                    // 다이얼로그 띄어주기
//
//
//                }
                DialogClick(v, isCheck);
                position1 = position;

                Intent intent = new Intent(getApplicationContext(), ActivityEditRoutine.class);
                intent.putExtra("제목", routineList.get(position).getTitle());
                intent.putExtra("색상", routineList.get(position).getColor());
                intent.putExtra("start_hour", routineList.get(position).getStart_hour());
                intent.putExtra("start_minute", routineList.get(position).getStart_minute());
                intent.putExtra("end_hour", routineList.get(position).getEnd_hour());
                intent.putExtra("end_minute", routineList.get(position).getEnd_minute());
                intent.putExtra("array_day", routineList.get(position).getArray_day());
                intent.putExtra("position", position);
                Log.e(TAG, String.valueOf(position));
                intent.putExtra("루틴 리스트", routineList);

                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });

        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        // 루틴 정하는 창에서 루틴 데이터 받아오기
        routinePlus = (ImageButton) findViewById(R.id.plusRoutine);
        routinePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivitySetRoutine.class);
                intent.putExtra("루틴 리스트", routineList);

                startActivityForResult(intent, REQUEST_CODE);
            }



        });


        L1 = findViewById(R.id.b1);
        L1.setBackgroundColor(Color.BLUE);
        L2 = findViewById(R.id.b2);
        L2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean click = true;

                v.setBackgroundColor(Color.BLUE);


                Intent intent = new Intent(getApplicationContext(), ActivitySetPlan.class);
                startActivity(intent);
                finish();
            }
        });

        L3 = findViewById(R.id.b3);
        L3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.BLUE);
                Intent intent = new Intent(getApplicationContext(), ActivityTimeRecord.class);
                startActivity(intent);

                finish();
            }
        });

        L4 = findViewById(R.id.b4);
        L4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityStatisticOneday.class);
                startActivity(intent);
                finish();
            }
        });

        L5 = findViewById(R.id.b5);
        L5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.BLUE);
                Intent intent = new Intent(getApplicationContext(), ActivityStatisticMonth.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void addItem(String title, int start_hour, int start_minute, int end_hour, int end_minute, String day, int color, ArrayList<String> array_day, String att){
        RoutinePlanItem item = new RoutinePlanItem();

        item.setTitle(title);
        item.setStart_hour(start_hour);
        item.setStart_minute(start_minute);
        item.setEnd_hour(end_hour);
        item.setEnd_minute(end_minute);
        item.setDay(day);
        item.setColor(color);
        item.setArray_day(array_day);
        item.setAtt("R");
        routineList.add(item);
//        oneDayRoutineList.add(item);

    }

    public void editItem(String title, int start_hour, int start_minute, int end_hour, int end_minute, String day, int color, ArrayList<String> array_day, int position, String att){
        RoutinePlanItem item = new RoutinePlanItem();

        item.setTitle(title);
        item.setStart_hour(start_hour);
        item.setStart_minute(start_minute);
        item.setEnd_hour(end_hour);
        item.setEnd_minute(end_minute);
        item.setDay(day);
        item.setColor(color);
        item.setArray_day(array_day);
        item.setAtt("R");
        routineList.set(position, item);
//        oneDayRoutineList.set(position, item);

    }


    // 루틴 선택창으로부터 받아오는 데이터
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        int start_hour, start_minute, end_hour, end_minute;
        if(resultCode == 1234){
            String day = "";
            receive_day = data.getStringArrayListExtra("요일선택");

            for(int i = 0; i<receive_day.size(); i++){
                day = day + receive_day.get(i);

            }

            routineDay = day;
            routineColor = data.getExtras().getInt("색상");

            start_hour = data.getExtras().getInt("start_hour");
            start_minute = data.getExtras().getInt("start_minute");
            end_hour = data.getExtras().getInt("end_hour");
            end_minute = data.getExtras().getInt("end_minute");

            routineTitle = data.getExtras().getString("제목");
            addItem(routineTitle, start_hour, start_minute, end_hour, end_minute, routineDay, routineColor, receive_day, "R");
            mRecyclerAdapter.notifyDataSetChanged();

        } else if (resultCode == 1111) {

            String day = "";
            receive_day = data.getStringArrayListExtra("요일선택");

            for(int i = 0; i<receive_day.size(); i++){
                day = day + receive_day.get(i);

            }

            routineDay = day;
            routineColor = data.getExtras().getInt("색상");

            start_hour = data.getExtras().getInt("start_hour");
            start_minute = data.getExtras().getInt("start_minute");
            end_hour = data.getExtras().getInt("end_hour");
            end_minute = data.getExtras().getInt("end_minute");

            routineTitle = data.getExtras().getString("제목");
            editItem(routineTitle, start_hour, start_minute, end_hour, end_minute, routineDay, routineColor, receive_day, position1, "R");
            mRecyclerAdapter.notifyDataSetChanged();

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
        SharedClass.saveRoutine(getApplicationContext(), routineList, "routine list");
            //        SharedClass.saveOneDayRoutine(getApplicationContext(), oneDayRoutineList, pickedDate);
        Log.e(TAG, "onPause");
        super.onPause();
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


    public void DialogClick(View view, final Boolean isCheck) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        builder.setTitle("루틴 수정").setMessage("기록중인 루틴이 초기화됩니다. 수정하시겠습니까?");

        builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) { } });


    }




}
