package com.example.androidclient.adapter.interview_adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.adapter.KnowledgeBasedAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import me.codeboy.android.aligntextview.AlignTextView;

public class InterviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final List<InterviewBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private userProfile profile;

    public InterviewAdapter(List<InterviewBean> mData) {
        this.mData = mData;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getSubView(parent, viewType);
        return new InterviewInnerHolder(view);
    }

    protected View getSubView(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.interview_list_view, null);
        return view;
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((InterviewInnerHolder) holder).setData(mData.get(position), position);
    }

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

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void add(InterviewBean interview, int position) {
        mData.add(position, interview);
        notifyItemInserted(position);
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

}
