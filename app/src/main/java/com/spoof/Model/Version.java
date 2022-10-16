package com.spoof.Model;

public class Version {
    private Long Current;
    private String Link;

    public Version() {
    }

    public Version(Long current, String link) {
       Current = current;
        Link = link;
    }

    public Long getCurrent() {
        return Current;
    }

    public void setCurrent(Long current) {
        Current = current;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
