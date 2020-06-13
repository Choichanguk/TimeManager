package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityEditPlan extends AppCompatActivity {
    RadioGroup rg; // 색상 선택
    RadioGroup rg2; // 우선순위 선택
    RadioGroup rg3; // 중요일정 등록 여부
    RadioButton R1, R2, R3, R4, R5, r1, r2, r3, rYES, rNO;  // 색상, 우선순위
    TextView timeSet1, timeSet2;  // 시간 표시 view
    int start_hour, start_minute, end_hour, end_minute, position;
    int color, deleted, count;
    String title, ipt, isYES;
    String deleteDay;
    ArrayList<RoutinePlanItem> rList = new ArrayList<RoutinePlanItem>();
    ArrayList<RoutinePlanItem> mList = new ArrayList<RoutinePlanItem>();
    ArrayList<RoutinePlanTime> RPTime = new ArrayList<RoutinePlanTime>();


    private static final String TAG= "ActivityEditPlan";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);

        Intent intent = getIntent();

        title = intent.getExtras().getString("제목");
        start_hour = intent.getExtras().getInt("start_hour");
        start_minute = intent.getExtras().getInt("start_minute");
        end_hour = intent.getExtras().getInt("end_hour");
        end_minute = intent.getExtras().getInt("end_minute");
        color = intent.getExtras().getInt("색상");
        ipt = intent.getExtras().getString("우선순위");
        position = intent.getExtras().getInt("position");
        isYES = intent.getExtras().getString("중요일정 등록여부");
        deleteDay = intent.getExtras().getString("삭제날짜");

        // 숫자를 00형태로 변환
        String startH, startM, endH, endM;
        startH = String.format( "%1$02d" , start_hour);
        startM = String.format( "%1$02d" , start_minute);
        endH = String.format( "%1$02d" , end_hour);
        endM = String.format( "%1$02d" , end_minute);



        rList = (ArrayList<RoutinePlanItem>) intent.getSerializableExtra("루틴 리스트");
        mList =  (ArrayList<RoutinePlanItem>) intent.getSerializableExtra("일과 리스트");
        addTime(rList, mList); // 루틴과 일과의 시간을 한 list에 담는다.
//        Log.e(TAG, String.valueOf(RPTime.size()));


        // 시간 선택 버튼
        timeSet1 = (TextView) findViewById(R.id.startTime);
        timeSet2 = (TextView) findViewById(R.id.finishTime);
        timeSet1.setText(startH + ":" + startM);
        timeSet2.setText(endH + ":" + endM);


        // plan 등록, 취소 버튼
        Button register = (Button) findViewById(R.id.registerButton); // plan에 관한 모든 데이터를 전달
        Button cancel = (Button) findViewById(R.id.cancel);

        // 입력창 focus 시 안에있는 힌트 제거
        final EditText et = (EditText) findViewById(R.id.et);
        et.setText(title);

        // 색상 선택 라디오 그룹버튼
        rg = (RadioGroup) findViewById(R.id.RG);

        // 색상 선택 라디오 버튼
        R1 = (RadioButton) findViewById(R.id.R1);
        R2 = (RadioButton) findViewById(R.id.R2);
        R3 = (RadioButton) findViewById(R.id.R3);
        R4 = (RadioButton) findViewById(R.id.R4);
        R5 = (RadioButton) findViewById(R.id.R5);

        rg = (RadioGroup) findViewById(R.id.RG);
        ArrayList<RadioButton> array_color = new ArrayList<RadioButton>(Arrays.asList(R1, R2, R3, R4, R5));
        for(int i=0; i<array_color.size(); i++){
            ColorDrawable cd = (ColorDrawable) array_color.get(i).getBackground();
            int colorCode = cd.getColor();
            if (colorCode == color) {
                rg.check(array_color.get(i).getId());
            }
        }

        r1 = (RadioButton) findViewById(R.id.r1);
        r2 = (RadioButton) findViewById(R.id.r2);
        r3 = (RadioButton) findViewById(R.id.r3);


        rg2 = (RadioGroup) findViewById(R.id.RG2);
        ArrayList<RadioButton> array_ipt = new ArrayList<RadioButton>(Arrays.asList(r1, r2, r3));

        for(int i=0; i<array_ipt.size(); i++){
            if(array_ipt.get(i).getText().toString().equals(ipt)){
                rg2.check(array_ipt.get(i).getId());
            }

        }

        rg3 = (RadioGroup) findViewById(R.id.RG3);
        rYES = (RadioButton) findViewById(R.id.yes);
        rNO = (RadioButton) findViewById(R.id.no);
        ArrayList<RadioButton> array_isYES = new ArrayList<RadioButton>(Arrays.asList(rYES, rNO));

        for(int i=0; i < array_isYES.size(); i++){
            if(array_isYES.get(i).getText().toString().equals(isYES)){
                rg3.check(array_isYES.get(i).getId());
            }
        }


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
                ipt = (String) rb2.getText();

                // 중요일정 등록여부
                RadioButton rb3 = (RadioButton) findViewById(rg3.getCheckedRadioButtonId());
                isYES = (String) rb3.getText();

                // 제목
                if (et.getText().toString().getBytes().length <= 0) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                // 시간
                else if (start_hour > end_hour || (start_hour == end_hour && start_minute >= end_minute)){
                    Toast.makeText(getApplicationContext(), "시간을 올바로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                else {
                    // 루틴 갯수가 0일때
                    if (rList.size() == 0) {
                        if (mList.size() == 1) {
                            //인텐트를 통해 데이터 전달
                            Intent intent = new Intent();
                            intent.putExtra("우선순위", ipt);
                            intent.putExtra("제목", et.getText().toString());
                            intent.putExtra("색상", colorCode);

                            intent.putExtra("start_hour", start_hour);
                            intent.putExtra("start_minute", start_minute);
                            intent.putExtra("end_hour", end_hour);
                            intent.putExtra("end_minute", end_minute);
                            intent.putExtra("중요일정 등록여부", isYES);

                            setResult(2222, intent);
                            finish();
                        }

                        else{
                            for(int i=0; i < mList.size(); i++){

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
                            if (count == (mList.size()-1)) {
                                Intent intent = new Intent();

                                intent.putExtra("우선순위", ipt);
                                intent.putExtra("제목", et.getText().toString());
                                intent.putExtra("색상", colorCode);

                                intent.putExtra("start_hour", start_hour);
                                intent.putExtra("start_minute", start_minute);
                                intent.putExtra("end_hour", end_hour);
                                intent.putExtra("end_minute", end_minute);
                                intent.putExtra("중요일정 등록여부", isYES);

                                setResult(2222, intent);
                                finish();
                            }

                            else{

                                Log.e(TAG, String.valueOf(mList.size()-1));
                                Log.e(TAG, String.valueOf(count));
                                Toast.makeText(getApplicationContext(), "기존 일정과 시간이 겹칩니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    // 루틴 갯수가 1 이상일때
                    else{
                        for(int i =0; i < rList.size(); i++){
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

                        // 루틴과 시간 비교후 겹치지 않으면 if문 실행
                        if(count == (rList.size() - deleted)){
                            count = 0;
                            for(int i=0; i < mList.size(); i++){
                                if(i != position){
                                    if (end_hour < mList.get(i).getStart_hour() || start_hour > mList.get(i).getEnd_hour()) {
                                        count++;
                                    } else if (end_hour == mList.get(i).getStart_hour() && end_minute <= mList.get(i).getStart_minute()) {
                                        count++;
                                    }
                                    else if(start_hour == mList.get(i).getEnd_hour() && start_minute >= mList.get(i).getEnd_minute()){
                                        count++;
                                    }
                                }
                            }

                            // 루틴, plan 모두와 시간 안겹칠 경우
                            if(count == (mList.size()-1)){
                                Intent intent = new Intent();

                                intent.putExtra("우선순위", ipt);
                                intent.putExtra("제목", et.getText().toString());
                                intent.putExtra("색상", colorCode);

                                intent.putExtra("start_hour", start_hour);
                                intent.putExtra("start_minute", start_minute);
                                intent.putExtra("end_hour", end_hour);
                                intent.putExtra("end_minute", end_minute);
                                intent.putExtra("중요일정 등록여부", isYES);

                                setResult(2222, intent);
                                finish();
                            }

                            // 루틴과 시간 안겹치지만 plan이랑 시간 겹칠 경우
                            else{
                                Toast.makeText(getApplicationContext(), "기존 일정과 시간이 겹칩니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // 루틴과 시간이 겹칠 경우
                        else{
                            Toast.makeText(getApplicationContext(), "기존 일정과 시간이 겹칩니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            }
        });

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


    // 루틴과 일과의 시간을 저장하는 메서드 (plan 정할 때 한번에 시간비교하기 위해)
    public void addTime(ArrayList<RoutinePlanItem> rList, ArrayList<RoutinePlanItem> mList){

        if (rList.size() > 0) {
            RoutinePlanTime Time = new RoutinePlanTime();
            for(int i=0; i < rList.size(); i++){
                Time.setStart_hour(rList.get(i).getStart_hour());
                Time.setStart_minute(rList.get(i).getStart_minute());
                Time.setEnd_hour(rList.get(i).getEnd_hour());
                Time.setEnd_minute(rList.get(i).getEnd_minute());
                RPTime.add(Time);
            }
        }

        if (mList.size() > 0) {
            RoutinePlanTime Time = new RoutinePlanTime();
            for(int i=0; i < mList.size(); i++){
                Time.setStart_hour(mList.get(i).getStart_hour());
                Time.setStart_minute(mList.get(i).getStart_minute());
                Time.setEnd_hour(mList.get(i).getEnd_hour());
                Time.setEnd_minute(mList.get(i).getEnd_minute());
                RPTime.add(Time);
            }
        }



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
}
