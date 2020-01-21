package com.dayday.yuntaesik.dayday.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.util.Contact;
import com.dayday.yuntaesik.dayday.util.SharedPrefsUtils;

/**
 * Created by YunTaeSik on 2016-07-20.
 */
public class LockConfirmActivity extends Activity implements View.OnClickListener {
    private String setKey;
    private String confirmKey = "";
    private Button number_one;
    private Button number_two;
    private Button number_three;
    private Button number_four;
    private Button number_five;
    private Button number_six;
    private Button number_seven;
    private Button number_eight;
    private Button number_nine;
    private Button number_zero;
    private Button cancel_btn;
    private Button erase_btn;
    private EditText key_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockconfirm);

        number_one = (Button) findViewById(R.id.number_one);
        number_two = (Button) findViewById(R.id.number_two);
        number_three = (Button) findViewById(R.id.number_three);
        number_four = (Button) findViewById(R.id.number_four);
        number_five = (Button) findViewById(R.id.number_five);
        number_six = (Button) findViewById(R.id.number_six);
        number_seven = (Button) findViewById(R.id.number_seven);
        number_eight = (Button) findViewById(R.id.number_eight);
        number_nine = (Button) findViewById(R.id.number_nine);
        number_zero = (Button) findViewById(R.id.number_zero);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        erase_btn = (Button) findViewById(R.id.erase_btn);
        key_text = (EditText) findViewById(R.id.key_text);

        number_one.setOnClickListener(this);
        number_two.setOnClickListener(this);
        number_three.setOnClickListener(this);
        number_four.setOnClickListener(this);
        number_five.setOnClickListener(this);
        number_six.setOnClickListener(this);
        number_seven.setOnClickListener(this);
        number_eight.setOnClickListener(this);
        number_nine.setOnClickListener(this);
        number_zero.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        erase_btn.setOnClickListener(this);

        setKey = getIntent().getStringExtra("setKey");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.number_one:
            case R.id.number_two:
            case R.id.number_three:
            case R.id.number_four:
            case R.id.number_five:
            case R.id.number_six:
            case R.id.number_seven:
            case R.id.number_eight:
            case R.id.number_nine:
            case R.id.number_zero:
                if (key_text.getText().toString().length() < 4) {
                    key_text.append("*");
                    confirmKey += v.getTag().toString();
                    if (key_text.getText().toString().length() == 4) {
                        if (setKey.equals(confirmKey)) {
                            finish();
                            SharedPrefsUtils.setStringPreference(getApplicationContext(), "setKey", setKey);
                            SharedPrefsUtils.setStringPreference(getApplicationContext(), Contact.LOCK_SWITCH, "ON");
                            Intent finish_action = new Intent("finish");
                            sendBroadcast(finish_action);
                        } else {
                            key_text.setText("");
                            confirmKey = "";

                            Toast toast = Toast.makeText(getApplicationContext(), "맞지 않습니다.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
                            toast.show();

                        }
                    }

                }
                break;
            case R.id.cancel_btn:
                if (key_text.getText().toString().length() > 0) {
                    try {
                        String resetText = (key_text.getText().toString()).substring(0, key_text.length() - 1);
                        confirmKey = confirmKey.substring(0, confirmKey.length() - 1);
                        key_text.setText(resetText);
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.erase_btn:
                key_text.setText("");
                confirmKey = "";
                break;

        }
    }
}
