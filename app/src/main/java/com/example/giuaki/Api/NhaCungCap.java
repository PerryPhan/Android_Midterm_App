package com.example.giuaki.Api;

public class NhaCungCap {
    private String maNCC;
    private String tenNCC;
    private String emailNCC;

    public NhaCungCap(String maNCC, String tenNCC, String emailNCC) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.emailNCC = emailNCC;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getEmailNCC() {
        return emailNCC;
    }

    public void setEmailNCC(String emailNCC) {
        this.emailNCC = emailNCC;
    }
}
