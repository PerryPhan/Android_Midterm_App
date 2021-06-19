package com.example.giuaki.Mainv2;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import androidx.appcompat.app.AppCompatActivity;


import com.example.giuaki.Adapter.VPPAdapter;
import com.example.giuaki.Api.CapPhat;
import com.example.giuaki.Api.ChiTietCungCap;
import com.example.giuaki.Api.NhanVien;
import com.example.giuaki.Api.PhieuCungCap;
import com.example.giuaki.Api.PhongBan;
import com.example.giuaki.Api.VanPhongPham;
import com.example.giuaki.Bell;
import com.example.giuaki.Entities.Rows;
import com.example.giuaki.Helper.JSONHelper;
import com.example.giuaki.R;
import com.example.giuaki.Request.CapPhatRequest;
import com.example.giuaki.Request.NhanVienRequest;
import com.example.giuaki.Request.PhongBanRequest;
import com.example.giuaki.Request.VanPhongPhamRequest;
import com.example.giuaki.WebService;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PropertyPermission;

public class CapPhatCRUDLayout extends AppCompatActivity {
    // Main Layout
    TableLayout table;
    Button exit,
           insertBtn,
          previewBtn;
    Spinner NVSpinner,
            PBSpinner;
    // Navigation
    ImageView bell;

    // Dialog
    Dialog dialog;
        Button backBtn,
               yesBtn,
                noBtn;

        EditText inputSP;
        Spinner PBSpinnerMini,
                NVSpinnerMini,
                VPPSpinnerMini;
        List<VanPhongPham> VPPSpinnerMiniList;
        List<String> PBSpinnerMiniList;
        List<String> NVSpinnerMiniList;
        String PBSpinnerMiniData;
        String NVSpinnerMiniData;
        VanPhongPham VPPSpinnerMiniData;
        
        DatePicker inputNC;
            String inputNC_data;
        EditText inputSL;

        TextView showSPError;
        TextView showNCError;
        TextView showSLError;

        TextView showResult;
        TextView showConfirm;
        TextView showLabel;

    Spinner VPPSpinner;

    // ListAll
    List<CapPhat> capphat_list;
    List<NhanVien> nhanvien_list;
    List<VanPhongPham> vanphongpham_list;
    List<PhongBan> phongban_list;

    // SpinnerList
    List<String> PBSpinnerList;
    List<String> maPBSpinnerList;
    List<String> NVSpinnerList;

    // DisplayTable
    List<CapPhat> displayCapphatList;

    // Database
    PhongBanRequest phongbanDB;
    NhanVienRequest nhanvienDB;
    VanPhongPhamRequest vanphongphamDB;
    CapPhatRequest capPhatDatabase;

    //String
    String PBSpinnerData;
    String maPBSpinnerData;
    String NVSpinnerData;

    //Focus
    int indexofRow = 0;
    TableRow focusRow = null;
    VanPhongPham focusVPP = null ;

    //Scale
    float scale;
    String webPath = "http://"+ WebService.host()+"/ImageController-get?hinh=";
    Bell b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capphat_vpp_crud);
        scale = this.getResources().getDisplayMetrics().density;
        setControl();
        loadDatabase();
        setEvent();
        setNavigation();
    }

    private void setControl() {
        // Button
        exit = findViewById(R.id.CPcrud_backBtn);
        insertBtn = findViewById(R.id.CPcrud_insertBtn);
        previewBtn = findViewById(R.id.CPcrud_previewBtn);
        // Navigation
        // Spinner
        PBSpinner = findViewById(R.id.CPcrud_PBSpinner);
        NVSpinner = findViewById(R.id.CPcrud_NVSpinner);
        // Table
        table    = findViewById(R.id.CPcrud_table_list);
        // Notification
        bell = findViewById(R.id.CPcrud_bell);
        b = new Bell(this, bell);
    }
    public void loadDatabase(){
        // Khai báo
        phongbanDB = new PhongBanRequest();
        nhanvienDB = new NhanVienRequest();
        vanphongphamDB = new VanPhongPhamRequest();
        capPhatDatabase = new CapPhatRequest();
        // Load Database ra các List
        capphat_list = convertToCapPhatList(
                returnListfromJSON( capPhatDatabase.doGet("show"),"CapPhat")
        );
        nhanvien_list = convertToNhanvienList(
                returnListfromJSON( nhanvienDB.doGet("show"),"NhanVien")
        );
        vanphongpham_list = convertToVanPhongPhamList(
                returnListfromJSON( vanphongphamDB.doGet("show"),"VanPhongPham")
        );
        phongban_list = convertToPhongBanList(
                returnListfromJSON( phongbanDB.doGet("show"),"PhongBan")
        );
    }
    private void setEvent() {
        // set Event for Button
        setEventButton();
        // set Data + Event for Spinner
        setSpinner();
//        setEventTableList();
    }

    private void setNavigation() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // ---------------- SUPPORT -------------------------------
        public String getMapb(String text){
            for(PhongBan pb: phongban_list){
                if(text.equalsIgnoreCase(pb.getTenpb())){
                    return pb.getMapb();
                }
            }
            return "";
        }
        public VanPhongPham getVPPfromList( String mavpp ){
            if( mavpp == null || mavpp.trim().equalsIgnoreCase("") ) return null;
            if( vanphongpham_list == null || vanphongpham_list.size() == 0 ) return  null;
            for( VanPhongPham vpp : vanphongpham_list){
                if( vpp.getMaVpp().equalsIgnoreCase(mavpp)){
                    return vpp;
                }
            }
            return null;
        }
        private List<String> getListNVwith(String data, boolean all) {
            if(data == null || data.trim().equalsIgnoreCase("")) return null;
            String maPB = data;
            List<String> NVList = new ArrayList<>();
            if(all) {
                Log.d("data","Return all List Nhan Vien");
                NVList.add("Tất cả nhân viên");
                if( maPB.trim().equalsIgnoreCase("Tất cả phòng ban"))
                    maPB = data;
                else
                    maPB= getMapb(data);
            }
            if(maPB.trim().equalsIgnoreCase("Tất cả phòng ban")){
                for( NhanVien nv : nhanvien_list ){
                    NVList.add(nv.toSpinnerString());
                }
            }else {
                // Lấy List Nhân viên với mã phòng ban cho trước
                Log.d("data","Return List Nhan Vien with PB : "+maPB);
                for( NhanVien nv : nhanvien_list ){
                    if( nv.getMaPb().trim().equalsIgnoreCase( maPB ) ) {
                        Log.d("data","List Nhan Vien add : "+nv.getMaNv());
                        NVList.add(nv.toSpinnerString());
                    }
                }
            }
            return NVList;
        }
        public void setDataImageView(ImageView imageView, String imageUri){
            if( imageUri == null || imageUri == "null" || imageUri.equalsIgnoreCase("")) return;
            Picasso.get().load(webPath+imageUri).into(imageView);
        }

    // ========================================== TABLE  ====================================
        public void renderTable(){
            if( capphat_list == null || capphat_list.size() == 0) return;
            displayCapphatList = new ArrayList<>();
            if(table.getChildCount()>1) table.removeViews(1,table.getChildCount()-1);
            for( int i = 0; i < capphat_list.size(); i++){
                CapPhat cp = capphat_list.get(i);
                displayCapphatList.add(cp);
                TableRow tr = createRow(CapPhatCRUDLayout.this,cp);
                tr.setId( i+1 );
                table.addView( tr );
            }
            setEventTableList(table);
        }
        public void renderTableWithPB(String maPB){
            if( maPB == null || maPB.trim().equalsIgnoreCase("")) return;
            if( table.getChildCount()>1 ) table.removeViews(1,table.getChildCount()-1);
            if( maPB.equalsIgnoreCase("Tất cả phòng ban")){
                return; // Vì nó đã gọi setNVSpinner phía ngoài
            }
            else {
                displayCapphatList = new ArrayList<>();
                for( NhanVien nv : nhanvien_list)
                    if( nv.getMaPb().trim().equalsIgnoreCase(maPB) )
                        renderTableWithNV( nv.getMaNv() , true );
                setEventTableList(table);
            }
            return;
        }
        public void renderTableWithNV(String maNV, boolean appendMode){
            if( maNV == null || maNV.trim().equalsIgnoreCase("")) return;
            if( !appendMode ) {
                displayCapphatList = new ArrayList<>();
                if (table.getChildCount() > 1) table.removeViews(1, table.getChildCount() - 1);
            }// PB = i, NV = i
            for( int i = 0; i < capphat_list.size(); i++){
                CapPhat cp = capphat_list.get(i);
                if(cp.getMaNv().equalsIgnoreCase( maNV ) ){
                    TableRow tr = createRow(CapPhatCRUDLayout.this,cp);
                    tr.setId( i+1 );
                    displayCapphatList.add(cp);
                    table.addView( tr );
                }
            }
        }

        private void setEventTableList( TableLayout list) {
            if( list == null || list.getChildCount() == 1 ) return;
            for ( int i = 0 ; i < list.getChildCount(); i++){
                list.getChildAt(i).setId(i);
                setEventTableRow((TableRow) list.getChildAt(i), list );
            }
        }
        private void setEventTableRow(TableRow tr , TableLayout list) {
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                    indexofRow = (int) v.getId();
                    Log.d("row",indexofRow+"");
                    focusRow = (TableRow) list.getChildAt(indexofRow);
                    Log.d("row",focusRow.getId()+"");
                    TextView tv = (TextView) focusRow.getChildAt(2);
                    focusVPP = getVPPfromList( tv.getText().toString() );
                    setNormalBGTableRows(list);
                    previewBtn.setVisibility(View.VISIBLE);
                }
            });
        }
        public void setNormalBGTableRows(TableLayout list) {
            // 0: là thằng example đã INVISIBLE
            // Nên bắt đầu từ 1 -> 9
            Log.d("clearBG","Doit");
            for (int i = 1; i < list.getChildCount(); i++) {
                TableRow row = (TableRow) list.getChildAt((int) i);
                if (indexofRow != (int) row.getId())
                    row.setBackgroundColor(getResources().getColor(R.color.white));
            }
          }

    // ========================================== BUTTON ====================================
        private void setEventButton() {
            previewBtn.setVisibility(View.INVISIBLE);
            previewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = null;
                    // Image from Database is handled to load here
                    createDialog(R.layout.popup_vpp_previewimage);

                    // Custom set Control
                    tv = dialog.findViewById(R.id.VPP_IP_maVPP);
                    tv.setText( (String) focusVPP.getMaVpp());
                    tv = dialog.findViewById(R.id.VPP_IP_tenVPP);
                    tv.setText(focusVPP.getTenVpp());
                    tv = dialog.findViewById(R.id.VPP_IP_DVT);
                    tv.setText(focusVPP.getDvt());
                    tv = dialog.findViewById(R.id.VPP_IP_Gia);
                    tv.setText(focusVPP.getGiaNhap());
                    tv = dialog.findViewById(R.id.VPP_IP_Soluong);
                    tv.setText(focusVPP.getSoLuong());
                    tv = dialog.findViewById(R.id.VPP_IP_nhaCC);
                    tv.setText(focusVPP.getMaNcc());
                    // Load Data
                    setDataImageView(
                            dialog.findViewById(R.id.VPP_IP_Hinh)
                            ,focusVPP.getHinh() );
                }
            });
            insertBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog( R.layout.popup_capphat_crud);
                    setControlDialog();
                    settingDialog();
                    setEventDialog(v);
                }
            });

        }

    // ========================================== DIALOG ====================================
        public void createDialog(int layout) {
            dialog = new Dialog(CapPhatCRUDLayout.this);
            dialog.setContentView(layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        public void setControlDialog() {
            backBtn = dialog.findViewById(R.id.CPcrud_backBtn);
            yesBtn = dialog.findViewById(R.id.CPcrud_yesInsertBtn);
            noBtn = dialog.findViewById(R.id.CPcrud_noInsertBtn);

            PBSpinnerMini = dialog.findViewById(R.id.CPcrud_PBSpinner_mini);
            NVSpinnerMini = dialog.findViewById(R.id.CPcrud_NVSpinner_mini);
            VPPSpinnerMini = dialog.findViewById(R.id.CPcrud_VPPSpinner_mini);

            inputSP = dialog.findViewById(R.id.CPcrud_inputSP);
            inputNC = dialog.findViewById(R.id.CPcrud_inputNC);
            inputSL = dialog.findViewById(R.id.CPcrud_inputSL);

            showSPError = dialog.findViewById(R.id.CPcrud_showSPError);
            showNCError = dialog.findViewById(R.id.CPcrud_showNCError);
            showSLError = dialog.findViewById(R.id.CPcrud_showSLError);

            showResult = dialog.findViewById(R.id.CPcrud_showResult);
            showConfirm = dialog.findViewById(R.id.CPcrud_showConfirm);
            showLabel = dialog.findViewById(R.id.CPcrud_showLabel);
        }
        private void settingDialog() {
            // Setup Spinner Mini
            setSpinnerMini();
            setEventDatePicker(getDate());
            showLabel.setText("Thêm phiếu cấp phát");
            showConfirm.setText("Khi cấp phát, sẽ không hoàn tác được.");
        }
            public String getDate(){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                return formatter.format(date);
            }
            public void setEventDatePicker( String init ){
                inputNC_data = init;
                int[] date = StringtoIntDateReverse( inputNC_data );
                inputNC.init(date[0],date[1]-1,date[2],new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        inputNC_data = formatDate(InttoStringDate(dayOfMonth,monthOfYear+1,year), true);
                   }
                });
            }
        public void setEventDialog(View view) {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean success = false;
                    switch (view.getId()) {
                        case R.id.CPcrud_insertBtn: {
                            if (!isSafeDialog()) break;
                            CapPhat cp = new CapPhat(
                                    inputSP.getText().toString() + "",
                                    inputNC_data + "",
                                    VPPSpinnerMiniData.getMaVpp() + "",
                                    NVSpinnerMiniData.split(",")[0].trim() + "",
                                    Integer.parseInt(inputSL.getText().toString())+""
                            );
                            insert(cp);
                            success = true;
                        }
                        break;
                    }
                    if (success) {
                        showResult.setText(showLabel.getText() + " thành công !");
                        showResult.setTextColor(getResources().getColor(R.color.yes_color));
                        showResult.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showResult.setVisibility(View.INVISIBLE);
                                dialog.dismiss();
                            }
                        }, 1000);
                    } else {
                        showResult.setTextColor(getResources().getColor(R.color.thoatbtn_bgcolor));
                        showResult.setText(showLabel.getText() + " thất bại !");
                        showResult.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
            private void insert(CapPhat cp) {
                // API
                capPhatDatabase.doPost( cp,"insert" );

                // List
                capphat_list.add(cp);
                TableRow tr = createRow(this, cp);
                if( maPBSpinnerData.trim().equalsIgnoreCase( NVSpinnerMiniData.split(",")[3].trim())
                        || maPBSpinnerData.trim().equalsIgnoreCase("Tất cả phòng ban"))
                if( NVSpinnerData.split(",")[0].trim().equalsIgnoreCase(cp.getMaNv()) || NVSpinnerData.equals("Tất cả nhân viên") ){
                    displayCapphatList.add(cp);
                    tr.setId( table.getChildCount() );
                    table.addView(tr);
                }
                // Trừ VPP số lượng
                for( VanPhongPham vpp : vanphongpham_list){
                    if( vpp.getMaVpp().equalsIgnoreCase(cp.getMaVpp()) ){
                        vpp.setSoLuong(
                                Integer.parseInt(vpp.getSoLuong())
                                - Integer.parseInt(cp.getSoLuong()) +""
                        );
                        break;
                    }
                }
                setEventTableList(table);
            }
            public boolean isSafeDialog( ) {
                String sp = inputSP.getText().toString().trim(),
                       nc = inputNC_data,
                       sl = inputSL.getText().toString().trim();
                boolean noError = true;
                if( sp.trim().equalsIgnoreCase("") ){
                    showSPError.setVisibility(View.VISIBLE);
                    showSPError.setText("Số phiếu không được trống");
                    noError = false;
                }else{
                    showSPError.setVisibility(View.INVISIBLE);
                    noError = true;
                }
                if( !nc.trim().equalsIgnoreCase(getDate()+"") ){
                    showNCError.setVisibility(View.VISIBLE);
                    showNCError.setText("Ngày cấp không hợp lệ");
                    noError = false;
                }else{
                    showNCError.setVisibility(View.INVISIBLE);
                    if(noError == true) noError = true;
                }
                if( sl.equalsIgnoreCase("") ){
                    showSLError.setVisibility(View.VISIBLE);
                    showSLError.setText("Số lượng không được trống");
                    noError = false;
                }else{
                    sl = Integer.parseInt(sl)+"";
                    if (sl.equalsIgnoreCase("0")) {
                        showSLError.setText("Số lượng không được bằng 0 ");
                        showSLError.setVisibility(View.VISIBLE);
                        noError = false;
                    }else if( Integer.parseInt(sl) > Integer.parseInt(VPPSpinnerMiniData.getSoLuong()) ){
                        showSLError.setText("Số lượng không được vượt quá "+VPPSpinnerMiniData.getSoLuong());
                        showSLError.setVisibility(View.VISIBLE);
                        noError = false;
                    }else{
                        showSLError.setVisibility(View.INVISIBLE);
                        if(noError == true) noError = true;
                    }
                }
                if( noError == true ){
                    for( CapPhat cp : capphat_list ){
                        if( cp.getSoPhieu().equalsIgnoreCase(sp+"") ){
                            showSPError.setText("Số phiếu không được trùng ");
                            showSPError.setVisibility(View.VISIBLE);
                            noError = false;
                            return noError;
                        }
                    }
                    showSPError.setVisibility(View.INVISIBLE);
                    showNCError.setVisibility(View.INVISIBLE);
                    showSLError.setVisibility(View.INVISIBLE);
                }
                return noError;
            }
    // ========================================= SET SPINNER ==========================================
        private void setSpinner() {
            setPBSpinner( );
        }
        private void setSpinnerMini(){
            setPBSpinnerMini();
            setVPPSpinnerMini();
        }
    // ========================================== SPINNER ====================================
    // @ setPBSpinner  ---------------------------------------------
        private void setPBSpinner() {
            // Data
            PBSpinner.setAdapter( loadPBSpinner() );
            // Event
            setEventPBSpinner();
        }
        private void setPBSpinnerMini(){
            // Data
            PBSpinnerMini.setAdapter( loadPBSpinnerMini() );
            // Event
            setEventPBSpinnerMini();
        }
        public ArrayAdapter<String> loadPBSpinner(){
            // 1. Tạo list Phong ban // 2. Đổ Phong_ban.getTenPB() ra 1 List // 3. setAdapter cho cái list getTenPB() đó
            PBSpinnerList = new ArrayList<>();
            maPBSpinnerList = new ArrayList<>();
            PBSpinnerList.add("Tất cả phòng ban");
            maPBSpinnerList.add("Tất cả phòng ban");

            // Phục vụ cho việc xổ ra Option cho Spinner
            for ( PhongBan pb : phongban_list){
                maPBSpinnerList.add(pb.getMapb());
                PBSpinnerList.add(pb.getTenpb());
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item, PBSpinnerList
                    );
            return adapter;
        }
        public ArrayAdapter<String> loadPBSpinnerMini(){
            // 1. Tạo list Phong ban // 2. Đổ Phong_ban.getTenPB() ra 1 List // 3. setAdapter cho cái list getTenPB() đó
            PBSpinnerMiniList = new ArrayList<>();
            // Phục vụ cho việc xổ ra Option cho Spinner
            for ( PhongBan pb : phongban_list){
                PBSpinnerMiniList.add(pb.getMapb()+" , "+pb.getTenpb());
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item, PBSpinnerMiniList
                    );
            return adapter;
        }
        public void setEventPBSpinner(){
            // Loop
            PBSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PBSpinnerData = PBSpinnerList.get(position);
                    maPBSpinnerData = maPBSpinnerList.get(position);
                    // PB = all, PB = i

                    NVSpinnerList = getListNVwith( PBSpinnerData, true );
                    setNVSpinner();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        public void setEventPBSpinnerMini(){
            // Loop
            PBSpinnerMini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PBSpinnerMiniData = PBSpinnerMiniList.get(position).split(",")[0].trim();
                    // PB = all, PB = i
                    NVSpinnerMiniList = getListNVwith( PBSpinnerMiniData, false );
                    Log.d("data",NVSpinnerMiniList.size()+"");
                    setNVSpinnerMini();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

    // @ setNVSpinner   ---------------------------------------------
        private void setNVSpinner() {
            // Data
            NVSpinner.setAdapter( loadNVSpinner() );
            // Event
            setEventNVSpinner();
        }
        private void setNVSpinnerMini() {
            // Data
            NVSpinnerMini.setAdapter( loadNVSpinnerMini() );
            // Event
            setEventNVSpinnerMini();
        }
        public ArrayAdapter<String> loadNVSpinner(){
            // 1. Tạo list Phong ban // 2. Đổ Phong_ban.getTenPB() ra 1 List // 3. setAdapter cho cái list getTenPB() đó
            if( NVSpinnerList == null || NVSpinnerList.size() == 0 ) {
                NVSpinnerList = new ArrayList<>();
                NVSpinnerList.add("Tất cả nhân viên");
                // Phục vụ cho việc xổ ra Option cho Spinner
                for (NhanVien nv : nhanvien_list) {
                    NVSpinnerList.add(nv.toSpinnerString());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, NVSpinnerList);
            return adapter;
        }
        public ArrayAdapter<String> loadNVSpinnerMini(){
            // 1. Tạo list Phong ban // 2. Đổ Phong_ban.getTenPB() ra 1 List // 3. setAdapter cho cái list getTenPB() đó
            if( NVSpinnerMiniList == null || NVSpinnerMiniList.size() == 0 ) {
                NVSpinnerMiniList = new ArrayList<>();
                NVSpinnerMiniList.add( new NhanVien(
                        "empty",
                        "Không có nhân viên",
                        "",
                        ""
                ).toSpinnerString() );
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    NVSpinnerMiniList);
            return adapter;
        }
        public void setEventNVSpinner(){
            if( NVSpinnerList == null ) return;
            NVSpinnerData = NVSpinnerList.get(0);
            NVSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NVSpinnerData = NVSpinnerList.get(position);
                if (position == 0 ) {
                    // all : all
                    if( PBSpinnerData.trim().equalsIgnoreCase("Tất cả phòng ban") )
                        renderTable();
                    // all : index
                        else {
                            renderTableWithPB(getMapb(PBSpinnerData));
                        }
                    // index : index
                    }else{
                        renderTableWithNV( NVSpinnerData.split(",")[0], false );
                        setEventTableList(table);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
        public void setEventNVSpinnerMini(){
            if( NVSpinnerMiniList == null ) return;
            NVSpinnerMiniData = NVSpinnerMiniList.get(0);
            NVSpinnerMini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    NVSpinnerMiniData = NVSpinnerMiniList.get(position);
//                    Toast.makeText(CapPhatCRUDLayout.this, NVSpinnerMiniData, Toast.LENGTH_LONG).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
    }

    // @ setVPPSpinner  ---------------------------------------------
        private void setVPPSpinnerMini() {
            VPPSpinnerMini.setAdapter(loadVPPSpinnerMini());
            setEventVPPSpinnerMini();
        }
        public VPPAdapter loadVPPSpinnerMini(){
            VanPhongPham VPPClass = new VanPhongPham();
            VPPSpinnerMiniList = new ArrayList<>();
            for ( VanPhongPham vpp : vanphongpham_list){
                if( Integer.parseInt(vpp.getSoLuong()) > 0 )
                    VPPSpinnerMiniList.add(vpp);
            }
            return new VPPAdapter(this,VPPSpinnerMiniList);
        }
        public void setEventVPPSpinnerMini() {
            if (VPPSpinnerMiniList == null) return;
            VPPSpinnerMiniData = VPPSpinnerMiniList.get(0);
            VPPSpinnerMini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // all : returns
                    VPPSpinnerMiniData = VPPSpinnerMiniList.get(position);
//                    Toast.makeText(CapPhatCRUDLayout.this, VPPSpinnerMiniData.getMaVpp(), Toast.LENGTH_LONG);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

    // ============================= CONVERT ===============================
        public List<NhanVien> convertToNhanvienList(List<Object> list ){
            if( list == null ) return null;
            List<com.example.giuaki.Api.NhanVien> nhanvienlist = new ArrayList<>();
            for( Object li : list ){
                nhanvienlist.add( (com.example.giuaki.Api.NhanVien) li);
            }
            return nhanvienlist;
        }
        public List<VanPhongPham> convertToVanPhongPhamList(List<Object> list ){
            if( list == null ) return null;
            List<VanPhongPham> vanphongphamlist = new ArrayList<>();
            for( Object li : list ){
                vanphongphamlist.add( (VanPhongPham) li);
            }
            return vanphongphamlist;
        }
        public List<PhongBan> convertToPhongBanList(List<Object> list ){
            if( list == null ) return null;
            List<PhongBan> phongbanlist = new ArrayList<>();
            for( Object li : list ){
                phongbanlist.add( (PhongBan) li);
            }
            return phongbanlist;
        }
        public List<CapPhat> convertToCapPhatList(List<Object> list ){
        if( list == null ) return null;
        List<CapPhat> capphatlist = new ArrayList<>();
        for( Object li : list ){
            capphatlist.add( (CapPhat) li);
        }
        return capphatlist;
    }

    // ======================= RETURN FROM JSON =======================
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
    }

    // ======================= CUSTOM HELPER ===========================
        public int DPtoPix(int dps) {
            return (int) (dps * scale + 0.5f);
        }
        // This Custom Columns' Max Width : 80 p0 / 120 / 85 p0 / <= 100 p0
        public TableRow createRow(Context context, CapPhat cp) {
            TableRow tr = new TableRow(context);
            //  SoPhieuview
            TextView SoPhieuview = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà SoPhieuview đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
            // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
            SoPhieuview.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            SoPhieuview.setMaxWidth(DPtoPix(70));
            SoPhieuview.setPadding(0,0,0,0);
            SoPhieuview.setText(cp.getSoPhieu());

            TextView NgayCapview = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà NgayGiaoview đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
            // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
            NgayCapview.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            NgayCapview.setMaxWidth(DPtoPix(125));
            SoPhieuview.setPadding(0,0,0,0);
            NgayCapview.setText(formatDate(cp.getNgayCap(), false));
    //        NgayCapview.setText(formatDate("2021-06-02",false));

            TextView MaVPPview = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà NhaCungCapview đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
            // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
            MaVPPview.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            MaVPPview.setMaxWidth(DPtoPix(75));
            SoPhieuview.setPadding(0,0,0,0);
            MaVPPview.setText(cp.getMaVpp());

            TextView MaNVview = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà NhaCungCapview đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
            // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
            MaNVview.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            MaNVview.setMaxWidth(DPtoPix(65));
            MaNVview.setPadding(0,0,0,0);
            MaNVview.setText(cp.getMaNv());

              TextView SoLuongview = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà NhaCungCapview đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
            // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
            SoLuongview.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            SoLuongview.setMaxWidth(DPtoPix(55));
            SoLuongview.setPadding(0,0,0,0);
            SoLuongview.setText(cp.getSoLuong());

            int color = R.color.white;
            tr.addView(SoPhieuview);
            tr.addView(NgayCapview);
            tr.addView(MaVPPview);
            tr.addView(MaNVview);
            tr.addView(SoLuongview);
            return tr;
        }
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
    }
        public int[] StringtoIntDateReverse(String str) {
            int[] date = new int[3];
            String[] arr = str.split("-");
            date[0] = Integer.parseInt(arr[0]);
            date[1] = Integer.parseInt(arr[1]);
            date[2] = Integer.parseInt(arr[2]);
            return date; // 1999-08-30 -> [1999,08,30]
        }
        public String InttoStringDate(int date_day, int date_month, int date_year) {
            String day = (date_day < 10) ? "0" + date_day + "" : date_day + "";
            String month = (date_month < 10) ? "0" + date_month + "" : date_month + "";
            String year = date_year + "";
            return day + "/" + month + "/" + year; // [30,08,1999] -> 30/08/1999
        }
}
