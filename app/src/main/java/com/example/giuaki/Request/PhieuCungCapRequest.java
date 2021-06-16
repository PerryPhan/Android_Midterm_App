package com.example.giuaki.Request;

import android.util.Log;

import com.example.giuaki.WebService;
import com.example.giuaki.Api.ChiTietCungCap;
import com.example.giuaki.Api.PhieuCungCap;
import com.example.giuaki.Helper.RequestHelper;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class PhieuCungCapRequest {

    public String doGet(String method) {
        RequestHelper requestHelper = new RequestHelper();
        String[] request = {"get", String.format("http://%s/PhieuCungCapController-%s", WebService.host(), method)};
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

    public String doPost(PhieuCungCap phieuCungCap,
                         ChiTietCungCap chiTietCungCap,
                         String method) {
        RequestHelper requestHelper = new RequestHelper();
        // Tạo HashMap để tạo requestBody
        HashMap<String, String> hashMap = new HashMap<>();

        if (phieuCungCap != null) {
            hashMap.put("sophieu", phieuCungCap.getSoPhieu());
            hashMap.put("trangthai", phieuCungCap.getTrangThai());
            hashMap.put("mancc", phieuCungCap.getMaNcc());
            hashMap.put("ngaydat", phieuCungCap.getMaNcc());
            hashMap.put("ngaygiao", phieuCungCap.getNgaygiao());
        }
        if (chiTietCungCap != null) {
            hashMap.put("sophieu", chiTietCungCap.getSoPhieu());
            hashMap.put("mavpp", chiTietCungCap.getMaVpp());
            hashMap.put("soluong", chiTietCungCap.getSoLuong());
            hashMap.put("thanhtien", chiTietCungCap.getThanhTien());
        }

        // Bắt đầu thực hiện request
        requestHelper.buildRequestBody(hashMap);
        String[] request = {"post", String.format("http://%s/PhieuCungCapController-%s",
                WebService.host(), method)};
        String response = "";
        try {
            response = requestHelper.execute(request).get();
            Log.d("data",response+"");
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
