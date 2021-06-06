package com.example.giuaki.Api;

import org.w3c.dom.Text;

public class PhongBan {

    private String mapb;
    private String tenpb;

    public PhongBan(String mapb, String tenpb) {
        this.mapb = mapb;
        this.tenpb = tenpb;
    }

    @Override
    public String toString() {
        return "PhongBan{" + "mapb=" + mapb + ", tenpb=" + tenpb + '}';
    }

    public String toIDandName(){
        return mapb+"-"+tenpb;

    }

    public String getMapb() {
        return mapb;
    }

    public void setMapb(String mapb) {
        this.mapb = mapb;
    }

    public String getTenpb() {
        return tenpb;
    }

    public void setTenpb(String tenpb) {
        this.tenpb = tenpb;
    }


}
