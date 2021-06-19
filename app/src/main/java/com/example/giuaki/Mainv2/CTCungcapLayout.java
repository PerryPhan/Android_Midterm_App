package com.example.giuaki.Mainv2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.giuaki.Adapter.VPPAdapter;
import com.example.giuaki.Api.ChiTietCungCap;
import com.example.giuaki.Api.NhaCungCap;
import com.example.giuaki.Api.PhieuCungCap;
import com.example.giuaki.Api.VanPhongPham;
import com.example.giuaki.Helper.JSONHelper;
import com.example.giuaki.R;
import com.example.giuaki.Request.PhieuCungCapRequest;
import com.example.giuaki.Request.VanPhongPhamRequest;
import com.example.giuaki.WebService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CTCungcapLayout extends AppCompatActivity {
    // Main Layout
    TableLayout ct_table_list = null;
    TextView spView,
             ngView,
             nccView,
             ttView,
             ttmoneyView;

    Button  previewBtn,
            insertBtn,
            editBtn,
            delBtn;

    // Database
    PhieuCungCapRequest phieucungcapDB;
    VanPhongPhamRequest vanphongphamDB;

    // List
    String[] trangthai = {"OPENING","CONFIRMED","DELIVERIED","CANCELED"};
    List<ChiTietCungCap> CTCCList = null;
    List<VanPhongPham> VPPList = null;
    List<VanPhongPham> VPPListfromAPI = null;

    // Focus
    PhieuCungCap pcc = null;
    String tenNcc = null;
    boolean isChanged = false;
    int indexofRow = -1;
    String focusID = null;
    TableRow focusRow = null;
    VanPhongPham focusVPP = null;

    boolean isOpenning = false;
    // Dialog
    Dialog dialog;
        // button
        Button backBtn,
                yesBtn,
                 noBtn;
        // textview
        TextView d_idView,
                 d_nccView,
                 d_moneyView;
        // spinner
        Spinner VPPSpinner_mini;
        // input
        EditText inputSL;
        // error
        TextView showSLError;
        TextView showVPPError;
        // label
        TextView showResult,
                 showConfirm,
                 showLabel;
        // Variable
        String thanhtien = "";
        int soluong = 0, gia = 0;
        VanPhongPham vpp = null;
        String maVPP ="VPP01";
    // Navigation
    Button exit;

    // Other
    float scale;
    String webPath = "http://"+ WebService.host()+"/ImageController-get?hinh=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ct_cungcap_layout);
        scale = this.getResources().getDisplayMetrics().density;
        setControl();
        init(); // Lấy bundle từ Intent
        LoadDatabase();
        setEvent();
        setNavigation();
    }

    private void setNavigation() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChanged) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("tongtien", reverseMoneyFormat(ttmoneyView.getText().toString()) );
                    setResult(Activity.RESULT_OK, resultIntent);
                }
                finish();
            }
        });
    }

    public int chooseColor( String tag ){
        switch (tag){
            case "OPENING": // Cho phép tất cả
                return R.color.focus_opening_color;
            case "CONFIRMED":
                return R.color.focus_confirmed_color;
            case "DELIVERIED":
                return R.color.yes_color;
            case "CANCELED":
                return R.color.disable_color;
            default:
                return R.color.white;
        }
    }

    private void init() {
        Bundle b = getIntent().getExtras();
        if( b == null ) {
            pcc = new PhieuCungCap("PB01","OPENING","VPPKBTC","02/06/2018", "100000");
            tenNcc = "ABC";
            return;
        }
        String sp = b.getString("sp") == null ? "SP01" : b.getString("sp");
        String ng = b.getString("ng") == null ? "02/06/2018" : b.getString("ng");
        String ncc = b.getString("ncc") == null ? "A" : b.getString("ncc");
        tenNcc = b.getString("tenNCC") == null ? "ABC" : b.getString("tenNCC");
        String ttien = b.getString("ttien") == null ? "" : b.getString("ttien");
        String tt  = b.getString("tt") == null ? "OPENING" : b.getString("tt");
        pcc = new PhieuCungCap(sp,tt,ncc,ng,ttien);
        int color = chooseColor(tt);
            spView.setText(sp); spView.setTextColor( getResources().getColor(color));
            ngView.setText(ng); ngView.setTextColor( getResources().getColor(color));
            nccView.setText(tenNcc); nccView.setTextColor( getResources().getColor(color));
            ttView.setText(tt); ttView.setTextColor( getResources().getColor(color));
            ttmoneyView.setText(ttien); ttmoneyView.setTextColor( getResources().getColor(color));
    }

    private void setControl() {
        // TextView
        spView = findViewById(R.id.CT_SPview);
        ngView = findViewById(R.id.CT_NGview);
        nccView = findViewById(R.id.CT_NCCview);
        ttView = findViewById(R.id.CT_TTview);
        ttmoneyView = findViewById(R.id.CT_totalMoney);
        // Button
        insertBtn = findViewById(R.id.CT_insertBtn);
        editBtn = findViewById(R.id.CT_editBtn);
        delBtn = findViewById(R.id.CT_delBtn);
        previewBtn = findViewById(R.id.CT_previewBtn);
        exit = findViewById(R.id.CT_backBtn);
        // Table
        ct_table_list = findViewById(R.id.CT_table_list);
    }

    public List<VanPhongPham> convertToVanPhongPhamList(List<Object> list ){
        if( list == null ) return null;
        List<VanPhongPham> vanphongphamlist = new ArrayList<>();
        for( Object li : list ){
            vanphongphamlist.add( (VanPhongPham) li);
        }
        return vanphongphamlist;
    }
    public List<ChiTietCungCap> convertToChiTietList(List<Object> list ){
        if( list == null ) return null;
        List<ChiTietCungCap> chitietlist = new ArrayList<>();
        for( Object li : list ){
            chitietlist.add( (ChiTietCungCap) li);
        }
        return chitietlist;
    }
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
    public VanPhongPham getVPPfromList( String mavpp){
//        Log.d("data",mavpp.trim());
        if( mavpp == null || mavpp.trim().equalsIgnoreCase("") ) return null;
        if( VPPList == null || VPPList.size() == 0 ) return  null;
        for( VanPhongPham vpp : VPPList){
            if( vpp.getMaVpp().equals(mavpp)){
                return vpp;
            }
        }
        return null;
    }
    private void LoadDatabase() {


        // Get all VPP
        vanphongphamDB = new VanPhongPhamRequest();
        phieucungcapDB = new PhieuCungCapRequest();
        VPPListfromAPI = convertToVanPhongPhamList(
                returnListfromJSON(
                        vanphongphamDB.doGet("show")
                        , "VanPhongPham"
                )
        );
        if(VPPListfromAPI == null){
            Toast.makeText(this,"Error: Return VPP JSON fail",Toast.LENGTH_LONG).show();
            return;
        }else if( VPPListfromAPI.size() == 0){
            Toast.makeText(this,"Không có VPP nào để cấp phát",Toast.LENGTH_LONG).show();
            return;
        }else{
            VPPList = new ArrayList<>();
            for( VanPhongPham vpp : VPPListfromAPI ){
                if( vpp.getMaNcc().trim().equalsIgnoreCase( pcc.getMaNcc() )) VPPList.add(vpp);
            }
        }

        // Api // get from pcc
        Log.d("SP",pcc.getSoPhieu());
        CTCCList = convertToChiTietList(
                returnListfromJSON(
                        phieucungcapDB.doGet(String.format("showDetail?sophieu=%s",pcc.getSoPhieu() ))
                        ,"ChiTiet"
                )
        );
        if(pcc.getTrangThai().equals("OPENING")) isOpenning = true;
        if( isOpenning ) insertBtn.setEnabled(isOpenning);
        renderTable();
    }

    private void renderTable() {
        int dem = 1;
        if( ct_table_list.getChildCount() > 1) {
            ct_table_list.removeViews(1, ct_table_list.getChildCount()-1);
        }
        for( ChiTietCungCap ct : CTCCList){
            TableRow tr = createRow(this, ct);
            tr.setId( dem++ );
            ct_table_list.addView(tr);
        }
        setEventTableList( ct_table_list );
        calculateTotalMoney();
    }
    public int convertMoneyStrtoInt( String moneystr ){
        if( moneystr == null || moneystr.trim().equalsIgnoreCase("")) return 0;
        String mm = "";
//        Log.d("data",moneystr.substring(0,moneystr.length()-1).split("\\.")[0]+"");
        for(String m : moneystr.substring(0,moneystr.length()-1).split("\\."))
        {
            mm += m;
        }
        if( mm.trim().equalsIgnoreCase("") ) return 0;
        return Integer.parseInt(mm);
    }
    private void calculateTotalMoney() {
        if( ct_table_list.getChildCount() <= 1) return;
        int totalMoney = 0; String moneystr = "" ;int money = 0;
        for( int i = 1; i< ct_table_list.getChildCount(); i++){
            TableRow tr = (TableRow) ct_table_list.getChildAt(i);
            TextView ttien = (TextView) tr.getChildAt(4);
            moneystr = ttien.getText().toString();
//            Log.d("data",moneystr);
            money = convertMoneyStrtoInt(moneystr+"");
//            Log.d("data",money+"");
            totalMoney += money;
            ttmoneyView.setText( MoneyFormat(totalMoney) );
        }

    }

    private void setEvent() {
        hideButton();
        setEventButton();
    }


    private void hideButton() {
        editBtn.setVisibility(View.INVISIBLE);
        delBtn.setVisibility(View.INVISIBLE);
        previewBtn.setVisibility(View.INVISIBLE);
    }

    public void setDataImageView(ImageView imageView, String imageUri){
        if( imageUri == null || imageUri == "null" || imageUri.equalsIgnoreCase("")) return;
        Log.d("data",webPath+imageUri);
        Picasso.get().load(webPath+imageUri).into(imageView);
    }

    private void setEventButton() {
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
                        , null);

            }
        });
        int layout = R.layout.popup_ct_cungcap;
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(layout);
                setControlDialog();

                settingDialog("","insert");
                setEventDialog(v);
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(layout);
                setControlDialog();

                showConfirm.setText("Bạn có muốn sửa hàng này không ? ");
                settingDialog("Sửa chi tiết phiếu cung cấp","edit");
                setEventDialog(v);
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(layout);
                setControlDialog();

                showConfirm.setText("Bạn có muốn xóa hàng này không ? ");
                settingDialog("Xóa chi tiết phiếu cung cấp", "del");
                setEventDialog(v);
            }
        });
    }
    public int findMaxinList( List<ChiTietCungCap> list){
        int max = -1;
        for( ChiTietCungCap ct : list)
            if( max < Integer.parseInt(ct.getSoPhieu()) )
                max = Integer.parseInt(ct.getSoPhieu());
        return max;
    }
    public void settingDialog(String label, String mode){
        if( !label.equals("") ) showLabel.setText(label);
        // Variable
        String id = "0";
          soluong = 1;
        thanhtien = "0";
        switch (mode){
            case "insert": {

            }
            break;
            case "edit":
            case "del": {
                TextView tv = (TextView) focusRow.getChildAt(0);
                id = tv.getText().toString().trim();
                tv = (TextView) focusRow.getChildAt(3);
                soluong = Integer.parseInt(tv.getText().toString().trim());
                tv = (TextView) focusRow.getChildAt(4);
                thanhtien = tv.getText().toString().trim();
            }break;
            default:
                break;
        }
        // Constant
        if(tenNcc == null || tenNcc.trim().equalsIgnoreCase("")) tenNcc = "Empty";
        inputSL.setText(soluong+"");
        d_nccView.setText( tenNcc );
        if(!id.equalsIgnoreCase("0")) d_idView.setText(id);
        d_moneyView.setText( MoneyFormat( 0 ));


        if( mode.equalsIgnoreCase("del") ) {
            inputSL.setEnabled(false);
            VPPSpinner_mini.setEnabled(false);
        }else{
            inputSL.setEnabled(true);
            VPPSpinner_mini.setEnabled(true);
            setSpinner();
            // init
                int money = soluong * gia;
                thanhtien = money + "";
                d_moneyView.setText(MoneyFormat(money));
            // => maVPP
            setinputSLEvent( soluong +"");
//            Log.d("data",soluong+"");
        }
    }

    private void setinputSLEvent( String strsoluong ) {
        inputSL.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("") ) {
                    String convert = new StringBuilder(s).toString();
                    soluong = convert.equals("") ? 0 : Integer.parseInt(convert);
                    int money = soluong * gia;
                    thanhtien = money + "";
                    Log.d("data",soluong+"");
                    d_moneyView.setText(MoneyFormat(money));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                s = strsoluong;
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("data",soluong+"");
            }
        });
    }
    public  String checkStr( String str ){
        if( str == null || str.length() == 0 || str.equalsIgnoreCase("null"))
            return "";
        return str;
    }
    private void setSpinner() {
        if( VPPList.size() == 0) return;
        VPPSpinner_mini.setAdapter( new VPPAdapter(this, VPPList ));
        vpp = VPPList.get(0);
        maVPP = vpp.getMaVpp();
        gia = checkStr(vpp.getGiaNhap()).trim().equalsIgnoreCase("") ?
                1
                : Integer.parseInt(vpp.getGiaNhap()) ;
        thanhtien = (gia * soluong) +"";
        d_moneyView.setText(MoneyFormat(
                Integer.parseInt(thanhtien)
        ));
        VPPSpinner_mini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vpp = VPPList.get(position);
                maVPP = vpp.getMaVpp();
                Log.d("data",maVPP+""+vpp.getTenVpp());
                gia = checkStr(vpp.getGiaNhap()).trim().equalsIgnoreCase("") ?
                        1
                        : Integer.parseInt(vpp.getGiaNhap()) ;
                thanhtien = (gia * soluong) +"";
                d_moneyView.setText(MoneyFormat(
                    Integer.parseInt(thanhtien)
                ));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vpp = VPPList.get(0);
            }
        });
    }

    public void setEventTableList( TableLayout list ){
        for (int i = 0; i < list.getChildCount(); i++) {
            setEventTableRow((TableRow) list.getChildAt(i), list);
        }
    }

    public void setNormalBGTableRows(TableLayout list) {
        for (int i = 1; i < list.getChildCount(); i++) {
            TableRow row = (TableRow) list.getChildAt((int) i);
            if (indexofRow != (int) row.getId())
                row.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }
    public void setEventTableRow( TableRow tr, TableLayout list){
        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexofRow = (int) v.getId();
                v.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                previewBtn.setVisibility(View.VISIBLE);
                focusRow = (TableRow) list.getChildAt(indexofRow);
                TextView tv = (TextView) focusRow.getChildAt(1);
                focusVPP = getVPPfromList((String) tv.getText());
                tv = (TextView) focusRow.getChildAt(0);
                focusID = tv.getText().toString().trim();
                setNormalBGTableRows(list);
                if( isOpenning ) {
                    editBtn.setVisibility(View.VISIBLE);
                    delBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    // ---------------------------- DIALOG -----------------------------------
    // --------------- DIALOG HELPER -----------------------------------------------------------------
    public void createDialog(int layout) {
        dialog = new Dialog(CTCungcapLayout.this);
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setControlDialog() {
        // button
        backBtn = dialog.findViewById(R.id.CT_backBtn);
        yesBtn = dialog.findViewById(R.id.CT_yesInsertBtn);
        noBtn = dialog.findViewById(R.id.CT_noInsertBtn);
        // Spinner
        VPPSpinner_mini = dialog.findViewById(R.id.CT_VPPSpinner_mini);
        // input
        inputSL = dialog.findViewById(R.id.CT_inputSL);
        // textview
        d_idView = dialog.findViewById(R.id.CT_IDview);
        d_nccView = dialog.findViewById(R.id.CT_NCCview);
        d_moneyView = dialog.findViewById(R.id.CT_moneyView);
        // error
        showSLError = dialog.findViewById(R.id.CT_showSLError);
        showVPPError = dialog.findViewById(R.id.CT_showVPPError);
        // label
        showResult = dialog.findViewById(R.id.CT_showResult);
        showConfirm = dialog.findViewById(R.id.CT_showConfirm);
        showLabel = dialog.findViewById(R.id.CT_showLabel);
    }

    public void setEventDialog( View view ){
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
                    case R.id.CT_insertBtn:{
                        if ( !isSafeDialog( false) ) break;
                        ChiTietCungCap ctcc = new ChiTietCungCap(
                                "0",
                            pcc.getSoPhieu()+"",
                            maVPP+"",
                            soluong+"",
                            thanhtien+""
                        );
                        insert( ctcc );
                        success = true;}
                    break;
                    case R.id.CT_editBtn:{
                        if ( !isSafeDialog( true) ) break;

                        ChiTietCungCap ctcc = new ChiTietCungCap(
                                focusID+""
                                ,pcc.getSoPhieu()+"",
                                maVPP+"",
                                soluong+"",
                                thanhtien+""
                        );
                        Log.d("data",ctcc.toString());
                        edit( ctcc );
                        success = true;}
                    break;
                    case R.id.CT_delBtn:
                        ChiTietCungCap ctcc = new ChiTietCungCap(
                                focusID+"",
                                pcc.getSoPhieu()+"",
                                maVPP+"",
                                soluong+"",
                                thanhtien+""
                        );
                        delete( ctcc );
                        success = true;
                        break;
                }
                if (success) {
                    isChanged = success;
                    showResult.setText(showLabel.getText() + " thành công !");
                    showResult.setTextColor(getResources().getColor(R.color.yes_color));
                    showResult.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.INVISIBLE);
                    delBtn.setVisibility(View.INVISIBLE);
                    previewBtn.setVisibility(View.INVISIBLE);
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

    private void insert(ChiTietCungCap ctcc) {
        // API
//        Log.d("data","Insert: "+ctcc.toString());
        String id = null;
        try {
            JSONObject json = new JSONObject(phieucungcapDB.doPost(pcc,ctcc,"insertDetail"));
            id = json.get("ID").toString();
        }catch (JSONException e){
            e.printStackTrace();
            return;
        }
        if( id == null ) return;
        ctcc.setId(id);
        // List
        CTCCList.add(ctcc);
        // Table
        TableRow tr = createRow(this,ctcc);
        tr.setId( ct_table_list.getChildCount() );
        ct_table_list.addView(tr);
        setEventTableRow(tr,ct_table_list);
        calculateTotalMoney();
    }

    private void delete(ChiTietCungCap ctcc) {
        // API
        phieucungcapDB.doPost(pcc,ctcc,"removeDetail");
        // List
        CTCCList.remove(indexofRow-1);
        // Table
        ct_table_list.removeViewAt(indexofRow);
        if(indexofRow != ct_table_list.getChildCount()){
            for( int i = indexofRow; i < ct_table_list.getChildCount(); i++){
                ct_table_list.getChildAt(i).setId(i);
            }
        }
        setEventTableList(ct_table_list);
        calculateTotalMoney();
    }

    private void edit(ChiTietCungCap ctcc) {
        // API
        phieucungcapDB.doPost(pcc,ctcc,"updateDetail");
        // List
        CTCCList.set(indexofRow-1, ctcc);
        // Table
        TableRow tr = createRow(this, ctcc);
        ct_table_list.removeViewAt(indexofRow);
        ct_table_list.addView(tr,indexofRow);
        if(indexofRow != ct_table_list.getChildCount()){
            for( int i = indexofRow; i < ct_table_list.getChildCount(); i++){
                ct_table_list.getChildAt(i).setId(i);
            }
        }
        setEventTableList(ct_table_list);
        calculateTotalMoney();
    }

    // --------------- CUSTOM HELPER --------------------------------------------------------------------
    public int DPtoPix(int dps) {
        return (int) (dps * scale + 0.5f);
    }
    // This Custom Columns' Max Width : 70 / 70 p0 / 85 p0 / 55 p0 / <= 110 p0
    public TableRow createRow(Context context, ChiTietCungCap ct ) {
        TableRow tr = new TableRow(context);
        //  SoPhieuview
        TextView idView = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // Cần cái này để khi mà idView đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
        // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
        idView.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        idView.setMaxWidth(DPtoPix(70));
        idView.setText(ct.getId());

        TextView vppView = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // Cần cái này để khi mà vppView đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
        // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
        vppView.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        vppView.setMaxWidth(DPtoPix(70));
        vppView.setPadding(0,0,0,0);
        vppView.setText(ct.getMaVpp());

        TextView tenView = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // Cần cái này để khi mà tenView đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
        // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
        tenView.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        tenView.setMaxWidth(DPtoPix(85));
        tenView.setPadding(0,0,0,0);
        tenView.setText( getVPPfromList(ct.getMaVpp()).getTenVpp() );

        TextView slView = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // Cần cái này để khi mà slView đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
        // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
        slView.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        slView.setMaxWidth(DPtoPix(55));
        slView.setPadding(0,0,0,0);
        slView.setText(
                ct.getSoLuong().trim().equalsIgnoreCase("") || ct.getSoLuong() == null ? "0" : ct.getSoLuong()
        );

        TextView ttienView = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // Cần cái này để khi mà ttienView đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
        // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
        ttienView.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        ttienView.setMaxWidth(DPtoPix(110));
        ttienView.setPadding(0,0,0,0);
        ttienView.setText( MoneyFormat( Integer.parseInt(
                ct.getThanhTien().trim().equalsIgnoreCase("") || ct.getThanhTien() == null ? "0" : ct.getThanhTien()
        ) ));

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        tr.addView(idView);
        tr.addView(vppView);
        tr.addView(tenView);
        tr.addView(slView);
        tr.addView(ttienView);
        return tr;
    }
    public String reverseMoneyFormat( String money ){
        if(money == null ) return null;
        if(money.trim().equalsIgnoreCase("") ) return "";
        String str = "";
        for( String m : money.split("\\.")){
            str += m;
        }
        return str.equals("0") ? str : str.substring(0,str.length()-1) ;
    }

    public String MoneyFormat( int money ){
        if( money == 0) return "0đ";
        int temp_money = money;
        String moneyFormat = "";
        if( money < 1000) return String.valueOf(money) +"đ";
        else {
            int count = 0;
            while (temp_money != 0) {
                moneyFormat += (temp_money % 10) + "";
                if ((count + 1) % 3 == 0 && temp_money >= 10) moneyFormat += ".";
                count++;
                temp_money /= 10;
            }
        }
        return new StringBuilder(moneyFormat).reverse().toString() +"đ";
    }

    public boolean isSafeDialog( boolean edit ) {
        String sl, maVpp;
        boolean noError = true;
        if( edit == false )
        if(vpp != null && VPPList != null && VPPList.size() != 0 )
            for( ChiTietCungCap ct : CTCCList ){
                if( ct.getMaVpp().equals(vpp.getMaVpp()) ){
                    showVPPError.setText("VPP này đã được đăng kí rồi");
                    showVPPError.setVisibility(View.VISIBLE);
                    noError = false;
                    break;
                }
            }
        sl = soluong+"";
        if (sl.equals("0")) {
            showSLError.setText("Số lượng không được bằng 0 ");
            showSLError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            showSLError.setVisibility(View.INVISIBLE);
            if(noError == true) noError = true;
        }
        return noError;
    }
}