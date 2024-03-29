package com.example.androidclient.mycollection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.activity_interview;
import com.example.androidclient.activity_knowledge;
import com.example.androidclient.adapter.interview_adapter.InterviewAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.example.androidclient.mypost.activity_post_interview;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class activity_interview_collection extends Activity {

    RequestQueue requestQueue;
    private userProfile profile;

    private RecyclerView recyclerView;

    private List<InterviewBean> mData;

    private RefreshLayout mRefreshLayout;

    private InterviewAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_collection);
        requestQueue = Volley.newRequestQueue(this);
        profile = (userProfile) getApplicationContext();

        recyclerView = findViewById(R.id.collection_interview_recycler);

        mRefreshLayout = findViewById(R.id.collection_interview_refresh);

        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this));

        mData = new ArrayList<>();

        initData();

        VolleyPost_interview();

        handlerDownPullUpdate();

        super.onStart();

    }

    public void initData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InterviewAdapter(mData);
        recyclerView.setAdapter(adapter);
        LandingAnimator animator = new LandingAnimator();

        recyclerView.setItemAnimator(animator);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        alphaInAnimationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);

        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));

        initListener();

    }

    public void handlerDownPullUpdate() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //mData.add(0, data);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //adapter.notifyDataSetChanged();
                        //adapter.add(data, 0);
                        /*currentPage++;
                        volleyPostInitial(binding.getRoot());
                        mRefreshLayout.finishRefresh(600);*/
                        adapter.removeAll();
                        VolleyPost_interview();
                    }
                }, 600);
                mRefreshLayout.finishRefresh();
            }
        });
    }

    private void initListener() {
        adapter.setOnItemClickListener(new InterviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                VolleyPost_interview_one(position);
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Toast.makeText(activity_interview_collection.this, "no more interview", Toast.LENGTH_SHORT).show();
                mRefreshLayout.finishLoadMore(600);
            }
        });
    }

    public void VolleyPost_interview() {
        String url = "http://120.77.98.16:8080/users_like";
        JsonObjectRequest str = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = null;
                        try {
                            code = response.get("code").toString();
                            Toast.makeText(activity_interview_collection.this, code, Toast.LENGTH_SHORT).show();
                            if (code.equals("00")) {
                                JSONObject data = (JSONObject) response.get("data");
                                JSONObject entities2 = (JSONObject) data.get("interviews");
                                JSONArray array = (JSONArray) entities2.get("entities");
                                Toast.makeText(activity_interview_collection.this, String.valueOf(array.length()), Toast.LENGTH_SHORT).show();

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject buffer = array.getJSONObject(i);

                                    InterviewBean bean = new InterviewBean(buffer.get("interviewId").toString(), buffer.get("userId").toString(),
                                            buffer.get("userName").toString(), buffer.get("title").toString(), buffer.get("description").toString(),
                                            buffer.get("company").toString(), buffer.get("uploadTime").toString(), buffer.get("level").toString(),
                                            buffer.get("interviewTime").toString(), buffer.get("position").toString(), buffer.get("location").toString(),
                                            (Integer) buffer.get("isLiked"));
                                    adapter.add(bean, mData.size());
                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_interview_collection.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("token", profile.getToken());
                return headers;
            }
        };
        requestQueue.add(str);
    }

    public void VolleyPost_interview_one(int position) {
        String url = "http://120.77.98.16:8080/interview_service/query?interviewId="+mData.get(position).getInterview_id();
        JsonObjectRequest str = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = null;
                        try {
                            code = response.get("code").toString();
                            Toast.makeText(activity_interview_collection.this, code, Toast.LENGTH_SHORT).show();
                            if (code.equals("00")) {
                                JSONObject data = (JSONObject) response.get("data");
                                JSONObject buffer = (JSONObject) data.get("interview");
                                JSONArray array = (JSONArray) ((JSONObject) data.get("questions")).get("entities");
                                Toast.makeText(activity_interview_collection.this, String.valueOf(array.length()), Toast.LENGTH_SHORT).show();
                                List<KnowledgeBean> knowledgeBeanList = new ArrayList<>();
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject buffer2 = (JSONObject) array.get(j);
                                    KnowledgeBean knowledgeBean = new KnowledgeBean(buffer2.get("knowledgeId").toString(),
                                            buffer2.get("question_content").toString(), "", buffer2.get("userid").toString(),
                                            buffer2.get("interviewId").toString(), buffer2.get("userName").toString(), "",
                                            buffer2.get("company").toString(), buffer2.get("tag").toString(), buffer2.get("uploadTime").toString(),
                                            (Integer) buffer2.get("isLiked"), 0, 0, 0, 0, 0, 0);
                                    knowledgeBeanList.add(knowledgeBean);
                                }

                                InterviewBean bean = new InterviewBean(buffer.get("interviewId").toString(), buffer.get("userId").toString(),
                                        buffer.get("userName").toString(), buffer.get("title").toString(), buffer.get("description").toString(),
                                        buffer.get("company").toString(), buffer.get("uploadTime").toString(), buffer.get("level").toString(),
                                        buffer.get("interviewTime").toString(), buffer.get("position").toString(), buffer.get("location").toString(),
                                        (Integer) buffer.get("isLiked"));
                                bean.setQuestions(knowledgeBeanList);
                                Intent intent = new Intent(activity_interview_collection.this, activity_interview.class);
                                intent.putExtra("interview", (Serializable) bean);
                                startActivity(intent);
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_interview_collection.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("token", profile.getToken());
                return headers;
            }
        };
        requestQueue.add(str);
    }

}
