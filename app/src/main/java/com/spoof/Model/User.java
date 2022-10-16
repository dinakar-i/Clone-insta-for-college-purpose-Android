package com.spoof.Model;

public class User {
    private String Username;
    private String UserId;
    private String Password;
    private String ImageUrl;
    private String Email,Cmail;
    private String Bio;
    private String Dept,Cast;
    private String RegisterNumber;

    public String getRegisterNumber() {
        return RegisterNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        RegisterNumber = registerNumber;
    }

    public String getDept() {
        return Dept;
    }

    public String getCast() {
        return Cast;
    }

    public void setCast(String cast) {
        Cast = cast;
    }

    public void setDept(String dept) {
        Dept = dept;
    }

    public String getCmail() {
        return Cmail;
    }

    public void setCmail(String cmail) {
        Cmail = cmail;
    }

    private String Fullname;

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public User() {
    }


    public User(String username, String userId, String password, String imageUrl, String email,String cmail, String bio, String fullname,String Dept,String Cast,String RegisterNumber) {
        this.Username = username;
        this.UserId = userId;
        this.Password = password;
        this.ImageUrl = imageUrl;
        this.Email = email;
        this.Bio = bio;
        this.Fullname=fullname;
        this.Cmail=cmail;
        this.Dept=Dept;
        this.Cast=Cast;
        this.RegisterNumber=RegisterNumber;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        this.Bio = bio;
    }
}
