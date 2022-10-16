package com.spoof.Model;

public class MemorieImage {
    private String ImageUrl;

    public MemorieImage(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public MemorieImage() {
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
