package com.spoof.Model;

public class News {
    private String ImageUrl,Publisher,CurrentDate,CurrentTime,newsKey,newsText,newsTitle,whoSee;

    public News(String imageUrl, String publisher, String currentDate, String currentTime, String newsKey, String newsText, String newsTitle, String whoSee) {
        ImageUrl = imageUrl;
        Publisher = publisher;
        CurrentDate = currentDate;
        CurrentTime = currentTime;
        this.newsKey = newsKey;
        this.newsText = newsText;
        this.newsTitle = newsTitle;
        this.whoSee = whoSee;
    }

    public News() {
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }

    public String getNewsKey() {
        return newsKey;
    }

    public void setNewsKey(String newsKey) {
        this.newsKey = newsKey;
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getWhoSee() {
        return whoSee;
    }

    public void setWhoSee(String whoSee) {
        this.whoSee = whoSee;
    }
}
