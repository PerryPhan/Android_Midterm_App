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
        displayHtml();
    }
    public void displayHtml() {
        RequestHelper requestHelper = new RequestHelper();
        String[] request = {"get", String.format("http://%s/PrinterController-printDocs", WebService.host())};
        String response = "";
        try {
            response = requestHelper.execute(request).get();
        }
        catch(ExecutionException e){
            response = e.getMessage();
        }
        catch(InterruptedException e){
            response = e.getMessage();
        }

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