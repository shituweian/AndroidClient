package com.example.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.JobBean;
import com.example.androidclient.adapter.interview_detailed_adapter.InterviewDetailedAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.vstechlab.easyfonts.EasyFonts;

import br.tiagohm.markdownview.MarkdownView;
import me.codeboy.android.aligntextview.AlignTextView;

public class activity_job extends Activity {

    private JobBean jobBean;
    private TextView company;
    private AlignTextView uploadtime;
    private AlignTextView expire_time;
    private MarkdownView md;
    private AlignTextView position;
    private AlignTextView salary;
    private AlignTextView hr;
    private TextView location;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        Intent intent =getIntent();
        jobBean=(JobBean) intent.getSerializableExtra("job");
        company=findViewById(R.id.job_activity_company);
        uploadtime=findViewById(R.id.job_activity_upload_time);
        expire_time=findViewById(R.id.job_activity_expire_time);
        md=findViewById(R.id.job_activity_md);
        position=findViewById(R.id.job_activity_position);
        salary=findViewById(R.id.job_activity_salary);
        hr=findViewById(R.id.job_activity_hr);
        location=findViewById(R.id.job_activity_location);

        company.setTypeface(EasyFonts.caviarDreamsBold(this));
        location.setTypeface(EasyFonts.captureIt(this));

        company.setText(jobBean.getCompany());
        uploadtime.setText(jobBean.getUploadTime());

        expire_time.setText(jobBean.getExpiryDate());

        md.loadMarkdown(jobBean.getDescriptionFull());

        if(jobBean.getJobPosition()!=null&&!jobBean.getJobPosition().equals("null")) {
            position.setText(jobBean.getJobPosition());
        }else{
            position.setText("");
        }
        if(jobBean.getSalary()!=null&&!jobBean.getSalary().equals("null")) {
            salary.setText(jobBean.getSalary());
        }else{
            salary.setText("");
        }
        if(jobBean.getHrContract()!=null&&!jobBean.getHrContract().equals("null")) {
            hr.setText(jobBean.getHrContract());
        }else{
            hr.setText("");
        }

        location.setText(jobBean.getJobLocation());
    }

}
