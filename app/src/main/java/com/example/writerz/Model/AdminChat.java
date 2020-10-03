package com.example.writerz.Model;

public class AdminChat {
    private  String mt;
    private  String m;


    public AdminChat(String mt, String m) {
        this.mt = mt;
        this.m = m;
    }

    public AdminChat() {

    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}
