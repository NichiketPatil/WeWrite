package com.example.writerz;


public class card {
    private String imgURL;
    private String title;
    private String profile;

    public card(String imgURL, String title,String profile) {
        this.imgURL = imgURL;
        this.title = title;
        this.profile = profile;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
