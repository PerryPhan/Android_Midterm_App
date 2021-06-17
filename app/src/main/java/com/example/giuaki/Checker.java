package com.example.giuaki;

import android.util.Log;
import android.widget.TableLayout;

public class Checker {
    public static boolean checkTableList(TableLayout list , int atLine){
        if( list == null || list.getChildCount() <= 1 ) {
            Log.d("Error","Lỗi ở checkTableList() ở dòng "+atLine+"");
            return  false;
        }
        return true;
    }
}
