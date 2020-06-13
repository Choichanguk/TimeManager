package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CustomDatePicker extends AppCompatActivity {
    private int mYear =0, mMonth=0, mDay=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_date_picker);

        Calendar calendar = new GregorianCalendar();

        mYear = calendar.get(Calendar.YEAR);

        mMonth = calendar.get(Calendar.MONTH);

        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        android.widget.DatePicker datePicker = findViewById(R.id.vDatePicker);

        datePicker.init(mYear, mMonth, mDay,mOnDateChangedListener);
    }

    public void mOnClick(View v){
        if(v.getId() == R.id.enter){
            Intent intent = new Intent();

            intent.putExtra("mYear",mYear);

            intent.putExtra("mMonth", mMonth);

            intent.putExtra("mDay", mDay);

            setResult(1234, intent);

            finish();
        }
        else if(v.getId() == R.id.cancel){
            finish();
        }

    }

    android.widget.DatePicker.OnDateChangedListener mOnDateChangedListener = new android.widget.DatePicker.OnDateChangedListener(){

        @Override

        public void onDateChanged(android.widget.DatePicker datePicker, int yy, int mm, int dd) {

            mYear = yy;

            mMonth = mm;

            mDay = dd;

        }

    };
}
