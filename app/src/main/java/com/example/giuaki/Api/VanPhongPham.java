package com.example.giuaki.Api;

public class VanPhongPham {

    private String maVpp;
    private String tenVpp;
    private String dvt;
    private String giaNhap;
    private String hinh;
    private String soLuong;
    private String maNcc;

    public VanPhongPham() {
    }

    public VanPhongPham(String maVpp, String tenVpp, String dvt, String giaNhap, String hinh, String soLuong, String maNcc) {
        this.maVpp = maVpp;
        this.tenVpp = tenVpp;
        this.dvt = dvt;
        this.giaNhap = giaNhap;
        this.hinh = hinh;
        this.soLuong = soLuong;
        this.maNcc = maNcc;
    }

    @Override
    public String toString() {
        return "VanPhongPham{" + "maVpp=" + maVpp + ", tenVpp=" + tenVpp + ", dvt=" + dvt + ", giaNhap=" + giaNhap + ", hinh=" + hinh + ", soLuong=" + soLuong + ", maNcc=" + maNcc + '}';
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

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getMaNcc() {
        return maNcc;
    }

    public void setMaNcc(String maNcc) {
        this.maNcc = maNcc;
    }
}
