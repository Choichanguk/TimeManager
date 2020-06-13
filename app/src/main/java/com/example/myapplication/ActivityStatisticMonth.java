package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class ActivityStatisticMonth extends AppCompatActivity {
    private static final String TAG= "ActivityStatisticMonth";

    BarChart barChart1, barChart2, barChart3, barChart4;

    HorizontalBarChart mchart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelsNames;
    ArrayList<MonthDalesData> monthDalesDataArrayList = new ArrayList<>();
    TextView PercentA, PercentB, PercentC, PercentR;
    int a, b, c, d = 0;

    ArrayList<RoutineItem> mList = new ArrayList<RoutineItem>();

    ArrayList<String> keyArray = new ArrayList<>();

    ArrayList<RoutinePlanItem> FirstList = new ArrayList<>();
    ArrayList<RoutinePlanItem> SecondList = new ArrayList<>();
    ArrayList<RoutinePlanItem> ThirdList = new ArrayList<>();
    ArrayList<RoutinePlanItem> ForthList = new ArrayList<>();

    float FirstRegisteredR, FirstRegisteredA, FirstRegisteredB, FirstRegisteredC, FirstFinishedR, FirstFinishedA, FirstFinishedB, FirstFinishedC = 0;
    float SecondRegisteredR, SecondRegisteredA, SecondRegisteredB, SecondRegisteredC, SecondFinishedA, SecondFinishedB, SecondFinishedC, SecondFinishedR = 0;
    float ThirdRegisteredR, ThirdRegisteredA, ThirdRegisteredB, ThirdRegisteredC, ThirdFinishedR, ThirdFinishedA, ThirdFinishedB, ThirdFinishedC = 0;
    float ForthRegisteredR, ForthRegisteredA, ForthRegisteredB, ForthRegisteredC, ForthFinishedR, ForthFinishedA, ForthFinishedB, ForthFinishedC = 0;
    float MonthRegisteredR, MonthFinishedR, MonthRegisteredA, MonthFinishedA, MonthRegisteredB, MonthFinishedB, MonthRegisteredC, MonthFinishedC = 0;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    String todayDate = simpleDateFormat.format(date);
    int DateInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_month);

        barChart1 = findViewById(R.id.barChart1);
        DateInt = Integer.parseInt(todayDate);

        keyArray = SharedClass.getStatisticDataAll(this);
        Log.e("array: ", String.valueOf(ForthList));
        for(int i=0; i<keyArray.size(); i++){


            int key = Integer.parseInt(keyArray.get(i));
            if (key < DateInt){
                int weekOfMonth = getWeekOfMonth(keyArray.get(i));
//                Log.e("몇주?: ", String.valueOf(weekOfMonth));

                if(weekOfMonth == 1){
                    ArrayList<RoutinePlanItem> tempArray = SharedClass.loadStatisticData(this, key);
                    FirstList.addAll(tempArray);
                }

                else if(weekOfMonth == 2){
                    ArrayList<RoutinePlanItem> tempArray = SharedClass.loadStatisticData(this, key);
                    SecondList.addAll(tempArray);
                }

                else if(weekOfMonth == 3){
                    ArrayList<RoutinePlanItem> tempArray = SharedClass.loadStatisticData(this, key);
                    ThirdList.addAll(tempArray);
                }

                else {
                    ArrayList<RoutinePlanItem> tempArray = SharedClass.loadStatisticData(this, key);
                    ForthList.addAll(tempArray);
                }
            }
        }

        Log.e("1주차: ", String.valueOf(FirstList.size()));
        Log.e("2주차: ", String.valueOf(SecondList.size()));
        Log.e("3주차: ", String.valueOf(ThirdList.size()));
        Log.e("4주차: ", String.valueOf(ForthList.size()));

        /**
         * first week statistic
         */
        for(int i=0; i< FirstList.size(); i++){
            if(FirstList.get(i).getAtt().equals("R")){
                FirstRegisteredR++;
                MonthRegisteredR++;
                if(FirstList.get(i).isFinish()){
                    FirstFinishedR++;
                    MonthFinishedR++;
                }
            }
            else if(FirstList.get(i).getAtt().equals("A")){
                FirstRegisteredA++;
                MonthRegisteredA++;
                if(FirstList.get(i).isFinish()){
                    FirstFinishedA++;
                    MonthFinishedA++;
                }
            }
            else if(FirstList.get(i).getAtt().equals("B")){
                FirstRegisteredB++;
                MonthRegisteredB++;
                if(FirstList.get(i).isFinish()){
                    FirstFinishedB++;
                    MonthFinishedB++;
                }
            }
            else if(FirstList.get(i).getAtt().equals("C")){
                FirstRegisteredC++;
                MonthRegisteredC++;
                if(FirstList.get(i).isFinish()){
                    FirstFinishedC++;
                    MonthFinishedC++;
                }
            }
        }

        /**
         * second week statistic
         */
        for(int i=0; i< SecondList.size(); i++){
            if(SecondList.get(i).getAtt().equals("R")){
                SecondRegisteredR++;
                MonthRegisteredR++;
                if(SecondList.get(i).isFinish()){
                    SecondFinishedR++;
                    MonthFinishedR++;
                }
            }
            else if(SecondList.get(i).getAtt().equals("A")){
                SecondRegisteredA++;
                MonthRegisteredA++;
                if(SecondList.get(i).isFinish()){
                    SecondFinishedA++;
                    MonthFinishedA++;
                }
            }
            else if(SecondList.get(i).getAtt().equals("B")){
                SecondRegisteredB++;
                MonthRegisteredB++;
                if(SecondList.get(i).isFinish()){
                    SecondFinishedB++;
                    MonthFinishedB++;
                }
            }
            else if(SecondList.get(i).getAtt().equals("C")){
                SecondRegisteredC++;
                MonthRegisteredC++;
                if(SecondList.get(i).isFinish()){
                    SecondFinishedC++;
                    MonthFinishedC++;
                }
            }
        }

        Log.e("SecondRegisteredR" , String.valueOf(SecondRegisteredR));
        Log.e("SecondFinishedR", String.valueOf(SecondFinishedR));

        /**
         * Third week statistic
         */
        for(int i=0; i< ThirdList.size(); i++){
            if(ThirdList.get(i).getAtt().equals("R")){
                ThirdRegisteredR++;
                MonthRegisteredR++;
                if(ThirdList.get(i).isFinish()){
                    ThirdFinishedR++;
                    MonthFinishedR++;
                }
            }
            else if(ThirdList.get(i).getAtt().equals("A")){
                ThirdRegisteredA++;
                MonthRegisteredA++;
                if(ThirdList.get(i).isFinish()){
                    ThirdFinishedA++;
                    MonthFinishedA++;
                }
            }
            else if(ThirdList.get(i).getAtt().equals("B")){
                ThirdRegisteredB++;
                MonthRegisteredB++;
                if(ThirdList.get(i).isFinish()){
                    ThirdFinishedB++;
                    MonthFinishedB++;
                }
            }
            else if(ThirdList.get(i).getAtt().equals("C")){
                ThirdRegisteredC++;
                MonthRegisteredC++;
                if(ThirdList.get(i).isFinish()){
                    ThirdFinishedC++;
                    MonthFinishedC++;
                }
            }
        }

        /**
         * Forth week statistic
         */

        for(int i=0; i< ForthList.size(); i++){
            if(ForthList.get(i).getAtt().equals("R")){
                ForthRegisteredR++;
                MonthRegisteredR++;
                if(ForthList.get(i).isFinish()){
                    ForthFinishedR++;
                    MonthFinishedR++;
                }
            }
            else if(ForthList.get(i).getAtt().equals("A")){
                ForthRegisteredA++;
                MonthRegisteredA++;
                if(ThirdList.get(i).isFinish()){
                    ForthFinishedA++;
                    MonthFinishedA++;
                }
            }
            else if(ForthList.get(i).getAtt().equals("B")){
                ForthRegisteredB++;
                MonthRegisteredB++;
                if(ForthList.get(i).isFinish()){
                    ForthFinishedB++;
                    MonthFinishedB++;
                }
            }
            else if(ForthList.get(i).getAtt().equals("C")){
                ForthRegisteredC++;
                MonthRegisteredC++;
                if(ForthList.get(i).isFinish()){
                    ForthFinishedC++;
                    MonthFinishedC++;
                }
            }
        }

        float MonthA, MonthB, MonthC, MonthR;
        if(MonthRegisteredA == 0 && MonthFinishedA ==0){
            MonthA = 0;
        }
        else{
            MonthA = MonthFinishedA/MonthRegisteredA;
        }
        if(MonthRegisteredB == 0 && MonthFinishedB ==0){
            MonthB = 0;
        }
        else{
            MonthB = MonthFinishedB/MonthRegisteredB;
        }
        if(MonthRegisteredC == 0 && MonthFinishedC ==0){
            MonthC = 0;
        }
        else{
            MonthC = MonthFinishedC/MonthRegisteredC;
        }
        if(MonthRegisteredR == 0 && MonthFinishedR ==0){
            MonthR = 0;
        }
        else{
            MonthR = MonthFinishedR/MonthRegisteredR;
        }

        PercentA = findViewById(R.id.A);
        PercentB = findViewById(R.id.B);
        PercentC = findViewById(R.id.C);
        PercentR = findViewById(R.id.routine);

        PercentA.setText(String.format("%.1f", MonthA*100) + "%");
        PercentB.setText(String.format("%.1f", MonthB*100) + "%");
        PercentC.setText(String.format("%.1f", MonthC*100) + "%");
        PercentR.setText(String.format("%.1f", MonthR*100) + "%");


        // create new object of bare entries arraylist and labels arraylist
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

        //Description description = new Description();
        //description.setText("month");
        //barChart.setDescription(description);
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
        xAxis.setGranularity(1f);
        //xAxis.setLabelCount(labelsNames.size());
        //xAxis.setLabelRotationAngle(270);
        //barChart.animateY(2000);
        barChart1.invalidate();

        YAxis yAxisRight = barChart1.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        YAxis yAxisLeft = barChart1.getAxisLeft(); //Y축의 왼쪽 설정
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setAxisMaximum(100);

        // ------------------------------------------------------------------------

        barChart2 = findViewById(R.id.barChart2);

        // create new object of bare entries arraylist and labels arraylist
        barEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();

        fillMonthSales2();
        for(int i=0; i<monthDalesDataArrayList.size(); i++){
            String month = monthDalesDataArrayList.get(i).getMonth();
            float sales = monthDalesDataArrayList.get(i).getSales();
            barEntryArrayList.add(new BarEntry(i, sales));
            labelsNames.add(month);
        }

        BarDataSet barDataSet2 = new BarDataSet(barEntryArrayList, "");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        //Description description = new Description();
        //description.setText("month");
        //barChart.setDescription(description);
        BarData barData2 = new BarData(barDataSet2);
        barChart2.setData(barData2);

        // we need to XAxis value formatter
        XAxis xAxis2 = barChart2.getXAxis();

        // 라벨 네임 보여줌
        xAxis2.setValueFormatter(new IndexAxisValueFormatter(labelsNames));

        // set position of label(month names)
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setDrawGridLines(false);
        xAxis2.setDrawAxisLine(false);
        xAxis2.setGranularity(1f);
//        xAxis2.setTextSize(30f);
        //xAxis.setLabelCount(labelsNames.size());
        //xAxis.setLabelRotationAngle(270);
        //barChart.animateY(2000);
        barChart2.invalidate();

        YAxis yAxisRight2 = barChart2.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight2.setDrawLabels(false);
        yAxisRight2.setDrawAxisLine(false);
        yAxisRight2.setDrawGridLines(false);

        YAxis yAxisLeft2 = barChart2.getAxisLeft(); //Y축의 왼쪽 설정
        yAxisLeft2.setDrawLabels(false);
        yAxisLeft2.setDrawAxisLine(false);
        yAxisLeft2.setDrawGridLines(false);
        yAxisLeft2.setAxisMinimum(0);
        yAxisLeft2.setAxisMaximum(100);
        // -------------------------------------------------------------------------

        barChart3 = findViewById(R.id.barChart3);

        // create new object of bare entries arraylist and labels arraylist
        barEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();

        fillMonthSales3();
        for(int i=0; i<monthDalesDataArrayList.size(); i++){
            String month = monthDalesDataArrayList.get(i).getMonth();
            float sales = monthDalesDataArrayList.get(i).getSales();
            barEntryArrayList.add(new BarEntry(i, sales));
            labelsNames.add(month);
        }

        BarDataSet barDataSet3 = new BarDataSet(barEntryArrayList, "");
        barDataSet3.setColors(ColorTemplate.COLORFUL_COLORS);
        //Description description = new Description();
        //description.setText("month");
        //barChart.setDescription(description);
        BarData barData3 = new BarData(barDataSet3);
        barChart3.setData(barData3);

        // we need to XAxis value formatter
        XAxis xAxis3 = barChart3.getXAxis();

        // 라벨 네임 보여줌
        xAxis3.setValueFormatter(new IndexAxisValueFormatter(labelsNames));

        // set position of label(month names)
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis3.setDrawGridLines(false);
        xAxis3.setDrawAxisLine(false);
        xAxis3.setGranularity(1f);
        //xAxis.setLabelCount(labelsNames.size());
        //xAxis.setLabelRotationAngle(270);
        //barChart.animateY(2000);
        barChart3.invalidate();

        YAxis yAxisRight3 = barChart3.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight3.setDrawLabels(false);
        yAxisRight3.setDrawAxisLine(false);
        yAxisRight3.setDrawGridLines(false);

        YAxis yAxisLeft3 = barChart3.getAxisLeft(); //Y축의 왼쪽 설정
        yAxisLeft3.setDrawLabels(false);
        yAxisLeft3.setDrawAxisLine(false);
        yAxisLeft3.setDrawGridLines(false);
        yAxisLeft3.setAxisMinimum(0);
        yAxisLeft3.setAxisMaximum(100);

        //--------------------------------------------------------------------------

        barChart4 = findViewById(R.id.barChart4);

        // create new object of bare entries arraylist and labels arraylist
        barEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();

        fillMonthSales4();
        for(int i=0; i<monthDalesDataArrayList.size(); i++){
            String month = monthDalesDataArrayList.get(i).getMonth();
            float sales = monthDalesDataArrayList.get(i).getSales();
            barEntryArrayList.add(new BarEntry(i, sales));
            labelsNames.add(month);
        }

        BarDataSet barDataSet4 = new BarDataSet(barEntryArrayList, "");
        barDataSet4.setColors(ColorTemplate.COLORFUL_COLORS);
        //Description description = new Description();
        //description.setText("month");
        //barChart.setDescription(description);
        BarData barData4 = new BarData(barDataSet4);
        barChart4.setData(barData4);

        // we need to XAxis value formatter
        XAxis xAxis4 = barChart4.getXAxis();

        // 라벨 네임 보여줌
        xAxis4.setValueFormatter(new IndexAxisValueFormatter(labelsNames));

        // set position of label(month names)
        xAxis4.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis4.setDrawGridLines(false);
        xAxis4.setDrawAxisLine(false);
        xAxis4.setGranularity(1f);

        //xAxis.setLabelCount(labelsNames.size());
        //xAxis.setLabelRotationAngle(270);
        //barChart.animateY(2000);
        barChart4.invalidate();

        YAxis yAxisRight4 = barChart4.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight4.setDrawLabels(false);
        yAxisRight4.setDrawAxisLine(false);
        yAxisRight4.setDrawGridLines(false);

        YAxis yAxisLeft4 = barChart4.getAxisLeft(); //Y축의 왼쪽 설정
        yAxisLeft4.setDrawLabels(false);
        yAxisLeft4.setDrawAxisLine(false);
        yAxisLeft4.setDrawGridLines(false);
        yAxisLeft4.setAxisMinimum(0);
        yAxisLeft4.setAxisMaximum(100);
        //---------------------------------------------------------------------------------

//        mchart = (HorizontalBarChart) findViewById(R.id.barChartLabel);
//        setData(4, 10);
//        YAxis yAxisRight5 = mchart.getAxisRight();
//        //yAxisRight5.setPosition(XAxis.XAxisPosition.BOTTOM);
//        yAxisRight5.setDrawGridLines(false);
//        yAxisRight5.setDrawAxisLine(false);
//        yAxisRight5.setGranularity(1f);

        ImageButton button = findViewById(R.id.b5);
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
                intent.putExtra("루틴 리스트", mList);
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

    }

    private void fillMonthSales(){
        float A, B, C, R;
        if(FirstRegisteredA == 0 && FirstFinishedA ==0){
            A = 0;
        }
        else{
            A = FirstFinishedA/FirstRegisteredA;
        }
        if(FirstRegisteredB == 0 && FirstFinishedB ==0){
            B = 0;
        }
        else{
            B = FirstFinishedB/FirstRegisteredB;
        }
        if(FirstRegisteredC == 0 && FirstFinishedC ==0){
            C = 0;
        }
        else{
            C = FirstFinishedC/FirstRegisteredC;
        }
        if(FirstRegisteredR == 0 && FirstFinishedR ==0){
            R = 0;
        }
        else{
            R = FirstFinishedR/FirstRegisteredR;
        }

        monthDalesDataArrayList.clear();
        monthDalesDataArrayList.add(new MonthDalesData("A", A*100));
        monthDalesDataArrayList.add(new MonthDalesData("B", B*100));
        monthDalesDataArrayList.add(new MonthDalesData("C", C*100));
        monthDalesDataArrayList.add(new MonthDalesData("반복일정", R*100));
    }

    private void fillMonthSales2(){
        float A, B, C, R;
        if(SecondRegisteredA == 0 && SecondFinishedA ==0){
            A = 0;
        }
        else{
            A = SecondFinishedA/SecondRegisteredA;
        }
        if(SecondRegisteredB == 0 && SecondFinishedB ==0){
            B = 0;
        }
        else{
            B = SecondFinishedB/SecondRegisteredB;
        }
        if(SecondRegisteredC == 0 && SecondFinishedC ==0){
            C = 0;
        }
        else{
            C = SecondFinishedC/SecondRegisteredC;
        }
        if(SecondRegisteredR == 0 && SecondFinishedR ==0){
            R = 0;
        }
        else{
            R = SecondFinishedR/SecondRegisteredR;
        }
        Log.e("R: ", String.valueOf(R));
        monthDalesDataArrayList.clear();
        monthDalesDataArrayList.add(new MonthDalesData("A", A*100));
        monthDalesDataArrayList.add(new MonthDalesData("B", B*100));
        monthDalesDataArrayList.add(new MonthDalesData("C", C*100));
        monthDalesDataArrayList.add(new MonthDalesData("반복일정", R*100));
    }

    private void fillMonthSales3(){
        float A, B, C, R;
        if(ThirdRegisteredA == 0 && ThirdFinishedA ==0){
            A = 0;
        }
        else{
            A = ThirdFinishedA/ThirdRegisteredA;
        }
        if(ThirdRegisteredB == 0 && ThirdFinishedB ==0){
            B = 0;
        }
        else{
            B = ThirdFinishedB/ThirdRegisteredB;
        }
        if(ThirdRegisteredC == 0 && ThirdFinishedC ==0){
            C = 0;
        }
        else{
            C = ThirdFinishedC/ThirdRegisteredC;
        }
        if(ThirdRegisteredR == 0 && ThirdFinishedR ==0){
            R = 0;
        }
        else{
            R = ThirdFinishedR/ThirdRegisteredR;
        }
        monthDalesDataArrayList.clear();
        monthDalesDataArrayList.add(new MonthDalesData("A", A*100));
        monthDalesDataArrayList.add(new MonthDalesData("B", B*100));
        monthDalesDataArrayList.add(new MonthDalesData("C", C*100));
        monthDalesDataArrayList.add(new MonthDalesData("반복일정", R*100));
    }

    private void fillMonthSales4(){
        float A, B, C, R;
        if(ForthRegisteredA == 0 && ForthFinishedA ==0){
            A = 0;
        }
        else{
            A = ForthFinishedA/ForthRegisteredA;
        }
        if(ForthRegisteredB == 0 && ForthFinishedB ==0){
            B = 0;
        }
        else{
            B = ForthFinishedB/ForthRegisteredB;
        }
        if(ForthRegisteredC == 0 && ForthFinishedC ==0){
            C = 0;
        }
        else{
            C = ForthFinishedC/ForthRegisteredC;
        }
        if(ForthRegisteredR == 0 && ForthFinishedR ==0){
            R = 0;
        }
        else{
            R = ForthFinishedR/ForthRegisteredR;
        }
        monthDalesDataArrayList.clear();
        monthDalesDataArrayList.add(new MonthDalesData("A", A*100));
        monthDalesDataArrayList.add(new MonthDalesData("B", B*100));
        monthDalesDataArrayList.add(new MonthDalesData("C", C*100));
        monthDalesDataArrayList.add(new MonthDalesData("반복일정", R*100));
    }


    private void setData (int count, int range){

        ArrayList<BarEntry> yvals = new ArrayList<>();
        float barWidth = 9f;
        float spaceForBar = 10f;
        for(int i=0; i<count; i++){
            yvals.add(new BarEntry(i, 1f));

        }

        BarDataSet set1;

        set1 = new BarDataSet(yvals, "Data Set1");
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData((set1));
        mchart.setData(data);
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

    private int getWeekOfMonth(String date) {
        Calendar calendar = Calendar.getInstance();

        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));

        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.WEEK_OF_MONTH);

    }


}
