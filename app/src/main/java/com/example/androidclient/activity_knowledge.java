package com.example.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.OvershootInterpolator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.BasedBean_knowledge_detail;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.adapter.LoadMoreAdapter;
import com.example.androidclient.adapter.knowledge_detailed_adapter.KnowledgeDetailedBasedAdapter;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class activity_knowledge extends Activity {

    RequestQueue requestQueue;

    private RecyclerView recyclerView;

    private KnowledgeBean knowledge;

    private KnowledgeDetailedBasedAdapter adapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        requestQueue= Volley.newRequestQueue(this);
        Intent intent=getIntent();
        knowledge=(KnowledgeBean) intent.getSerializableExtra("knowledge");
        recyclerView=findViewById(R.id.knowledge_activity_recyclerView);

    }

    public void onStart() {
        super.onStart();
        initData();
    }

    public void initData(){
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new KnowledgeDetailedBasedAdapter(knowledge);
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
