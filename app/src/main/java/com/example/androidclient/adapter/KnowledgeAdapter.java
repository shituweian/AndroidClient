package com.example.androidclient.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;

import java.util.List;

public class KnowledgeAdapter extends KnowledgeBasedAdapter{

    public static final int TYPE_NORMAL=0;
    public static final int TYPE_LOADER_MORE=1;


    private OnRefreshListener mUpPullRefreshListener;

    public KnowledgeAdapter(List<KnowledgeBean> data){
        super(data);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = getSubView(parent, viewType);
        if (viewType == TYPE_NORMAL) {
            return new KnowledgeInnerHolder(view);
        } else {
            return new loaderMoreHolder(view);
        }


    }

    protected View getSubView(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_NORMAL) {

            view = View.inflate(parent.getContext(), R.layout.knowledge_list_view, null);
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_list_load_more, null);
        }
        return view;
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //if (getItemViewType(position) == TYPE_NORMAL && holder instanceof KnowledgeInnerHolder) {
            ((KnowledgeInnerHolder) holder).setData(mData.get(position), position);
        /*} else if (getItemViewType(position) == TYPE_LOADER_MORE && holder instanceof LoadMoreAdapter.loaderMoreHolder) {
            ((loaderMoreHolder) holder).update(loaderMoreHolder.LOAD_STATE_LOADING);
        }*/
    }

    public int getItemViewType(int position) {
        /*if (position == getItemCount() - 1) {
            return TYPE_LOADER_MORE;
        }*/
        return TYPE_NORMAL;
    }



    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mUpPullRefreshListener = listener;
    }

    public interface OnRefreshListener {
        void onUpPullRefresh(KnowledgeInnerHolder Holder);
    }

    public void remove(int position){
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll(){
        int position=mData.size();
        mData.clear();
        notifyItemRangeRemoved(0,position);
    }

    public void add(KnowledgeBean interview, int position){
        mData.add(position,interview);
        notifyItemInserted(position);
    }

    public class loaderMoreHolder extends RecyclerView.ViewHolder {
        public static final int LOAD_STATE_LOADING = 0;
        public static final int LOAD_STATE_RELOAD = 1;
        public static final int LOAD_STATE_NORMAL = 2;

        private LinearLayout mload;
        private TextView mReload;

        public loaderMoreHolder(@NonNull View itemView) {
            super(itemView);

            mload = itemView.findViewById(R.id.loading);
            mReload = itemView.findViewById(R.id.reload);

            mReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    update(LOAD_STATE_LOADING);
                }
            });
        }

        public void update(int state) {

            mload.setVisibility(View.GONE);
            mReload.setVisibility(View.GONE);

            switch (state) {
                case LOAD_STATE_LOADING:
                    mload.setVisibility(View.VISIBLE);
                    startLoadMore();
                    break;
                case LOAD_STATE_RELOAD:
                    mReload.setVisibility(View.VISIBLE);
                    break;
                case LOAD_STATE_NORMAL:
                    mload.setVisibility(View.GONE);
                    mReload.setVisibility(View.GONE);
                    break;
            }
        }

        private void startLoadMore() {
            if (mUpPullRefreshListener != null) {
                //mUpPullRefreshListener.onUpPullRefresh(this);
            }
        }
    }

}
