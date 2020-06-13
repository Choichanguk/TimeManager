package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class ActivityTimeRecord extends AppCompatActivity {
    int addedRecord=0;
    int finishedR, finishedA, finishedB, finishedC = 0;
    int DateInt;
    ArrayList<RoutinePlanItem> RPItem, newRPItem, tempRPItem = new ArrayList<RoutinePlanItem>();
    ArrayList<RoutinePlanItem> routineList;
    ArrayList<RoutinePlanItem> planList;
    ArrayList<Integer> RPTime = new ArrayList<Integer>();

    RecyclerView mRecyclerView = null;
    RecordAdapter mRecordAdapter = null;

    // TAG
    private static final String TAG= "ActivityTimeRecord";
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy.M.d.");
    String todayDate = simpleDateFormat.format(date);
    String todayDate2 = simpleDateFormat2.format(date);
    String today = null;

    Intent myIntent = null;
    int int_from_service;
    MyMainReceiver myMainReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_record);

        /**
         * onCreate()할 때 서비스를 시작시킨다. (서비스는 계속 startService 를 호출시켜도 되는지 알아보기)
         */
        final Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);

        DateInt = Integer.parseInt(todayDate);
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                today = "일";
                break;
            case Calendar.MONDAY:
                today = "월";
                break;
            case Calendar.TUESDAY:
                today = "화";
                break;
            case Calendar.WEDNESDAY:
                today = "수";
                break;
            case Calendar.THURSDAY:
                today = "목";
                break;
            case Calendar.FRIDAY:
                today = "금";
                break;
            case Calendar.SATURDAY:
                today = "토";
                break;
            default:
                break;
        }

//        DateInt = SharedClass.loadIntDay(this, "DateInt");  // 수정

        // 현재 날짜에 맞는  plan과 routine리스트를 불러온다.
        planList = SharedClass.loadOneDayPlan(this, DateInt);
        routineList = SharedClass.loadRoutine(this, "routine list");



        // routine 리스트 요일 flag 초기화
        for(int i=0; i<routineList.size(); i++){
            routineList.get(i).setContainDay(false);
        }

        // 해당 요일에 맞는 루틴에 요일 flag 달아줌
        for(int i=0; i<routineList.size(); i++){
            if(routineList.get(i).getArray_day().contains(today)){
                routineList.get(i).setContainDay(true);
            }
        }



        addTime(routineList, planList);

        makeList(routineList, planList);

        renewList(RPTime, RPItem);





            mRecyclerView = findViewById(R.id.recordRecycler);
            mRecordAdapter = new RecordAdapter(this, newRPItem, todayDate2 + today);
            mRecordAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, View v, int position) {
                    if(v.getId() == R.id.start){

                        newRPItem.get(position).setClick(true);
//                        newRPItem.get(position).setRecord(true);


                        for(int i =0; i < newRPItem.size(); i++){
                            if(i != position){
                                newRPItem.get(i).setClick(false);
                            }
                        }


                        v.setVisibility(View.GONE);
                        itemView.findViewById(R.id.stop).setVisibility(View.VISIBLE);
//                        itemView.findViewById(R.id.finish).setVisibility(View.GONE);

                        Intent intent = new Intent();
                        intent.setAction(MyService.ACTION_RESTART_THREAD);
                        intent.putExtra("position", position);
                        intent.putExtra("time", newRPItem.get(position).getTime());
                        sendBroadcast(intent);
                        onStartForegroundService(v);


                        mRecordAdapter.notifyDataSetChanged();


                    }

                    else if(v.getId() == R.id.stop){

                        newRPItem.get(position).setClick(false);
                        v.setVisibility(View.GONE);
                        itemView.findViewById(R.id.start).setVisibility(View.VISIBLE);
//                        itemView.findViewById(R.id.finish).setVisibility(View.GONE);

                        Intent intent = new Intent();
                        intent.setAction(MyService.ACTION_STOP_THREAD);
                        intent.putExtra("position", position);
                        intent.putExtra("time", newRPItem.get(position).getTime());
                        sendBroadcast(intent);

                        mRecordAdapter.notifyDataSetChanged();
                    }

                    else if(v.getId() == R.id.finish){
                        newRPItem.get(position).setFinish(true);
                        v.setVisibility(View.GONE);
                        itemView.findViewById(R.id.start).setVisibility(View.GONE);
                        itemView.findViewById(R.id.stop).setVisibility(View.GONE);
                        itemView.findViewById(R.id.isFinish).setVisibility(View.VISIBLE);

                        Intent intent = new Intent();
                        intent.setAction(MyService.ACTION_STOP_THREAD2);
                        intent.putExtra("position", position);
                        sendBroadcast(intent);


                        Toast.makeText(getApplicationContext(),"계획을 완료했습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mRecyclerView.setAdapter(mRecordAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecordAdapter.notifyDataSetChanged();


        ImageButton button = findViewById(R.id.b3);
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

            ImageButton button2 = findViewById(R.id.b2);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(Color.BLUE);
                    Intent intent = new Intent(getApplicationContext(), ActivitySetPlan.class);
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

            final Button finishRecord = findViewById(R.id.finishRecord);
            finishRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog();
                    if (addedRecord == 1) {

                    }
                }
            });

            Button viewStatistic = findViewById(R.id.ViewStatistic);
            viewStatistic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addedRecord == 1) {

                        Intent intent = new Intent(getApplicationContext(), ActivityStatisticOneday.class);

                        startActivity(intent);
                        finish();
                    } else if (addedRecord == 0) {
                        Toast.makeText(getApplicationContext(), "측정이 종료된 후 통계를 확인할 수 있습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });




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

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        planList.clear();
        routineList.clear();
        for(int i=0; i<newRPItem.size(); i++){
            if(newRPItem.get(i).getAtt().equals("R")){
                routineList.add(newRPItem.get(i));
            }
            else{
                planList.add(newRPItem.get(i));
            }
        }
        SharedClass.saveStatisticData(getApplicationContext(), newRPItem, DateInt);
        SharedClass.saveOneDayPlan(getApplicationContext(), planList, DateInt);
        SharedClass.saveRoutine(getApplicationContext(), routineList, "routine list");
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

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart");
        myMainReceiver = new MyMainReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.ACTION_UPDATE_CNT);
        registerReceiver(myMainReceiver, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        unregisterReceiver(myMainReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    public void Dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("측정 종료");
        builder.setMessage("측정을 종료하시겠습니까?\r\n(측정 종료 후 하루 통계를 확인할 수 있습니다.)" );
        builder.setPositiveButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(),"우측버튼 클릭됨",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addedRecord = 1;
                        Toast.makeText(getApplicationContext(),"측정을 종료합니다.",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }
    

    class MyMainReceiver extends BroadcastReceiver {

        public MyMainReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MyService.ACTION_UPDATE_CNT)){
                int position = intent.getIntExtra("position", 0);
                int_from_service = intent.getIntExtra(MyService.KEY_INT_FROM_SERVICE, 0);

                newRPItem.get(position).setTime(int_from_service);
                mRecordAdapter.notifyDataSetChanged();
            }
        }
    }


    public void onStartForegroundService(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction("startForeground"); // 인텐트에 액션을 담는다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        else {
            startService(intent);
        }
    }

}
