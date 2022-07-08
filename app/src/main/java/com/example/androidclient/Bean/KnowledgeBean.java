package com.example.androidclient.Bean;

import java.util.List;

public class KnowledgeBean {

    private String knowledge_id;
    private String question_content;
    private String answer_list;
    private String user_id;
    private String interview_id;
    private String username;
    private String comment__list;
    private String company;
    private String tag;
    private String uploadTime;
    private int isliked;
    private int answer_currentPage;
    private int answer_pageSize;
    private int answer_totalRecord;
    private int comment_currentPage;
    private int comment_pageSize;
    private int comment_totalRecord;
    private List<AnswerBean> answers;
    private List<CommentBean> comments;

    public KnowledgeBean() {

    }

    public KnowledgeBean(String knowledge_id, String question_content, String answer_list, String user_id, String interview_id,
                         String username, String comment__list, String company, String tag, String uploadTime, int isliked, int answer_currentPage,
                         int answer_pageSize, int answer_totalRecord, int comment_currentPage, int comment_pageSize, int comment_totalRecord) {
        this.knowledge_id = knowledge_id;
        this.question_content = question_content;
        this.answer_list = answer_list;
        this.user_id = user_id;
        this.interview_id = interview_id;
        this.username = username;
        this.comment__list = comment__list;
        this.company = company;
        this.tag = tag;
        this.uploadTime = uploadTime;
        this.isliked=isliked;
        this.answer_currentPage=answer_currentPage;
        this.answer_pageSize=answer_pageSize;
        this.answer_totalRecord=answer_totalRecord;
        this.comment_currentPage=comment_currentPage;
        this.comment_pageSize=comment_pageSize;
        this.comment_totalRecord=comment_totalRecord;
    }

    public KnowledgeBean(String knowledge_id, String question_content, String answer_list, String user_id, String interview_id,
                         String username, String comment__list, String company, String tag, String uploadTime, int isliked, int answer_currentPage,
                         int answer_pageSize, int answer_totalRecord, int comment_currentPage, int comment_pageSize, int comment_totalRecord,List<AnswerBean> answers, List<CommentBean> comments) {
        this.knowledge_id = knowledge_id;
        this.question_content = question_content;
        this.answer_list = answer_list;
        this.user_id = user_id;
        this.interview_id = interview_id;
        this.username = username;
        this.comment__list = comment__list;
        this.company = company;
        this.tag = tag;
        this.uploadTime = uploadTime;
        this.answers = answers;
        this.comments = comments;
        this.isliked=isliked;
        this.answer_currentPage=answer_currentPage;
        this.answer_pageSize=answer_pageSize;
        this.answer_totalRecord=answer_totalRecord;
        this.comment_currentPage=comment_currentPage;
        this.comment_pageSize=comment_pageSize;
        this.comment_totalRecord=comment_totalRecord;
    }

    public void setQuestion_content(String Question_content) {
        this.question_content = Question_content;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAnswers(List<AnswerBean> answers) {
        this.answers = answers;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }

    public void setIsliked(int isliked){
        this.isliked=isliked;
    }

    public void addAnswer(AnswerBean answer) {
        answers.add(answer);
    }

    public void addComments(CommentBean comment) {
        comments.add(comment);
    }

    public void addAnswer(int position, AnswerBean answer) {
        answers.add(position, answer);
    }

    public void addComments(int position, CommentBean comment) {
        comments.add(position, comment);
    }

    public String getKnowledge_id() {
        return knowledge_id;
    }

    public String getUsername() {
        return username;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public String getAnswer_list() {
        return answer_list;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public String getCompany() {
        return company;
    }

    public String getTag() {
        return tag;
    }

    public String getInterview_id() {
        return interview_id;
    }

    public String getComment__list() {
        return comment__list;
    }

    public int getIsliked(){
        return isliked;
    }

    public List<AnswerBean> getAnswers() {
        return answers;
    }

    public List<CommentBean> getComments() {
        return comments;
    }

}
