package com.example.giuaki.Api;

import java.util.List;

public class PhieuCungCap {

    private String soPhieu;
    private String trangThai;
    private String maNcc;
    private String ngaygiao;
    private String ngaydat = null;
    private String tongtien = null;
//    private List<ChiTietCungCap> chitiet = null;

    public PhieuCungCap(String soPhieu, String trangThai, String maNcc, String ngaygiao, String tongtien) {
        this.soPhieu = soPhieu.toUpperCase();
        this.trangThai = trangThai.toUpperCase();
        this.maNcc = maNcc.toUpperCase();
        this.ngaygiao = ngaygiao;
        this.tongtien = tongtien;
    }

    public String getTongtien() {
        return tongtien;
    }

    public void setTongtien(String tongtien) {
        this.tongtien = tongtien;
    }

//    public List<ChiTietCungCap> getChitiet() {
//        return chitiet;
//    }

//    public void setChitiet(List<ChiTietCungCap> chitiet) {
//        this.chitiet = chitiet;
//    }

    @Override
    public String toString() {
        return "PhieuCungCap{" +
                "soPhieu='" + soPhieu + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", maNcc='" + maNcc + '\'' +
                ", ngaygiao='" + ngaygiao + '\'' +
                ", ngaydat='" + ngaydat + '\'' +
                ", tongtien='" + tongtien + '\'' +
                '}';
    }

    public String getNgaydat() {
        return ngaydat;
    }

    public void setNgaydat(String ngaydat) {
        this.ngaydat = ngaydat;
    }

    public String getNgaygiao() {
        return ngaygiao;
    }

    public void setNgaygiao(String ngaygiao) {
        this.ngaygiao = ngaygiao;
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
