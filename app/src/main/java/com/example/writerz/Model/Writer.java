package com.example.writerz.Model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class Writer {

    private String un;
    private String pURL;
    private String lat;
    private String lon;
    private String no;
    private String h1URL;
    private String h2URL;
    private String ut;
    private String loc;


    public Writer(String un, String pURL, String lat, String lon, String no, String h1URL, String h2URL, String ut, String loc) {
        this.un = un;
        this.pURL = pURL;
        this.lat = lat;
        this.lon = lon;
        this.no = no;
        this.h1URL = h1URL;
        this.h2URL = h2URL;
        this.ut = ut;
        this.loc = loc;
    }

    public Writer() {}


    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public String getpURL() {
        return pURL;
    }

    public void setpURL(String pURL) {
        this.pURL = pURL;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getH1URL() {
        return h1URL;
    }

    public void setH1URL(String h1URL) {
        this.h1URL = h1URL;
    }

    public String getH2URL() {
        return h2URL;
    }

    public void setH2URL(String h2URL) {
        this.h2URL = h2URL;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public String getLoc(Context mContext){
        String city = "",subLocality = "";
        try {
                    Geocoder geocoder;
                    List<Address> addresses = null;
                    Double latitude, longitude;
                    geocoder = new Geocoder(mContext,Locale.getDefault());
                    latitude = Double.parseDouble(getLat());
                    longitude = Double.parseDouble(getLon());
                 addresses = geocoder.getFromLocation(latitude,longitude, 1);
                 city = addresses.get(0).getLocality();
                 subLocality = addresses.get(0).getSubLocality();

        }
        catch (Exception e){
           // Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

//        String address = addresses.get(0).getAddressLine(0);
//        String city2 = addresses.get(0).getSubAdminArea();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();
        return subLocality.concat(", "+city);
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
