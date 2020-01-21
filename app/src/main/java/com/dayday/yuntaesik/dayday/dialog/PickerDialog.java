package com.dayday.yuntaesik.dayday.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.dayday.yuntaesik.dayday.R;

import java.util.Calendar;

/**
 * Created by YunTaeSik on 2016-08-08.
 */
public class PickerDialog extends AppCompatActivity implements View.OnClickListener {
    private Button ok_btn;
    private Button cancel_btn;
    private NumberPicker year_number;
    private NumberPicker mouth_number;
    private Calendar calendar;
    private int year;
    private int mouth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_pickerdialog);

        setFinishOnTouchOutside(false);
        ok_btn = (Button) findViewById(R.id.ok_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        year_number = (NumberPicker) findViewById(R.id.year_number);
        mouth_number = (NumberPicker) findViewById(R.id.mouth_number);

        ok_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        mouth = (calendar.get(Calendar.MONTH) + 1);

        year_number.setMinValue(year - 20);
        year_number.setMaxValue(year + 20);
        year_number.setValue(year);

        mouth_number.setMinValue(1);
        mouth_number.setMaxValue(12);
        mouth_number.setValue(mouth);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                Intent intent = new Intent("picker_select");
                intent.putExtra("year", year_number.getValue());
                intent.putExtra("mouth", mouth_number.getValue());
                sendBroadcast(intent);
                finish();
                break;
            case R.id.cancel_btn:
                finish();
                break;
        }
    }
}
