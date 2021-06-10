package com.example.giuaki.Api;

public class CapPhat {

    private String soPhieu;
    private String ngayCap;
    private String maVpp;
    private String maNv;
    private String soLuong;

    public CapPhat( String soPhieu, String ngayCap, String maVpp, String maNv, String soLuong) {
        this.soPhieu = soPhieu;
        this.ngayCap = ngayCap;
        this.maVpp = maVpp;
        this.maNv = maNv;
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "CapPhat{" +
                "soPhieu='" + soPhieu + '\'' +
                ", ngayCap='" + ngayCap + '\'' +
                ", maVpp='" + maVpp + '\'' +
                ", maNv='" + maNv + '\'' +
                ", soLuong='" + soLuong + '\'' +
                '}';
    }

    public String getSoPhieu() {
        return soPhieu;
    }

    public void setSoPhieu(String soPhieu) {
        this.soPhieu = soPhieu;
    }

    public String getNgayCap() {
        return ngayCap;
    }

    public void setNgayCap(String ngayCap) {
        this.ngayCap = ngayCap;
    }

    public String getMaVpp() {
        return maVpp;
    }

    public void setMaVpp(String maVpp) {
        this.maVpp = maVpp;
    }

    public String getMaNv() {
        return maNv;
    }

    public void setMaNv(String maNv) {
        this.maNv = maNv;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }
}
