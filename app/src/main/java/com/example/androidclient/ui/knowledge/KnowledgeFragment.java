package com.example.androidclient.ui.knowledge;

import static java.lang.Thread.sleep;

import android.app.ProgressDialog;
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
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.activity_knowledge;
import com.example.androidclient.activity_knowledge_add;
import com.example.androidclient.activity_knowledge_search;
import com.example.androidclient.activity_login_in;
import com.example.androidclient.activity_register;
import com.example.androidclient.adapter.KnowledgeAdapter;
import com.example.androidclient.adapter.KnowledgeBasedAdapter;
import com.example.androidclient.adapter.LoadMoreAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.example.androidclient.databinding.FragmentKnowledgeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.header.FalsifyFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.header.TwoLevelHeader;
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

import jahirfiquitiva.libs.fabsmenu.FABsMenuLayout;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class KnowledgeFragment extends Fragment {

    RequestQueue requestQueue;
    private userProfile profile;

    private FragmentKnowledgeBinding binding;

    private RecyclerView recyclerView;

    private List<KnowledgeBean> mData;

    private RefreshLayout mRefreshLayout;

    private KnowledgeAdapter adapter;

    private Context context;

    private int currentPage=1;

    private TitleFAB add;

    private TitleFAB search;

    private FABsMenuLayout fab_layout;

    private FrameLayout frameLayout;

    private LottieAnimationView loading;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        KnowledgeViewModel knowledgeViewModel =
                new ViewModelProvider(this).get(KnowledgeViewModel.class);

        binding = FragmentKnowledgeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.knowledge_recycler);

        mRefreshLayout = root.findViewById(R.id.knowledge_refresh);

        frameLayout=root.findViewById(R.id.knowledge_framelayout);

        loading=root.findViewById(R.id.knowledge_loading);

        //mRefreshLayout.setRefreshHeader(new TwoLevelHeader(this.getContext()));



        requestQueue = Volley.newRequestQueue(this.getContext());

        context = this.getContext();

        profile=(userProfile)this.getActivity().getApplicationContext();

        add=root.findViewById(R.id.knowledge_floatingButton_add);

        search=root.findViewById(R.id.knowledge_floatingButton_search);

        fab_layout=root.findViewById(R.id.knowledge_fab_layout);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_knowledge_add.class);

                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_knowledge_search.class);

                startActivity(intent);
            }
        });

        mData = new ArrayList<>();
        initData();
        volleyPostInitial(binding.getRoot());
        handlerDownPullUpdate();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                frameLayout.setVisibility(View.VISIBLE);
                fab_layout.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }, 2000);

        super.onStart();

        return root;
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
                        volleyPostInitial(binding.getRoot());
                    }
                }, 600);
                mRefreshLayout.finishRefresh();
            }
        });
    }

    public void initData() {
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this.getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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
                Intent intent =new Intent(getActivity(), activity_knowledge.class);
                intent.putExtra("knowledge",(Serializable) mData.get(position));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void volleyPostInitial(View v) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageFirst", currentPage);
        map.put("pageSizeFirst", 5);
        map.put("pageSecond", 1);
        map.put("pageSizeSecond", 5);
        map.put("pageThird", 1);
        map.put("pageSizeThird", 5);
        map.put("type", 0);
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
                                        adapter.add(bean, mData.size());
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

    public void volleyPostAdd(View v) {
        final ProgressDialog dialog = new ProgressDialog(this.getContext());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageFirst", currentPage);
        map.put("pageSizeFirst", 10);
        map.put("pageSecond", 1);
        map.put("pageSizeSecond", 5);
        map.put("pageThird", 1);
        map.put("pageSizeThird", 5);
        map.put("type", 0);
        map.put("tag1", 0);
        map.put("tag2", 0);
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject buffer = array.getJSONObject(i);


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
                                    adapter.add(bean, mData.size());
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


}