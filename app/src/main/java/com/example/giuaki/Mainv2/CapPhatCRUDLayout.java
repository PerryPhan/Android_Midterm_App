package com.example.giuaki.Mainv2;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.giuaki.Adapter.VPPAdapter;
import com.example.giuaki.Api.CapPhat;
import com.example.giuaki.Api.NhanVien;
import com.example.giuaki.Api.PhieuCungCap;
import com.example.giuaki.Api.PhongBan;
import com.example.giuaki.Api.VanPhongPham;
import com.example.giuaki.Entities.Rows;
import com.example.giuaki.Helper.JSONHelper;
import com.example.giuaki.R;
import com.example.giuaki.Request.CapPhatRequest;
import com.example.giuaki.Request.NhanVienRequest;
import com.example.giuaki.Request.PhongBanRequest;
import com.example.giuaki.Request.VanPhongPhamRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyPermission;

public class CapPhatCRUDLayout extends AppCompatActivity {
    // btn Back
    Button CP_backBtn;
    
    // Object

    // Khai Bao Bien Spinner
    Spinner NVSpinner;
    Spinner PBSpinner;
    Spinner VPPSpinner;
    
    // Khai bao List
    List<CapPhat> capphat_list;
    List<NhanVien> nhanvien_list;
    List<VanPhongPham> vanphongpham_list;
    List<PhongBan> phongban_list;
    
    // Spinner 
    List<VanPhongPham> VPPSpinnerList;
    List<String> PBSpinnerList;
    List<String> NVSpinnerList;

    //List String
    ArrayList<String> nhanvienStringList;

    //Data
    PhongBanRequest phongbanDB;
    NhanVienRequest nhanvienDB;
    VanPhongPhamRequest vanphongphamDB;
    CapPhatRequest capPhatDatabase;

    //Khai Bao String
    String dataMaPBSpinner;
    String PBSpinnerData;
    String NVSpinnerData;

    // Table
    TableLayout table;
    List<TableRow> rows = null;

    // Row
    Rows rowGenerator = null;

    //Scale
    float scale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capphat_vpp_crud);
        scale = this.getResources().getDisplayMetrics().density;
        setControl();
        loadDatabase();
        setEvent();
    }

    private void setControl() {
        PBSpinner = findViewById(R.id.CPcrud_PBSpinner);
        CP_backBtn = findViewById(R.id.CP_backBtn);
        NVSpinner = findViewById(R.id.CPcrud_NVSpinner);
        table    = findViewById(R.id.CPcrud_table_list);
        VPPSpinner = findViewById(R.id.CPcrud_VPPSpinner);
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
    }

    private void setEventButton() {
        CP_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // ========================================= SET SPINNER ==========================================
        private void setSpinner() {
            setPBSpinner( );
//            setNVSpinner( ); không cần bởi vì setPBSpinner đã có rồi 
            setVPPSpinner( );
        }
    // ========================================== SPINNER ====================================
    // @ setPBSpinner  ---------------------------------------------
        private void setPBSpinner() {
            // Data
            PBSpinner.setAdapter( loadPBSpinner() );
            // Event
            setEventPBSpinner();
        }
        public ArrayAdapter<String> loadPBSpinner(){
            // 1. Tạo list Phong ban // 2. Đổ Phong_ban.getTenPB() ra 1 List // 3. setAdapter cho cái list getTenPB() đó
            PBSpinnerList = new ArrayList<>();
            PBSpinnerList.add("Tất cả phòng ban");
            // Phục vụ cho việc xổ ra Option cho Spinner
            for ( PhongBan pb : phongban_list){
                PBSpinnerList.add(pb.getTenpb());
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item, PBSpinnerList
                    );
            return adapter;
        }
        public void setEventPBSpinner(){
            // Loop
            PBSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PBSpinnerData = PBSpinnerList.get(position);
                    // PB = all, PB = i
                    NVSpinnerList = getListNVwith( PBSpinnerData );
                    setNVSpinner();
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
            setNVSpinnerEvent();
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
        public void setNVSpinnerEvent(){
            if( NVSpinnerList == null ) return;
            NVSpinnerData = NVSpinnerList.get(0);
            NVSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 ) {
                    // all : all
                    if( PBSpinnerData.trim().equalsIgnoreCase("Tất cả phòng ban") )
                        renderTable();
                    // all : index
                    else
                        renderTableWithPB(getMapb(PBSpinnerData));
                }else{
                    // index : index
                    NVSpinnerData = NVSpinnerList.get(position);
                    renderTableWithNV( NVSpinnerData.split(",")[0], false );
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // @ setVPPSpinner  ---------------------------------------------
        private void setVPPSpinner() {
            VPPSpinner.setAdapter(loadVPPSpinner());
        }
        public VPPAdapter loadVPPSpinner(){
        VanPhongPham VPPClass = new VanPhongPham();
        VPPClass.setMaVpp("all");
        VPPSpinnerList = new ArrayList<>();
        VPPSpinnerList.add(VPPClass);
        // Phục vụ cho việc xổ ra Option cho Spinner
        for ( VanPhongPham vpp : vanphongpham_list){
            Log.d("data",vpp.getMaVpp());
            VPPSpinnerList.add(vpp);
        }
        return new VPPAdapter(this,VPPSpinnerList);
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
    private List<String> getListNVwith(String PBSpinnerData) {
        if(PBSpinnerData == null || PBSpinnerData.trim().equalsIgnoreCase("")) return null;
        String maPB = getMapb(PBSpinnerData);
        List<String> NVList = new ArrayList<>();
        NVList.add("Tất cả nhân viên");
        if(PBSpinnerData.trim().equalsIgnoreCase("Tất cả phòng ban")){
            for( NhanVien nv : nhanvien_list ){
                NVList.add(nv.toSpinnerString());
            }
        }else {
            // Lấy List Nhân viên với mã phòng ban cho trước
            for( NhanVien nv : nhanvien_list ){
                if( nv.getMaPb().trim().equalsIgnoreCase( maPB ) )
                    NVList.add(nv.toSpinnerString());
            }
        }
        return NVList;
    }

    // ----------------- TABLE --------------------------------
    public void renderTable(){
        if(table.getChildCount()>1) table.removeViews(1,table.getChildCount()-1);
        for( int i = 0; i < capphat_list.size(); i++){
            CapPhat cp = capphat_list.get(i);
            TableRow tr = createRow(CapPhatCRUDLayout.this,cp);
            tr.setId( i+1 );
            table.addView( tr );
        }
    }
    public void renderTableWithPB(String maPB){
        if( maPB == null || maPB.trim().equalsIgnoreCase("")) return;
        if( table.getChildCount()>1 ) table.removeViews(1,table.getChildCount()-1);
        if( maPB.equalsIgnoreCase("Tất cả phòng ban")){
            return; // Vì nó đã gọi setNVSpinner phía ngoài
        }
        else {
            for( NhanVien nv : nhanvien_list)
                if( nv.getMaPb().trim().equalsIgnoreCase(maPB) )
                    renderTableWithNV( nv.getMaNv() , true );
        }
        return;
    }
    public void renderTableWithNV(String maNV, boolean appendMode){
        if( maNV == null || maNV.trim().equalsIgnoreCase("")) return;
        if( !appendMode )
            if( table.getChildCount()>1 ) table.removeViews(1,table.getChildCount()-1);
        // PB = i, NV = i
        for( int i = 0; i < capphat_list.size(); i++){
            CapPhat cp = capphat_list.get(i);
            if(cp.getMaNv().equals( maNV ) ){
                TableRow tr = createRow(CapPhatCRUDLayout.this,cp);
                tr.setId( i+1 );
                table.addView( tr );
            }
        }
    }
    


    // ============================= CONVERT ===============================
    public List<String> convertRawtoListString( String raw , String regex){
        if(raw == null || raw.length() == 0) return null;
        List<String> list = new ArrayList<>();
        for( String s : raw.split(regex+"")){
            list.add(s);
        }
        return list;
    }
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
    //DATE
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
        NgayCapview.setText(cp.getNgayCap());
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
}
