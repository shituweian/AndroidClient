package com.example.androidclient.Bean;

public class InterviewBean {
    private String name;
    private String time;
    private String content;

    public void setName(String name){
        this.name=name;
    }

    public void setTime(String time){
        this.time=time;
    }

    public void setContent(String content){
        this.content=content;
    }

    public String[] getAll(){
        String[] ans=new String[3];
        ans[0]=name;
        ans[1]=time;
        ans[2]=content;
        return ans;
    }
}
