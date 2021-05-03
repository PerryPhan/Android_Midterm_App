package com.example.giuaki.Entities;

public class NhanVien {

//    private long id;
    private String maNv;
    private String hoTen;
    private String ngaySinh;
    private String maPb;

//    public NhanVien(long id, String maNv, String hoTen, String ngaySinh, String maPb) {
//        this.id = id;
//        this.maNv = maNv;
//        this.hoTen = hoTen;
//        this.ngaySinh = ngaySinh;
//        this.maPb = maPb;
//    }
    public NhanVien( String maNv, String hoTen, String ngaySinh, String maPb) {
        this.maNv = maNv;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.maPb = maPb;
    }



//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public String getMaNv() {
        return maNv;
    }

    public void setMaNv(String maNv) {
        this.maNv = maNv;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getMaPb() {
        return maPb;
    }

    public void setMaPb(String maPb) {
        this.maPb = maPb;
    }
}
