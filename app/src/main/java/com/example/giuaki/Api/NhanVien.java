package com.example.giuaki.Api;

public class NhanVien {

    private String maNv;
    private String hoTen;
    private String ngaySinh;
    private String maPb;

    public NhanVien( String maNv, String hoTen, String ngaySinh, String maPb) {
        this.maNv = maNv.toUpperCase();
        this.hoTen = hoTen.toUpperCase();
        this.ngaySinh = ngaySinh;
        this.maPb = maPb.toUpperCase();
    }

    @Override
    public String toString() {
        return "NhanVien{" + "maNv=" + maNv + ", hoTen=" + hoTen + ", ngaySinh=" + ngaySinh + ", maPb=" + maPb + '}';
    }

    public String toSpinnerString() {
        if(maNv.equals("empty")){
            return hoTen;
        }
        return maNv+", "+hoTen+", "+formatDate(ngaySinh, false)+", "+maPb;
    }

    public String formatDate(String str, boolean toSQL ){
        String[] date ;
        String result = "";
        if( toSQL ){
            date = str.split("/");
            result = date[2] +"-"+ date[1] +"-"+ date[0];
        }else{
            date = str.split("-");
            result = date[2] +"/"+ date[1] +"/"+ date[0];
        }

        return result;
    }

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
