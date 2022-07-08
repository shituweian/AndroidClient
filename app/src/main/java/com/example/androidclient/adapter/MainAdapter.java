package com.example.androidclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.R;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    private Context content;
    private List<InterviewBean> dataset;

    public MainAdapter(Context content, List<InterviewBean> dataset){
        this.content=content;
        this.dataset=dataset;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype){
        View v= LayoutInflater.from(content).inflate(R.layout.interview_list_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    public int getItemCount(){
        return dataset.size();
    }

    public void remove(int position){
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public void add(InterviewBean interview,int position){
        dataset.add(position,interview);
        notifyItemInserted(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView time;
        public ViewHolder(View itemview){
            super(itemview);
            name=itemview.findViewById(R.id.listview_name);
            time=itemView.findViewById(R.id.listview_time);
        }

    }

}
