package com.example.androidclient.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.JobBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.activity_job;
import com.example.androidclient.activity_knowledge;
import com.example.androidclient.adapter.JobAdapter;
import com.example.androidclient.adapter.KnowledgeAdapter;
import com.example.androidclient.adapter.ListViewAdapter;
import com.example.androidclient.adapter.LoadMoreAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.example.androidclient.comment.interviews;
import com.example.androidclient.databinding.FragmentHomeBinding;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.TwoLevelHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
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
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class HomeFragment extends Fragment {

    RequestQueue requestQueue;

    private userProfile profile;

    private FragmentHomeBinding binding;

    private RecyclerView recyclerView;

    private List<JobBean> mData;

    private SmartRefreshLayout mRefreshLayout;

    private JobAdapter jobAdapter;

    private LottieAnimationView loading;

    private int currentPage;

    private FrameLayout frameLayout;

    private Context context;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView=root.findViewById(R.id.job_recycler);

        mRefreshLayout=root.findViewById(R.id.job_refresh);

        loading = root.findViewById(R.id.job_loading);

        frameLayout =root.findViewById(R.id.job_framelayout);

        requestQueue = Volley.newRequestQueue(this.getContext());

        profile=(userProfile)this.getActivity().getApplicationContext();

        currentPage=1;

        context = this.getContext();

        mData=new ArrayList<>();
        initData();
        volleyPostInitial();
        handlerDownPullUpdate();

        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                recyclerView.scrollToPosition(0);
                frameLayout.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }, 2000);

        super.onStart();


        return root;
    }

    public void initData(){
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this.getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        jobAdapter = new JobAdapter(mData);
        recyclerView.setAdapter(jobAdapter);
        LandingAnimator animator = new LandingAnimator();

        recyclerView.setItemAnimator(animator);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(jobAdapter);
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
                        currentPage=1;
                        jobAdapter.removeAll();
                        volleyPostInitial();
                    }
                }, 600);
                mRefreshLayout.finishRefresh();
            }
        });
    }

    private void initListener() {
        jobAdapter.setOnItemClickListener(new JobAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                Intent intent =new Intent(getActivity(), activity_job.class);
                intent.putExtra("job",(Serializable) mData.get(position));
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

    public void volleyPostInitial(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageFirst", currentPage);
        map.put("pageSizeFirst", 22);
        JSONObject jsonObject = new JSONObject(map);
        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/job_service/load", jsonObject,
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if(array.length()!=0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject buffer = array.getJSONObject(i);

                                        JobBean jobBean=new JobBean(buffer.get("uuid").toString(),buffer.get("company").toString(),
                                                buffer.get("jobPosition").toString(),buffer.get("jobLocation").toString(),buffer.get("descriptionShort").toString(),
                                                buffer.get("descriptionFull").toString(),buffer.get("salary").toString(),buffer.get("hrContract").toString(),buffer.get("uploadTime").toString(),
                                                buffer.get("expiryDate").toString(),(boolean)buffer.get("expired"));
                                        jobAdapter.add(jobBean,mData.size());
                                    }
                                    if(currentPage!=1) {
                                        recyclerView.scrollToPosition(mData.size() - 1);
                                    }
                                }else{
                                    currentPage--;
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (code.equals("01")) {
                            Toast.makeText(context, "incorrect password", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("02")) {
                            Toast.makeText(context, "no such account", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}