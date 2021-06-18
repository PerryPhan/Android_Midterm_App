package com.example.giuaki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.giuaki.Helper.RequestHelper;

import java.util.concurrent.ExecutionException;

public class Webform extends AppCompatActivity {
    WebView html;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webform);
        // SetControl
        html = findViewById(R.id.HTMLView);
        //
        Bundle b = getIntent().getExtras();
        String loai = b.getString("loai");
        String ma = b.getString("ma");
        displayHtml(loai, ma);
    }
    public void displayHtml( String loai, String ma) {
        RequestHelper requestHelper = new RequestHelper();
        String response = "";
        if( loai.trim().equals("PhongBan") ) {
            String[] request = {"get", String.format("http://%s/PrinterController-printDocsPhongBan?mapb=%s", WebService.host(), ma)};
            try {
                response = requestHelper.execute(request).get();
            } catch (ExecutionException e) {
                response = e.getMessage();
            } catch (InterruptedException e) {
                response = e.getMessage();
            }
        }else {
            String[] request = {"get", String.format("http://%s/PrinterController-printDocsNhanVien?manv=%s", WebService.host(), ma)};
            try {
                response = requestHelper.execute(request).get();
            } catch (ExecutionException e) {
                response = e.getMessage();
            } catch (InterruptedException e) {
                response = e.getMessage();
            }
        }
        if( response == "") return;
        html.loadData(response, "text/html", "UTF-8");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (html != null) {
            html.destroy();
        }
    }

}