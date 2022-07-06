package com.example.androidclient.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.R;
import com.example.androidclient.adapter.ListViewAdapter;
import com.example.androidclient.adapter.LoadMoreAdapter;
import com.example.androidclient.comment.interviews;
import com.example.androidclient.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private RecyclerView List;

    private java.util.List<InterviewBean> mData;

    private SwipeRefreshLayout mRefreshLayout;

    private LoadMoreAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List = root.findViewById(R.id.knowledge_recycler);

        mRefreshLayout=root.findViewById(R.id.knowledge_refresh);

        return root;
    }

    public void onStart(){

        super.onStart();
        initData();
        handlerDownPullUpdate();
    }

    public void handlerDownPullUpdate(){
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                InterviewBean data=new InterviewBean();
                data.setName("Gao shiwei");
                data.setTime("27 July");
                data.setContent("I love U guys");
                mData.add(0,data);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //adapter.notifyDataSetChanged();
                        adapter.add(data,0);
                        mRefreshLayout.setRefreshing(false);
                    }
                },600);
            }
        });
    }

    public void initData(){
        mData =new ArrayList<>();

        for(int i=0;i<10;i++){
            InterviewBean interview=new InterviewBean();
            interview.setName("user "+i);
            interview.setTime("27 July");
            interview.setContent(interviews.content[i]);
            mData.add(interview);
        }

        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        List.setLayoutManager(layoutManager);
        adapter = new LoadMoreAdapter(mData);
        List.setAdapter(adapter);
        LandingAnimator animator=new LandingAnimator();

        List.setItemAnimator(animator);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        alphaInAnimationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);

        List.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));

        initListener();
    }

    private void initListener(){
        adapter.setOnItemClickListener(new ListViewAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), "您点击的是第" + position + "个条目", Toast.LENGTH_SHORT).show();
            }
        });

        if(adapter instanceof LoadMoreAdapter){
            ((LoadMoreAdapter)adapter).setOnRefreshListener(new LoadMoreAdapter.OnRefreshListener() {
                @Override
                public void onUpPullRefresh(final LoadMoreAdapter.loaderMoreHolder holder) {
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            Random random =new Random();

                            if(random.nextInt()%2==0){
                                InterviewBean data=new InterviewBean();
                                data.setName("Gao shiwei");
                                data.setTime("27 July");
                                data.setContent("I love U guys");

                                adapter.add(data,mData.size());
                                holder.update(LoadMoreAdapter.loaderMoreHolder.LOAD_STATE_NORMAL);
                            }else {
                                holder.update(LoadMoreAdapter.loaderMoreHolder.LOAD_STATE_RELOAD);
                            }
                        }
                    },600);
                }
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}