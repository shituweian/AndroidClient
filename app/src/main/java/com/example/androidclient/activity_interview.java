package com.example.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.OvershootInterpolator;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.adapter.interview_adapter.InterviewAdapter;
import com.example.androidclient.adapter.interview_detailed_adapter.InterviewDetailedAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class activity_interview extends Activity {

    RequestQueue requestQueue;
    private userProfile profile;

    private RecyclerView recyclerView;

    private InterviewBean interviewBean;

    private InterviewDetailedAdapter adapter;

    private SmartRefreshLayout swipe_knowledge;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interview_activity);
        requestQueue= Volley.newRequestQueue(this);
        Intent intent =getIntent();
        interviewBean =(InterviewBean) intent.getSerializableExtra("interview");

        recyclerView=findViewById(R.id.interview_activity_recyclerView);

        swipe_knowledge=findViewById(R.id.interview_activity_swipe_answer);

        swipe_knowledge.setRefreshFooter(new BallPulseFooter(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

    }

    public void onStart(){
        super.onStart();
        initData();
    }

    public void initData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new InterviewDetailedAdapter(interviewBean);
        recyclerView.setAdapter(adapter);
        LandingAnimator animator=new LandingAnimator();

        recyclerView.setItemAnimator(animator);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        alphaInAnimationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);

        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));

    }

}
