package com.example.giuaki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    ViewStub stub;
    LinearLayout view_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1. Control
        // 2. InitView
        // 3. Event
        // 4. Navigation

        setControl();
        InitView();
    }
    public void setControl(){
        view_container = findViewById(R.id.table_container);
//        stub = findViewById(R.id.stub);
    }

    public void InitView(){
//        stub.setLayoutResource(R.layout.activity_vanphongpham_layout);
//        stub.inflate();

    }
}