package com.spoof.Model;

public class Post {
    private String PostImage,PostId,PostDate,PostTime,Caption,Publisher;
    public Post(String postImage, String postId, String postDate, String postTime, String caption, String publisher) {
        PostImage = postImage;
        PostId = postId;
        PostDate = postDate;
        PostTime = postTime;
        Caption = caption;
        Publisher = publisher;
    }

    public Post() {
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }

    public String getPostTime() {
        return PostTime;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }
}
