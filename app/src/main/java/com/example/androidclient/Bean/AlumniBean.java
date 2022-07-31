package com.example.androidclient.Bean;

import java.io.Serializable;

public class AlumniBean implements Serializable {

    private String email;
    private String name;
    private String school;

    public AlumniBean(String email, String name, String school){
        this.email=email;
        this.name=name;
        this.school=school;
    }

    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }

    public String getSchool(){
        return school;
    }

}
