package com.example.giuaki.Helper;

import android.util.Log;

import com.example.giuaki.Api.*;

import org.json.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONHelper {

    /**
     * Kiểm tra xem response có lỗi hay không
     * @param json - json string được gửi về từ server
     * @return - PASS hoặc là error message
     */
    public static String verifyJSON(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            if (!jsonObj.getBoolean("success")) {
                return jsonObj.getString("message");
            }
        }
        catch (JSONException error) {
            return error.getMessage();
        }
        return "PASS";
    }

    /**
     * parse cái string json lại theo các entities
     * @param json - json string được gửi về từ server
     * @param objectName - tên của các entity
     * @return Object entities
     * @throws JSONException
     */
    public static List<Object> parseJSON(String json, String objectName) throws JSONException {
        JSONObject jsonObj = new JSONObject(json);
        JSONArray viewData = jsonObj.getJSONArray("viewData");
        System.out.println(json);

        List<Object> entitiesList = new ArrayList<>();

        if (objectName.trim().equals("NhaCungCap")) {
            for(int i = 0; i < viewData.length(); i++) {
                NhaCungCap nhaCungCap = new NhaCungCap(
                        viewData.getJSONObject(i).get("MANCC").toString(),
                        viewData.getJSONObject(i).get("TENNCC").toString(),
                        viewData.getJSONObject(i).get("EMAIL").toString()
                );
                entitiesList.add(nhaCungCap);
            }
        }

        if (objectName.trim().equals("PhongBan")) {
            for(int i = 0; i < viewData.length(); i++) {
                PhongBan phongBan = new PhongBan(
                        viewData.getJSONObject(i).get("MAPB").toString(),
                        viewData.getJSONObject(i).get("TENPB").toString()
                );
                entitiesList.add(phongBan);
            }
        }

        if (objectName.trim().equals("NhanVien")) {
            for(int i = 0; i < viewData.length(); i++) {
                NhanVien nhanVien = new NhanVien(
                        viewData.getJSONObject(i).get("MANV").toString(),
                        viewData.getJSONObject(i).get("HOTEN").toString(),
                        viewData.getJSONObject(i).get("NGAYSINH").toString(),
                        viewData.getJSONObject(i).get("MAPB").toString()
                );
                entitiesList.add(nhanVien);
            }
        }

        if (objectName.trim().equals("VanPhongPham")) {
            for(int i = 0; i < viewData.length(); i++) {
                VanPhongPham vanPhongPham = new VanPhongPham(
                        viewData.getJSONObject(i).get("MAVPP").toString(),
                        viewData.getJSONObject(i).get("TENVPP").toString(),
                        viewData.getJSONObject(i).get("DVT").toString(),
                        viewData.getJSONObject(i).get("GIANHAP").toString(),
                        viewData.getJSONObject(i).get("HINH").toString(),
                        viewData.getJSONObject(i).get("SOLUONG").toString(),
                        viewData.getJSONObject(i).get("MANCC").toString()
                );
                entitiesList.add(vanPhongPham);
            }
        }

        if (objectName.trim().equals("CapPhat")) {
            for(int i = 0; i < viewData.length(); i++) {
                CapPhat capPhat = new CapPhat(
                        viewData.getJSONObject(i).get("SOPHIEU").toString(),
                        viewData.getJSONObject(i).get("NGAYCAP").toString(),
                        viewData.getJSONObject(i).get("MAVPP").toString(),
                        viewData.getJSONObject(i).get("MANV").toString(),
                        viewData.getJSONObject(i).get("SOLUONG").toString()
                );
                entitiesList.add(capPhat);
            }
        }

        if (objectName.trim().equals("PhieuCungCap")) {
            for(int i = 0; i < viewData.length(); i++) {
                PhieuCungCap phieuCungCap = new PhieuCungCap(
                        viewData.getJSONObject(i).get("SOPHIEU").toString(),
                        viewData.getJSONObject(i).get("TRANGTHAI").toString(),
                        viewData.getJSONObject(i).get("MANCC").toString(),
                        viewData.getJSONObject(i).get("NGAYGIAO").toString(),
                        viewData.getJSONObject(i).get("THANHTIEN").toString()
                );
                entitiesList.add(phieuCungCap);
            }
            Log.d("data",viewData.length()+"");
        }

        if (objectName.trim().equals("ChiTiet")) {
            for(int i = 0; i < viewData.length(); i++) {
                ChiTietCungCap chiTietCungCap = new ChiTietCungCap(
                        viewData.getJSONObject(i).get("ID").toString(),
                        viewData.getJSONObject(i).get("SOPHIEU").toString(),
                        viewData.getJSONObject(i).get("MAVPP").toString(),
                        viewData.getJSONObject(i).get("SOLUONG").toString(),
                        viewData.getJSONObject(i).get("THANHTIEN").toString()
                );
                entitiesList.add(chiTietCungCap);
            }
            Log.d("data",viewData.length()+"");
        }

        return entitiesList;
    }

    /**
     * Phương thức dùng để parse tự động tất cả phần tử trong mảng viewData
     * @param json - json string được gửi về từ server
     * @return - 1 cái string chứa tất cả dữ liệu trong đó, nhưng để chỉ show ra để test kết quả
     */
    public static String rawParseJSON(String json) {
        String parse = "";

        try {
            // Khởi tạo json object
            JSONObject jsonObj = new JSONObject(json);
            // Trích xuất array viewData để lấy dữ liệu
            JSONArray viewData = jsonObj.getJSONArray("viewData");
            for(int i = 0; i < viewData.length(); i++) {
                JSONObject jsonData = viewData.getJSONObject(i);
                // Iterator khóa trong từng phần tử json object của viewData
                Iterator<String> keys = jsonData.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object data = jsonData.get(key);
                    if (data != null) parse += String.format("%s,", data.toString());
                }
            }
        }
        catch (JSONException error) {
            parse = error.getMessage();
        }

        return parse;
    }

    /**
     * Phương thức dùng để parse các hàm thống kê, báo cáo
     * @param json - json string được gửi về từ server
     * @return - ArrayList chứa toàn bộ dữ liệu trong đó
     */
    public List<String> parseJSON(String json) {
        return new ArrayList<>();
    }


    public void printPhongBanEntity(List<Object> phongBanList) {
        for(Object obj: phongBanList) {
            PhongBan pb = (PhongBan)obj;
            System.out.println(pb.getTenpb());
        }
    }


}
