package com.example.androidclient.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.activity_knowledge;
import com.github.rubensousa.raiflatbutton.RaiflatButton;

import java.util.List;

import me.codeboy.android.aligntextview.AlignTextView;

public abstract class KnowledgeBasedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected final List<KnowledgeBean> mData;
    private OnItemClickListener mOnItemClickListener;

    public KnowledgeBasedAdapter(List<KnowledgeBean> mData){
        this.mData=mData;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = getSubView(parent, viewType);
        return new KnowledgeInnerHolder(view);
    }


    protected abstract View getSubView(ViewGroup parent, int viewType);

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((KnowledgeInnerHolder)holder).setData(mData.get(position),position);
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

    public class KnowledgeInnerHolder extends RecyclerView.ViewHolder{

        private AlignTextView question;
        private RaiflatButton tag;
        private RaiflatButton company;
        private AlignTextView username;
        private int position;

    public KnowledgeInnerHolder(View itemView){
        super(itemView);
        question=itemView.findViewById(R.id.knowledge_question);
        tag=itemView.findViewById(R.id.knowledge_tag);
        company=itemView.findViewById(R.id.knowledge_company);
        username=itemView.findViewById(R.id.knowledge_username);

        itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public void setData(KnowledgeBean knowledgeBean){
        question.setText(knowledgeBean.getQuestion_content());
        tag.setText(" "+knowledgeBean.getTag()+" ");
        company.setText(" "+knowledgeBean.getCompany()+" ");
        username.setText(knowledgeBean.getUsername());
    }

        public void setData(KnowledgeBean knowledgeBean, int position){
            this.position=position;
            question.setText(knowledgeBean.getQuestion_content());
            tag.setText(knowledgeBean.getTag());
            company.setText(knowledgeBean.getCompany());
            username.setText(knowledgeBean.getUsername());
        }


    }

}
