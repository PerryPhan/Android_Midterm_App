package com.example.giuaki;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.giuaki.Mainv2.BaocaoVPPAllLayout;

public class XinchoLayout extends AppCompatActivity {
    Button cancelBtn;
    boolean cancel = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._xincho_layout);

        // Control
            cancelBtn = findViewById(R.id.XC_cancelBtn);
        // Event
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel = true;
                    finish();

                }
            });

            new android.os.Handler(Looper.getMainLooper()).postDelayed(
            new Runnable() {
                public void run() {
                        sendRequest();
                }
            },
            5000);

    }
    public void sendRequest(){
        // WebView
        Bundle b = getIntent().getExtras();
        Intent intent = new Intent(XinchoLayout.this, Webform.class);

        intent.putExtras(b);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        startActivityForResult( intent ,1);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", 1);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}