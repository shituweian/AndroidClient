package com.example.androidclient.adapter;

import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.JobBean;
import com.example.androidclient.R;
import com.example.androidclient.adapter.interview_adapter.InterviewAdapter;
import com.example.androidclient.applicationContent.userProfile;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;

import me.codeboy.android.aligntextview.AlignTextView;

public class JobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final List<JobBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private userProfile profile;

    public JobAdapter(List<JobBean> mData) {
        this.mData = mData;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getSubView(parent, viewType);
        return new JobInnerHolder(view);
    }

    protected View getSubView(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.job_list_view, null);
        return view;
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((JobInnerHolder) holder).setData(mData.get(position), position);
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

    public void removeAll() {
        int position = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, position);
    }

    public void add(JobBean jobBean, int position) {
        mData.add(position, jobBean);
        notifyItemInserted(position);
    }

    public class JobInnerHolder extends RecyclerView.ViewHolder {

        private TextView company;
        private AlignTextView upload_time;
        private AlignTextView expire_time;
        private AlignTextView description;
        private AlignTextView position;
        private AlignTextView salary;
        private AlignTextView hr;
        private TextView location;
        private int position_in_data;

        public JobInnerHolder(View itemView) {
            super(itemView);
            profile = (userProfile) itemView.getContext().getApplicationContext();

            company = itemView.findViewById(R.id.job_company);
            upload_time = itemView.findViewById(R.id.job_upload_time);
            expire_time = itemView.findViewById(R.id.job_expire_time);
            description = itemView.findViewById(R.id.job_description);
            position = itemView.findViewById(R.id.job_position);
            salary = itemView.findViewById(R.id.job_salary);
            hr = itemView.findViewById(R.id.job_hr);
            location = itemView.findViewById(R.id.job_location);

            company.setTypeface(EasyFonts.caviarDreamsBold(itemView.getContext()));
            location.setTypeface(EasyFonts.captureIt(itemView.getContext()));

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position_in_data);
                    }
                }
            });
        }

        public void setData(JobBean jobBean) {
            company.setText(jobBean.getCompany());
            upload_time.setText(jobBean.getUploadTime());

            expire_time.setText(jobBean.getExpiryDate());

            description.setText(jobBean.getDescriptionShort());

            if(jobBean.getJobPosition()!=null&&!jobBean.getJobPosition().equals("null")) {
                position.setText(jobBean.getJobPosition());
            }else{
                position.setText("");
            }
            if(jobBean.getSalary()!=null&&!jobBean.getSalary().equals("null")) {
                salary.setText(jobBean.getSalary());
            }else{
                salary.setText("");
            }
            if(jobBean.getHrContract()!=null&&!jobBean.getHrContract().equals("null")) {
                hr.setText(jobBean.getHrContract());
            }else{
                hr.setText("");
            }

            location.setText(jobBean.getJobLocation());

        }

        public void setData(JobBean jobBean, int position_in_data) {
            this.position_in_data = position_in_data;
            company.setText(jobBean.getCompany());
            upload_time.setText(jobBean.getUploadTime());

            expire_time.setText(jobBean.getExpiryDate());

            description.setText(jobBean.getDescriptionShort());

            if(jobBean.getJobPosition()!=null&&!jobBean.getJobPosition().equals("null")) {
                position.setText(jobBean.getJobPosition());
            }else{
                position.setText("");
            }
            if(jobBean.getSalary()!=null&&!jobBean.getSalary().equals("null")) {
                salary.setText(jobBean.getSalary());
            }else{
                salary.setText("");
            }
            if(jobBean.getHrContract()!=null&&!jobBean.getHrContract().equals("null")) {
                hr.setText(jobBean.getHrContract());
            }else{
                hr.setText("");
            }

            location.setText(jobBean.getJobLocation());

        }

    }


}
