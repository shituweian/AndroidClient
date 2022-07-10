package com.example.androidclient.applicationContent;

import android.app.Application;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class userProfile extends Application {
    private String email;
    private String username;
    private String school;
    private String date;
    private String type;
    private String company;
    private String token;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            initTypeface();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getSchool() {
        return school;
    }

    public String getCompany() {
        return company;
    }

    public String getToken() {
        return token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void initTypeface() throws NoSuchFieldException {
        Typeface newfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Regular.ttf");
        Field monospace = Typeface.class.getDeclaredField("MONOSPACE");
        monospace.setAccessible(true);
        try {
            monospace.set(null, newfont);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
