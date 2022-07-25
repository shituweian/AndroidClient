package com.example.androidclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.AnswerBean;
import com.example.androidclient.Bean.BasedBean_knowledge_detail;
import com.example.androidclient.Bean.CommentBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.adapter.LoadMoreAdapter;
import com.example.androidclient.adapter.knowledge_detailed_adapter.KnowledgeAnswerAdapter;
import com.example.androidclient.adapter.knowledge_detailed_adapter.KnowledgeDetailedBasedAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.example.androidclient.mypost.activity_post_knowledge;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
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
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class activity_knowledge extends Activity {

    RequestQueue requestQueue;
    private userProfile profile;

    private RecyclerView recyclerView_answer;

    private RecyclerView recyclerView_comment;

    private KnowledgeBean knowledge;

    private KnowledgeDetailedBasedAdapter adapter_comment;

    private KnowledgeAnswerAdapter adapter_answer;

    private TextFieldBoxes reply_box;

    private ExtendedEditText reply;

    private SmartRefreshLayout swipe_answer;

    private SmartRefreshLayout swipe_comment;

    private final String[] change = new String[]{"COMMENT", "ANSWER"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        knowledge = (KnowledgeBean) intent.getSerializableExtra("knowledge");


        recyclerView_answer = findViewById(R.id.knowledge_activity_recyclerView_answer);
        recyclerView_comment=findViewById(R.id.knowledge_activity_recyclerView_comment);

        reply_box = findViewById(R.id.activity_knowledge_box);
        reply = findViewById(R.id.activity_knowledge_extend);
        profile=(userProfile)getApplicationContext();
        swipe_answer=findViewById(R.id.activity_knowledge_swipe_answer);
        swipe_comment=findViewById(R.id.activity_knowledge_swipe_comment);


        swipe_answer.setRefreshFooter(new BallPulseFooter(this));
        swipe_comment.setRefreshFooter(new BallPulseFooter(this));
        recyclerView_answer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                reply_box.clearFocus();
                return false;
            }
        });

        recyclerView_comment.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent){
                reply_box.clearFocus();
                return false;
            }
        });

        recyclerView_answer.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView_comment.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


        reply_box.getIconImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reply_box.getLabelText().equals(change[0])) {
                    reply_box.setLabelText(change[1]);
                    swipe_answer.setVisibility(View.VISIBLE);
                    swipe_comment.setVisibility(View.GONE);
                } else {
                    reply_box.setLabelText(change[0]);
                    swipe_answer.setVisibility(View.GONE);
                    swipe_comment.setVisibility(View.VISIBLE);
                }
            }
        });
        reply_box.getEndIconImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reply_box.getLabelText().equals(change[0])) {
                    volleySendComment(view);
                }else{
                    volleySendAnswer(view);
                }
            }
        });
        reply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager managerinput=(InputMethodManager) activity_knowledge.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        managerinput.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        handlerDownPullUpdate();

    }

    public void handlerDownPullUpdate() {
        swipe_answer.setOnRefreshListener(new OnRefreshListener() {
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
                        VolleyPost_Knowledge_one();
                    }
                }, 600);
                swipe_answer.finishRefresh();
            }
        });
        swipe_comment.setOnRefreshListener(new OnRefreshListener() {
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
                        VolleyPost_Knowledge_one();
                    }
                }, 600);
                swipe_comment.finishRefresh();
            }
        });

    }

    public void onStart() {
        super.onStart();
        initData_answer();
        initData_comment();
    }

    public void initData_answer() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_answer.setLayoutManager(layoutManager);
        adapter_answer = new KnowledgeAnswerAdapter(knowledge);
        recyclerView_answer.setAdapter(adapter_answer);
        LandingAnimator animator = new LandingAnimator();

        recyclerView_answer.setItemAnimator(animator);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter_answer);
        alphaInAnimationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);

        recyclerView_answer.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));

    }

    public void initData_comment() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_comment.setLayoutManager(layoutManager);
        adapter_comment = new KnowledgeDetailedBasedAdapter(knowledge);
        recyclerView_comment.setAdapter(adapter_comment);
        LandingAnimator animator = new LandingAnimator();

        recyclerView_comment.setItemAnimator(animator);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter_comment);
        alphaInAnimationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);

        recyclerView_comment.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));

    }

    public void volleySendComment(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("knowledgeId", knowledge.getKnowledge_id());
        map.put("provider", profile.getEmail());
        map.put("content",reply.getText().toString());
        JSONObject jsonObject = new JSONObject(map);
        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/knowledge_service/comment", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = "";
                        try {
                            code = (String) response.get("code");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code.equals("00")) {

                            try {
                                Toast.makeText(activity_knowledge.this, "successful", Toast.LENGTH_SHORT).show();
                                reply.setText("");
                                reply_box.clearFocus();
                                VolleyPost_Knowledge_one();
                            } catch (Exception e) {
                                Toast.makeText(activity_knowledge.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (code.equals("99")) {
                            Toast.makeText(activity_knowledge.this, "incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_knowledge.this, error.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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

    public void volleySendAnswer(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("knowledgeId", knowledge.getKnowledge_id());
        map.put("provider", profile.getEmail());
        map.put("content",reply.getText().toString());
        JSONObject jsonObject = new JSONObject(map);
        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/knowledge_service/answer", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = "";
                        try {
                            code = (String) response.get("code");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code.equals("00")) {

                            try {
                                Toast.makeText(activity_knowledge.this, "successful", Toast.LENGTH_SHORT).show();
                                reply.setText("");
                                reply_box.clearFocus();
                                VolleyPost_Knowledge_one();
                            } catch (Exception e) {
                                Toast.makeText(activity_knowledge.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (code.equals("99")) {
                            Toast.makeText(activity_knowledge.this, "incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_knowledge.this, error.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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

    public void VolleyPost_Knowledge_one() {
        String url = "http://120.77.98.16:8080/knowledge_service?uuid="+knowledge.getKnowledge_id();
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
                                knowledge=bean;
                                initData_answer();
                                initData_comment();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_knowledge.this, error.toString(), Toast.LENGTH_SHORT).show();
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
