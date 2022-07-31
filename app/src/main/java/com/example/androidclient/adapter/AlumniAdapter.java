package com.example.androidclient.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidclient.Bean.AlumniBean;
import com.example.androidclient.Bean.JobBean;
import com.example.androidclient.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.w3c.dom.Text;

import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;

public class AlumniAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected final List<AlumniBean> mData;
    private OnItemClickListener mOnItemClickListener;

    public AlumniAdapter(List<AlumniBean> mData){
        this.mData=mData;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getSubView(parent, viewType);
        return new AlumniInnerHolder(view);
    }

    protected View getSubView(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.alumni_list_view, null);
        return view;
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AlumniAdapter.AlumniInnerHolder) holder).setData(mData.get(position), position);
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

    public void add(AlumniBean alumniBean, int position) {
        mData.add(position, alumniBean);
        notifyItemInserted(position);
    }

    public class AlumniInnerHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView email;
        private TextView school;
        private AvatarImageView avatar;
        private int position;

        public AlumniInnerHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.alumni_name);
            email=itemView.findViewById(R.id.alumni_email);
            school=itemView.findViewById(R.id.alumni_school);
            avatar=itemView.findViewById(R.id.alumni_avatar);

            name.setTypeface(EasyFonts.caviarDreamsBold(itemView.getContext()));
            email.setTypeface(EasyFonts.caviarDreamsBold(itemView.getContext()));
            school.setTypeface(EasyFonts.captureIt(itemView.getContext()));
        }

        public void setData(AlumniBean alumniBean){
            name.setText(alumniBean.getName());
            email.setText(alumniBean.getEmail());
            school.setText(alumniBean.getSchool());
            /*name.setText("Gao Shiwei");
            email.setText("1258264684@qq.com");
            school.setText("Hong Kong University");*/
            if(alumniBean.getName().length()>=3) {
                avatar.setTextAndColorSeed(alumniBean.getName().substring(0,3),alumniBean.getName().substring(0,3));
            }else{
                avatar.setTextAndColorSeed(alumniBean.getName(),alumniBean.getName());
            }
            /*avatar.setTextAndColorSeed("GAO","GAO");*/
        }

        public void setData(AlumniBean alumniBean, int position){
            this.position=position;
            name.setText(alumniBean.getName());
            email.setText(alumniBean.getEmail());
            school.setText(alumniBean.getSchool());
            /*name.setText("Gao Shiwei");
            email.setText("1258264684@qq.com");
            school.setText("Hong Kong University");*/
            if(alumniBean.getName().length()>=3) {
                avatar.setTextAndColorSeed(alumniBean.getName().substring(0,3),alumniBean.getName().substring(0,3));
            }else{
                avatar.setTextAndColorSeed(alumniBean.getName(),alumniBean.getName());
            }
            /*avatar.setTextAndColorSeed("GAO","GAO");*/
        }

    }

}
