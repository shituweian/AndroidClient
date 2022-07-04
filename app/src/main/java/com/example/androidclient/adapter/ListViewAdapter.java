package com.example.androidclient.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.R;

import java.util.List;

import me.codeboy.android.aligntextview.AlignTextView;

public abstract class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected final List<InterviewBean> mData;
    private OnItemClickListener mOnItemClickListener;

    public ListViewAdapter(List<InterviewBean> mData) {
        this.mData = mData;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

       View view = getSubView(parent, viewType);
       return new InnerHolder(view);

    }

    protected abstract View getSubView(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((InnerHolder)holder).setData(mData.get(position),position);
    }

    @Override
    public int getItemCount() {
        if(mData!=null){
            return mData.size();
        }
        return 0;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    public class InnerHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView time;
        private AlignTextView content;
        private int mPosition;

        public InnerHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.listview_name);
            time=itemView.findViewById(R.id.listview_time);
            content=itemView.findViewById(R.id.listview_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClick(mPosition);
                    }
                }
            });

        }

        public void setData(InterviewBean interviewBean){
            String[] contents=interviewBean.getAll();
            name.setText(contents[0]);
            time.setText(contents[1]);
            content.setText(contents[2]);
        }

        public void setData(InterviewBean interviewBean,int position){
            this.mPosition=position;
            String[] contents=interviewBean.getAll();
            name.setText(contents[0]);
            time.setText(contents[1]);
            content.setText(contents[2]);
        }

    }

}
