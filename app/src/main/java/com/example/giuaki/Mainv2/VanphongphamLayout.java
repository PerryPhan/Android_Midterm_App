package com.example.giuaki.Mainv2;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giuaki.Api.NhaCungCap;
import com.example.giuaki.Api.VanPhongPham;
import com.example.giuaki.Bell;
import com.example.giuaki.Helper.JSONHelper;
import com.example.giuaki.R;
import com.example.giuaki.Request.VanPhongPhamRequest;
import com.example.giuaki.WebService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VanphongphamLayout extends AppCompatActivity {
    // Main Layout
    TableLayout vpp_table_list;
    Spinner NCCSpinner;

    Button insertBtn;
    Button editBtn;
    Button delBtn;
    Button exitBtn;
    Button previewBtn; // <- Nút này để set Preview cái hàng đó, bấm vào là hiên Pop up chỉ để coi, ko chỉnh sửa
    // Preview Image Layout
        TextView VPP_IP_nhaCC;
        TextView VPP_IP_maVPP;
        TextView VPP_IP_tenVPP;
        TextView VPP_IP_DVT;
        TextView VPP_IP_Gia;
        TextView VPP_IP_Soluong;
        ImageView VPP_IP_Hinh;
        
    // --------------

    // Navigation
    Button navPB;
    Button navNV;
    Button navVPP;
    Button navCP;
    Button navCC;
    ImageView bell;

    EditText search;

    // Dialog Layout
    Dialog vppdialog;
        Button backBtn;
        Button yesBtn;
        Button noBtn;

        Spinner NCCSpinner_mini;
        EditText inputMaVPP;
        EditText inputTenVPP;
        EditText inputDVT;
        EditText inputSL;
        EditText inputGia;
        ImageView inputHinh;

        TextView showMVPPError;
        TextView showTVPPError;
        TextView showDVTError;
        TextView showGiaError;
        TextView showSLError;

        TextView showResult;
        TextView showConfirm;
        TextView showLabel;

    // Database Controller
    VanPhongPhamRequest vanphongphamDB;

    // Focus
    int indexofRow = -1;
    TableRow focusRow;
    TextView focusMaVPP;
    TextView focusTenVPP;
    TextView focusDVT;
    TextView focusSL;
    TextView focusGia;
    String focusHinh = null;
    String NCCSpinner_data = null;
    String NCCSpinner_mini_data = null;
    File inputFile = null;

    // List
    List<NhaCungCap> ncclist;
    List<VanPhongPham> vpplist;
    ArrayList<String> image_list = new ArrayList<>();
    // Other
    float scale;
    String webPath = "http://"+WebService.host()+"/ImageController-get?hinh=";
    // Key Code
    int IMAGE_FOLDER = 1000;
    int PERMISSION_GRANTED = 1001;
    int layout = R.layout.activity_vanphongpham_layout;
    Bell b;

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

    // ------------------------------ MAIN -----------------------------------------
        public void setControl(){
            vpp_table_list = findViewById(R.id.VPP_table_list);
            insertBtn = findViewById(R.id.VPP_insertBtn);
            editBtn = findViewById(R.id.VPP_editBtn);
            delBtn = findViewById(R.id.VPP_delBtn);
            exitBtn = findViewById(R.id.VPP_exitBtn);
            previewBtn = findViewById(R.id.VPP_previewBtn);

            navPB = findViewById(R.id.VPP_navbar_phongban);
            navNV = findViewById(R.id.VPP_navbar_nhanvien);
            navVPP = findViewById(R.id.VPP_navbar_VPP);
            navCP = findViewById(R.id.VPP_navbar_capphat);
            navCC = findViewById(R.id.VPP_navbar_cungcap);

            search = findViewById(R.id.VPP_searchEdit);
            NCCSpinner = findViewById(R.id.VPP_NCCSpinner);
            bell = findViewById(R.id.VPP_bell);
            b = new Bell(this, bell);
        } // OK
        public void loadDatabase(){
            vanphongphamDB = new VanPhongPhamRequest();
            vpplist = new ArrayList<>();
            TableRow tr = null;
            vpplist = convertToVanPhongPhamList( returnListfromJSON( vanphongphamDB.doGet("show"), "VanPhongPham" ) );
            for (int i = 0; i < vpplist.size(); i++) {
                tr = createRow(this, vpplist.get(i));
                tr.setId((int) i + 1);
                vpp_table_list.addView(tr);
            }
            ncclist = convertToNhaCungCapList( returnListfromJSON(vanphongphamDB.getNCCList(),"NhaCungCap"));
            if(ncclist == null ) return;
            ArrayList<String> tenNcclist = new ArrayList<>();
            tenNcclist.add("Tất cả nhà cung cấp");
            for( NhaCungCap ncc : ncclist){
                tenNcclist.add(ncc.getTenNCC());
            }
            NCCSpinner.setAdapter(loadSpinnerAdapter(tenNcclist));
        }
        public void setEvent(){
            editBtn.setVisibility(View.INVISIBLE); // turn on when click items
            delBtn.setVisibility(View.INVISIBLE);  // this too
            previewBtn.setVisibility(View.INVISIBLE);
            setEventTable(vpp_table_list);
            setEventSpinner();
        } // OK
        public void setNavigation(){
            // navVPP onclick none
            // navPB
            navPB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(VanphongphamLayout.this, PhongbanLayout.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    startActivity( intent );
                }
            });
            // navNV
            navNV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(VanphongphamLayout.this, NhanvienLayout.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    startActivity( intent );

                }
            });
            // navCP
            navCP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VanphongphamLayout.this, CapphatVPPLayout.class);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    startActivity( intent );
                }

            });
            // navCC
            navCC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VanphongphamLayout.this, CungcapLayout.class);
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

    // ------------------------------ CONVERTER ---------------------------------------
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
        } // OK
        public List<VanPhongPham> convertToVanPhongPhamList(List<Object> list ){
            if( list == null ) return null;
            List<VanPhongPham> vanphongphamlist = new ArrayList<>();
            for( Object li : list ){
                vanphongphamlist.add( (VanPhongPham) li);
            }
            return vanphongphamlist;
        } // OK
        public List<NhaCungCap> convertToNhaCungCapList(List<Object> list ){
            if( list == null ) return null;
            List<NhaCungCap> nhacuncaplist = new ArrayList<>();
            for( Object li : list ){
                nhacuncaplist.add( (NhaCungCap) li);
            }
            return nhacuncaplist;
        } // OK

    // ------------------------------ SPINNER ---------------------------------------
        public ArrayAdapter<String> loadSpinnerAdapter(ArrayList<String> tenNCClist) {
            if( tenNCClist == null || tenNCClist.size() == 0) return null;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tenNCClist);
            return adapter;
        } // OK
        public void setEventSpinner() {
            if(NCCSpinner == null) return;
            NCCSpinner_data = "All";
            NCCSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        if (NCCSpinner.getChildCount() < ncclist.size() + 1) {
                            NCCSpinner_data = "All";
                            image_list = new ArrayList<>();
                            // Nếu có sort trước đó làm cho số nhân viên nhỏ hơn số nhân viên tổng thì mới sort lại theo all
                            vpp_table_list.removeViews(1,vpp_table_list.getChildCount()-1);
                            TableRow tr = null;
                            // Tag sẽ bắt đầu ở 1 vì phải cộng thêm thằng example đã có sẵn
                            for (int i = 0; i < vpplist.size(); i++) {
                                VanPhongPham vpp = vpplist.get(i);
                                tr = createRow(VanphongphamLayout.this, vpp);
                                tr.setId((int) i + 1);
                                vpp_table_list.addView(tr);
                                setEventTableRows(tr, vpp_table_list);
                            }
                        }
                    } else {
                        int dem = 1;
                        String mancc = ncclist.get(position - 1).getMaNCC();
                        NCCSpinner_data = mancc;
                        image_list = new ArrayList<>();
                        // Select lại toàn bộ table
                        vpp_table_list.removeViews(1,vpp_table_list.getChildCount()-1);
                        TableRow tr = null;
                        for (int i = 0; i < vpplist.size(); i++) {
                            VanPhongPham vpp = vpplist.get(i);
                            if (vpp.getMaNcc().trim().equals( mancc.trim())) {
                                tr = createRow(VanphongphamLayout.this, vpp);
                                // For index count start linearly
                                tr.setId((int) dem++);
                                vpp_table_list.addView(tr);
                                setEventTableRows(tr, vpp_table_list);
                            }
                        }
                    }
                    editBtn.setVisibility(View.INVISIBLE); // turn on when click items
                    delBtn.setVisibility(View.INVISIBLE);  // this too
                    previewBtn.setVisibility(View.INVISIBLE); // this three
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    NCCSpinner_data = "All";
                }
            });
        } // OK
        public void setEventSpinnerMini() {
            if(NCCSpinner_mini == null) return;
            NCCSpinner_mini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    NCCSpinner_mini_data = ncclist.get(position).getMaNCC();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    NCCSpinner_mini_data = ncclist.get(0).getMaNCC();
                }
            });
        } // OK
        public int findIndexofNCC( String maNCC ){
            for( int i = 0; i < ncclist.size(); i++){
                NhaCungCap ncc = ncclist.get(i);
                if( ncc.getMaNCC().equalsIgnoreCase(maNCC) ){
                    return i;
                }
            }
            return -1;
        }

    // ------------------------------ TABLE ---------------------------------------
        public void setEventTable(TableLayout list) {
            setEventTableList(list);
            previewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Image from Database is handled to load here
                    createDialog(R.layout.popup_vpp_previewimage);
                    // Custom set Control
                    VPP_IP_maVPP = vppdialog.findViewById(R.id.VPP_IP_maVPP);
                    VPP_IP_tenVPP = vppdialog.findViewById(R.id.VPP_IP_tenVPP);
                    VPP_IP_DVT = vppdialog.findViewById(R.id.VPP_IP_DVT);
                    VPP_IP_Gia = vppdialog.findViewById(R.id.VPP_IP_Gia);
                    VPP_IP_Hinh = vppdialog.findViewById(R.id.VPP_IP_Hinh);
                    VPP_IP_Soluong = vppdialog.findViewById(R.id.VPP_IP_Soluong);
                    VPP_IP_nhaCC = vppdialog.findViewById(R.id.VPP_IP_nhaCC);
                    // Load Data
                    setDataImageView( VPP_IP_Hinh, image_list.get(indexofRow -1));

                    VPP_IP_maVPP.setText(focusMaVPP.getText().toString().trim());
                    VPP_IP_tenVPP.setText(focusTenVPP.getText().toString().trim());
                    VPP_IP_DVT.setText(focusDVT.getText().toString().trim());
                    VPP_IP_Gia.setText(focusGia.getText().toString().trim());
                    VPP_IP_Soluong.setText(focusSL.getText().toString().trim());
                    VPP_IP_nhaCC.setText((String) focusRow.getTag());
                }
            });

            // Khi tạo, dùng n làm tag để thêm row
            insertBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // H bấm 1 cái là hiện ra cái pop up
                    createDialog(R.layout.popup_vpp);
                    // Control
                    setControlDialog();
                    // Event
                    setEventSpinnerMini();
                    setEventImagePicker();
                    setEventDialog(v);
                }
            });
            // Khi edit
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (indexofRow != -1) {
                        // Test
                        // Toast.makeText( VanphongphamLayout.this, focusRowID+"", Toast.LENGTH_LONG).show();
                        createDialog(R.layout.popup_vpp);
                        // Control
                        setControlDialog();
                        showLabel.setText("Sửa văn phòng phẩm");
                        showConfirm.setText("Bạn có muốn sửa hàng này không?");
                        // Event
                        setEventSpinnerMini();
                        NCCSpinner_mini.setSelection( findIndexofNCC( focusRow.getTag().toString().trim() ) );
                        setEventImagePicker();
                        setEventDialog(v);
                        setDataImageView(inputHinh, focusHinh);
                        inputMaVPP.setText(focusMaVPP.getText());
                        inputTenVPP.setText(focusTenVPP.getText());
                        inputDVT.setText(focusDVT.getText());
                        inputGia.setText(focusGia.getText());
                        inputSL.setText(focusSL.getText());
                        inputMaVPP.setEnabled(false);
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
                        // Test
                        // Toast.makeText( VanphongphamLayout.this, focusRowID+"", Toast.LENGTH_LONG).show();
                        createDialog(R.layout.popup_vpp);
                        // Control
                        setControlDialog();
                        showLabel.setText("Xóa văn phòng phẩm");
                        showConfirm.setText("Bạn có muốn sửa hàng này không?");
                        // Event
                        setEventDialog(v);
                        setDataImageView(inputHinh, focusHinh);
                        NCCSpinner_mini.setSelection( findIndexofNCC( focusRow.getTag().toString().trim() ) );
                        inputMaVPP.setText(focusMaVPP.getText());
                        inputTenVPP.setText(focusTenVPP.getText());
                        inputDVT.setText(focusDVT.getText());
                        inputGia.setText(focusGia.getText());
                        inputSL.setText(focusSL.getText());

                        inputMaVPP.setEnabled(false);
                        inputTenVPP.setEnabled(false);
                        inputDVT.setEnabled(false);
                        inputGia.setEnabled(false);
                        inputSL.setEnabled(false);
                        inputHinh.setEnabled(false);
                    }
                }
            });

        } // OK
        public void setNormalBGTableRows(TableLayout list) {
            for (int i = 1; i < list.getChildCount(); i++) {
                TableRow row = (TableRow) list.getChildAt((int) i);
                if (indexofRow != (int) row.getId())
                    row.setBackgroundColor(getResources().getColor(R.color.white));
            }
        } // OK
        public void setEventTableList(TableLayout list){
            if( list != null && list.getChildCount() > 1) {
                for (int i = 0; i < list.getChildCount(); i++) {
                    TableRow tr = (TableRow) list.getChildAt(i);
//                    int id = tr.getId();
//                    tr.setId(i+1);
//                    Log.d("data","Change id from "+ id +" to "+tr.getId());
                    setEventTableRows( tr, list);
                }
            }
        }
        public void setEventTableRows(TableRow tr, TableLayout list) {
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editBtn.setVisibility(View.VISIBLE);
                    delBtn.setVisibility(View.VISIBLE);
                    previewBtn.setVisibility(View.VISIBLE);
                    // v means TableRow
                    v.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                    indexofRow = (int) v.getId();
                    focusRow = (TableRow) list.getChildAt(indexofRow);
                    focusMaVPP = (TextView) focusRow.getChildAt(0);
                    focusTenVPP = (TextView) focusRow.getChildAt(1);
                    focusDVT = (TextView) focusRow.getChildAt(2);
                    focusGia = (TextView) focusRow.getChildAt(3);
                    focusSL = (TextView) focusRow.getChildAt(4);
                    focusHinh = image_list.get( focusRow.getId() - Integer.parseInt("1") );
                    setNormalBGTableRows(list);
                }
            });
        } // OK

    // --------------- IMAGE PICKER & LOADER  ---------------------------------------------------
    // ! Nhớ thêm dòng <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> trong file Manifest.xml
        public void setDataImageView(ImageView imageView, String imageUri){
            if( imageUri == null || imageUri == "null" || imageUri.equalsIgnoreCase("")) return;
            Picasso.get().load(webPath+imageUri).into(imageView);
        } // OK
        public void pickImageFromStorage(){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_FOLDER);
        } // OK
        public void setEventImagePicker(){
            inputHinh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permissions, PERMISSION_GRANTED);
                        }
                        else{
                            pickImageFromStorage();
                        }
                    }
                    else{
                        pickImageFromStorage();
                    }
                }
            });
        } // OK
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == PERMISSION_GRANTED){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromStorage();
                }
            }
            else{
                Toast.makeText(VanphongphamLayout.this,"Permission's denied....", Toast.LENGTH_SHORT).show();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } // OK
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == IMAGE_FOLDER && resultCode == RESULT_OK && data != null) {
                inputHinh.setImageURI(data.getData());

                Uri selectedImage = data.getData();
                String[] filepathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,filepathColumn,null,null,null);
                cursor.moveToFirst();

                int columnIndex =cursor.getColumnIndex(filepathColumn[0]);
                String picturePath = cursor.getString(columnIndex);

                cursor.close();
                inputFile = new File(picturePath);
                if (inputFile.exists()) {
                    Toast.makeText(VanphongphamLayout.this,
                            String.format("Thêm hình thành công ở %s", inputFile.getAbsolutePath()),
                            Toast.LENGTH_SHORT).show();
                } else {
                    inputFile = null;
                    Toast.makeText(VanphongphamLayout.this,
                            "Tạo Object File hình thất bại",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } // OK

    // --------------- DIALOG -----------------------------------------------------------------
        public void createDialog(int layout) {
            vppdialog = new Dialog(VanphongphamLayout.this);
            vppdialog.setContentView(layout);
            vppdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            vppdialog.show();
        } // OK
        public void setControlDialog() {
            backBtn = vppdialog.findViewById(R.id.VPP_backBtn);
            yesBtn = vppdialog.findViewById(R.id.VPP_yesInsertBtn);
            noBtn = vppdialog.findViewById(R.id.VPP_noInsertBtn);

            NCCSpinner_mini = vppdialog.findViewById(R.id.VPP_NCCSpinner_mini);

            inputMaVPP = vppdialog.findViewById(R.id.VPP_inputMaVPP);
            inputTenVPP = vppdialog.findViewById(R.id.VPP_inputTenVPP);
            inputDVT = vppdialog.findViewById(R.id.VPP_inputDVT);
            inputGia = vppdialog.findViewById(R.id.VPP_inputGia);
            inputHinh = vppdialog.findViewById(R.id.VPP_inputHinh);
            inputSL = vppdialog.findViewById(R.id.VPP_inputSL);

            showMVPPError = vppdialog.findViewById(R.id.VPP_showMVPPError);
            showTVPPError = vppdialog.findViewById(R.id.VPP_showTVPPError);
            showDVTError = vppdialog.findViewById(R.id.VPP_showDVTError);
            showGiaError = vppdialog.findViewById(R.id.VPP_showGiaError);
            showSLError = vppdialog.findViewById(R.id.VPP_showSLError);

            showResult = vppdialog.findViewById(R.id.VPP_showResult);
            showConfirm = vppdialog.findViewById(R.id.VPP_showConfirm);
            showLabel = vppdialog.findViewById(R.id.VPP_showLabel);

            ArrayList<String> tenNcclist = new ArrayList<>();
            for( NhaCungCap ncc : ncclist){
                tenNcclist.add(ncc.getTenNCC());
            }
            NCCSpinner_mini.setAdapter(loadSpinnerAdapter(tenNcclist));
        } // OK
        public void setEventDialog(View view) {
            //  Toast.makeText( PhongbanLayout.this, (view.getId() == R.id.PB_editBtn)+"", Toast.LENGTH_LONG).show();
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vppdialog.dismiss();
                }
            });
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vppdialog.dismiss();
                }
            });
            // Dựa vào các nút mà thằng yesBtn sẽ có event khác
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean success = false;
                    switch (view.getId()) {
                        case R.id.VPP_insertBtn: {
                            if (!isSafeDialog( false )) break;
                            // Khai báo
                            VanPhongPham vpp = new VanPhongPham(
                                    inputMaVPP.getText().toString().trim() + "",
                                    inputTenVPP.getText().toString().trim() + "",
                                    inputDVT.getText().toString().trim() + "",
                                    inputGia.getText().toString().trim()+"",
                                    null,
                                    inputSL.getText().toString().trim()+"",
                                    NCCSpinner_mini_data+"");
                            // Gọi Request
                            String response = vanphongphamDB.doPost(vpp, inputFile,"insert");
                            if( !JSONHelper.verifyJSON(response)
                                    .equalsIgnoreCase("pass") ) break;
                            // Nhận response và trả hình ra theo Bất đồng bộ hóa
                            JSONObject fileNameResponse = null;
                            try {
                                fileNameResponse = new JSONObject(response);
                                vpp.setHinh(fileNameResponse.getString("fileName"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            // Tạo ra 1 row
                            vpplist.add(vpp);
                            TableRow tr = createRow(VanphongphamLayout.this, vpp);
                            int n = vpp_table_list.getChildCount();
                            tr.setId(n);
                            if ( !NCCSpinner_mini_data.trim().equalsIgnoreCase( NCCSpinner_data.trim()) ){
                                if( NCCSpinner_data.trim().equalsIgnoreCase("All") ){
                                    vpp_table_list.addView(tr);
                                }
                            }else{
                                vpp_table_list.addView(tr);
                            }
                            setEventTable(vpp_table_list);
                            editBtn.setVisibility(View.INVISIBLE);
                            delBtn.setVisibility(View.INVISIBLE);
                            previewBtn.setVisibility(View.INVISIBLE);
                            focusRow = null;
                            focusMaVPP = null;
                            focusTenVPP = null;
                            focusDVT = null;
                            focusGia = null;
                            success = true;
                        }
                        break;
                        case R.id.VPP_editBtn: {
                            if (!isSafeDialog( true )) break;
                            TableRow tr = (TableRow) vpp_table_list.getChildAt(indexofRow);
                            String hinh = image_list.get(indexofRow-1) == null
                                    || image_list.get(indexofRow-1).trim().equalsIgnoreCase("")
                                    ? null
                                    : image_list.get(indexofRow-1);
                            VanPhongPham vpp = new VanPhongPham(
                                    inputMaVPP.getText().toString().trim() + "",
                                    inputTenVPP.getText().toString().trim() + "",
                                    inputDVT.getText().toString().trim() + "",
                                    inputGia.getText().toString().trim()+"",
                                    hinh + "",
                                    inputSL.getText().toString().trim()+"",
                                    NCCSpinner_mini_data+"");
                            String response = vanphongphamDB.doPost(vpp, inputFile,"update");
                            if( !JSONHelper.verifyJSON(response)
                                    .equalsIgnoreCase("pass") ) break;
                            JSONObject fileNameResponse = null;
                            try {
                                fileNameResponse = new JSONObject(response);
                                vpp.setHinh(fileNameResponse.getString("fileName"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            // edit
                            int index = 0;
                            for( int i = 0; i < vpplist.size(); i++ ){
                                VanPhongPham vppi = vpplist.get(i);
                                if( vppi.getMaVpp().equalsIgnoreCase(vpp.getMaVpp()) ){
                                    index = i; break;
                                }
                            }
                            vpplist.set(index, vpp);
                            // INDEX !=
                            if ( !NCCSpinner_mini_data.trim().equalsIgnoreCase( NCCSpinner_data.trim()) ){
                                vpp_table_list.removeViewAt(indexofRow);
                                // ALL
                                if( NCCSpinner_data.trim().equalsIgnoreCase("All") ){
                                        vpp_table_list.addView(tr, indexofRow);
                                }
                                for( int i = indexofRow; i < vpp_table_list.getChildCount(); i++){
                                    vpp_table_list.getChildAt(i).setId(i);
                                }
                            }else{ // Match
                                vpp_table_list.removeViewAt(indexofRow);
                                vpp_table_list.addView(tr, indexofRow);
                                for( int i = indexofRow; i < vpp_table_list.getChildCount(); i++){
                                    vpp_table_list.getChildAt(i).setId(i);
                                }

                            }
                            setEventTableList(vpp_table_list);
                            focusTenVPP.setText( inputTenVPP.getText().toString().trim() + "");
                            focusDVT.setText( inputDVT.getText().toString().trim() + "");
                            focusGia.setText( inputGia.getText().toString().trim() + "");
                            focusSL.setText( inputSL.getText().toString().trim()+"" );
                            image_list.set( indexofRow-1, vpp.getHinh() );
                            focusHinh = vpp.getHinh();
                            success = true;
                        }
                        break;
                        case R.id.VPP_delBtn: {
                            String hinh = image_list.get(indexofRow-1) == null
                                    || image_list.get(indexofRow-1).trim().equalsIgnoreCase("")
                                    ? null
                                    : image_list.get(indexofRow-1);
                            VanPhongPham vpp = new VanPhongPham(
                                    inputMaVPP.getText().toString().trim() + "",
                                    inputTenVPP.getText().toString().trim() + "",
                                    inputDVT.getText().toString().trim() + "",
                                    inputGia.getText().toString().trim()+"",
                                    hinh +"",
                                    inputSL.getText().toString().trim()+"",
                                    NCCSpinner_mini_data+"");
                            if( !JSONHelper.verifyJSON(vanphongphamDB.doPost(vpp, null,"remove"))
                                    .equalsIgnoreCase("pass") ) {
                                Toast.makeText(VanphongphamLayout.this
                                        , "Văn Phòng Phẩm này đã tồn tại trong Cấp Phát "
                                        ,Toast.LENGTH_LONG).show();
                                break;
                            }
                            if (indexofRow == vpp_table_list.getChildCount() - 1) {
                                vpp_table_list.removeViewAt(indexofRow);
                            } else {
                                vpp_table_list.removeViewAt(indexofRow);
                                for (int i = 0; i < vpp_table_list.getChildCount(); i++) {
                                    vpp_table_list.getChildAt(i).setId((int) i);
                                }
                            }
                            int index = 0;
                            for( int i = 0; i< vpplist.size(); i++){
                                if( vpplist.get(i).getMaVpp().equals(inputMaVPP.getText().toString().trim() + "") ){
                                    index = i ; break;
                                }
                            }
                            vpplist.remove( index );
                            image_list.remove(indexofRow -1);
                            editBtn.setVisibility(View.INVISIBLE);
                            delBtn.setVisibility(View.INVISIBLE);
                            previewBtn.setVisibility(View.INVISIBLE);
                            focusRow = null;
                            focusMaVPP = null;
                            focusTenVPP = null;
                            focusDVT = null;
                            focusGia = null;
                            focusHinh = null;
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
                                inputMaVPP.setText("");
                                inputTenVPP.setText("");
                                inputDVT.setText("");
                                inputGia.setText("");
                                inputSL.setText("");
                                showResult.setVisibility(View.INVISIBLE);
                                vppdialog.dismiss();
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
        public boolean isSafeDialog( boolean allowSameID ) {
            String id, mavpp, tenvpp, dvt, gia, sl;
            // Mã PB không được trùng với Mã PB khác và ko để trống
            mavpp = inputMaVPP.getText().toString().trim();
            boolean noError = true;
            if (mavpp.trim().equalsIgnoreCase("")) {
                showMVPPError.setText("Mã VPP không được trống ");
                showMVPPError.setVisibility(View.VISIBLE);
                noError = false;
            }else{
                showMVPPError.setVisibility(View.INVISIBLE);
                if( noError == true ) noError = true;
            }

            // Tên PB không được để trống và không trùng
            tenvpp = inputTenVPP.getText().toString().trim();
            if (tenvpp.trim().equalsIgnoreCase("")) {
                showTVPPError.setText("Tên VPP không được trống ");
                showTVPPError.setVisibility(View.VISIBLE);
                noError = false;
            }else{
                showTVPPError.setVisibility(View.INVISIBLE);
                if( noError == true ) noError = true;
            }

            // dvt không được để trống và không chữ số
            boolean hasDigits = false;
            dvt = inputDVT.getText().toString().trim();
            hasDigits = dvt.matches(".*\\d.*");
            if (dvt.trim().equalsIgnoreCase("")) {
                showDVTError.setText("DVT không được trống ");
                showDVTError.setVisibility(View.VISIBLE);
                noError = false;
            }else if( hasDigits ){
                showDVTError.setText("DVT không được chứa số ");
                showDVTError.setVisibility(View.VISIBLE);
                noError = false;
            }else{
                showDVTError.setVisibility(View.INVISIBLE);
                if( noError == true ) noError = true;
            }

            // Giá không được để trống và không chữ cái
            gia = inputGia.getText().toString().trim();
            if (gia.length()!= 0 && gia.charAt(0) == '0')
                if (gia.length() > 1) gia = gia.substring(1, gia.length() - 1);
            if (gia.trim().equalsIgnoreCase("")) {
                showGiaError.setText("Giá không được trống ");
                showGiaError.setVisibility(View.VISIBLE);
                noError = false;
            } else {
                showGiaError.setVisibility(View.INVISIBLE);
                if( noError == true ) noError = true;
            }
            // Số lượng không được để trống và không chữ cái
            sl = inputSL.getText().toString().trim();
                if (sl.length()!= 0 && sl.charAt(0) == '0') if (sl.length() > 1) sl = sl.substring(1, sl.length() - 1);
                if (sl.trim().equalsIgnoreCase("")) {
                    showSLError.setText("Số lượng không được trống ");
                    showSLError.setVisibility(View.VISIBLE);
                    noError = false;
                } else {
                    showSLError.setVisibility(View.INVISIBLE);
                    if( noError == true ) noError = true;
                }

            if( noError ) {
                for (int i = 1; i < vpp_table_list.getChildCount(); i++) {
                    TableRow tr = (TableRow) vpp_table_list.getChildAt(i);
                    TextView mavpp_data = (TextView) tr.getChildAt(0);
                    TextView tenvpp_data = (TextView) tr.getChildAt(1);

                    if (!allowSameID)
                        if (mavpp.trim().equalsIgnoreCase(mavpp_data.getText().toString())) {
                            showMVPPError.setText("Mã VPP không được trùng ");
                            showMVPPError.setVisibility(View.VISIBLE);
                            return noError = false;
                        }

                    if (tenvpp.trim().equalsIgnoreCase(tenvpp_data.getText().toString())
                        && !tenvpp_data.getText().toString().trim().equalsIgnoreCase( focusTenVPP.getText().toString().trim() )) {
                        showTVPPError.setText("Tên VPP không được trùng");
                        showTVPPError.setVisibility(View.VISIBLE);
                        return noError = false;
                    }
                }
                showMVPPError.setVisibility(View.INVISIBLE);
                showTVPPError.setVisibility(View.INVISIBLE);
                showDVTError.setVisibility(View.INVISIBLE);
                showGiaError.setVisibility(View.INVISIBLE);
                showSLError.setVisibility(View.INVISIBLE);
            }
            return noError;
        } // OK

    // --------------- CUSTOM HELPER --------------------------------------------------------------------
        public int DPtoPix(int dps) {
            return (int) (dps * scale + 0.5f);
        } // OK
        // This Custom Columns' Max Width : 70 p0 / 140 / 55 p0 / 85 p0 / 55 p0
        //  Updated      <!--    70 p0 / 140 / 55 p0 / 85 p0 / <= 55 p0-->
        public TableRow createRow(Context context, VanPhongPham vpp) {
            TableRow tr = new TableRow(context);
            // Id
            //  Ma
            TextView maVPP = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà maVPP đạt tới max width thì nó sẽ tăng height cho bên tenVPP luôn
            // Lưu ý!! : khi đặt LayoutParams thì phải theo thằng cố nội và phải có weight
            maVPP.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            maVPP.setMaxWidth(DPtoPix(70));
            maVPP.setPadding(0,0,0,0);
            maVPP.setText(vpp.getMaVpp());

    //      Cũ
            //   Ten PB
            TextView tenVPP = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà tenVPP đạt tới max width thì nó sẽ tăng height cho bên maVPP luôn
            tenVPP.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            tenVPP.setText(vpp.getTenVpp());
    //        tenVPP.setMaxWidth(DPtoPix(200));
    //      Mới
            tenVPP.setMaxWidth(DPtoPix(140));

            TextView dvt = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà tenVPP đạt tới max width thì nó sẽ tăng height cho bên maVPP luôn
            dvt.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            dvt.setText(vpp.getDvt());
            dvt.setPadding(0,0,0,0);
            dvt.setMaxWidth(DPtoPix(55));

    //      Cũ
            TextView gianhap = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà tenVPP đạt tới max width thì nó sẽ tăng height cho bên maVPP luôn
            gianhap.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            gianhap.setText(vpp.getGiaNhap());
            gianhap.setPadding(0,0,0,0);
    //        gianhap.setMaxWidth(DPtoPix(55));
    //      Mới
            gianhap.setMaxWidth(DPtoPix(85));

    //      Mới
            TextView soluong = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
            // Cần cái này để khi mà tenVPP đạt tới max width thì nó sẽ tăng height cho bên maVPP luôn
            soluong.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
            soluong.setText(vpp.getSoLuong());
            soluong.setPadding(0,0,0,0);
            soluong.setMaxWidth(DPtoPix(55));

            image_list.add(vpp.getHinh());

            tr.setTag( vpp.getMaNcc() );
            tr.setBackgroundColor(getResources().getColor(R.color.white));
            tr.addView(maVPP);
            tr.addView(tenVPP);
            tr.addView(dvt);
            tr.addView(gianhap);
            tr.addView(soluong);
            return tr;
        } // OK
}