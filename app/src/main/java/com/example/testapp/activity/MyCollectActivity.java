package com.example.testapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.MyApp;
import com.example.testapp.R;
import com.example.testapp.adapter.LinearAdapter_MyCollect;
import com.xuexiang.xui.widget.actionbar.TitleBar;

// 我的收藏页面
public class MyCollectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        RecyclerView reView = findViewById(R.id.request_table);
        TitleBar titleBar = findViewById(R.id.titleBar_myCollect);
        titleBar.setLeftClickListener(v -> this.finish());
        reView.setLayoutManager(new LinearLayoutManager(MyCollectActivity.this));
        reView.setAdapter(new LinearAdapter_MyCollect(MyCollectActivity.this, MyApp.getUser_coll_list()));
    }
}