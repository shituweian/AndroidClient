package com.example.androidclient.ui.knowledge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.androidclient.adapter.KnowledgeAdapter;
import com.example.androidclient.adapter.LoadMoreAdapter;
import com.example.androidclient.databinding.FragmentKnowledgeBinding;

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

public class KnowledgeFragment extends Fragment {

    RequestQueue requestQueue;

    private FragmentKnowledgeBinding binding;

    private RecyclerView recyclerView;

    private List<KnowledgeBean> mData;

    private SwipeRefreshLayout mRefreshLayout;

    private KnowledgeAdapter adapter;

    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        KnowledgeViewModel knowledgeViewModel =
                new ViewModelProvider(this).get(KnowledgeViewModel.class);

        binding = FragmentKnowledgeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.knowledge_recycler);

        mRefreshLayout = root.findViewById(R.id.knowledge_refresh);

        requestQueue = Volley.newRequestQueue(this.getContext());

        context = this.getContext();

        return root;
    }

    public void onStart() {

        mData = new ArrayList<>();
        volleyPostInitial(binding.getRoot());

        super.onStart();
        initData();
        handlerDownPullUpdate();
    }

    public void handlerDownPullUpdate() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KnowledgeBean data = new KnowledgeBean();
                data.setQuestion_content("what is the 5 layers of network in computer");
                data.setTag("network");
                data.setCompany("Alibaba");
                data.setUsername("Gao Shiwei");
                mData.add(0, data);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //adapter.notifyDataSetChanged();
                        adapter.add(data, 0);
                        mRefreshLayout.setRefreshing(false);
                        recyclerView.scrollToPosition(0);
                    }
                }, 600);
            }
        });
    }

    public void initData() {

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
                Toast.makeText(getContext(), "您点击的是第" + position + "个条目", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(getActivity(), activity_knowledge.class);
                intent.putExtra("knowledge",(Serializable) mData.get(position));
                startActivity(intent);
            }
        });

        if (adapter instanceof KnowledgeAdapter) {
            ((KnowledgeAdapter) adapter).setOnRefreshListener(new KnowledgeAdapter.OnRefreshListener() {
                @Override
                public void onUpPullRefresh(final KnowledgeAdapter.loaderMoreHolder holder) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Random random = new Random();

                            if (random.nextInt() % 2 == 0) {
                                KnowledgeBean data = new KnowledgeBean();
                                data.setQuestion_content("what is the 5 layers of network in computer");
                                data.setTag("network");
                                data.setCompany("Alibaba");
                                data.setUsername("Gao Shiwei");

                                adapter.add(data, mData.size());
                                holder.update(LoadMoreAdapter.loaderMoreHolder.LOAD_STATE_NORMAL);
                                recyclerView.scrollToPosition(mData.size());
                            } else {
                                holder.update(LoadMoreAdapter.loaderMoreHolder.LOAD_STATE_RELOAD);
                            }
                        }
                    }, 600);
                }
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void volleyPostInitial(View v) {
        final ProgressDialog dialog = new ProgressDialog(this.getContext());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageFirst", 1);
        map.put("pageSizeFirst", 22);
        map.put("pageSecond", 1);
        map.put("pageSizeSecond", 5);
        map.put("pageThird", 1);
        map.put("pageSizeThird", 2);
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
                                    adapter.add(bean, i);
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
                headers.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJnYW9zaGl3ZWlAcXEuY29tIiwiZXhwIjoxNjU3MDgwMzkwLCJpbmZvIjp7ImFkbWluIjowLCJ1c2VybmFtZSI6IjEyMyJ9fQ.9V7GBiGvoxGUDMBUUHelSPWw_v5rDoGvpMCsrpHENiY");
                return headers;
            }
        };
        requestQueue.add(str);
    }

}