package com.example.androidclient.mycollection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.AnswerBean;
import com.example.androidclient.Bean.CommentBean;
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.activity_knowledge;
import com.example.androidclient.adapter.KnowledgeAdapter;
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

public class activity_knowledge_collection extends Activity {
    RequestQueue requestQueue;
    private userProfile profile;

    private RecyclerView recyclerView;

    private List<KnowledgeBean> mData;

    private RefreshLayout mRefreshLayout;

    private KnowledgeAdapter adapter;

    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_collection);

        requestQueue = Volley.newRequestQueue(this);
        profile = (userProfile) getApplicationContext();

        recyclerView = findViewById(R.id.collection_knowledge_recycler);

        mRefreshLayout = findViewById(R.id.collection_knowledge_refresh);

        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this));

        mData = new ArrayList<>();

        initData();

        handlerDownPullUpdate();

        VolleyPost_Knowledge();

    }

    public void initData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new KnowledgeAdapter(mData);
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
                        VolleyPost_Knowledge();
                    }
                }, 600);
                mRefreshLayout.finishRefresh();
            }
        });
    }

    private void initListener() {
        adapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                VolleyPost_Knowledge_one(position);
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Toast.makeText(activity_knowledge_collection.this, "no more knowledge", Toast.LENGTH_SHORT).show();
                mRefreshLayout.finishLoadMore(600);
            }
        });

    }

    public void VolleyPost_Knowledge() {
        String url = "http://120.77.98.16:8080/users_like";
        JsonObjectRequest str = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = null;
                        try {
                            code = response.get("code").toString();

                            if (code.equals("00")) {
                                JSONObject data = (JSONObject) response.get("data");
                                JSONObject entities = (JSONObject) data.get("entities");
                                JSONObject entities2=(JSONObject)entities.get("knowledge");
                                JSONArray array = (JSONArray) entities2.get("entities");
                                Toast.makeText(activity_knowledge_collection.this, String.valueOf(array.length()), Toast.LENGTH_SHORT).show();

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject buffer = array.getJSONObject(i);

                                    KnowledgeBean bean = new KnowledgeBean(buffer.get("knowledgeId").toString(), buffer.get("question_content").toString(),
                                            buffer.get("answer_list").toString(), buffer.get("userid").toString(), buffer.get("interviewId").toString(), buffer.get("userName").toString(),
                                            buffer.get("comment_list").toString(), buffer.get("company").toString(), buffer.get("tag").toString(),
                                            buffer.get("uploadTime").toString(), (Integer) buffer.get("isLiked"));
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
                Toast.makeText(activity_knowledge_collection.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    public void VolleyPost_Knowledge_one(int position) {
        String url = "http://120.77.98.16:8080/knowledge_service?uuid="+mData.get(position).getKnowledge_id();
        JsonObjectRequest str = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = null;
                        try {
                            code = response.get("code").toString();

                            if (code.equals("00")) {
                                JSONObject buffer=(JSONObject) response.get("data");
                                    JSONObject comments = buffer.getJSONObject("comments");
                                    JSONObject comment_queryInfo = comments.getJSONObject("queryInfo");
                                    JSONArray comment_array = comments.getJSONArray("entities");
                                    List<CommentBean> commentBeanList = new ArrayList<>();
                                    for (int j = 0; j < comment_array.length(); j++) {
                                        JSONObject comment = comment_array.getJSONObject(j);
                                        CommentBean commentBean = new CommentBean(comment.get("knowledgeCommentId").toString(), comment.get("knowledgeId").toString(),
                                                comment.get("providerId").toString(), comment.get("userName").toString(), comment.get("content").toString(), comment.get("uploadTime").toString());
                                        commentBeanList.add(commentBean);
                                    }

                                    JSONObject answers = buffer.getJSONObject("answers");
                                    JSONObject answer_queryInfo = answers.getJSONObject("queryInfo");
                                    JSONArray answer_array = answers.getJSONArray("entities");
                                    List<AnswerBean> answerBeanList = new ArrayList<>();
                                    for (int j = 0; j < answer_array.length(); j++) {
                                        JSONObject answer = answer_array.getJSONObject(j);
                                        AnswerBean answerBean = new AnswerBean(answer.get("knowledgeAnswerId").toString(), answer.get("knowledgeId").toString(), answer.get("providerId").toString(),
                                                answer.get("userName").toString(), answer.get("content").toString(), answer.get("uploadTime").toString());
                                        answerBeanList.add(answerBean);
                                    }

                                    KnowledgeBean bean = new KnowledgeBean(buffer.get("knowledgeId").toString(), buffer.get("question_content").toString(),
                                            buffer.get("answer_list").toString(), buffer.get("userid").toString(), buffer.get("interviewId").toString(), buffer.get("userName").toString(),
                                            buffer.get("comment_list").toString(), buffer.get("company").toString(), buffer.get("tag").toString(),
                                            buffer.get("uploadTime").toString(), (Integer) buffer.get("isLiked"), (Integer) answer_queryInfo.get("currentPage"), (Integer) answer_queryInfo.get("pageSize"),
                                            (Integer) answer_queryInfo.get("totalRecord"), (Integer) comment_queryInfo.get("currentPage"), (Integer) comment_queryInfo.get("pageSize"),
                                            (Integer) comment_queryInfo.get("totalRecord"));
                                    bean.setAnswers(answerBeanList);
                                    bean.setComments(commentBeanList);
                                Intent intent =new Intent(activity_knowledge_collection.this, activity_knowledge.class);
                                intent.putExtra("knowledge",(Serializable) bean);
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
                Toast.makeText(activity_knowledge_collection.this, error.toString(), Toast.LENGTH_SHORT).show();
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
