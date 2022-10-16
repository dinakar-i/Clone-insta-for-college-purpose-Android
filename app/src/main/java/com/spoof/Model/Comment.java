package com.spoof.Model;

public class Comment {
    private String commander,comment,Date,Time,Key,CommentPostid;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Comment(String commander, String comment, String date, String time,String key,String commentPostid) {
        this.commander = commander;
        this.comment = comment;
        Date = date;
        Time = time;
        Key=key;
        CommentPostid=commentPostid;
    }

    public Comment() {
    }

    public String getCommentPostid() {
        return CommentPostid;
    }

    public void setCommentPostid(String commentPostid) {
        CommentPostid = commentPostid;
    }

    public String getCommander() {
        return commander;
    }

    public void setCommander(String commander) {
        this.commander = commander;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
