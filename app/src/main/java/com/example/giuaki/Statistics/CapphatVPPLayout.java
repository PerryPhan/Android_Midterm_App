package com.example.giuaki.Statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giuaki.Databases.PhongBanDatabase;
import com.example.giuaki.Entities.PhongBan;
import com.example.giuaki.R;

import java.util.ArrayList;
import java.util.List;

public class CapphatVPPLayout extends AppCompatActivity {
    // Main Layout
    Button backBtn;
    Button searchBtn;

    Spinner PBSpinner;

    TableLayout cp_tablevpp_list;
    TableLayout cp_tablenv_list;

    TextView totalCount;
    TextView totalPrice;

    Button navBC;
    Button navTK;

    // Data
    List<PhongBan> phongban_list;
    List<String> phongbanNames_list;

    // Focus
    String dataMaPBSpinner;

    // Other
    float scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capphatvpp2_layout);
        scale = this.getResources().getDisplayMetrics().density;
        setControl();
        loadDatabase();
        setEvent();
        setNavigation();
    }
    // --------------- MAIN HELPER -----------------------------------------------------------------
    public void setControl() {
//        Log.d("process", "setControl");
        backBtn = findViewById(R.id.CP_backBtn);
        searchBtn = findViewById(R.id.CP_searchBtn);

        PBSpinner = findViewById(R.id.CP_PBSpinner);

        cp_tablevpp_list = findViewById(R.id.CP_tableVPP);
        cp_tablenv_list = findViewById(R.id.CP_tableNV);

        totalCount = findViewById(R.id.CP_totalCount);
        totalPrice = findViewById(R.id.CP_totalPrice);

        navBC = findViewById(R.id.CP_navbar_baocao);
        navTK = findViewById(R.id.CP_navbar_thongke);
    }

    public void loadDatabase(){
        // 1.  Load Spinner ra trước
//        Log.d("process", "loadDatabase");
        PBSpinner.setAdapter( loadPBSpinner() );
    }
    public ArrayAdapter<String> loadPBSpinner(){
        // 1. Tạo list Phong ban // 2. Đổ Phong_ban.getTenPB() ra 1 List // 3. setAdapter cho cái list getTenPB() đó
        phongban_list = new PhongBanDatabase(CapphatVPPLayout.this).select();
        phongbanNames_list = new ArrayList<>();
        phongbanNames_list.add("Tất cả phòng ban");
        // Phục vụ cho việc xổ ra Option cho Spinner
        for ( PhongBan pb : phongban_list){
            phongbanNames_list.add(pb.getTenpb());
//            Log.d("data", pb.getTenpb());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, phongbanNames_list);
        return adapter;
    }

    public void setEvent(){
        // 1. Set Event cho Spinner
        setEventPBSpinner();
    }
    public void setEventPBSpinner(){
//        PBSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                dataMaPBSpinner = phongban_list.get(position).getMapb();
//                Toast.makeText( CapphatVPPLayout.this, dataMaPBSpinner+"", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                dataMaPBSpinner = phongban_list.get(0).getMapb();
//            }
//        });
    }

    public void setNavigation(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        navBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   new BaoCao
            }
        });
        navTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   new ThongKe
            }
        });
    }
    // LAYOUT 01 -----------------------------------------------
    // Khi có 1 hàng từ PBSpinner mới có VPP Table ( VPP được cấp cho Phòng Ban đó )
    public void setPBSpinner(){

    }
    // Văn phòng phẩm khi init thì select theo thằng CP, sau đó focus vào thằng đầu tiên của VPP
    public void setEventTableVPP(){

    }
    // khi 1 hàng văn phòng phẩm được focus thì mới có nhân viên
    // --------------- CUSTOM HELPER --------------------------------------------------------------------
    public int DPtoPix(int dps) {
        return (int) (dps * scale + 0.5f);
    }

    // This Custom Columns' Max Width : 80 / 300
//    public TableRow createRow(Context context, VanPhongPham vpp) {
//        TableRow tr = new TableRow(context);
//        // Id
//
//        //   Ma PB
//        TextView maPB = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
//        // Cần cái này để khi mà maPB đạt tới max width thì nó sẽ tăng height cho bên tenPB luôn
//        // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
//        maPB.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
//        maPB.setMaxWidth(DPtoPix(80));
//        maPB.setText(pb.getMapb());
//
//        //   Ten PB
//        TextView tenPB = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
//        // Cần cái này để khi mà tenPB đạt tới max width thì nó sẽ tăng height cho bên maPB luôn
//        tenPB.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
//        tenPB.setText(pb.getTenpb());
//        tenPB.setMaxWidth(DPtoPix(300));
//
//        tr.setBackgroundColor(getResources().getColor(R.color.white));
//        // Add 2 thứ vào row
//        tr.addView(maPB);
//        tr.addView(tenPB);
//
//        return tr;
//    }
//    public TableRow createRow(Context context, NhanVien nv) {
//        TableRow tr = new TableRow(context);
//        // Id
//
//        //   Ma PB
//        TextView maPB = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
//        // Cần cái này để khi mà maPB đạt tới max width thì nó sẽ tăng height cho bên tenPB luôn
//        // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
//        maPB.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
//        maPB.setMaxWidth(DPtoPix(80));
//        maPB.setText(pb.getMapb());
//
//        //   Ten PB
//        TextView tenPB = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
//        // Cần cái này để khi mà tenPB đạt tới max width thì nó sẽ tăng height cho bên maPB luôn
//        tenPB.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
//        tenPB.setText(pb.getTenpb());
//        tenPB.setMaxWidth(DPtoPix(300));
//
//        tr.setBackgroundColor(getResources().getColor(R.color.white));
//        // Add 2 thứ vào row
//        tr.addView(maPB);
//        tr.addView(tenPB);
//
//        return tr;
//    }
    // Table 1
    //    <!-- 85 / 180 p0 / 50 p0 / 80  -->

    // Table 2
    // <!-- 80 / 230 / 90-->

    // Table 3
    // <!-- 80 / 150 / 60 / 60 / 60 -->
}