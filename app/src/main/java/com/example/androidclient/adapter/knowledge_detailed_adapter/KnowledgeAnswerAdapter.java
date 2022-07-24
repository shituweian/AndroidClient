package com.example.androidclient.adapter.knowledge_detailed_adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidclient.Bean.AnswerBean;
import com.example.androidclient.Bean.BasedBean_knowledge_detail;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;

import java.util.ArrayList;
import java.util.List;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;
import me.codeboy.android.aligntextview.AlignTextView;

public class KnowledgeAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected final List<BasedBean_knowledge_detail> mData;
    protected final KnowledgeBean knowledgeBean;
    protected int total;
    private OnItemClickListener mOnItemClickListener;

    public static final int TYPE_KNOWLEDGE=0;
    public static final int TYPE_COMMENT_ANSWER=1;

    public KnowledgeAnswerAdapter(KnowledgeBean knowledgeBean)
    {
        mData=new ArrayList<>();
        List<AnswerBean> answers=knowledgeBean.getAnswers();
        for(AnswerBean answer: answers){
            mData.add(answer);
        }
        this.knowledgeBean=knowledgeBean;
        total=answers.size();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view =getSubView(parent, viewType);
        if(viewType==TYPE_KNOWLEDGE){
            return new Knowledge_detail_InnerHolder(view);
        }else{
            return new Knowledge_comment_InnerHolder(view);
        }
    }

    protected View getSubView(ViewGroup parent, int viewType){
        View view;
        if(viewType==TYPE_KNOWLEDGE){
            view=View.inflate(parent.getContext(), R.layout.knowledge_list_view_detail,null);
        }else{
            view=View.inflate(parent.getContext(),R.layout.knowledge_list_view_comment,null);
        }
        return view;
    }


    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        if(getItemViewType(position)==TYPE_KNOWLEDGE && holder instanceof Knowledge_detail_InnerHolder) {
            ((Knowledge_detail_InnerHolder) holder).setData(knowledgeBean);
        }else if(getItemViewType(position)==TYPE_COMMENT_ANSWER && holder instanceof Knowledge_comment_InnerHolder){
            ((Knowledge_comment_InnerHolder)holder).setData(mData.get(position-1));
        }
    }

    public int getItemViewType(int position){
        if(position ==0){
            return TYPE_KNOWLEDGE;
        }else{
            return TYPE_COMMENT_ANSWER;
        }
    }

    public int getItemCount(){
        if(mData!=null){
            return mData.size()+1;
        }
        return 1;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public class Knowledge_detail_InnerHolder extends RecyclerView.ViewHolder{

        private AlignTextView name;
        private AlignTextView time;
        private MarkdownView question_content;
        private AlignTextView tag;
        private AlignTextView company;

        public Knowledge_detail_InnerHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.knowledge_detailed_name);
            time=itemView.findViewById(R.id.knowledge_detailed_time);
            question_content=itemView.findViewById(R.id.knowledge_detailed_question_content);
            question_content.addStyleSheet(new Github());
            tag=itemView.findViewById(R.id.knowledge_detailed_tag);
            company=itemView.findViewById(R.id.knowledge_detailed_company);

        }

        public void setData(KnowledgeBean knowledgeBean){
            name.setText(knowledgeBean.getUsername());
            question_content.loadMarkdown(knowledgeBean.getQuestion_content());
            time.setText(knowledgeBean.getUploadTime());
            tag.setText(knowledgeBean.getTag());
            company.setText(knowledgeBean.getCompany());
        }

    }

    public class Knowledge_comment_InnerHolder extends RecyclerView.ViewHolder{

        private AlignTextView name;
        private AlignTextView time;
        private AlignTextView content;
        private AlignTextView type;

        public Knowledge_comment_InnerHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.knowledge_comment_name);
            time=itemView.findViewById(R.id.knowledge_comment_time);
            content=itemView.findViewById(R.id.knowledge_comment_content);
            type=itemView.findViewById(R.id.knowledge_comment_type);

        }

        public void setData(BasedBean_knowledge_detail BasedBean){
            name.setText(BasedBean.getUserName());
            time.setText(BasedBean.getUploadTime());
            content.setText(BasedBean.getContent());
            if(BasedBean instanceof AnswerBean){
                type.setText("answer");
            }else{
                type.setText("comment");
            }
        }

    }

}
