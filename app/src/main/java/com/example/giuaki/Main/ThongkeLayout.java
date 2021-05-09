package com.example.giuaki.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.giuaki.Entities.CapPhat;
import com.example.giuaki.Entities.NhanVien;
import com.example.giuaki.Main.NhanvienLayout;
import com.example.giuaki.Main.PhongbanLayout;
import com.example.giuaki.Main.VanphongphamLayout;
import com.example.giuaki.MainActivity;
import com.example.giuaki.R;
import com.example.giuaki.Statistics.CapphatVPPLayout;

public class ThongkeLayout extends AppCompatActivity {
    Button backBtn;
    Button btn_cau1, btn_cau2, btn_cau3, btn_cau4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke_layout);

        setControl();
        setEvent();
        setAnim();

    }

    private void setControl() {
        backBtn = findViewById(R.id.TK_backBtn);
        btn_cau1 = findViewById(R.id.TK_btn_cau1);
        btn_cau2 = findViewById(R.id.TK_btn_cau2);
        btn_cau3 = findViewById(R.id.TK_btn_cau3);
        btn_cau4 = findViewById(R.id.TK_btn_cau4);
        btn_cau1.setVisibility(View.INVISIBLE);
        btn_cau2.setVisibility(View.INVISIBLE);
        btn_cau3.setVisibility(View.INVISIBLE);
        btn_cau4.setVisibility(View.INVISIBLE);
    }


    private void setEvent() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private void setAnim() {
        Animation animationLeft1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left);
        Animation animationLeft2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left);
        Animation animationLeft3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left);
        Animation animationLeft4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                btn_cau1.setVisibility(View.VISIBLE);
                btn_cau1.startAnimation(animationLeft1);
            }
        }, 350);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                btn_cau2.setVisibility(View.VISIBLE);
                btn_cau2.startAnimation(animationLeft2);
            }
        }, 450);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                btn_cau3.setVisibility(View.VISIBLE);
                btn_cau3.startAnimation(animationLeft3);
            }
        }, 550);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                btn_cau4.setVisibility(View.VISIBLE);
                btn_cau4.startAnimation(animationLeft4);
            }
        }, 650);


    }

}