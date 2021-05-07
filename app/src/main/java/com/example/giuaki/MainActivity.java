package com.example.giuaki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.giuaki.Databases.CapPhatDatabase;
import com.example.giuaki.Databases.NhanVienDatabase;
import com.example.giuaki.Databases.PhongBanDatabase;
import com.example.giuaki.Databases.VanPhongPhamDatabase;
import com.example.giuaki.Entities.CapPhat;
import com.example.giuaki.Entities.PhongBan;
import com.example.giuaki.Entities.VanPhongPham;
import com.example.giuaki.Main.PhongbanLayout;
import com.example.giuaki.Statistics.CapphatVPPLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TESTING ZONE --------------------------------------------------------------------
        PhongBanDatabase phongBanDatabase = new PhongBanDatabase(MainActivity.this);
        phongBanDatabase.reset();
        NhanVienDatabase nhanvienDatabase = new NhanVienDatabase(MainActivity.this);
        nhanvienDatabase.reset();
        CapPhatDatabase capPhatDatabase = new CapPhatDatabase(MainActivity.this);
        for(CapPhat cp : capPhatDatabase.reset() )
            Log.d("data",cp.toString());
        VanPhongPhamDatabase vanPhongPhamDatabase = new VanPhongPhamDatabase(MainActivity.this);
        vanPhongPhamDatabase.reset();
        // TESTING ZONE --------------------------------------------------------------------

    }


}