package com.example.giuaki.Mainv2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.giuaki.Api.NhanVien;
import com.example.giuaki.Api.PhongBan;
import com.example.giuaki.Helper.JSONHelper;
import com.example.giuaki.R;
import com.example.giuaki.Request.NhanVienRequest;
import com.example.giuaki.Request.PhongBanRequest;
import com.example.giuaki.Checker;

import java.util.ArrayList;
import java.util.List;

public class PhongbanLayout extends AppCompatActivity {
    // Main Layout
    TableLayout phongban_table_list;

    Button insertBtn;
    Button editBtn;
    Button delBtn;
    Button exitBtn;

    // Navigation
    Button navPB;
    Button navNV;
    Button navVPP;
    Button navCP;

    // Dialog Layout
    Dialog phongbandialog;
        Button backBtn;
        Button yesBtn;
        Button noBtn;

        EditText inputMaPB;
        EditText inputTenPB;

        TextView showMPBError;
        TextView showTPBError;
        TextView showResult;
        TextView showConfirm;
        TextView showLabel;

    // Database Controller
    PhongBanRequest phongbanDB;
    NhanVienRequest nhanvienDB;
    List<NhanVien> NVlist;
    // Focus
    int indexofRow = -1;
    TableRow focusRow;
    TextView focusMaPB;
    TextView focusTenPB;

    // Other
    float scale;
    int layout = R.layout.activity_phongban_layout;
    int dialogLayout = R.layout.popup_phongban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        scale = this.getResources().getDisplayMetrics().density;
        setControl();
        loadDatabase();
        setEvent();
        setNavigation();
    }

    // --------------- MAIN HELPER -----------------------------------------------------------------
    public void setControl() {
        phongban_table_list = findViewById(R.id.PB_table_list);
        insertBtn = findViewById(R.id.PB_insertBtn);
        editBtn = findViewById(R.id.PB_editBtn);
        delBtn = findViewById(R.id.PB_delBtn);
        exitBtn = findViewById(R.id.PB_exitBtn);

        navPB = findViewById(R.id.PB_navbar_phongban);
        navNV = findViewById(R.id.PB_navbar_nhanvien);
        navVPP= findViewById(R.id.PB_navbar_VPP);
        navCP= findViewById(R.id.PB_navbar_capphat);

    } // OK
    public void setEvent() {
        editBtn.setVisibility(View.INVISIBLE); // turn on when click items
        delBtn.setVisibility(View.INVISIBLE);  // this too
        setEventTable(phongban_table_list);
    } // OK
    // Load from the Database to the Table Layout
    public void loadDatabase() {
        phongbanDB = new PhongBanRequest();
        nhanvienDB = new NhanVienRequest();
        // Tag sẽ bắt đầu ở 1 vì phải cộng thêm thằng example đã có sẵn
        List<PhongBan> list = convertToPhongBanList(
                returnListfromJSON(
                        phongbanDB.doGet("show"),
                        "PhongBan"
                )
        );
        if( list == null){
            Toast.makeText(this,"Error: Không load được JSON!!",Toast.LENGTH_LONG).show();
            return;
        }else if(list.size() == 0){
            Toast.makeText(this,"Hiện tại không có phòng ban!!",Toast.LENGTH_LONG).show();
            return;
        }
        NVlist = convertToNhanvienList(
                returnListfromJSON(
                        nhanvienDB.doGet("show"),
                        "NhanVien"
                )
        );
        TableRow tr = null;
        for (int i = 0; i < list.size(); i++) {
            tr = createRow(this, list.get(i));
            tr.setId((int) i + 1);
            phongban_table_list.addView(tr);
        }
    } // OK
    public void setNavigation(){
        // navPB onclick none
        // navNV
        navNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PhongbanLayout.this, NhanvienLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity( intent );

            }
        });
        // navVPP
        navVPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PhongbanLayout.this, VanphongphamLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity( intent );

            }
        });
        // navCP
        navCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhongbanLayout.this, CapphatVPPLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity( intent );
            }

        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    } // OK

    // ------------------- CONVERTER -------------------------------------------------------------------
        public List<Object> returnListfromJSON( String resultfromQuery , String objectClass){
            List<Object> list = null ;
            String response = resultfromQuery;
            if( !JSONHelper.verifyJSON(response).equalsIgnoreCase("pass") ){
                Toast.makeText(this,"Vertify JSON from returnListfromJSON() failure",Toast.LENGTH_LONG).show();
                return null;
            }
            try{
                list = JSONHelper.parseJSON(response,objectClass);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                return list;
            }
        } // OK
        public List<PhongBan> convertToPhongBanList(List<Object> list ){
        if( list == null ) return null;
        List<PhongBan> phongbanlist = new ArrayList<>();
        for( Object li : list ){
            phongbanlist.add( (PhongBan) li);
        }
        return phongbanlist;
    } // OK
        public List<NhanVien> convertToNhanvienList( List<Object> list ){
            if( list == null ) return null;
            List<NhanVien> nhanvienlist = new ArrayList<>();
            for( Object li : list ){
                nhanvienlist.add( (NhanVien) li);
            }
            return nhanvienlist;
        } // OK
    // ------------------- TABLE HELPER --------------------------------------------------------
        public void setEventTable(TableLayout list) {
            // Log.d("count", list.getChildCount()+""); // số table rows + 1
            // Không cần thay đổi vì đây chỉ mới set Event
            // Do có thêm 1 thằng example để làm gốc, nên số row thì luôn luôn phải + 1
            // Có example thì khi thêm row thì nó sẽ theo khuôn
            if(Checker.checkTableList(list, 255))
                for (int i = 0; i < list.getChildCount(); i++) {
                    setEventTableRows((TableRow) list.getChildAt(i), list);
                }
            // Khi tạo, dùng n làm tag để thêm row
            insertBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // H bấm 1 cái là hiện ra cái pop up
                    createDialog(dialogLayout);
                    // Control
                    setControlDialog();
                    // Event
                    setEventDialog(v);
                }
            });
            // Khi edit
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (indexofRow != -1) {
                        // Test
                        // Toast.makeText( PhongbanLayout.this, focusRowID+"", Toast.LENGTH_LONG).show();
                        createDialog(dialogLayout);
                        // Control
                        setControlDialog();
                        showLabel.setText("Sửa phòng ban");
                        showConfirm.setText("Bạn có muốn sửa hàng này không?");
                        // Event
                        setEventDialog(v);
                        inputMaPB.setText(focusMaPB.getText());
                        inputMaPB.setEnabled(false);
                        inputTenPB.setText(focusTenPB.getText());
                    }
                }
            });
            // Khi delete, có 3 TH : nằm ở cuối hoặc nằm ở đầu hoặc chính giữa
            // Nằm ở cuối thì chỉ cần xóa cuối0
            // Còn lại thì sau khi xóa xong thì phải cập nhật lại tag cho toàn bộ col
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (indexofRow != -1) {
                        // Không thể xóa vì đang chứa nhân viên
                        if( NVlist != null && NVlist.size() != 0 )
                            for( NhanVien nv : NVlist){
                                if( nv.getMaPb().trim().equalsIgnoreCase(
                                        focusMaPB.getText().toString().trim()) ){
                                    Toast.makeText(PhongbanLayout.this,
                                            "Không thể xóa "+focusTenPB.getText().toString().trim()
                                            +" vì có chứa nhân viên !! "
                                            ,Toast.LENGTH_LONG).show();
                                      return;
                                }
                            }
                         createDialog(dialogLayout);
                        // Control
                        setControlDialog();
                        showLabel.setText("Xóa phòng ban");
                        showConfirm.setText("Bạn có muốn xóa hàng này không?");
                        // Event
                        setEventDialog(v);
                        inputMaPB.setText(focusMaPB.getText());
                        inputTenPB.setText(focusTenPB.getText());
                        inputMaPB.setEnabled(false);
                        inputTenPB.setEnabled(false);
                    }
                }
            });

        } // OK
    // To set all rows to normal state, set focusRowid = -1
        public void setEventTableRows(TableRow tr, TableLayout list) {
                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editBtn.setVisibility(View.VISIBLE);
                        delBtn.setVisibility(View.VISIBLE);
                        // v means TableRow
                        v.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                        indexofRow = (int) v.getId();
                        focusRow = (TableRow) list.getChildAt(indexofRow);
                        focusMaPB = (TextView) focusRow.getChildAt(0);
                        focusTenPB = (TextView) focusRow.getChildAt(1);
                        setNormalBGTableRows(list);
                        // Testing to get id of focusable row
                        //  Toast.makeText( PhongbanLayout.this, focusRowID+"", Toast.LENGTH_LONG).show();
                    }
                });
            } // OK
            public void setNormalBGTableRows(TableLayout list) {
                // 0: là thằng example đã INVISIBLE
                // Nên bắt đầu từ 1 -> 9
                for (int i = 1; i < list.getChildCount(); i++) {
                    TableRow row = (TableRow) list.getChildAt((int) i);
                    if (indexofRow != (int) row.getId())
                        row.setBackgroundColor(getResources().getColor(R.color.white));
                }
        //             Toast.makeText( PhongbanLayout.this, indexofRow+"", Toast.LENGTH_LONG).show();
        //        Toast.makeText(PhongbanLayout.this, indexofRow + ":" + (int) list.getChildAt(indexofRow).getId() + "", Toast.LENGTH_LONG).show();
            } // OK

    // --------------- DIALOG HELPER -----------------------------------------------------------------
        public void createDialog(int layout) {
            phongbandialog = new Dialog(PhongbanLayout.this);
            phongbandialog.setContentView(layout);
            phongbandialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            phongbandialog.show();
        } // OK
        public void setControlDialog() {
            backBtn = phongbandialog.findViewById(R.id.PB_backBtn);
            yesBtn = phongbandialog.findViewById(R.id.PB_yesInsertBtn);
            noBtn = phongbandialog.findViewById(R.id.PB_noInsertBtn);

            inputMaPB = phongbandialog.findViewById(R.id.PB_inputMaPB);
            inputTenPB = phongbandialog.findViewById(R.id.PB_inputTenPB);

            showMPBError = phongbandialog.findViewById(R.id.PB_showMPBError);
            showTPBError = phongbandialog.findViewById(R.id.PB_showTPBError);
            showResult = phongbandialog.findViewById(R.id.PB_showResult);
            showConfirm = phongbandialog.findViewById(R.id.PB_showConfirm);
            showLabel = phongbandialog.findViewById(R.id.PB_showLabel);
        } // OK
        public void setEventDialog(View view) {
            //  Toast.makeText( PhongbanLayout.this, (view.getId() == R.id.PB_editBtn)+"", Toast.LENGTH_LONG).show();
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phongbandialog.dismiss();
                }
            });
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phongbandialog.dismiss();
                }
            });
            // Dựa vào các nút mà thằng yesBtn sẽ có event khác
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean success = false;
                    switch (view.getId()) {
                        case R.id.PB_insertBtn: {
                            if (!isSafeDialog( false )) break;
                            PhongBan pb = new PhongBan(inputMaPB.getText().toString().trim() + "", inputTenPB.getText().toString().trim() + "");
                            if( !JSONHelper.verifyJSON(phongbanDB.doPost(pb,"insert"))
                                    .equalsIgnoreCase("pass") ) break;
                            TableRow tr = createRow(PhongbanLayout.this, pb);
                            int n = phongban_table_list.getChildCount();
                            tr.setId(n);
                            phongban_table_list.addView(tr);
                            setEventTableRows((TableRow) phongban_table_list.getChildAt(n), phongban_table_list);
                            success = true;
                            editBtn.setVisibility(View.INVISIBLE);
                            delBtn.setVisibility(View.INVISIBLE);
                            focusRow = null;
                            focusMaPB = null;
                            focusTenPB = null;
                        }
                        break;
                        case R.id.PB_editBtn: {
                            if (!isSafeDialog( true )) break;
                            TableRow tr = (TableRow) phongban_table_list.getChildAt(indexofRow);
                            TextView id = (TextView) tr.getChildAt(0);
                            TextView name = (TextView) tr.getChildAt(1);
                            PhongBan pb = new PhongBan(
                                    id.getText().toString().trim(),
                                    inputTenPB.getText().toString().trim()
                            );
                            if( !JSONHelper.verifyJSON(phongbanDB.doPost(pb,"update"))
                                    .equalsIgnoreCase("pass") ) break;
                            name.setText(inputTenPB.getText() + "");
                            success = true;
                        }
                        break;
                        case R.id.PB_delBtn: {
                            PhongBan pb = new PhongBan(
                                    focusMaPB.getText().toString().trim(),
                                    focusTenPB.getText().toString().trim()
                            );
                            if( !JSONHelper.verifyJSON(phongbanDB.doPost(pb,"remove"))
                                    .equalsIgnoreCase("pass") ) break;
                            if (indexofRow == phongban_table_list.getChildCount() - 1) {
                                phongban_table_list.removeViewAt(indexofRow);
                            } else {
                                phongban_table_list.removeViewAt(indexofRow);
                                for (int i = 0; i < phongban_table_list.getChildCount(); i++) {
                                    phongban_table_list.getChildAt(i).setId((int) i);
                                }
                            }
                            editBtn.setVisibility(View.INVISIBLE);
                            delBtn.setVisibility(View.INVISIBLE);
                            focusRow = null;
                            focusMaPB = null;
                            focusTenPB = null;
                            success = true;
                        }
                        break;
                        default:
                            break;
                    }
                    if (success) {
                        showResult.setText(showLabel.getText() + " thành công !");
                        showResult.setTextColor(getResources().getColor(R.color.yes_color));
                        showResult.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                inputMaPB.setText("");
                                inputTenPB.setText("");
                                showResult.setVisibility(View.INVISIBLE);
                                phongbandialog.dismiss();
                            }
                        }, 1000);
                    } else {
                        showResult.setTextColor(getResources().getColor(R.color.thoatbtn_bgcolor));
                        showResult.setText(showLabel.getText() + " thất bại !");
                        showResult.setVisibility(View.VISIBLE);
                    }
                }
            });


        } // OK
        public boolean isSafeDialog( boolean allowSameID ) {
            String mapb, tenpb;
            // Mã PB không được trùng với Mã PB khác và ko để trống
            mapb = inputMaPB.getText().toString().trim();
            boolean noError = true;
            if (mapb.equals("")) {
                showMPBError.setText("Mã PB không được trống ");
                showMPBError.setVisibility(View.VISIBLE);
                noError = false;
            }else{
                showMPBError.setVisibility(View.INVISIBLE);
                noError = true;
            }

            // Tên PB không được để trống và không trùng
            tenpb = inputTenPB.getText().toString().trim();
            if (tenpb.equals("")) {
                showTPBError.setText("Tên PB không được trống ");
                showTPBError.setVisibility(View.VISIBLE);
                noError = false;
            }else{
                showTPBError.setVisibility(View.INVISIBLE);
                if( noError == true ) noError = true;
            }

            if( noError ) {
                for (int i = 1; i < phongban_table_list.getChildCount(); i++) {
                    TableRow tr = (TableRow) phongban_table_list.getChildAt(i);
                    TextView mapb_data = (TextView) tr.getChildAt(0);
                    TextView tenpb_data = (TextView) tr.getChildAt(1);
                    if (!allowSameID)
                        if (mapb.equalsIgnoreCase(mapb_data.getText().toString())) {
                            showMPBError.setText("Mã PB không được trùng ");
                            showMPBError.setVisibility(View.VISIBLE);
                            return noError = false;
                        }
                    if (tenpb.equalsIgnoreCase(tenpb_data.getText().toString())
                            && !tenpb_data.getText().toString().equalsIgnoreCase(
                            focusTenPB.getText().toString().trim() )
                        ) {
                        showTPBError.setText("Tên PB không được trùng");
                        showTPBError.setVisibility(View.VISIBLE);
                        return noError = false;
                    }
                }
                showMPBError.setVisibility(View.INVISIBLE);
                showTPBError.setVisibility(View.INVISIBLE);
            }
            return noError;
        } // OK

    // --------------- CUSTOM HELPER --------------------------------------------------------------------
        public int DPtoPix(int dps) {
            return (int) (dps * scale + 0.5f);
        } // OK
        // This Custom Columns' Max Width : 80 / 300
        public TableRow createRow(Context context, PhongBan pb) {
            TableRow tr = new TableRow(context);
            //   Ma PB
            TextView maPB = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà maPB đạt tới max width thì nó sẽ tăng height cho bên tenPB luôn
            // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
            maPB.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            maPB.setMaxWidth(DPtoPix(80));
            maPB.setText(pb.getMapb().toUpperCase());

            //   Ten PB
            TextView tenPB = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà tenPB đạt tới max width thì nó sẽ tăng height cho bên maPB luôn
            tenPB.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            tenPB.setText(pb.getTenpb().toUpperCase());
            tenPB.setMaxWidth(DPtoPix(300));

            tr.setBackgroundColor(getResources().getColor(R.color.white));
            // Add 2 thứ vào row
            tr.addView(maPB);
            tr.addView(tenPB);

            return tr;
        } // OK
}