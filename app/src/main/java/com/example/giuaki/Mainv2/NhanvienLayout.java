package com.example.giuaki.Mainv2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.giuaki.Api.*;
import com.example.giuaki.Bell;
import com.example.giuaki.Checker;
import com.example.giuaki.Mainv2.*;
import com.example.giuaki.Helper.JSONHelper;
import com.example.giuaki.R;
import com.example.giuaki.Request.CapPhatRequest;
import com.example.giuaki.Request.NhanVienRequest;
import com.example.giuaki.Request.PhongBanRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NhanvienLayout extends AppCompatActivity {
    // Main Layout
    TableLayout nhanvien_table_list;

    Spinner PB_spinner;
    String PB_spinner_maPB;

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
    Dialog nhanviendialog;
        Button backBtn;
        Button yesBtn;
        Button noBtn;

        Spinner PB_spinner_mini;

        EditText inputMaNV;
        EditText inputTenNV;

        DatePicker datepickerNSNV;

        String PB_spinner_mini_maPB;
        String strDate;

        TextView showMNVError;
        TextView showTNVError;
        TextView showResult;
        TextView showConfirm;
        TextView showLabel;

    // Database Controller
    NhanVienRequest nhanvienDB;
    PhongBanRequest phongbanDB;
    CapPhatRequest capphatDB;

    List<PhongBan> phongbanlist;
    List<NhanVien> nhanvienlist;

    // Focus
    int indexofRow = -1;
    TableRow focusRow;
    TextView focusMaNV;
    TextView focusTenNV;
    TextView focusNSNV;
    String focusPBNV;

    // Other
    float scale;
    int layout = R.layout.activity_nhanvien_layout;
    int dialogLayout = R.layout.popup_nhanvien;
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        setControl();
        loadDatabase();
        setEvent();
        setNavigation();
    }

    // --------------- MAIN HELPER -----------------------------------------------------------------
        public void setControl() {
            nhanvien_table_list = findViewById(R.id.NV_table_list);
            PB_spinner = findViewById(R.id.NV_PBSpinner);
            insertBtn = findViewById(R.id.NV_insertBtn);
            editBtn = findViewById(R.id.NV_editBtn);
            delBtn = findViewById(R.id.NV_delBtn);
            exitBtn = findViewById(R.id.NV_exitBtn);

            navPB = findViewById(R.id.NV_navbar_phongban);
            navNV = findViewById(R.id.NV_navbar_nhanvien);
            navVPP= findViewById(R.id.NV_navbar_VPP);
            navCP= findViewById(R.id.NV_navbar_capphat);

        } // OK
        public void loadDatabase() {
            // -------------- Nhan Vien -------------------
            nhanvienDB = new NhanVienRequest();
            TableRow tr = null;
            nhanvienlist = convertToNhanvienList(
                    returnListfromJSON(
                            nhanvienDB.doGet("show")
                            , "NhanVien"
                    )
            );
            if( nhanvienlist == null ){
                Toast.makeText(this,"Error: NhanVienList is null" ,Toast.LENGTH_LONG).show();
                return;
            }else if( nhanvienlist.size() == 0){
                Toast.makeText(this,"Hiện tại chưa có nhân viên nào" ,Toast.LENGTH_LONG).show();
            }else
            // Tag sẽ bắt đầu ở 1 vì phải cộng thêm thằng example đã có sẵn
            for (int i = 0; i < nhanvienlist.size(); i++) {
                Log.d("data",nhanvienlist.get(i).toString());
                tr = createRow(this, nhanvienlist.get(i));
                tr.setId((int) i + 1);
                nhanvien_table_list.addView(tr);
            }
            // -------------- Phong Ban -------------------
            phongbanDB = new PhongBanRequest();
            phongbanlist = convertToPhongBanList(
                    returnListfromJSON(
                            phongbanDB.doGet("show")
                            , "PhongBan"
                    )
            );
            if( phongbanlist == null || phongbanlist.size() == 0 ) return;
                ArrayList<String> PB_name = new ArrayList<>();
                PB_name.add("Tất cả phòng ban");
                for (PhongBan pb : phongbanlist) {
                    PB_name.add(pb.getTenpb());
                }
            if( PB_name == null || PB_name.size() == 0) return;
            PB_spinner.setAdapter(loadSpinnerAdapter(PB_name));
            // --------------- CapPhat ------------------
            capphatDB = new CapPhatRequest();
        } // OK
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setEvent() {
            editBtn.setVisibility(View.INVISIBLE); // turn on when click items
            delBtn.setVisibility(View.INVISIBLE);  // this too
            setEventTable(nhanvien_table_list);
            setEventSpinner();
        } // OK
        public void setNavigation(){
            // navNV onclick none
            // navPB
            navPB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(NhanvienLayout.this, PhongbanLayout.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    startActivity( intent );

                }
            });
            // navVPP
            navVPP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(NhanvienLayout.this, VanphongphamLayout.class);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    startActivity( intent );

                }

            });
            // navCP
            navCP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NhanvienLayout.this, CapphatVPPLayout.class);
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

    // --------------- CONVERTER -----------------------------------------------------------------
        public List<Object> returnListfromJSON( String resultfromQuery , String objectClass){
            List<Object> list = null ;
            String response = resultfromQuery;
            if( !JSONHelper.verifyJSON(response).equalsIgnoreCase("pass") ) {
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
        public List<NhanVien> convertToNhanvienList( List<Object> list ){
            if( list == null ) return null;
            List<NhanVien> nhanvienlist = new ArrayList<>();
            for( Object li : list ){
                nhanvienlist.add( (NhanVien) li);
            }
            return nhanvienlist;
        } // OK
        public List<PhongBan> convertToPhongBanList( List<Object> list ){
            if( list == null ) return null;
            List<PhongBan> phongbanlist = new ArrayList<>();
            for( Object li : list ){
                phongbanlist.add( (PhongBan) li);
            }
            return phongbanlist;
        } // OK

    // --------------- SPINNER  -----------------------------------------------------------------
        public void setEventSpinner() {
            if( PB_spinner == null ){
                Log.d("Error","PBSpinner is null ");
                return;
            }
            PB_spinner_maPB = "All";
            PB_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        if (nhanvien_table_list.getChildCount() < nhanvienlist.size() + 1) {
                            PB_spinner_maPB = "All";
                            // Nếu có sort trước đó làm cho số nhân viên nhỏ hơn số nhân viên tổng thì mới sort lại theo all
                            nhanvien_table_list.removeViews(1,nhanvien_table_list.getChildCount()-1);
                            TableRow tr = null;
                            // Tag sẽ bắt đầu ở 1 vì phải cộng thêm thằng example đã có sẵn
                            for (int i = 0; i < nhanvienlist.size(); i++) {
                                NhanVien nv = nhanvienlist.get(i);
                                tr = createRow(NhanvienLayout.this, nv);
                                tr.setId((int) i + 1);
                                nhanvien_table_list.addView(tr);
                                setEventTableRows(tr, nhanvien_table_list);
                            }
                        }
                    } else {
                        int dem = 1;
                        String mapb = phongbanlist.get(position - 1).getMapb();
                        PB_spinner_maPB = mapb;
                        // Select lại toàn bộ table
                        nhanvien_table_list.removeViews(1,nhanvien_table_list.getChildCount()-1);
                        TableRow tr = null;
                        for (int i = 0; i < nhanvienlist.size(); i++) {
                            NhanVien nv = nhanvienlist.get(i);
                            if (nv.getMaPb().trim().equals(mapb.trim())) {
                                tr = createRow(NhanvienLayout.this, nv);
                                tr.setId((int) dem++);
                                nhanvien_table_list.addView(tr);
                                setEventTableRows(tr, nhanvien_table_list);
                            }
                        }
                    }
                    editBtn.setVisibility(View.INVISIBLE); // turn on when click items
                    delBtn.setVisibility(View.INVISIBLE);  // this too
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    PB_spinner_maPB = "All";
                }
            });
        } // OK
        public ArrayAdapter<String> loadSpinnerAdapter(ArrayList<String> phongban) {
            if( phongban == null || phongban.size() == 0) return null;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, phongban);
            return adapter;
        } // OK

    // --------------- TABLE -----------------------------------------------------------------
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setEventTable(TableLayout list) {
            // Log.d("count", list.getChildCount()+""); // số table rows + 1
            // Không cần thay đổi vì đây chỉ mới set Event
            // Do có thêm 1 thằng example để làm gốc, nên số row thì luôn luôn phải + 1
            // Có example thì khi thêm row thì nó sẽ theo khuôn
            if(Checker.checkTableList(list, 334 ))
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
                    strDate = formatDate(InttoStringDate(30,8,1999), true);
                    setEventDialog(v);
                    // Right to Select spinner
                }
            }); // OK
            // Khi edit
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (indexofRow != -1) {
                        createDialog(dialogLayout);
                        // Control
                        setControlDialog();
                        showLabel.setText("Sửa thông tin nhân viên");
                        showConfirm.setText("Bạn có muốn sửa hàng này không?");
                        // Event
                        setEventDialog(v);
                        // Right to Select spinner
                        int index = 0;
                        for (int i = 0; i < phongbanlist.size(); i++) {
                            // Nếu thằng được focus có mã PB trùng với PB trong list thì break
                            if (phongbanlist.get(i).getMapb().trim().equals(focusPBNV.trim())) {
                                index = i;
                                break;
                            }
                        }
                        inputMaNV.setText(focusMaNV.getText());
                        inputMaNV.setEnabled(false);
                        inputTenNV.setText(focusTenNV.getText());
                        PB_spinner_mini.setSelection(index);
                        // Date
                        int[] date = StringtoIntDate(focusNSNV.getText().toString().trim());
                        datepickerNSNV.updateDate(date[2], date[1] -1, date[0]);
                    }
                }
            }); // OK
            // Khi delete, có 3 TH : nằm ở cuối hoặc nằm ở đầu hoặc chính giữa
            // Nằm ở cuối thì chỉ cần xóa cuối
            // Còn lại thì sau khi xóa xong thì phải cập nhật lại tag cho toàn bộ col
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (indexofRow != -1) {
                        if( capphatDB != null ){
                            List<Object> capPhatList = returnListfromJSON(
                                    capphatDB.doGet(String.format("show?manv=%s",focusMaNV.getText().toString().trim()))
                                    , "CapPhat");
                            if( capPhatList != null && capPhatList.size() > 0 ) {
//                                Log.d("data", capPhatList.size() + " ");
//                                Log.d("data", capPhatList.get(0) + " ");
                                Toast.makeText(NhanvienLayout.this
                                        ,"Không thể xóa nhân viên "
                                                +focusTenNV.getText().toString().trim()
                                                +" vì đã có trong danh sách Cấp phát"
                                        ,Toast.LENGTH_LONG).show();
//                                Log.d("data","MANV: "+focusMaNV.getText().toString().trim());
                                return;
                            }
                        }
                        createDialog(dialogLayout);
                        // Control
                        setControlDialog();
                        showLabel.setText("Xóa thông tin nhân viên");
                        showConfirm.setText("Bạn có muốn xóa hàng này không?");
                        // Event
                        setEventDialog(v);
                        int index = 0;
                        for (int i = 0; i < phongbanlist.size(); i++) {
                            // Nếu thằng được focus có mã PB trùng với PB trong list thì break
                            if (phongbanlist.get(i).getMapb().trim().equals(focusPBNV.trim())) {
                                index = i;
                                break;
                            }
                        }
                        // Spinner
                        PB_spinner_mini.setSelection(index);
                        // Input
                        inputMaNV.setText(focusMaNV.getText());
                        inputTenNV.setText(focusTenNV.getText());
                        // Date
                        int[] date = StringtoIntDate(focusNSNV.getText().toString().trim());
                        datepickerNSNV.updateDate(date[2], date[1] - 1, date[0]);
                        // Disable, just show data
                        PB_spinner_mini.setEnabled(false);
                        inputMaNV.setEnabled(false);
                        inputTenNV.setEnabled(false);
                        datepickerNSNV.setEnabled(false);
                    }
                }
            }); // OK
        } // OK
        // To set all rows to normal state, set focusRowid = -1
        public void setNormalBGTableRows(TableLayout list) {
            // 0: là thằng example đã INVISIBLE
            // Nên bắt đầu từ 1 -> 9
            for (int i = 1; i < list.getChildCount(); i++) {
                TableRow row = (TableRow) list.getChildAt((int) i);
                if (indexofRow != (int) row.getId())
                    row.setBackgroundColor(getResources().getColor(R.color.white));
            }
        } // OK
        public void setEventTableRows(TableRow tr, TableLayout list) {
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editBtn.setVisibility(View.VISIBLE);
                    delBtn.setVisibility(View.VISIBLE);
                    v.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                    indexofRow = (int) v.getId();
                    focusRow = (TableRow) list.getChildAt(indexofRow);
                    focusMaNV = (TextView) focusRow.getChildAt(0);
                    focusTenNV = (TextView) focusRow.getChildAt(1);
                    focusNSNV = (TextView) focusRow.getChildAt(2);
                    focusPBNV = focusRow.getTag().toString();
                    setNormalBGTableRows(list);
                }
            });
        } // OK

    // --------------- DIALOG HELPER -----------------------------------------------------------------
        public void createDialog(int layout) {
            nhanviendialog = new Dialog(NhanvienLayout.this);
            nhanviendialog.setContentView(layout);
            nhanviendialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            nhanviendialog.show();
        } // OK
        public void setEventSpinnerMini() {
            if( PB_spinner_mini != null )
            PB_spinner_mini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PB_spinner_mini_maPB = phongbanlist.get(position).getMapb();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    PB_spinner_mini_maPB = phongbanlist.get(0).getMapb();
                }
            });
        } // OK
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setEventDatePicker(){
            strDate = formatDate(InttoStringDate(30,8,1999), true);
            if( datepickerNSNV == null ) return;
            datepickerNSNV.init(1999,07,30,new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                     strDate = formatDate(InttoStringDate(dayOfMonth,monthOfYear+1,year), true);
                }

            });
        } // OK
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setControlDialog() {
            backBtn = nhanviendialog.findViewById(R.id.NV_backBtn);
            yesBtn = nhanviendialog.findViewById(R.id.NV_yesInsertBtn);
            noBtn = nhanviendialog.findViewById(R.id.NV_noInsertBtn);

            PB_spinner_mini = nhanviendialog.findViewById(R.id.NV_PBSpinner_mini);

            inputMaNV = nhanviendialog.findViewById(R.id.NV_inputMaNV);
            inputTenNV = nhanviendialog.findViewById(R.id.NV_inputTenNV);

            datepickerNSNV = nhanviendialog.findViewById(R.id.NV_inputNS);

            showMNVError = nhanviendialog.findViewById(R.id.NV_showMNVError);
            showTNVError = nhanviendialog.findViewById(R.id.NV_showTNVError);

            showResult = nhanviendialog.findViewById(R.id.NV_showResult);
            showConfirm = nhanviendialog.findViewById(R.id.NV_showConfirm);
            showLabel = nhanviendialog.findViewById(R.id.NV_showLabel);

            ArrayList<String> PB_name = new ArrayList<>();
            for (PhongBan pb : phongbanlist) {
                PB_name.add(pb.getTenpb());
            }
            PB_spinner_mini.setAdapter(loadSpinnerAdapter(PB_name));
            setEventSpinnerMini();
            setEventDatePicker();

        } // OK
        public void setEventDialog(View view) {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nhanviendialog.dismiss();
                }
            });
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nhanviendialog.dismiss();
                }
            });
            // Dựa vào các nút mà thằng yesBtn sẽ có event khác
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean success = false;
                    switch (view.getId()) {
                        case R.id.NV_insertBtn: {
                            if ( !isSafeDialog( false )) break;
                            NhanVien nv = new NhanVien(
                                    inputMaNV.getText().toString().trim() +""
                                    , inputTenNV.getText().toString().trim()+""
                                    , strDate
                                    , PB_spinner_mini_maPB.trim());
                            if( !JSONHelper.verifyJSON(nhanvienDB.doPost(nv,"insert")).equalsIgnoreCase("pass")) break;
                            TableRow tr = createRow(NhanvienLayout.this, nv);
                            int n = nhanvien_table_list.getChildCount();
                            tr.setId(n);
                            if ( !PB_spinner_mini_maPB.trim().equals(PB_spinner_maPB.trim()) ){
                                // Nếu thằng bên trong là phòng ban nhưng bên ngoài là tất cả phòng ban thì
                                if( PB_spinner_maPB.trim().equals("All") ){
                                    // cứ insert như bth
                                    nhanvien_table_list.addView(tr);
                                    setEventTableRows((TableRow) nhanvien_table_list.getChildAt(n), nhanvien_table_list);
                                }
                                // Nếu thằng bên trong là phòng ban nhưng bên ngoài là phòng ban khác thì khỏi thêm table
                            // Nếu trùng
                            }else{
                                nhanvien_table_list.addView(tr);
                                setEventTableRows((TableRow) nhanvien_table_list.getChildAt(n), nhanvien_table_list);
                            }
                            nhanvienlist.add(nv);
                            editBtn.setVisibility(View.INVISIBLE);
                            delBtn.setVisibility(View.INVISIBLE);
                            focusRow = null;
                            focusMaNV = null;
                            focusTenNV = null;
                            focusNSNV = null;
                            focusPBNV = "";
                            success = true;
                        }
                        break;
                        case R.id.NV_editBtn: {
                            if (!isSafeDialog( true )) break;
                            TableRow tr = (TableRow) nhanvien_table_list.getChildAt(indexofRow);
                            TextView id = (TextView) tr.getChildAt(0);
                            TextView name = (TextView) tr.getChildAt(1);
                            TextView date = (TextView) tr.getChildAt(2);
                            NhanVien nv = new NhanVien(
                                    id.getText().toString().trim()
                                    ,inputTenNV.getText().toString().trim()
                                    ,strDate
                                    ,PB_spinner_mini_maPB);
                            if( !JSONHelper.verifyJSON(nhanvienDB.doPost(nv,"update"))
                                    .equalsIgnoreCase("pass") ) break;
                            //   Cập nhật nhân viên list bằng cách lấy cái index ra và add vào cái index đó
                            int index = 0;
                            for( int i = 0; i< nhanvienlist.size(); i++){
                                if( nhanvienlist.get(i).getMaNv().equalsIgnoreCase( id.getText().toString().trim() ) )
                                { index = i; break;}
                            }
                            nhanvienlist.set( index, nv );
                            // Cho biết rằng nên sửa hay thay đổi PB
                            boolean edit = false, changePB = false;
                            if ( !PB_spinner_mini_maPB.trim().equals( PB_spinner_maPB.trim() )){
                                // Khi không cần biết thay đổi PB như thế nào nhưng bên ngoài là All thì cứ edit thôi
                                if( PB_spinner_maPB.trim().equals("All") )
                                    edit = true;
                                // Vậy trường hợp đang là Phòng giám đốc muốn thay Phòng kỹ thuật thì
                                else{
                                    changePB = true;
                                }
                            }else{
                                // Khi giữ nguyên phòng ban
                                edit = true;
                            }
                            if(edit) {
                                name.setText(nv.getHoTen() + "");
                                date.setText(formatDate(strDate,false));
                                tr.setTag(nv.getMaPb());
                            }
                            if(changePB){
                                if (indexofRow == nhanvien_table_list.getChildCount() - 1) {
                                    Log.d("changePB",indexofRow+"");
                                    nhanvien_table_list.removeViewAt(indexofRow);
                                } else {
                                    nhanvien_table_list.removeViewAt(indexofRow);
                                    for (int i = 0; i < nhanvien_table_list.getChildCount(); i++) {
                                        nhanvien_table_list.getChildAt(i).setId((int) i);
                                    }
                                }
                            }
                            success = true;
                        }
                        break;
                        case R.id.NV_delBtn: {
                            NhanVien nv =  new NhanVien(
                                    focusMaNV.getText().toString().trim(),
                                    focusTenNV.getText().toString().trim(),
                                    formatDate(focusNSNV.getText().toString().trim(), true),
                                    focusPBNV.trim());
                            if( !JSONHelper.verifyJSON(nhanvienDB.doPost(nv,"remove"))
                                    .equalsIgnoreCase("pass") ) break;
                            if (indexofRow == nhanvien_table_list.getChildCount() - 1) {
                                    nhanvien_table_list.removeViewAt(indexofRow);
                                } else {
                                    nhanvien_table_list.removeViewAt(indexofRow);
                                    for (int i = 0; i < nhanvien_table_list.getChildCount(); i++) {
                                        nhanvien_table_list.getChildAt(i).setId((int) i);
                                    }
                                }
                                int index = 0;
                            for( int i = 0; i< nhanvienlist.size(); i++){
                                if( nhanvienlist.get(i).getMaNv().equals(focusMaNV.getText().toString().trim()) ){
                                    index = i ; break;
                                }
                            }
                                nhanvienlist.remove( index );
                            editBtn.setVisibility(View.INVISIBLE);
                            delBtn.setVisibility(View.INVISIBLE);
                            focusRow = null;
                            focusMaNV = null;
                            focusTenNV = null;
                            focusNSNV = null;
                            focusPBNV = "";
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
                                inputMaNV.setText("");
                                inputTenNV.setText("");
                                showResult.setVisibility(View.INVISIBLE);
                                nhanviendialog.dismiss();
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
            String  manv, tennv ;
            // Mã PB không được trùng với Mã PB khác và ko để trống
            manv = inputMaNV.getText().toString().trim();
            boolean noError = true;
            if ( manv.equals("")) {
                showMNVError.setText("Mã NV không được trống ");
                showMNVError.setVisibility(View.VISIBLE);
                noError = false;
            }else{
                showMNVError.setVisibility(View.INVISIBLE);
                noError = true;
            }

            // Tên PB không được để trống và không trùng
            tennv = inputTenNV.getText().toString().trim();
            if (tennv.equals("")) {
                showTNVError.setText("Tên NV không được trống ");
                showTNVError.setVisibility(View.VISIBLE);
                noError = false;
            }else{
                showTNVError.setVisibility(View.INVISIBLE);
                if(noError == true) noError = true;
            }

            if( noError ) {
                for (int i = 1; i < nhanvien_table_list.getChildCount(); i++) {
                    TableRow tr = (TableRow) nhanvien_table_list.getChildAt(i);
                    TextView mapb_data = (TextView) tr.getChildAt(0);
                    TextView tenpb_data = (TextView) tr.getChildAt(1);

                    if (!allowSameID)
                        if (manv.equalsIgnoreCase(mapb_data.getText().toString())) {
                            showMNVError.setText("Mã NV không được trùng ");
                            showMNVError.setVisibility(View.VISIBLE);
                            return noError = false;
                        }
                }
                showMNVError.setVisibility(View.INVISIBLE);
                showTNVError.setVisibility(View.INVISIBLE);
            }
            return noError;
        } // OK

    // --------------- CUSTOM HELPER -----------------------------------------------------------------
        public int DPtoPix(int dps) {
            return (int) (dps * scale + 0.5f);
        } // OK
        public int[] StringtoIntDate(String str) {
            int[] date = new int[3];
            String[] arr = str.split("/");
            date[0] = Integer.parseInt(arr[0]);
            date[1] = Integer.parseInt(arr[1]);
            date[2] = Integer.parseInt(arr[2]);
            return date; // 30/08/1999 -> [30,08,1999]
        } // OK
        public String InttoStringDate(int date_day, int date_month, int date_year) {
            Log.d("day",date_day+"");
            String day = (date_day < 10) ? "0" + date_day + "" : date_day + "";
            String month = (date_month < 10) ? "0" + date_month + "" : date_month + "";
            String year = date_year + "";
            return day + "/" + month + "/" + year; // [30,08,1999] -> 30/08/1999
        } // OK
        public String formatDate(String str, boolean toSQL ){
            String[] date ;
            String result = "";
            if( toSQL ){
                date = str.split("/");
                result = date[2] +"-"+ date[1] +"-"+ date[0];
            }else{
                date = str.split("-");
                result = date[2] +"/"+ date[1] +"/"+ date[0];
            }

            return result;
        } // OK
        // This Custom Columns' Max Width : 65 p0 / 220 / <= 100 p0
        public TableRow createRow(Context context, NhanVien nv) {
            TableRow tr = new TableRow(context);

            //   Mã NV
            TextView maNV = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

            maNV.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            maNV.setMaxWidth(DPtoPix(65));
            maNV.setPadding(0, 0, 0, 0);
            maNV.setText(nv.getMaNv().toUpperCase());

            //   Tên NV
            TextView tenNV = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

            tenNV.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            tenNV.setMaxWidth(DPtoPix(220));
            tenNV.setText(nv.getHoTen().toUpperCase());

            //  Ngày sinh
            TextView ngaysinhNV = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

            ngaysinhNV.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            ngaysinhNV.setPadding(0, 0, 0, 0);
            ngaysinhNV.setMaxWidth(DPtoPix(100));
            ngaysinhNV.setText(formatDate(nv.getNgaySinh(),false));

            tr.setBackgroundColor(getResources().getColor(R.color.white));
            //  Mã PB
            tr.setTag(nv.getMaPb()+"");
            tr.addView(maNV);
            tr.addView(tenNV);
            tr.addView(ngaysinhNV);

            return tr;
        } // OK
}