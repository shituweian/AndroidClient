package com.example.androidclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.AlumniBean;
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.JobBean;
import com.example.androidclient.adapter.AlumniAdapter;
import com.example.androidclient.adapter.interview_detailed_adapter.InterviewDetailedAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.example.androidclient.mypost.activity_post_interview;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class activity_alumni extends Activity {

    RequestQueue requestQueue;
    private userProfile profile;

    private RecyclerView recyclerView;

    private List<AlumniBean> mData;

    private AlumniAdapter adapter;

    private SmartRefreshLayout swipe_knowledge;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumni);

        requestQueue = Volley.newRequestQueue(this);

        profile=(userProfile)getApplicationContext();

        recyclerView = findViewById(R.id.alumni_recyclerView);

        swipe_knowledge = findViewById(R.id.alumni_swipe);

        swipe_knowledge.setRefreshFooter(new BallPulseFooter(this));

        mData=new ArrayList<>();

        initData();

        VolleyPost_interview();
    }

    public void initData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new AlumniAdapter(mData);
        recyclerView.setAdapter(adapter);
        LandingAnimator animator=new LandingAnimator();

        recyclerView.setItemAnimator(animator);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        alphaInAnimationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);

        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));
    }

    public void VolleyPost_interview() {
        String url = "http://120.77.98.16:8080/get_alumni";
        JsonObjectRequest str = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = null;
                        try {
                            code = response.get("code").toString();

                            if (code.equals("00")) {
                                JSONArray data = (JSONArray) response.get("data");

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject buffer = data.getJSONObject(i);

                                    AlumniBean alumniBean =new AlumniBean(buffer.get("email").toString(),buffer.getString("name"),buffer.getString("school"));
                                    adapter.add(alumniBean, mData.size());
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
                Toast.makeText(activity_alumni.this, error.toString(), Toast.LENGTH_SHORT).show();
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

