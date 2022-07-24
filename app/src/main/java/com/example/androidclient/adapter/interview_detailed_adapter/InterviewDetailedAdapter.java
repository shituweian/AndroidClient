package com.example.androidclient.adapter.interview_detailed_adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.AnswerBean;
import com.example.androidclient.Bean.CommentBean;
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.activity_knowledge;
import com.example.androidclient.adapter.KnowledgeBasedAdapter;
import com.example.androidclient.adapter.interview_adapter.InterviewAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;
import cn.carbs.android.avatarimageview.library.AvatarImageView;
import me.codeboy.android.aligntextview.AlignTextView;

public class InterviewDetailedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final List<KnowledgeBean> mData;
    protected final InterviewBean interviewBean;
    protected int total;
    protected OnItemClickListener mOnItemClickListener;
    private userProfile profile;

    public static final int TYPE_INTERVIEW=0;
    public static final int TYPE_KNOWLEDGE=1;

    public InterviewDetailedAdapter(InterviewBean interviewBean){
        mData=interviewBean.getQuestions();
        this.interviewBean=interviewBean;

    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view=getSubView(parent, viewType);
        if(viewType==TYPE_INTERVIEW){
            return new InterviewInnerHolder(view);
        }else{
            return new KnowledgeInnerHolder(view);
        }
    }

    protected View getSubView(ViewGroup parent, int viewType){
        View view;
        if(viewType==TYPE_INTERVIEW){
            view =View.inflate(parent.getContext(),R.layout.interview_list_view, null);
        }else{
            view =View.inflate(parent.getContext(),R.layout.knowledge_list_view,null);
        }
        return view;
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        if(getItemViewType(position)==TYPE_INTERVIEW && holder instanceof InterviewInnerHolder){
            ((InterviewInnerHolder)holder).setData(interviewBean);
        }else if(getItemViewType(position)==TYPE_KNOWLEDGE && holder instanceof KnowledgeInnerHolder){
            ((KnowledgeInnerHolder)holder).setData(mData.get(position-1));
        }
    }

    public int getItemViewType(int position){
        if(position==0){
            return TYPE_INTERVIEW;
        }else{
            return TYPE_KNOWLEDGE;
        }
    }

    public void add(KnowledgeBean knowledgeBean, int position){
        mData.add(position-1,knowledgeBean);
        notifyItemInserted(position);
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


    public class InterviewInnerHolder extends RecyclerView.ViewHolder {

        private AlignTextView title;
        private AlignTextView provider_name;
        private AlignTextView company;
        private AlignTextView uploadtime;
        private AlignTextView description;
        private AlignTextView position;
        private AlignTextView location;
        private AlignTextView question_number;
        private RequestQueue requestQueue;
        private AlignTextView level;
        private ShineButton liked;
        private AvatarImageView avatar;
        private int isLiked;
        private InterviewBean interviewBean;
        private int position_in_data;

        public InterviewInnerHolder(View itemView) {
            super(itemView);
            profile = (userProfile) itemView.getContext().getApplicationContext();

            requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(itemView.getContext());

            title = itemView.findViewById(R.id.interview_title);
            provider_name = itemView.findViewById(R.id.interview_provider_name);
            company = itemView.findViewById(R.id.interview_company);
            uploadtime = itemView.findViewById(R.id.interview_uploadtime);
            description = itemView.findViewById(R.id.interview_description);
            position = itemView.findViewById(R.id.interview_position);
            location = itemView.findViewById(R.id.interview_location);
            question_number = itemView.findViewById(R.id.interview_question_number);
            level = itemView.findViewById(R.id.interview_level);
            liked = itemView.findViewById(R.id.interview_list_like);
            avatar=itemView.findViewById(R.id.interview_avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position_in_data);
                    }
                }
            });

            liked.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View view, boolean checked) {
                    VolleyPost(view);
                }
            });
        }

        public void setData(InterviewBean interviewBean) {
            this.interviewBean = interviewBean;
            title.setText(interviewBean.getTitle());
            provider_name.setText(interviewBean.getProvider_name());
            if(interviewBean.getCompany()!=null&&!interviewBean.getCompany().equals("null")) {
                company.setText(interviewBean.getCompany());
            }else{
                company.setText("");
            }
            uploadtime.setText(interviewBean.getUploadtime());
            description.setText(interviewBean.getDescription());
            if(interviewBean.getPosition()!=null&&!interviewBean.getPosition().equals("null")) {
                position.setText(interviewBean.getPosition());
            }
            else{
                position.setText("");
            }
            if(interviewBean.getLocation()!=null&&!interviewBean.getLocation().equals("null")) {
                location.setText(interviewBean.getLocation());
            }else{
                location.setText("");
            }
            if(interviewBean.getQuestion_total()>=1) {
                question_number.setText(interviewBean.getQuestion_total() + " Questions");
            }else{
                question_number.setText("");
            }
            if(interviewBean.getLevel()!=null&&!interviewBean.getLevel().equals("null")) {
                level.setText(interviewBean.getLevel());
            }
            else{
                level.setText("Pass");
                level.setTextColor(Color.parseColor("#00CC33"));
            }
            isLiked = interviewBean.getIsliked();
            if (isLiked == 0) {
                liked.setChecked(false);
            } else {
                liked.setChecked(true);
            }
            if(interviewBean.getProvider_name().length()>=3) {
                avatar.setTextAndColorSeed(interviewBean.getProvider_name().substring(0,3),interviewBean.getProvider_name().substring(0,3));
            }else{
                avatar.setTextAndColorSeed(interviewBean.getProvider_name(),interviewBean.getProvider_name());
            }
        }

        public void setData(InterviewBean interviewBean, int position_in_data) {
            this.interviewBean = interviewBean;
            this.position_in_data = position_in_data;
            title.setText(interviewBean.getTitle());
            provider_name.setText(interviewBean.getProvider_name());
            if(interviewBean.getCompany()!=null&&!interviewBean.getCompany().equals("null")) {
                company.setText(interviewBean.getCompany());
            }else{
                company.setText("");
            }
            uploadtime.setText(interviewBean.getUploadtime());
            description.setText(interviewBean.getDescription());
            if(interviewBean.getPosition()!=null&&!interviewBean.getPosition().equals("null")) {
                position.setText(interviewBean.getPosition());
            }
            else{
                position.setText("");
            }
            if(interviewBean.getLocation()!=null&&!interviewBean.getLocation().equals("null")) {
                location.setText(interviewBean.getLocation());
            }else{
                location.setText("");
            }
            if(interviewBean.getQuestion_total()>=1) {
                question_number.setText(interviewBean.getQuestion_total() + " Questions");
            }else{
                question_number.setText("");
            }
            if(interviewBean.getLevel()!=null&&!interviewBean.getLevel().equals("null")) {
                level.setText(interviewBean.getLevel());
            }
            else{
                level.setText("Pass");
                level.setTextColor(Color.parseColor("#00CC33"));
            }
            isLiked = interviewBean.getIsliked();
            if (isLiked == 0) {
                liked.setChecked(false);
            } else {
                liked.setChecked(true);
            }
            if(interviewBean.getProvider_name().length()>=3) {
                avatar.setTextAndColorSeed(interviewBean.getProvider_name().substring(0,3),interviewBean.getProvider_name().substring(0,3));
            }else{
                avatar.setTextAndColorSeed(interviewBean.getProvider_name(),interviewBean.getProvider_name());
            }
        }
        public void VolleyPost(View v) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", interviewBean.getInterview_id());
            map.put("type", 1);
            JSONObject jsonObject = new JSONObject(map);
            JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/users_like", jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String code = "";
                            try {
                                code = (String) response.get("code");
                                if (code.equals("00")) {
                                    Toast.makeText(v.getContext(), "like", Toast.LENGTH_SHORT).show();
                                    liked.setChecked(true);
                                    isLiked = 10;


                                } else if (code.equals("11")) {
                                    Toast.makeText(v.getContext(), "dislike", Toast.LENGTH_SHORT).show();
                                    liked.setChecked(false);
                                    isLiked = 0;
                                }else{
                                    Toast.makeText(v.getContext(), code, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_SHORT).show();

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

    public class KnowledgeInnerHolder extends RecyclerView.ViewHolder{

        private MarkdownView question;
        private AlignTextView tag;
        private AlignTextView company;
        private AlignTextView username;
        private ShineButton liked;
        private RequestQueue requestQueue;
        private Button detail;
        private String knowledge_id;
        private KnowledgeBean knowledgeBean;
        private int position;
        private int isliked;

        public KnowledgeInnerHolder(View itemView){
            super(itemView);
            profile = (userProfile) itemView.getContext().getApplicationContext();
            requestQueue = Volley.newRequestQueue(itemView.getContext());
            question=(MarkdownView) itemView.findViewById(R.id.knowledge_question);
            question.addStyleSheet(new Github());
            tag=itemView.findViewById(R.id.knowledge_tag);
            company=itemView.findViewById(R.id.knowledge_company);
            liked = itemView.findViewById(R.id.knowledge_list_like);
            username=itemView.findViewById(R.id.knowledge_username);
            detail = itemView.findViewById(R.id.knowledge_list_detail);

            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    VolleyPost_Detail(view);
                }
            });

            liked.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View view, boolean checked) {
                    VolleyPost(view);
                }
            });

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VolleyPost_Detail(view);
                }
            });

        }

        public void setData(KnowledgeBean knowledgeBean){
            this.knowledgeBean = knowledgeBean;
            knowledge_id=knowledgeBean.getKnowledge_id();
            question.loadMarkdown(knowledgeBean.getQuestion_content());
            tag.setText(" "+knowledgeBean.getTag()+" ");
            company.setText(" "+knowledgeBean.getCompany()+" ");
            username.setText(knowledgeBean.getUsername());
            isliked = knowledgeBean.getIsLiked();
            if (isliked == 0) {
                liked.setChecked(false);
            } else {
                liked.setChecked(true);
            }
        }

        public void setData(KnowledgeBean knowledgeBean, int position){
            this.knowledgeBean = knowledgeBean;
            knowledge_id = knowledgeBean.getKnowledge_id();
            this.position=position;
            question.loadMarkdown(knowledgeBean.getQuestion_content());
            tag.setText(knowledgeBean.getTag());
            company.setText(knowledgeBean.getCompany());
            username.setText(knowledgeBean.getUsername());
            isliked = knowledgeBean.getIsLiked();
            if (isliked == 0) {
                liked.setChecked(false);
            } else {
                liked.setChecked(true);
            }
        }


        public void VolleyPost_Detail(View v){
            String url="http://120.77.98.16:8080/knowledge_service?uuid="+knowledge_id;
            JsonObjectRequest str=new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String code="";
                            try {
                                code = (String) response.get("code");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (code.equals("00")) {

                                try {

                                    JSONObject buffer=(JSONObject) response.get("data");

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

                                    Toast.makeText(v.getContext(), "successful", Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(v.getContext(),activity_knowledge.class);
                                    intent.putExtra("knowledge",bean);
                                    v.getContext().startActivity(intent);

                                } catch (Exception e) {
                                    Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            } else if (code.equals("99")) {
                                Toast.makeText(v.getContext(), "incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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

        public void VolleyPost(View v) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", knowledge_id);
            map.put("type", 0);
            JSONObject jsonObject = new JSONObject(map);
            JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/users_like", jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String code = "";
                            try {
                                code = (String) response.get("code");
                                if (code.equals("00")) {
                                    Toast.makeText(v.getContext(), "like", Toast.LENGTH_SHORT).show();
                                    liked.setChecked(true);
                                    isliked = 10;


                                } else if (code.equals("11")) {
                                    Toast.makeText(v.getContext(), "dislike", Toast.LENGTH_SHORT).show();
                                    liked.setChecked(false);
                                    isliked = 0;
                                }
                            } catch (JSONException e) {

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_SHORT).show();

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

}
