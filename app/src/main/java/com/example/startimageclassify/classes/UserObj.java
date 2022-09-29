package com.example.startimageclassify.classes;

public class UserObj {
    public String uname, fname, email;

    public UserObj(){

    }

    public UserObj(String uname, String fname, String email) {
        this.uname = uname;
        this.fname = fname;
        this.email = email;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
