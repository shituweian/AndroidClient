package com.example.androidclient.Bean;

import java.io.Serializable;

public class JobBean implements Serializable {
    private String uuid;
    private String company;
    private String jobPosition;
    private String jobLocation;
    private String descriptionShort;
    private String descriptionFull;
    private String salary;
    private String hrContract;
    private String uploadTime;
    private String expiryDate;
    private boolean expired;

    public JobBean(String uuid,
                   String company,
                   String jobPosition,
                   String jobLocation,
                   String descriptionShort,
                   String descriptionFull,
                   String salary,
                   String hrContract,
                   String uploadTime,
                   String expiryDate,
                   boolean expired){
        this.uuid=uuid;
        this.company=company;
        this.jobLocation=jobLocation;
        this.jobPosition=jobPosition;
        this.descriptionFull=descriptionFull;
        this.descriptionShort=descriptionShort;
        this.salary=salary;
        this.hrContract=hrContract;
        this.uploadTime = uploadTime.substring(0,uploadTime.indexOf('.'));
        this.uploadTime=this.uploadTime.replace('T',' ');
        this.expired=expired;
        this.expiryDate = expiryDate.substring(0,uploadTime.indexOf('.'));
        this.expiryDate=this.expiryDate.replace('T',' ');
    }

    public String getCompany(){
        return company;
    }
    public String getUuid(){
        return uuid;
    }
    public String getJobPosition(){
        return jobPosition;
    }

    public String getJobLocation(){
        return jobLocation;
    }

    public String getDescriptionShort(){
        return descriptionShort;
    }
    public String getDescriptionFull(){
        return descriptionFull;
    }

    public String getUploadTime(){
        return uploadTime;
    }

    public String getSalary(){
        return salary;
    }

    public String getExpiryDate(){
        return expiryDate;
    }

    public boolean getExpired(){
        return expired;
    }

    public String getHrContract(){
        return hrContract;
    }
}
