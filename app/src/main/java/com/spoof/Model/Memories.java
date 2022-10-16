package com.spoof.Model;

public class Memories {
    private String IMAGEURL,TITLE;

    public Memories(String imageUrl,String TITLE) {
        IMAGEURL = imageUrl;
        this.TITLE=TITLE;
    }

    public Memories() {
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getImageUrl() {
        return IMAGEURL;
    }

    public void setImageUrl(String imageUrl) {
        IMAGEURL = imageUrl;
    }
}
