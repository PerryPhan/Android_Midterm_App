package com.example.giuaki;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.giuaki.Mainv2.BaocaoVPPAllLayout;

public class XinchoLayout extends AppCompatActivity {
    Button cancelBtn;
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
                    finish();
                }
            });
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("result", 1);
//                    setResult(Activity.RESULT_OK, resultIntent);
//                    finish();
                    // WebView
                    Bundle b = getIntent().getExtras();
                    Intent intent = new Intent(XinchoLayout.this, Webform.class);
//                    Bundle c = new Bundle();
//                    c.putString("loai",b.getString("loai"));
//                    c.putString("loai",b.getString("loai"));
                    intent.putExtras(b);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    startActivityForResult( intent ,1);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result", 1);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }, 2000);
    }
}