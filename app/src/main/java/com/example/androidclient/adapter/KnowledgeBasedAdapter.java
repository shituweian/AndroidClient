package com.example.androidclient.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.activity_knowledge;
import com.example.androidclient.applicationContent.userProfile;
import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;
import cn.carbs.android.avatarimageview.library.AvatarImageView;
import me.codeboy.android.aligntextview.AlignTextView;

public abstract class KnowledgeBasedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final List<KnowledgeBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private userProfile profile;

    public KnowledgeBasedAdapter(List<KnowledgeBean> mData) {
        this.mData = mData;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getSubView(parent, viewType);
        return new KnowledgeInnerHolder(view);
    }


    protected abstract View getSubView(ViewGroup parent, int viewType);

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((KnowledgeInnerHolder) holder).setData(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class KnowledgeInnerHolder extends RecyclerView.ViewHolder {

        private MarkdownView question;
        private AlignTextView tag;
        private AlignTextView company;
        private AlignTextView username;
        private AvatarImageView avatar;
        private ShineButton liked;
        private RequestQueue requestQueue;
        private Button detail;
        private String knowledge_id;
        private KnowledgeBean knowledgeBean;
        private int position;
        private int isliked;

        public KnowledgeInnerHolder(View itemView) {
            super(itemView);
            profile = (userProfile) itemView.getContext().getApplicationContext();
            question = (MarkdownView) itemView.findViewById(R.id.knowledge_question);
            question.addStyleSheet(new Github());
            tag = itemView.findViewById(R.id.knowledge_tag);
            company = itemView.findViewById(R.id.knowledge_company);
            liked = itemView.findViewById(R.id.knowledge_list_like);
            requestQueue = Volley.newRequestQueue(itemView.getContext());
            username = itemView.findViewById(R.id.knowledge_username);
            detail = itemView.findViewById(R.id.knowledge_list_detail);
            avatar=itemView.findViewById(R.id.knowledge_list_avatar);
            avatar.setTextAndColorSeed("gsw","gsw");

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
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
                    Intent intent =new Intent(view.getContext(), activity_knowledge.class);
                    intent.putExtra("knowledge",(Serializable) knowledgeBean);
                    view.getContext().startActivity(intent);
                }
            });

        }

        public void setData(KnowledgeBean knowledgeBean) {
            this.knowledgeBean = knowledgeBean;
            knowledge_id = knowledgeBean.getKnowledge_id();
            question.loadMarkdown(knowledgeBean.getQuestion_content());
            tag.setText(" " + knowledgeBean.getTag() + " ");
            company.setText(" " + knowledgeBean.getCompany() + " ");
            isliked = knowledgeBean.getIsLiked();
            if (isliked == 0) {
                liked.setChecked(false);
            } else {
                liked.setChecked(true);
            }
            username.setText(knowledgeBean.getUsername());
            if(knowledgeBean.getUsername().length()>=3) {
                avatar.setTextAndColorSeed(knowledgeBean.getUsername().substring(0,3),knowledgeBean.getUsername().substring(0,3));
            }else{
                avatar.setTextAndColorSeed(knowledgeBean.getUsername(),knowledgeBean.getUsername());
            }
        }

        public void setData(KnowledgeBean knowledgeBean, int position) {
            this.knowledgeBean = knowledgeBean;
            knowledge_id = knowledgeBean.getKnowledge_id();
            this.position = position;
            question.loadMarkdown(knowledgeBean.getQuestion_content());
            tag.setText(knowledgeBean.getTag());
            company.setText(knowledgeBean.getCompany());
            isliked = knowledgeBean.getIsLiked();
            if (isliked == 0) {
                liked.setChecked(false);
            } else {
                liked.setChecked(true);
            }
            username.setText(knowledgeBean.getUsername());
            if(knowledgeBean.getUsername().length()>=3) {
                avatar.setTextAndColorSeed(knowledgeBean.getUsername().substring(0,3),knowledgeBean.getUsername().substring(0,3));
            }else{
                avatar.setTextAndColorSeed(knowledgeBean.getUsername(),knowledgeBean.getUsername());
            }
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
