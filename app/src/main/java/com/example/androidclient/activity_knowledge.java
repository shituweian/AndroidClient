package com.example.androidclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.androidclient.Bean.BasedBean_knowledge_detail;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.adapter.LoadMoreAdapter;
import com.example.androidclient.adapter.knowledge_detailed_adapter.KnowledgeDetailedBasedAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

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

    private RecyclerView recyclerView;

    private KnowledgeBean knowledge;

    private KnowledgeDetailedBasedAdapter adapter;

    private TextFieldBoxes reply_box;

    private ExtendedEditText reply;

    private SmartRefreshLayout swipe;

    private final String[] change = new String[]{"COMMENT", "ANSWER"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        knowledge = (KnowledgeBean) intent.getSerializableExtra("knowledge");
        recyclerView = findViewById(R.id.knowledge_activity_recyclerView);
        reply_box = findViewById(R.id.activity_knowledge_box);
        reply = findViewById(R.id.activity_knowledge_extend);
        profile=(userProfile)getApplicationContext();
        swipe=findViewById(R.id.activity_knowledge_swipe);


        swipe.setRefreshFooter(new BallPulseFooter(this));

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                reply_box.clearFocus();
                return false;
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        reply_box.getIconImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reply_box.getLabelText().equals(change[0])) {
                    reply_box.setLabelText(change[1]);
                } else {
                    reply_box.setLabelText(change[0]);
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

    }

    public void onStart() {
        super.onStart();
        initData();
    }

    public void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new KnowledgeDetailedBasedAdapter(knowledge);
        recyclerView.setAdapter(adapter);
        LandingAnimator animator = new LandingAnimator();

        recyclerView.setItemAnimator(animator);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        alphaInAnimationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);

        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));

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

    public void volleyGetKnowledge(View v) {
        String url="http://120.77.98.16:8080/knowledge_service?uuid="+knowledge.getKnowledge_id();
        JsonObjectRequest str = new JsonObjectRequest(Request.Method.GET, url,null,
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
