package com.example.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.AnswerBean;
import com.example.androidclient.Bean.CommentBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.adapter.KnowledgeAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.vstechlab.easyfonts.EasyFonts;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class activity_knowledge_search extends Activity {
    RequestQueue requestQueue;
    private userProfile profile;
    private TextView company_text;
    private TextView tag_text;
    private NiceSpinner company;
    private NiceSpinner tag;
    private String company_id;
    private String tag_id;
    private LottieAnimationView lottie;
    private int currentPage=1;
    private RecyclerView recyclerView;

    private List<KnowledgeBean> mData;

    private RefreshLayout mRefreshLayout;

    private KnowledgeAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_search);
        company_text=findViewById(R.id.knowledge_search_company_text);
        tag_text=findViewById(R.id.knowledge_search_tag_text);
        company=findViewById(R.id.knowledge_search_company);
        tag=findViewById(R.id.knowledge_search_tag);
        lottie=findViewById(R.id.knowledge_search_lottie);
        recyclerView=findViewById(R.id.knowledge_search_recycler);
        mRefreshLayout=findViewById(R.id.knowledge_search_refresh);

        requestQueue = Volley.newRequestQueue(this);
        profile=(userProfile)getApplicationContext();
        company_text.setTypeface(EasyFonts.captureIt(this));
        tag_text.setTypeface(EasyFonts.captureIt(this));
        List<String> dataset = new LinkedList<>(Arrays.asList("others","Alibaba", "Amzon", "Tencent", "Google", "Facebook","Microsoft"));
        company.attachDataSource(dataset);

        List<String> dataset2 = new LinkedList<>(Arrays.asList("others","Network", "Operation System", "Database", "Data Structure", "Design Pattern","Algorithm"));
        tag.attachDataSource(dataset2);

        company.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                company_id=dataset.get(position);

            }
        });

        tag.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                tag_id=dataset2.get(position);
                Toast.makeText(activity_knowledge_search.this, tag_id+" "+company_id, Toast.LENGTH_SHORT).show();
            }
        });

        mData = new ArrayList<>();
        handlerDownPullUpdate();
        initData();

        lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_knowledge_search.this, "lottie", Toast.LENGTH_SHORT).show();
                lottie.setRepeatCount(-1);
                lottie.setSpeed((float)3);
                lottie.playAnimation();
                Thread thread =new Thread(){
                    public void run(){
                        try {
                            sleep(2000);
                            volleyPostInitial();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
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
                        currentPage=1;
                        adapter.removeAll();
                        volleyPostInitial();
                    }
                }, 600);
                mRefreshLayout.finishRefresh();
            }
        });
    }

    public void initData() {
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this));
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

    private void initListener() {
        adapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                Intent intent =new Intent(activity_knowledge_search.this, activity_knowledge.class);
                intent.putExtra("knowledge",(Serializable) mData.get(position));
                startActivity(intent);
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                volleyPostInitial();
                mRefreshLayout.finishLoadMore(600);
            }
        });


    }

    public void volleyPostInitial() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageFirst", currentPage);
        map.put("pageSizeFirst", 5);
        map.put("pageSecond", 1);
        map.put("pageSizeSecond", 5);
        map.put("pageThird", 1);
        map.put("pageSizeThird", 5);
        map.put("type", 0);
        map.put("tag1",company_id);
        map.put("tag2",tag_id);
        JSONObject jsonObject = new JSONObject(map);
        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/knowledge_load", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = "";
                        JSONObject data = new JSONObject();
                        JSONArray array = new JSONArray();
                        try {
                            code = (String) response.get("code");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code.equals("00")) {
                            try {
                                data = (JSONObject) response.get("data");
                                array = (JSONArray) data.get("entities");
                                Toast.makeText(activity_knowledge_search.this, "success", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if(array.length()!=0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject buffer = array.getJSONObject(i);


                                        JSONObject comments = buffer.getJSONObject("comments");
                                        JSONObject comment_queryInfo = comments.getJSONObject("queryInfo");
                                        JSONArray comment_array = comments.getJSONArray("entities");
                                        List<CommentBean> commentBeanList = new ArrayList<>();
                                        for (int j = 0; j < comment_array.length(); j++) {
                                            JSONObject comment = comment_array.getJSONObject(j);
                                            CommentBean commentBean = new CommentBean(comment.get("knowledgeCommentId").toString(), comment.get("knowledgeId").toString(),
                                                    comment.get("providerId").toString(), "", comment.get("content").toString(), comment.get("uploadTime").toString());
                                            commentBeanList.add(commentBean);
                                        }

                                        JSONObject answers = buffer.getJSONObject("answers");
                                        JSONObject answer_queryInfo = answers.getJSONObject("queryInfo");
                                        JSONArray answer_array = answers.getJSONArray("entities");
                                        List<AnswerBean> answerBeanList = new ArrayList<>();
                                        for (int j = 0; j < answer_array.length(); j++) {
                                            JSONObject answer = answer_array.getJSONObject(j);
                                            AnswerBean answerBean = new AnswerBean(answer.get("knowledgeAnswerId").toString(), answer.get("knowledgeId").toString(), answer.get("providerId").toString(),
                                                    "", answer.get("content").toString(), answer.get("uploadTime").toString());
                                            answerBeanList.add(answerBean);
                                        }

                                        KnowledgeBean bean = new KnowledgeBean(buffer.get("knowledgeId").toString(), buffer.get("question_content").toString(),
                                                buffer.get("answer_list").toString(), buffer.get("userid").toString(), buffer.get("interviewId").toString(), "",
                                                buffer.get("comment_list").toString(), buffer.get("company").toString(), buffer.get("tag").toString(),
                                                buffer.get("uploadTime").toString(), (Integer) buffer.get("isLiked"), (Integer) answer_queryInfo.get("currentPage"), (Integer) answer_queryInfo.get("pageSize"),
                                                (Integer) answer_queryInfo.get("totalRecord"), (Integer) comment_queryInfo.get("currentPage"), (Integer) comment_queryInfo.get("pageSize"),
                                                (Integer) comment_queryInfo.get("totalRecord"));
                                        bean.setAnswers(answerBeanList);
                                        bean.setComments(commentBeanList);
                                        adapter.add(bean, mData.size());

                                       }
                                    if(currentPage!=1) {
                                        recyclerView.scrollToPosition(mData.size() - 1);
                                    }
                                }else{
                                    currentPage--;
                                }
                                lottie.cancelAnimation();
                            } catch (JSONException e) {
                                Toast.makeText(activity_knowledge_search.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (code.equals("01")) {
                            Toast.makeText(activity_knowledge_search.this, "incorrect password", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("02")) {
                            Toast.makeText(activity_knowledge_search.this, "no such account", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_knowledge_search.this, error.toString(), Toast.LENGTH_SHORT).show();
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
