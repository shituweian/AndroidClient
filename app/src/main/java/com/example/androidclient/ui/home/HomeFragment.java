package com.example.androidclient.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.androidclient.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerView List;

    private List<InterviewBean> mData;

    private SwipeRefreshLayout mRefreshLayout;

    private ListViewAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List = root.findViewById(R.id.recycler_view);

        mRefreshLayout=root.findViewById(R.id.home_refresh);


        return root;
    }

    public void onStart() {

        super.onStart();
        initData();
        handlerDownPullUpdate();
    }


    public void handlerDownPullUpdate(){
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                InterviewBean data=new InterviewBean();
                data.name="gsw";
                mData.add(0,data);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
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
            interview.name=i+"dwa";
            interview.time="dwa"+i;
            mData.add(interview);
        }

        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        List.setLayoutManager(layoutManager);
        adapter = new LoadMoreAdapter(mData);
        List.setAdapter(adapter);

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
                                data.name="GaoShiwei";
                                mData.add(data);

                                adapter.notifyDataSetChanged();
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