package com.example.giuaki.Api;

public class ChiTietCungCap {
    private String id;
    private String soPhieu;
    private String maVpp;
    private String soLuong;
    private String thanhTien;

    public ChiTietCungCap(String id ,String soPhieu, String maVpp, String soLuong, String thanhTien) {
        this.id = id;
        this.soPhieu = soPhieu;
        this.maVpp = maVpp;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChiTietCungCap{" +
                "soPhieu='" + soPhieu + '\'' +
                ", maVpp='" + maVpp + '\'' +
                ", soLuong='" + soLuong + '\'' +
                ", thanhTien='" + thanhTien + '\'' +
                '}';
    }

    public String getSoPhieu() {
        return soPhieu;
    }

    public void setSoPhieu(String soPhieu) {
        this.soPhieu = soPhieu;
    }

    public String getMaVpp() {
        return maVpp;
    }

    public void setMaVpp(String maVpp) {
        this.maVpp = maVpp;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(String thanhTien) {
        this.thanhTien = thanhTien;
    }
}
