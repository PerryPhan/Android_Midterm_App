package com.example.giuaki.Mainv2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giuaki.Api.NhanVien;
import com.example.giuaki.Helper.JSONHelper;
import com.example.giuaki.Request.CapPhatRequest;
import com.example.giuaki.Request.NhanVienRequest;
import com.example.giuaki.Api.PhongBan;
import com.example.giuaki.Entities.Rows;
import com.example.giuaki.R;
import com.example.giuaki.Mainv2.CapphatVPPLayout;
import com.example.giuaki.XinchoLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BaocaoVPPLayout extends AppCompatActivity {
    // MainLayout
    Button backBtn,
           printBtn;
    TableLayout table;
    TextView tenPBView,
           countNVView,
         countVPPView,
       totalMoneyView,
             dateView;
    // Request
    CapPhatRequest capPhatDatabase;
    NhanVienRequest nhanVienDatabase;
    // Other
    PhongBan pb = CapphatVPPLayout.selectedPB;
    float scale;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baocaovpp_layout);

        scale = this.getResources().getDisplayMetrics().density;
        Rows.scale = scale;
        Rows.tvtemplate = R.layout.tvtemplate;

        setControl();
        setEvent();
    }

    private void setControl() {
        backBtn = findViewById(R.id.BC_index_backBtn);

        table = findViewById(R.id.BC_index_table);

        tenPBView = findViewById(R.id.BC_index_tenPB);
        countNVView = findViewById(R.id.BC_index_countNV);
        countVPPView = findViewById(R.id.BC_index_countVPP);
        totalMoneyView = findViewById(R.id.BC_index_totalMoney);
        dateView = findViewById(R.id.BC_index_date);

        printBtn = findViewById(R.id.BC_index_printBtn);
    }

    //  //----------------------------------{{}}------------------------------------------//   //
    //    ------------------------------ RETURNER -----------------------------------
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
    public List<String> returnListfromJSON( String resultfromQuery){
        String raw = null ;
        String response = resultfromQuery;
        if( !JSONHelper.verifyJSON(response).equalsIgnoreCase("pass") ) return null;
        try{
            raw = JSONHelper.rawParseJSON(response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return raw != null ? convertRawtoListString(raw,",") : null;
        }
    }
    //    ----------------------------- CONVERTTER ----------------------------------
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
    public List<PhongBan> convertToPhongBanList(List<Object> list ){
        if( list == null ) return null;
        List<PhongBan> phongbanlist = new ArrayList<>();
        for( Object li : list ){
            phongbanlist.add( (PhongBan) li);
        }
        return phongbanlist;
    }
    //  //----------------------------------{{}}------------------------------------------//   //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if( resultCode == RESULT_OK ) {
                Toast.makeText(BaocaoVPPLayout.this, "In báo cáo thành công", Toast.LENGTH_LONG).show();
                int result = data.getIntExtra("result",0);
            } else {
                Toast.makeText(BaocaoVPPLayout.this, "In báo cáo thất bại", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEvent() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaocaoVPPLayout.this, XinchoLayout.class);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                Bundle b = new Bundle();
                b.putString("loai","PhongBan");
                b.putString("ma",pb.getMapb()+"");
                intent.putExtras(b);
                startActivityForResult( intent, 1 );
            }
        });
        setPhongBanView();
        setTableLayout();
        setDateView();
        setTotal();
    }

    private void setTotal() {
        List<String> countVPP =
                returnListfromJSON(
                            capPhatDatabase.doGet(
                                    String.format("countVPPfromPB?mapb=%s"
                                    , pb.getMapb())
                        )
                );
        countVPPView.setText( countVPP.get(1).trim() );
        totalMoneyView.setText( MoneyFormat( CapphatVPPLayout.totalMoney ) );
    }

    public void setTableLayout(){
        //        <!-- 40 / 80 p0 / 50 / 90 p0 / 67 p0 / 63 p0 -->
        capPhatDatabase = new CapPhatRequest();
        Rows rowGenarator = new Rows(this );
        int[] sizeOfCell = {40,80,50,90,67,63};
        boolean[] isPaddingZero = {false, false, true, true ,true, true};
        rowGenarator.setSizeOfCell(sizeOfCell);
        rowGenarator.setIsCellPaddingZero(isPaddingZero);
        rowGenarator.enhanceRowData(
                returnListfromJSON(
                        capPhatDatabase.doGet(
                                String.format("baocaoQuery?mapb=%s"
                                        , pb.getMapb())
                        )
                )
                , 6 );
        rowGenarator.setSizeOfCell(sizeOfCell);
        rowGenarator.setIsCellPaddingZero(isPaddingZero);
        List<TableRow> rows = rowGenarator.generateArrayofRows();
        for( TableRow row : rows ){
            table.addView(row);
        }
    }

    private void setPhongBanView() {
        if( pb != null ) {
            tenPBView.setText( pb.getTenpb());
            nhanVienDatabase = new NhanVienRequest();
            List<NhanVien> list = convertToNhanvienList(
                    returnListfromJSON(
                            nhanVienDatabase.doGet(
                                    String.format("show?mapb=%s"
                                            , pb.getMapb())
                            )
                    ,"NhanVien")
            );
            countNVView.setText( list.size() + "" );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDateView(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String str = dtf.format(now);
        String[] date ;
        date = str.split("/");
        String msg = "TPHCM, ngày "+ date[2] +" tháng "+ date[1] +" năm "+ date[0];
        dateView.setText(msg);
    }

    public String MoneyFormat( int money ){
        if( money == 0) return "0 đ";
        int temp_money = money;
        String moneyFormat = "";
        if( money < 1000) return String.valueOf(money) +" đ";
        else {
            int count = 0;
            while (temp_money != 0) {
                moneyFormat += (temp_money % 10) + "";
                if ((count + 1) % 3 == 0 && temp_money >= 10) moneyFormat += ".";
                count++;
                temp_money /= 10;
            }
        }
        return new StringBuilder(moneyFormat).reverse().toString() +" đ";
    }
}