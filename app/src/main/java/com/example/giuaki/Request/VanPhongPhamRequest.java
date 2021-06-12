package com.example.giuaki.Request;

import android.annotation.SuppressLint;

import com.example.giuaki.WebService;
import com.example.giuaki.Api.NhanVien;
import com.example.giuaki.Api.VanPhongPham;
import com.example.giuaki.Helper.RequestHelper;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VanPhongPhamRequest {

    public String getExtensionName(File file) {
        String fileName = file.getName();
        String[] split = fileName.split("\\.");
        return split.length != 0 ? split[split.length - 1] : "";
    }

    public String doGet(String method) {
        RequestHelper requestHelper = new RequestHelper();
        String[] request = {"get", String.format("http://%s/VanPhongPhamController-%s",
                WebService.host(), method)};
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

    public String doPost(VanPhongPham vanPhongPham, File imageFile, String method) {
        RequestHelper requestHelper = new RequestHelper();
        // Tạo multipart form
        @SuppressLint("DefaultLocale") MultipartBody.Builder formDataPartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("mavpp", vanPhongPham.getMaVpp())
                .addFormDataPart("tenvpp", vanPhongPham.getTenVpp())
                .addFormDataPart("dvt", vanPhongPham.getDvt())
                .addFormDataPart("gianhap", vanPhongPham.getGiaNhap())
                .addFormDataPart("soluong", vanPhongPham.getSoLuong())
                .addFormDataPart("mancc", vanPhongPham.getMaNcc());

        // Tạo input:file cho file image theo đúng media type
        if (imageFile != null) {
            RequestBody image = RequestBody.create(imageFile,
                    MediaType.parse(String.format("image/%s", getExtensionName(imageFile))));
            formDataPartBuilder.addFormDataPart("hinh", imageFile.getName(), image);
        }

        RequestBody formDataPart = formDataPartBuilder.build();
        requestHelper.setRequestBody(formDataPart);

        // Bắt đầu thực hiện request
        String[] request = {"post", String.format("http://%s/VanPhongPhamController-%s",
                WebService.host(), method)};
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

    public String getNCCList() {
        RequestHelper requestHelper = new RequestHelper();
        String[] request = {"get", String.format("http://%s/NhaCungCapController-%s",
                WebService.host(), "show")};
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
