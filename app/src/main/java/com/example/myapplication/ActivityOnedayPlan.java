package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityOnedayPlan extends AppCompatActivity {
    private static final String TAG= "ActivityOnedayPlan";
    RadioGroup rg, rg2, rg3; // 색상 선택, 우선순위 선택, 중요일정 등록 여부

    RadioButton R1, R2, R3;  // 색상, 우선순위, 중요일정 등록
    TextView timeSet1, timeSet2;  // 시간 표시 view
    int start_hour, start_minute, end_hour, end_minute;
    int count, deleted;
    String deleteDay;
    ArrayList<RoutinePlanItem> rList = new ArrayList<RoutinePlanItem>();
    ArrayList<RoutinePlanItem> mList = new ArrayList<RoutinePlanItem>();
    ArrayList<RoutinePlanTime> RPTime = new ArrayList<RoutinePlanTime>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day_plan);

        Intent intent = getIntent();

        rList = (ArrayList<RoutinePlanItem>) intent.getSerializableExtra("루틴 리스트");
        mList =  (ArrayList<RoutinePlanItem>) intent.getSerializableExtra("일과 리스트");
        deleteDay = intent.getExtras().getString("삭제날짜");
//        addTime(rList, mList);

        // 시간 선택 버튼
        timeSet1 = (TextView) findViewById(R.id.startTime);
        timeSet2 = (TextView) findViewById(R.id.finishTime);

        // plan 등록, 취소 버튼
        Button register = (Button) findViewById(R.id.registerButton); // plan에 관한 모든 데이터를 전달
        Button cancel = (Button) findViewById(R.id.cancel);

        // 입력창 focus 시 안에있는 힌트 제거
        final EditText et = (EditText) findViewById(R.id.et);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                et.setHint("");
            }
        });

        // 색상 선택 라디오 그룹버튼
        rg = (RadioGroup) findViewById(R.id.RG);
        rg2 = (RadioGroup) findViewById(R.id.RG2);
        rg3 = (RadioGroup) findViewById(R.id.RG3);
        R1 = (RadioButton) findViewById(R.id.R1);
        R2 = (RadioButton) findViewById(R.id.r1);
        R3 = (RadioButton) findViewById(R.id.no);

        // 디폴트 색상 정하기
        rg.check(R1.getId());

        // 디폴트 우선순위 정하기
        rg2.check(R2.getId());

        // 디폴트 중요일정 등록여부 정하기
        rg3.check(R3.getId());

        // 하루계획 등록버튼 (하루계획에 관한 모든 데이터를 전달)
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                deleted = 0;


                // 색상 정하기
                RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId()); // 체크된 라디오 버튼 id값 이용해서 가져오기
                ColorDrawable cd = (ColorDrawable) rb.getBackground(); // 라디오 버튼의 배경색을 cd변수에 담는다.
                int colorCode = cd.getColor();

                // 우선순위 정하기
                RadioButton rb2 = (RadioButton) findViewById(rg2.getCheckedRadioButtonId());
                String ABC = (String) rb2.getText();

                // 중요일정 등록 여부 정하기
                RadioButton rb3 = (RadioButton) findViewById(rg3.getCheckedRadioButtonId());
                String isYES = (String) rb3.getText();


                // 제목
                if (et.getText().toString().getBytes().length <= 0) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                // 시간
                else if (start_hour > end_hour || (start_hour == end_hour && start_minute >= end_minute)){
                        Toast.makeText(getApplicationContext(), "시간을 올바로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                else {
                    // 루틴 개수가 0일때
                    if (rList.size() == 0) {
                        // plan 개수가 0이면 비교 필요 x
                        if (mList.size() == 0) {
                            //인텐트를 통해 데이터 전달
                            Intent intent = new Intent();

                            intent.putExtra("우선순위", ABC);
                            intent.putExtra("제목", et.getText().toString());
                            intent.putExtra("색상", colorCode);
                            intent.putExtra("start_hour", start_hour);
                            intent.putExtra("start_minute", start_minute);
                            intent.putExtra("end_hour", end_hour);
                            intent.putExtra("end_minute", end_minute);
                            intent.putExtra("중요일정 등록여부", isYES);

                            setResult(5678, intent);
                            finish();
                        }

                        else{
                            for(int i=0; i<mList.size(); i++){
                                if (end_hour < mList.get(i).getStart_hour() || start_hour > mList.get(i).getEnd_hour()) {
                                    count++;
                                }
                                else if (end_hour == mList.get(i).getStart_hour() && end_minute <= mList.get(i).getStart_minute()) {
                                        count++;
                                }
                                else if(start_hour == mList.get(i).getEnd_hour() && start_minute >= mList.get(i).getEnd_minute()){
                                        count++;
                                }
                            }
                            if (count == mList.size()) {
                                Intent intent = new Intent();

                                intent.putExtra("우선순위", ABC);
                                intent.putExtra("제목", et.getText().toString());
                                intent.putExtra("색상", colorCode);
                                intent.putExtra("start_hour", start_hour);
                                intent.putExtra("start_minute", start_minute);
                                intent.putExtra("end_hour", end_hour);
                                intent.putExtra("end_minute", end_minute);
                                intent.putExtra("중요일정 등록여부", isYES);

                                setResult(5678, intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "기존 일정과 시간이 겹칩니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    // 루틴 개수가 1 이상일 때
                    else{
                        for(int i=0; i<rList.size(); i++){

                            if (!rList.get(i).isContainDay() || rList.get(i).getDeleteDate().contains(deleteDay)) {
                                deleted++;
                            }
                            else{
                                if (end_hour < rList.get(i).getStart_hour() || start_hour > rList.get(i).getEnd_hour()) {
                                    count++;
                                }
                                else if (end_hour == rList.get(i).getStart_hour() && end_minute <= rList.get(i).getStart_minute()) {
                                    count++;

                                }
                                else if(start_hour == rList.get(i).getEnd_hour() && start_minute >= rList.get(i).getEnd_minute()){
                                    count++;
                                }

                            }

                        }
                        // 루틴과 겹치는 시간이 없으면 plan과 시간 비교
                        if (count == (rList.size() - deleted)) {
                            count = 0;
                            for(int i =0; i < mList.size(); i++){
                                if (end_hour < mList.get(i).getStart_hour() || start_hour > mList.get(i).getEnd_hour()) {
                                    count++;
                                }
                                else if (end_hour == mList.get(i).getStart_hour() && end_minute <= mList.get(i).getStart_minute()) {
                                        count++;
                                }
                                else if(start_hour == mList.get(i).getEnd_hour() && start_minute >= mList.get(i).getEnd_minute()){
                                        count++;
                                }
                            }
                            if(count == mList.size()){
                                Intent intent = new Intent();

                                intent.putExtra("우선순위", ABC);
                                intent.putExtra("제목", et.getText().toString());
                                intent.putExtra("색상", colorCode);
                                intent.putExtra("start_hour", start_hour);
                                intent.putExtra("start_minute", start_minute);
                                intent.putExtra("end_hour", end_hour);
                                intent.putExtra("end_minute", end_minute);
                                intent.putExtra("중요일정 등록여부", isYES);

                                setResult(5678, intent);
                                finish();
                            }
                            else{

                                Toast.makeText(getApplicationContext(), "기존 일정과 시간이 겹칩니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "기존 일정과 시간이 겹칩니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        });

        // -----------------------------------------작업 중----------------------------------------------

        // 하루계획 취소 버튼
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 커스텀 타임피커 다이얼로그 설정
        final CustomTimePickerDialog dialog1 = new CustomTimePickerDialog(this, listener1, 12, 0, true);
        timeSet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.show();
            }
        });

        final CustomTimePickerDialog dialog2 = new CustomTimePickerDialog(this, listener2, 12, 0, true);
        timeSet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
            }
        });
    }

    //타임피커 리스너
    private CustomTimePickerDialog.OnTimeSetListener listener1 = new CustomTimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String startH, startM;
            startH = String.format( "%1$02d" , hourOfDay);
            startM = String.format( "%1$02d" , minute);

            start_hour = hourOfDay;
            start_minute = minute;
            timeSet1.setText(startH + ":" + startM);

        }
    };

    private CustomTimePickerDialog.OnTimeSetListener listener2 = new CustomTimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String endH, endM;
            endH = String.format( "%1$02d" , hourOfDay);
            endM = String.format( "%1$02d" , minute);

            end_hour = hourOfDay;
            end_minute = minute;

            timeSet2.setText(endH + ":" + endM);

        }
    };

//    public void addTime(ArrayList<RoutinePlanItem> rList, ArrayList<RoutinePlanItem> mList){
//
//        if (rList.size() > 0) {
//            RoutinePlanTime Time = new RoutinePlanTime();
//            for(int i=0; i < rList.size(); i++){
//                Time.setStart_hour(rList.get(i).getStart_hour());
//                Time.setStart_minute(rList.get(i).getStart_minute());
//                Time.setEnd_hour(rList.get(i).getEnd_hour());
//                Time.setEnd_minute(rList.get(i).getEnd_minute());
//                RPTime.add(Time);
//            }
//        }
//
//        if (mList.size() > 0) {
//            RoutinePlanTime Time = new RoutinePlanTime();
//            for(int i=0; i < mList.size(); i++){
//                Time.setStart_hour(mList.get(i).getStart_hour());
//                Time.setStart_minute(mList.get(i).getStart_minute());
//                Time.setEnd_hour(mList.get(i).getEnd_hour());
//                Time.setEnd_minute(mList.get(i).getEnd_minute());
//                RPTime.add(Time);
//            }
//        }
//
//
//
//    }


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
}
