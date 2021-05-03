package com.example.giuaki.Entities;

public class VanPhongPham {

    private long id;
    private String maVpp;
    private String tenVpp;
    private String dvt;
    private String giaNhap;
    private byte[] hinh;

    public VanPhongPham(long id, String maVpp, String tenVpp, String dvt, String giaNhap, byte[] hinh) {
        this.id = id;
        this.maVpp = maVpp;
        this.tenVpp = tenVpp;
        this.dvt = dvt;
        this.giaNhap = giaNhap;
        this.hinh = hinh;
    }
    public VanPhongPham( String maVpp, String tenVpp, String dvt, String giaNhap, byte[] hinh) {
        this.maVpp = maVpp;
        this.tenVpp = tenVpp;
        this.dvt = dvt;
        this.giaNhap = giaNhap;
        this.hinh = hinh;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMaVpp() {
        return maVpp;
    }

    public void setMaVpp(String maVpp) {
        this.maVpp = maVpp;
    }

    public String getTenVpp() {
        return tenVpp;
    }

    public void setTenVpp(String tenVpp) {
        this.tenVpp = tenVpp;
    }

    public String getDvt() {
        return dvt;
    }

    public void setDvt(String dvt) {
        this.dvt = dvt;
    }

    public String getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(String giaNhap) {
        this.giaNhap = giaNhap;
    }

    public byte[] getHinh() {
        return hinh;
    }

    public void setHinh(byte[] hinh) {
        this.hinh = hinh;
    }
}
