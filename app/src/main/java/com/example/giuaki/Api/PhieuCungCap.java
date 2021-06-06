package com.example.giuaki.Api;

public class PhieuCungCap {

    private String soPhieu;
    private String trangThai;
    private String maNcc;

    public PhieuCungCap(String soPhieu, String trangThai, String maNcc) {
        this.soPhieu = soPhieu;
        this.trangThai = trangThai;
        this.maNcc = maNcc;
    }

    public String getSoPhieu() {
        return soPhieu;
    }

    public void setSoPhieu(String soPhieu) {
        this.soPhieu = soPhieu;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaNcc() {
        return maNcc;
    }

    public void setMaNcc(String maNcc) {
        this.maNcc = maNcc;
    }
}
