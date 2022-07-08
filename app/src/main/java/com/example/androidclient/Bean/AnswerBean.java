package com.example.androidclient.Bean;

public class AnswerBean {
    private String knowledgeAnswer_id;
    private String knowledge_id;
    private String providerId;
    private String userName;
    private String content;
    private String uploadTime;

    public AnswerBean(){

    }

    public AnswerBean(String knowledgeAnswer_id,String knowledge_id,String providerId,String userName,String content,String uploadTime){
        this.knowledgeAnswer_id=knowledgeAnswer_id;
        this.knowledge_id=knowledge_id;
        this.providerId=providerId;
        this.userName=userName;
        this.content=content;
        this.uploadTime=uploadTime;
    }

    public String getKnowledgeAnswer_id(){
        return knowledgeAnswer_id;
    }

    public String getKnowledge_id(){
        return knowledge_id;
    }

    public String getProviderId(){
        return providerId;
    }

    public String getUserName(){
        return userName;
    }

    public String getContent(){
        return content;
    }

    public String getUploadTime(){
        return uploadTime;
    }

    public String[] getAll(){
        String[] ans=new String[6];
        ans[0]=knowledgeAnswer_id;
        ans[1]=knowledge_id;
        ans[2]=providerId;
        ans[3]=userName;
        ans[4]=content;
        ans[5]=uploadTime;
        return ans;
    }

}
