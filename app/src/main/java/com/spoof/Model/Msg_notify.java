package com.spoof.Model;

public class Msg_notify {

    private String sender,msg,notify_key;

    public Msg_notify(String sender, String msg, String notify_key) {
        this.sender = sender;
        this.msg = msg;
        this.notify_key = notify_key;
    }

    public Msg_notify() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNotify_key() {
        return notify_key;
    }

    public void setNotify_key(String notify_key) {
        this.notify_key = notify_key;
    }
}
