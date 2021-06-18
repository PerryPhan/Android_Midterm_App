package com.example.giuaki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.giuaki.Api.*;
import com.example.giuaki.Helper.JSONHelper;
import com.example.giuaki.Mainv2.*;
import com.example.giuaki.Request.PhieuCungCapRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Main Layout
    CardView cardView1,
             cardView2,
             cardView3,
             cardView4;

    // Dialog
    Dialog dialog;
        Button yesBtn;
        Button noBtn;

    // Database
    PhieuCungCapRequest phieuCungCapRequest = new PhieuCungCapRequest();
    List<VanPhongPham> list;
    // Notification
    ImageView bell;
    Bell b;
    int layout = R.layout.activity_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( layout );
        setControl();
        setNotification();
        setNavigation();
        setAnim();
    }

    // - ----------------------------------------- MAIN ---------------------------------------------
        private void setControl() {
            // INIT
            cardView1 = findViewById(R.id.cardView1);
            cardView2 = findViewById(R.id.cardView2);
            cardView3 = findViewById(R.id.cardView3);
            cardView4 = findViewById(R.id.cardView4);
            // INVISIBLE
            cardView1.setVisibility(View.INVISIBLE);
            cardView2.setVisibility(View.INVISIBLE);
            cardView3.setVisibility(View.INVISIBLE);
            cardView4.setVisibility(View.INVISIBLE);
            // NOTIFICATION
            bell = findViewById(R.id.bell);
            b = new Bell(this, bell);
        } // OK
        private void setNavigation() {
            cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, NhanvienLayout.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    startActivity( intent );
                }
            });
            cardView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, PhongbanLayout.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    startActivity( intent );
                }
            });
            cardView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, VanphongphamLayout.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    startActivity( intent );
                }
            });
            cardView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CapphatVPPLayout.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    startActivity( intent );
                }
            });
        } // OK
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
                    cardView1.setVisibility(View.VISIBLE);
                    cardView1.startAnimation(animationLeft1);
                }
            }, 350);
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    cardView2.setVisibility(View.VISIBLE);
                    cardView2.startAnimation(animationLeft2);
                }
            }, 450);
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    cardView3.setVisibility(View.VISIBLE);
                    cardView3.startAnimation(animationLeft3);
                }
            }, 550);
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    cardView4.setVisibility(View.VISIBLE);
                    cardView4.startAnimation(animationLeft4);
                }
            }, 650);


        } // OK
        private void setNotification() {
             list = convertToVanPhongPhamList(
                    returnListfromJSON(
                            phieuCungCapRequest.doGet("getDeliveriedItemToday")
                            , "VanPhongPham")
                );
            if( list != null && list.size() > 0){
                if( Bell.isActive() == false ) b.toggleActive();
                    new Notification( this , 1 ).sendNotification( list ,true);
            }
        } // OK

    // -------------------------------------------- LOADER -----------------------------------------
        public List<Object> returnListfromJSON( String resultfromQuery , String objectClass){
            List<Object> list = null ;
            String response = resultfromQuery;
            if( !JSONHelper.verifyJSON(response).equalsIgnoreCase("pass") ) return null;
            try{
                list = JSONHelper.parseJSON(response,objectClass);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                return list;
            }
        } // OK
        public List<VanPhongPham> convertToVanPhongPhamList(List<Object> list ){
            if( list == null ) return null;
            List<VanPhongPham> vanphongphamlist = new ArrayList<>();
            for( Object li : list ){
                vanphongphamlist.add( (VanPhongPham) li);
            }
            return vanphongphamlist;
        } // OK

    // ------------------------------------------- DIALOG ------------------------------------------
        @Override
        public void onBackPressed() {
            createDialog( R.layout._thongbao_layout);
            setControlDialog();
            setEventDialog();
        }
            public void createDialog(int layout) {
                dialog = new Dialog(this);
                dialog.setContentView(layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
            public void setControlDialog(){
                yesBtn = dialog.findViewById(R.id.main_yesBtn);
                noBtn = dialog.findViewById(R.id.main_noBtn);
            }
            public void setEventDialog(){
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
}