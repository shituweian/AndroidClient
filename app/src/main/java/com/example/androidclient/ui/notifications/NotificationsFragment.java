package com.example.androidclient.ui.notifications;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.activity_interview;
import com.example.androidclient.adapter.interview_adapter.InterviewAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.example.androidclient.databinding.FragmentNotificationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

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

public class NotificationsFragment extends Fragment {

    RequestQueue requestQueue;
    private userProfile profile;

    private FragmentNotificationsBinding binding;

    private RecyclerView recyclerView;

    private List<InterviewBean> mData;

    private RefreshLayout mRefreshLayout;

    private InterviewAdapter adapter;

    private Context context;

    private int currentPage=1;

    private FloatingActionButton add;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView=root.findViewById(R.id.interview_recycler);

        mRefreshLayout=root.findViewById(R.id.interview_refresh);

        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this.getContext()));

        requestQueue = Volley.newRequestQueue(this.getContext());

        context = this.getContext();

        profile=(userProfile)this.getActivity().getApplicationContext();


        add=root.findViewById(R.id.interview_floatingButton);

        mData=new ArrayList<>();
        initData();
        volleyPostInitial(binding.getRoot());

        super.onStart();

        return root;
    }


    public void initData(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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

    private void initListener(){
        adapter.setOnItemClickListener(new InterviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent =new Intent(getActivity(), activity_interview.class);
                intent.putExtra("interview",(Serializable) mData.get(position));
                startActivity(intent);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                volleyPostInitial(binding.getRoot());
                mRefreshLayout.finishLoadMore(600);
            }
        });
    }

    public void volleyPostInitial(View v) {
        final ProgressDialog dialog = new ProgressDialog(this.getContext());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageFirst", currentPage);
        map.put("pageSizeFirst", 5);
        map.put("pageSecond", 1);
        map.put("pageSizeSecond", 5);
        map.put("pageThird", 1);
        map.put("pageSizeThird", 5);
        map.put("type", 0);
        map.put("tag1", 0);
        map.put("tag2", 0);
        JSONObject jsonObject = new JSONObject(map);
        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/interview_service/load", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code="";
                        JSONObject data=new JSONObject();
                        JSONArray array=new JSONArray();
                        try{
                            code=response.get("code").toString();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(code.equals("00")){
                            try{
                                data=(JSONObject) response.get("data");
                                array=(JSONArray) data.get("entities");
                                for(int i=0;i<array.length();i++){
                                    JSONObject buffer=array.getJSONObject(i);

                                    JSONObject questions=(JSONObject) buffer.get("questions");
                                    JSONObject question_info=(JSONObject) questions.get("queryInfo");
                                    JSONArray entities=(JSONArray) questions.get("entities");
                                    List<KnowledgeBean> knowledgeBeanList=new ArrayList<>();
                                    for(int j=0;j<entities.length();j++){
                                        JSONObject buffer2=(JSONObject) entities.get(j);
                                        KnowledgeBean knowledgeBean=new KnowledgeBean(buffer2.get("knowledgeId").toString(),
                                                buffer2.get("question_content").toString(),"",buffer2.get("userid").toString(),
                                                buffer2.get("interviewId").toString(),buffer2.get("userName").toString(),"",
                                                buffer2.get("company").toString(),buffer2.get("tag").toString(),buffer2.get("uploadTime").toString(),
                                                (Integer)buffer2.get("isLiked"),0,0,0,0,0,0);
                                        knowledgeBeanList.add(knowledgeBean);
                                    }

                                    InterviewBean bean=new InterviewBean(buffer.get("interviewId").toString(),buffer.get("userId").toString(),
                                            buffer.get("userName").toString(),buffer.get("title").toString(),buffer.get("description").toString(),
                                            buffer.get("company").toString(),buffer.get("uploadTime").toString(),buffer.get("level").toString(),
                                            buffer.get("interviewTime").toString(),buffer.get("position").toString(),buffer.get("location").toString(),
                                            (Integer) buffer.get("isLiked"));

                                    bean.setQuestions(knowledgeBeanList);

                                    adapter.add(bean,mData.size());
                                }

                                recyclerView.scrollToPosition(mData.size()-1);

                            }catch(Exception e){
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }else if (code.equals("01")) {
                            Toast.makeText(context, "incorrect password", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("02")) {
                            Toast.makeText(context, "no such account", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}