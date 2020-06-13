package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityEditRoutine extends AppCompatActivity {
    private static final String TAG= "ActivityEditRoutine";
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7;
    RadioGroup rg; // 색상 선택
    RadioButton R1, R2, R3, R4, R5;
    String title;
    int color;
    int start_hour, start_minute, end_hour, end_minute, position;
    TextView timeSet1, timeSet2;
    ArrayList<String> array_day = new ArrayList<>();
    ArrayList<RoutinePlanItem> mList = new ArrayList<RoutinePlanItem>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);

        Intent intent = getIntent();
        title = intent.getExtras().getString("제목");
        start_hour = intent.getExtras().getInt("start_hour");
        start_minute = intent.getExtras().getInt("start_minute");
        end_hour = intent.getExtras().getInt("end_hour");
        end_minute = intent.getExtras().getInt("end_minute");
        color = intent.getExtras().getInt("색상");
        array_day = intent.getExtras().getStringArrayList("array_day");
        position = intent.getExtras().getInt("position");
        mList = (ArrayList<RoutinePlanItem>) intent.getSerializableExtra("루틴 리스트");

        // 시간 선택 버튼
        String startH, startM, endH, endM;
        startH = String.format( "%1$02d" , start_hour);
        startM = String.format( "%1$02d" , start_minute);
        endH = String.format( "%1$02d" , end_hour);
        endM = String.format( "%1$02d" , end_minute);
        timeSet1 = (TextView) findViewById(R.id.startTime);
        timeSet2 = (TextView) findViewById(R.id.finishTime);
        timeSet1.setText(startH + ":" + startM);
        timeSet2.setText(endH + ":" + endM);



        // 루틴 등록, 취소 버튼
        Button register = (Button) findViewById(R.id.registerButton); // 루틴에 관한 모든 데이터를 전달
        Button cancel = (Button) findViewById(R.id.cancel);

        // 입력창 focus 시 안에있는 힌트 제거
        final EditText et = (EditText) findViewById(R.id.et);
        et.setText(title);
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                et.setHint("");
//            }
//        });

        // 요일 선택 체크박스
        final ArrayList<String> chooseDay = new ArrayList<String>();
        checkBox1 = (CheckBox)findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox)findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox)findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox)findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox)findViewById(R.id.checkBox5);
        checkBox6 = (CheckBox)findViewById(R.id.checkBox6);
        checkBox7 = (CheckBox)findViewById(R.id.checkBox7);

        // 색상 선택 라디오 버튼
        R1 = (RadioButton) findViewById(R.id.R1);
        R2 = (RadioButton) findViewById(R.id.R2);
        R3 = (RadioButton) findViewById(R.id.R3);
        R4 = (RadioButton) findViewById(R.id.R4);
        R5 = (RadioButton) findViewById(R.id.R5);

        rg = (RadioGroup) findViewById(R.id.RG);
        ArrayList<RadioButton> array_radio = new ArrayList<RadioButton>(Arrays.asList(R1, R2, R3, R4, R5));
        for(int i=0; i<array_radio.size(); i++){
            ColorDrawable cd = (ColorDrawable) array_radio.get(i).getBackground();
            int colorCode = cd.getColor();
            if (colorCode == color) {
                rg.check(array_radio.get(i).getId());
            }
        }

        for(int i=0; i<array_day.size(); i++){
            if (array_day.get(i).equals("월")) {
                checkBox1.setChecked(true);
            }
            else if (array_day.get(i).equals("화")) {
                checkBox2.setChecked(true);
            }
            else if (array_day.get(i).equals("수")) {
                checkBox3.setChecked(true);
            }
            else if (array_day.get(i).equals("목")) {
                checkBox4.setChecked(true);
            }
            else if (array_day.get(i).equals("금")) {
                checkBox5.setChecked(true);
            }
            else if (array_day.get(i).equals("토")) {
                checkBox6.setChecked(true);
            }
            else if (array_day.get(i).equals("일")) {
                checkBox7.setChecked(true);
            }
        }

//        checkBox1.setChecked(true); // 선택됨 상태

        // 루틴 등록버튼 (루틴에 관한 모든 데이터를 전달)
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDay.clear();
                int count = 0;

                if (checkBox1.isChecked()) {
                    chooseDay.add("월");
                }
                if (checkBox2.isChecked()) {
                    chooseDay.add("화");
                }
                if (checkBox3.isChecked()) {
                    chooseDay.add("수");
                }
                if (checkBox4.isChecked()) {
                    chooseDay.add("목");
                }
                if (checkBox5.isChecked()) {
                    chooseDay.add("금");
                }
                if (checkBox6.isChecked()) {
                    chooseDay.add("토");
                }
                if (checkBox7.isChecked()) {
                    chooseDay.add("일");
                }


                // 색상 정하기

                RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId()); // 체크된 라디오 버튼 id값 이용해서 가져오기
                ColorDrawable cd = (ColorDrawable) rb.getBackground(); // 라디오 버튼의 배경색을 cd변수에 담는다.
                int colorCode = cd.getColor();

                // 제목
                if (et.getText().toString().getBytes().length <= 0) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                //시간
                else if (start_hour > end_hour || (start_hour == end_hour && start_minute >= end_minute)) {
                    Toast.makeText(getApplicationContext(), "시간을 올바로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                //요일
                else if(chooseDay.size() == 0) {
                    Toast.makeText(getApplicationContext(), "요일을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }

                else {

                    if (mList.size() == 1) {
                        //인텐트를 통해 데이터 전달
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putStringArrayListExtra("요일선택", chooseDay);
                        intent.putExtra("제목", et.getText().toString());
                        intent.putExtra("색상", colorCode);
                        intent.putExtra("시작시간", timeSet1.getText());
                        intent.putExtra("종료시간", timeSet2.getText());
                        intent.putExtra("start_hour", start_hour);
                        intent.putExtra("start_minute", start_minute);
                        intent.putExtra("end_hour", end_hour);
                        intent.putExtra("end_minute", end_minute);
                        setResult(1111, intent);
                        finish();
                    }
                    else{
                        int times=0;
                        for(int i=0; i < mList.size(); i++){
                            int day = 0;
                            if(i != position){
                                for(int j=0; j < chooseDay.size(); j++){

                                    // 요일 겹치는지 검사
                                    if(mList.get(i).getArray_day().contains(chooseDay.get(j))){
                                        day++;

                                    }
                                }

                                // 만약 요일이 겹친다면 시간 비교
                                if (day != 0) {
                                    times++;
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
                            }

                        }
                        if (count == times) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putStringArrayListExtra("요일선택", chooseDay);
                            intent.putExtra("제목", et.getText().toString());
                            intent.putExtra("색상", colorCode);
                            intent.putExtra("시작시간", timeSet1.getText());
                            intent.putExtra("종료시간", timeSet2.getText());
                            intent.putExtra("start_hour", start_hour);
                            intent.putExtra("start_minute", start_minute);
                            intent.putExtra("end_hour", end_hour);
                            intent.putExtra("end_minute", end_minute);
                            setResult(1111, intent);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "기존 루틴과 시간이 겹칩니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });


        // 루틴 등록 취소 버튼
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
}
