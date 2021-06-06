package com.example.giuaki.Helper;

import com.example.giuaki.Api.*;

import org.json.*;

import java.util.ArrayList;
import java.util.List;

public class JSONHelper {

    /**
     * Kiểm tra xem response có lỗi hay không
     * @param json
     * @return
     */
    public static String verifyJSON(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            if (jsonObj.has("error")) {
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
     * @param json
     * @param objectName
     * @return Object entities
     * @throws JSONException
     */
    public static List<Object> parseJSON(String json, String objectName) throws JSONException {
        JSONObject jsonObj = new JSONObject(json);
        JSONArray viewData = jsonObj.getJSONArray("viewData");
        System.out.println(json);

        List<Object> entitiesList = new ArrayList<>();

        if (objectName.trim().equals("PhongBan")) {
            for(int i = 0; i < viewData.length(); i++) {
                PhongBan phongBan = new PhongBan(viewData.getJSONObject(i).getString("MAPB"),
                        viewData.getJSONObject(i).getString("TENPB"));
                entitiesList.add(phongBan);
            }
        }

        if (objectName.trim().equals("NhanVien")) {
            for(int i = 0; i < viewData.length(); i++) {
                NhanVien nhanVien = new NhanVien(
                        viewData.getJSONObject(i).getString("MANV"),
                        viewData.getJSONObject(i).getString("HOTEN"),
                        viewData.getJSONObject(i).getString("NGAYSINH"),
                        viewData.getJSONObject(i).getString("MAPB")
                );
                entitiesList.add(nhanVien);
            }
        }

        if (objectName.trim().equals("VanPhongPham")) {
            for(int i = 0; i < viewData.length(); i++) {
                VanPhongPham vanPhongPham = new VanPhongPham(
                        viewData.getJSONObject(i).getString("MAVPP"),
                        viewData.getJSONObject(i).getString("TENVPP"),
                        viewData.getJSONObject(i).getString("DVT"),
                        viewData.getJSONObject(i).getString("GIANHAP"),
                        viewData.getJSONObject(i).get("HINH").toString(),
                        viewData.getJSONObject(i).getInt("SOLUONG"),
                        viewData.getJSONObject(i).getString("MANCC")
                );
                entitiesList.add(vanPhongPham);
            }
        }

        if (objectName.trim().equals("CapPhat")) {
            for(int i = 0; i < viewData.length(); i++) {
                CapPhat capPhat = new CapPhat(
                        viewData.getJSONObject(i).getString("SOPHIEU"),
                        viewData.getJSONObject(i).getString("NGAYCAP"),
                        viewData.getJSONObject(i).getString("MAVPP"),
                        viewData.getJSONObject(i).getString("MANV"),
                        (long)viewData.getJSONObject(i).getInt("SOLUONG")
                );
                entitiesList.add(capPhat);
            }
        }

        if (objectName.trim().equals("NhaCungCap")) {

        }

        if (objectName.trim().equals("PhieuCungCap")) {

        }

        return entitiesList;
    }

    /**
     * 
     * @param json
     * @return
     */
    public static List<String> parseJSON(String json) {
        return new ArrayList<>();
    }

    public void printPhongBanEntity(List<Object> phongBanList) {
        for(Object obj: phongBanList) {
            PhongBan pb = (PhongBan)obj;
            System.out.println(pb.getTenpb());
        }
    }
}
