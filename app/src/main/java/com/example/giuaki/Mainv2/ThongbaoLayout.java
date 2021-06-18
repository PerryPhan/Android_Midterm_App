package com.example.giuaki.Mainv2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.giuaki.Adapter.VPPAdapter2;
import com.example.giuaki.Api.PhieuCungCap;
import com.example.giuaki.Api.VanPhongPham;
import com.example.giuaki.Helper.JSONHelper;
import com.example.giuaki.R;
import com.example.giuaki.Request.PhieuCungCapRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThongbaoLayout extends AppCompatActivity {
    // MainLayout
    ListView mainList;
    TextView dateView;
    TextView countView;
    // Navigation
    Button backBtn;
    // Database
    List<VanPhongPham> VPPList = null;
    PhieuCungCapRequest phieuCungCapRequest ;
    int layout = R.layout.thongbao_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        setControl();
        loadDatabase();
        setEvent();
        setNavigation();
    }
    // ----------------------- MAIN ------------------------------
        private void setControl() {
            mainList = findViewById(R.id.TB_mainList);
            backBtn = findViewById(R.id.TB_backBtn);
            dateView = findViewById(R.id.TB_dateView);
            countView = findViewById(R.id.TB_countView);
        }
        private void loadDatabase() {
            // API
            phieuCungCapRequest = new PhieuCungCapRequest();
            VPPList = convertToVanPhongPhamList(
                        returnListfromJSON(
                                phieuCungCapRequest.doGet("getDeliveriedItemToday")
                        , "VanPhongPham")
            );
            // List
            if( VPPList == null ) return;
            else if( VPPList.size() == 0 ) {
                Toast.makeText(this, "Không có Nhà cung cấp nào giao trong hôm nay",Toast.LENGTH_LONG).show();
            }
        } // OK
        private void setEvent() {
            setDate();
            renderList( VPPList );
            mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(ThongbaoLayout.this,"Go to Chi tiet Cung cap Layout ",Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        private void setNavigation() {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    // ----------------------- DATE ------------------------------
        public String getDate(){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            return formatter.format(date);
        }
        private void setDate() {
            int[] date = StringtoIntDateReverse(getDate());
            dateView.setText(
                    String.format("Ngày %2s tháng %2s năm %4s ",
                            date[2] < 10 ? "0" + date[2] : date[2]+"",
                            date[1] < 10 ? "0" + date[1] : date[1]+"",
                            date[0] + ""
                    )
            );
        }
        public int[] StringtoIntDateReverse(String str) {
            int[] date = new int[3];
            String[] arr = str.split("-");
            date[0] = Integer.parseInt(arr[0]);
            date[1] = Integer.parseInt(arr[1]);
            date[2] = Integer.parseInt(arr[2]);
            return date; // 1999-08-30 -> [1999,08,30]
        }

    // ----------------------- LOADER ----------------------------
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
        private void renderList(List<VanPhongPham> list) {
            countView.setText(list.size()+"");
            mainList.setAdapter( new VPPAdapter2(this, list ));
        }
        public List<VanPhongPham> exampleList(){
            int n = 10;
            List<VanPhongPham> list = new ArrayList<>();
            VanPhongPham vpp = new VanPhongPham(
                    "VPP01","Giấy A4","Hộp","50000",null,"10","A"
            );
            for( int i = 0; i < n; i++ )
                list.add(vpp);
            return list;
        }

}
