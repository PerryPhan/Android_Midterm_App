package com.example.giuaki.Request;

import com.example.giuaki.WebService;
import com.example.giuaki.Api.NhanVien;
import com.example.giuaki.Helper.JSONHelper;
import com.example.giuaki.Helper.RequestHelper;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class NhanVienRequest {

    private RequestHelper requestHelper;

    public NhanVienRequest() {
        requestHelper = new RequestHelper();
    }

    public String doGet(String method) {
        String[] request = {"get", String.format("http://%s/NhanVienController-%s", WebService.host(), method)};
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

    public String doPost(NhanVien nhanVien, String method) {
        // Tạo HashMap để tạo requestBody
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("manv", nhanVien.getMaNv());
        hashMap.put("hoten", nhanVien.getHoTen());
        hashMap.put("ngaysinh", nhanVien.getNgaySinh());
        hashMap.put("mapb", nhanVien.getMaPb());

        // Bắt đầu thực hiện request
        requestHelper = new RequestHelper();
        requestHelper.buildRequestBody(hashMap);
        String[] request = {"post", String.format("http://%s/NhanVienController-%s", WebService.host(), method)};
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
