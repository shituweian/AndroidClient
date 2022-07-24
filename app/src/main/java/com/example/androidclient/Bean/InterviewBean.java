package com.example.androidclient.Bean;

import java.io.Serializable;
import java.util.List;

public class InterviewBean implements Serializable {
    private String interview_id;
    private String provider_id;
    private String provider_name;
    private String title;
    private String description;
    private String company;
    private String uploadtime;
    private String level;
    private String interview_time;
    private String position;
    private String location;
    private int isliked;
    private int question_total;
    private int currentPage;
    private int pageSize;
    private List<KnowledgeBean> questions;

    public InterviewBean(String interview_id, String provider_id,String provider_name,
                         String title, String description,String company,String uploadtime,
                         String level,String interview_time,String position,String location,
                         int isliked){
        this.interview_id=interview_id;
        this.provider_id=provider_id;
        this.provider_name=provider_name;
        this.title=title;
        this.description=description;
        this.company=company;
        this.uploadtime=uploadtime.substring(0,uploadtime.indexOf('.'));
        this.uploadtime=this.uploadtime.replace('T',' ');
        this.level=level;
        this.interview_time=interview_time;
        this.position=position;
        this.location=location;
        this.isliked=isliked;
        question_total=0;
    }

    public InterviewBean(String interview_id, String provider_id,String provider_name,
                         String title, String description,String company,String uploadtime,
                         String level,String interview_time,String position,String location,
                         int isliked, int question_total, int currentPage, int pageSize,
                         List<KnowledgeBean> questions){
        this.interview_id=interview_id;
        this.provider_id=provider_id;
        this.provider_name=provider_name;
        this.title=title;
        this.description=description;
        this.company=company;
        this.uploadtime = uploadtime.substring(0,uploadtime.indexOf('.'));
        this.uploadtime=this.uploadtime.replace('T',' ');
        this.level=level;
        this.interview_time=interview_time;
        this.position=position;
        this.location=location;
        this.isliked=isliked;
        this.question_total=question_total;
        this.currentPage=currentPage;
        this.pageSize=pageSize;
        this.questions=questions;
        question_total=questions.size();
    }

    public String getTitle(){
        return title;
    }

    public String getProvider_name(){
        return provider_name;
    }
    public String getProvider_id(){
        return provider_id;
    }
    public String getCompany(){
        return company;
    }
    public String getUploadtime(){
        return uploadtime;
    }
    public String getDescription(){
        return description;
    }
    public String getPosition(){
        return position;
    }
    public String getLocation(){
        return location;
    }
    public String getLevel(){
        return level;
    }
    public List<KnowledgeBean> getQuestions(){
        return questions;
    }

    public int getQuestion_total(){
        return question_total;
    }

    public void setQuestions(List<KnowledgeBean> knowledgeBeans){
        question_total=knowledgeBeans.size();
        this.questions=knowledgeBeans;
    }

    public int getIsliked(){
        return isliked;
    }

    public void setIsliked(int isliked){
        this.isliked=isliked;
    }

    public String getInterview_id(){
        return interview_id;
    }

    public void setInterview_id(String interview_id){
        this.interview_id=interview_id;
    }


}
