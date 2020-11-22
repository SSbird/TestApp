package com.example.testapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.testapp.R;

public class MyMoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_money);
        Intent intent = getIntent();
        double money = intent.getDoubleExtra("money", 0);
        TextView textView = findViewById(R.id.money);
        textView.setText("￥".concat(String.valueOf(money)));
    }
}