package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityStatisticOneday extends AppCompatActivity {
    private static final String TAG= "ActivityStatisticOneday";
    TextView percentA, percentB, percentC, percentR;


    int DateInt = 0;
    ArrayList<RoutinePlanItem> newRPItem = new ArrayList<RoutinePlanItem>();
    RecyclerView mRecyclerView = null;
    StatisticAdapter statisticAdapter = null;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy.M.d.");
    String todayDate = simpleDateFormat.format(date);
    String todayDate2 = simpleDateFormat2.format(date);
    String today = null;

    float routineRegister, ARegister, BRegister, CRegister = 0;
    float routineFinished, AFinished, BFinished, CFinished = 0;

    /**
     * 브로드캐스트 리시버 관련 변수
     */

    int int_from_service;
    MyMainReceiver myMainReceiver;




    /**
     * barChart 관련 변수
     */
    BarChart barChart1;
    HorizontalBarChart mchart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelsNames;
    ArrayList<MonthDalesData> monthDalesDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_oneday);

        /**
         * onCreat할 때 startService를 시킨다.
         */
        final Intent intent = new Intent(getApplicationContext(), MyService.class);

        startService(intent);



        DateInt = SharedClass.loadIntDay(this, "DateInt");
        newRPItem = SharedClass.loadStatisticData(this, DateInt);

        for(int i=0; i<newRPItem.size(); i++){
            if(newRPItem.get(i).getAtt().equals("R")){
                if(newRPItem.get(i).isRegister()){
                    routineRegister++;
                }

            }
            else if(newRPItem.get(i).getAtt().equals("A")){
                ARegister++;
            }
            else if(newRPItem.get(i).getAtt().equals("B")){
                BRegister++;
            }
            else if(newRPItem.get(i).getAtt().equals("C")){
                CRegister++;
            }
        }

        for(int i=0; i<newRPItem.size(); i++){
            if(newRPItem.get(i).getAtt().equals("R")){
                if(newRPItem.get(i).isFinish()){
                    routineFinished++;
                }

            }
            else if(newRPItem.get(i).getAtt().equals("A")){
                if(newRPItem.get(i).isFinish()){
                    AFinished++;
                }
            }
            else if(newRPItem.get(i).getAtt().equals("B")){
                if(newRPItem.get(i).isFinish()){
                    BFinished++;
                }
            }
            else if(newRPItem.get(i).getAtt().equals("C")){
                if(newRPItem.get(i).isFinish()){
                    CFinished++;
                }
            }

        }



        percentA = findViewById(R.id.A);
        percentB = findViewById(R.id.B);
        percentC = findViewById(R.id.C);
        percentR = findViewById(R.id.routine);


        percentA.setText((int)AFinished + "/" + (int)ARegister);
        percentB.setText((int)BFinished + "/" + (int)BRegister);
        percentC.setText((int)CFinished + "/" + (int)CRegister);
        percentR.setText((int)routineFinished + "/" + (int)routineRegister);



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

        int count=0;
        for(int i=0; i<newRPItem.size(); i++){
            if(newRPItem.get(i).isClick())   {
                count++;
            }
        }

        Log.e(TAG, String.valueOf(count));


        /**
         *  계획 시간 vs 측정 시간 비교 리사이클러뷰
         */
        mRecyclerView = findViewById(R.id.statisticRecycler);
        statisticAdapter = new StatisticAdapter(newRPItem, todayDate2+today);
        mRecyclerView.setAdapter(statisticAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        statisticAdapter.notifyDataSetChanged();



        /**
         * -------------------------------그래프-----------------------------------
         */
        barChart1 = findViewById(R.id.barChart);

        barEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();

        fillMonthSales();
        for(int i=0; i<monthDalesDataArrayList.size(); i++){
            String month = monthDalesDataArrayList.get(i).getMonth();
            float sales = monthDalesDataArrayList.get(i).getSales();
            barEntryArrayList.add(new BarEntry(i, sales));
            labelsNames.add(month);
        }

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(15f);
        barDataSet.setFormLineWidth(2);


        BarData barData = new BarData(barDataSet);
        barChart1.setData(barData);

        // we need to XAxis value formatter
        XAxis xAxis = barChart1.getXAxis();

        // 라벨 네임 보여줌
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));


        // set position of label(month names)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(15f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        barChart1.invalidate(); // 확인 필요

        YAxis yAxisRight = barChart1.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        YAxis yAxisLeft = barChart1.getAxisLeft(); //Y축의 왼쪽 설정
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setAxisMaximum(100);









        ImageButton button = findViewById(R.id.b4);
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


    }



    private void fillMonthSales(){


        float A, B, C, R;
        if(ARegister == 0 && AFinished ==0){
            A = 0;
        }
        else{
            A = AFinished/ARegister;
        }
        if(BRegister == 0 && BFinished ==0){
            B = 0;
        }
        else{
            B = BFinished/BRegister;
        }
        if(CRegister == 0 && CFinished ==0){
            C = 0;
        }
        else{
            C = CFinished/CRegister;
        }
        if(routineRegister == 0 && routineFinished ==0){
            R = 0;
        }
        else{
            R = routineFinished/routineRegister;
        }

        monthDalesDataArrayList.clear();
        monthDalesDataArrayList.add(new MonthDalesData("A", A * 100));
        monthDalesDataArrayList.add(new MonthDalesData("B", B * 100));
        monthDalesDataArrayList.add(new MonthDalesData("C", C * 100));
        monthDalesDataArrayList.add(new MonthDalesData("반복일정", R * 100));
    }


    private class MyMainReceiver extends BroadcastReceiver {

        public MyMainReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MyService.ACTION_UPDATE_CNT)){
                int position = intent.getIntExtra("position", 0);
                int_from_service = intent.getIntExtra(MyService.KEY_INT_FROM_SERVICE, 0);
                Log.e(TAG, String.valueOf(int_from_service));
                newRPItem.get(position).setTime(int_from_service);
                statisticAdapter.notifyDataSetChanged();
            }
        }
    }


    // 로그 기록
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

    @Override
    protected void onPause() {
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
}
