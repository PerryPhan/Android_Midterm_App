package com.example.giuaki.Mainv2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.giuaki.R;

import java.util.List;

public class CTCungcapLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ct_cungcap_layout);
        Bundle b = getIntent().getExtras();
        // Fetch data Số phiếu
        String sp = b.getString("sp");
        String ng = b.getString("ng");
        String ncc = b.getString("ncc");
        String ttien = b.getString("ttien");
        String tt  = b.getString("tt");
        Toast.makeText(this,sp+"",Toast.LENGTH_LONG).show();
    }
}