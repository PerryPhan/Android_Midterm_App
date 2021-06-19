package com.example.giuaki;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.giuaki.Mainv2.CungcapLayout;
import com.example.giuaki.Mainv2.PhongbanLayout;
import com.example.giuaki.Mainv2.ThongbaoLayout;
import com.example.giuaki.R;

public class Bell {
    // Config
    Context context;
    ImageView imageView;
    // Constant
    int shakeAnimation  = R.anim.shakeanimation;
    // Variable
    static boolean active = false;
    int NoActiveDrawable = R.drawable.notiicon;
    int ActiveDrawable = R.drawable.notiicon_active;

    // Constructor
    public Bell(Context context, ImageView imageView){
        this.context = context;
        this.imageView = imageView;
        setDestination();
        isActiveDoShake();
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        Bell.active = active;
    }

    public void isActiveDoShake() {
        if( active == true ) {
            this.shake();
        }else{
            this.noshake();
        }
    }

    // Toggle Bell Active
    public void toggleActive(){
        Bell.active = !Bell.active;
        Log.d("shake", Bell.active+"");
        if( active == true ) {
            Log.d("shake","shake");
            Animation shake = AnimationUtils.loadAnimation(context, shakeAnimation);
            Log.d("shake",shake+"");
            imageView.setImageResource( ActiveDrawable );
            imageView.setAnimation(shake);
        }else{
            noshake();
        }
    }

    public void setDestination(){
        this.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ThongbaoLayout.class);
                context.startActivity( intent );
                toggleActive();
            }
        });
    }

    // Default
    public void noshake() {
        imageView.setImageResource( NoActiveDrawable );
        imageView.setAnimation( null );
    }

    // Animation
    public void shake(){
        Animation shake = AnimationUtils.loadAnimation(context, shakeAnimation);
        imageView.setImageResource( ActiveDrawable );
        imageView.setAnimation(shake);
    }
}
