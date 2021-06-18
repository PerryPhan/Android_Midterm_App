package com.example.giuaki.Request;

import com.example.giuaki.Api.CapPhat;
import com.example.giuaki.Api.NhanVien;
import com.example.giuaki.Helper.RequestHelper;
import com.example.giuaki.WebService;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class CapPhatRequest {
    private RequestHelper requestHelper;

    public CapPhatRequest() {
        requestHelper = new RequestHelper();
    }

    public String doGet(String method) {
        String[] request = {"get", String.format("http://%s/CapPhatController-%s", WebService.host(), method)};
        String response = "";
        try {
            response = new RequestHelper().execute(request).get();
        }
        catch(ExecutionException e){
            response = e.getMessage();
        }
        catch(InterruptedException e){
            response = e.getMessage();
        }
        return response;
    }

    public String doPost(CapPhat cp, String method) {
        // Tạo HashMap để tạo requestBody
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sophieu", cp.getSoPhieu());
        hashMap.put("mavpp", cp.getMaVpp());
        hashMap.put("manv", cp.getMaNv());
        hashMap.put("soluong", cp.getSoLuong());

        // Bắt đầu thực hiện request
        requestHelper = new RequestHelper();
        requestHelper.buildRequestBody(hashMap);
        String[] request = {"post", String.format("http://%s/CapPhatController-%s", WebService.host(), method)};
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
        return response;
    }
}
