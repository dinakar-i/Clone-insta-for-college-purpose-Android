package com.spoof.Model;

public class AU {
    private String URL,TITLE,STATUS,TYPE;

    public AU(String URL, String TITLE, String STATUS,String TYPE) {
        this.URL = URL;
        this.TITLE = TITLE;
        this.STATUS = STATUS;
        this.TYPE=TYPE;
    }

    public AU() {
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}
