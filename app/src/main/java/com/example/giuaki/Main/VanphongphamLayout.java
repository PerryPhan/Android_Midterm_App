package com.example.giuaki.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giuaki.Databases.PhongBanDatabase;
import com.example.giuaki.Databases.VanPhongPhamDatabase;
import com.example.giuaki.Entities.PhongBan;
import com.example.giuaki.Entities.VanPhongPham;
import com.example.giuaki.R;

import java.util.ArrayList;
import java.util.List;

public class VanphongphamLayout extends AppCompatActivity {
    // Main Layout
    TableLayout vpp_table_list;

    List<VanPhongPham> vpplist;

    Button insertBtn;
    Button editBtn;
    Button delBtn;
    Button exitBtn;

    Button navPB;
    Button navNV;
    Button navVPP;

    // Database Controller
    VanPhongPhamDatabase vanphongphamDB;

    // Focus
    int indexofRow = -1;

    // Other
    float scale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanphongpham_layout);
        scale = this.getResources().getDisplayMetrics().density;
        setControl();
        loadDatabase();
        setEvent();
        setNavigation();
    }
    public void setControl(){
        vpp_table_list = findViewById(R.id.VPP_table_list);
        insertBtn = findViewById(R.id.VPP_insertBtn);
        editBtn = findViewById(R.id.VPP_editBtn);
        delBtn = findViewById(R.id.VPP_delBtn);
        exitBtn = findViewById(R.id.VPP_exitBtn);

        navPB = findViewById(R.id.VPP_navbar_phongban);
        navNV = findViewById(R.id.VPP_navbar_nhanvien);
        navVPP = findViewById(R.id.VPP_navbar_VPP);
    }
    public void loadDatabase(){
        vanphongphamDB = new VanPhongPhamDatabase(this);

        vpplist = new ArrayList<>();
        TableRow tr = null;
        vpplist = vanphongphamDB.select();
        // Tag sẽ bắt đầu ở 1 vì phải cộng thêm thằng example đã có sẵn
        for (int i = 0; i < vpplist.size(); i++) {
            Log.d("data",vpplist.get(i).toString());
            tr = createRow(this, vpplist.get(i));
            tr.setId((int) i + 1);
            vpp_table_list.addView(tr);
        }
    }
    public void setEvent(){
        editBtn.setVisibility(View.INVISIBLE); // turn on when click items
        delBtn.setVisibility(View.INVISIBLE);  // this too
        setEventTable(vpp_table_list);
    }

    public void setNavigation(){
        // navVPP onclick none
        // navPB
        navPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(VanphongphamLayout.this, PhongbanLayout.class);
                startActivity( intent );
            }
        });
        // navNV
        navNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(VanphongphamLayout.this, NhanvienLayout.class);
                startActivity( intent );
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setEventTable(TableLayout list) {
        // Log.d("count", list.getChildCount()+""); // số table rows + 1
        // Không cần thay đổi vì đây chỉ mới set Event
        // Do có thêm 1 thằng example để làm gốc, nên số row thì luôn luôn phải + 1
        // Có example thì khi thêm row thì nó sẽ theo khuôn
        for (int i = 0; i < list.getChildCount(); i++) {
            setEventTableRows((TableRow) list.getChildAt(i), list);
        }
        // Khi tạo, dùng n làm tag để thêm row
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Khi edit
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexofRow != -1) {

                }
            }
        });
        // Khi delete, có 3 TH : nằm ở cuối hoặc nằm ở đầu hoặc chính giữa
        // Nằm ở cuối thì chỉ cần xóa cuối
        // Còn lại thì sau khi xóa xong thì phải cập nhật lại tag cho toàn bộ col
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexofRow != -1) {

                }
            }
        });

    }
    public void setNormalBGTableRows(TableLayout list) {
        // 0: là thằng example đã INVISIBLE
        // Nên bắt đầu từ 1 -> 9
        for (int i = 1; i < list.getChildCount(); i++) {
            TableRow row = (TableRow) list.getChildAt((int) i);
            if (indexofRow != (int) row.getId())
                row.setBackgroundColor(getResources().getColor(R.color.white));
        }
//             Toast.makeText( PhongbanLayout.this, indexofRow+"", Toast.LENGTH_LONG).show();
        Toast.makeText(VanphongphamLayout.this, indexofRow + ":" + (int) list.getChildAt(indexofRow).getId() + "", Toast.LENGTH_LONG).show();
    }

    public void setEventTableRows(TableRow tr, TableLayout list) {
        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBtn.setVisibility(View.VISIBLE);
                delBtn.setVisibility(View.VISIBLE);
                // v means TableRow
                v.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                indexofRow = (int) v.getId();
                setNormalBGTableRows(list);
                // Testing to get id of focusable row
                //  Toast.makeText( PhongbanLayout.this, focusRowID+"", Toast.LENGTH_LONG).show();
            }
        });
    }
    // --------------- CUSTOM HELPER --------------------------------------------------------------------
    public int DPtoPix(int dps) {
        return (int) (dps * scale + 0.5f);
    }

    // This Custom Columns' Max Width : 70 p0 / 200 / 55 p0 / <= 55 p0
    public TableRow createRow(Context context, VanPhongPham vpp) {
        TableRow tr = new TableRow(context);
        // Id
        //   Ma
        TextView maVPP = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // Cần cái này để khi mà maVPP đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
        // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
        maVPP.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        maVPP.setMaxWidth(DPtoPix(70));
        maVPP.setPadding(0,0,0,0);
        maVPP.setText(vpp.getMaVpp());

        //   Ten PB
        TextView tenVPP = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // Cần cái này để khi mà tenVPP đạt tới max width thì nó sẽ tăng height cho bên maVPP luôn
        tenVPP.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        tenVPP.setText(vpp.getTenVpp());
        tenVPP.setMaxWidth(DPtoPix(200));

        TextView dvt = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // Cần cái này để khi mà tenVPP đạt tới max width thì nó sẽ tăng height cho bên maVPP luôn
        dvt.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        dvt.setText(vpp.getDvt());
        dvt.setPadding(0,0,0,0);
        dvt.setMaxWidth(DPtoPix(55));

        TextView gianhap = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // Cần cái này để khi mà tenVPP đạt tới max width thì nó sẽ tăng height cho bên maVPP luôn
        gianhap.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        gianhap.setText(vpp.getGiaNhap());
        gianhap.setPadding(0,0,0,0);
        gianhap.setMaxWidth(DPtoPix(55));

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        tr.addView(maVPP);
        tr.addView(tenVPP);
        tr.addView(dvt);
        tr.addView(gianhap);
        return tr;
    }


}