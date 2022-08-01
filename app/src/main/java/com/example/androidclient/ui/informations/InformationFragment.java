package com.example.androidclient.ui.informations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.activity_alumni;
import com.example.androidclient.activity_knowledge_search;
import com.example.androidclient.activity_login_in;
import com.example.androidclient.activity_profile_changing;
import com.example.androidclient.activity_register;
import com.example.androidclient.applicationContent.userProfile;
import com.example.androidclient.databinding.FragmentInformationBinding;
import com.example.androidclient.mycollection.activity_interview_collection;
import com.example.androidclient.mycollection.activity_knowledge_collection;
import com.example.androidclient.mypost.activity_post_interview;
import com.example.androidclient.mypost.activity_post_knowledge;
import com.example.androidclient.test;

import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class InformationFragment extends Fragment {

    private TitleFAB register;
    private TitleFAB edit;
    private TitleFAB log_in;
    private TitleFAB alumni;
    private Button login_out;
    private LottieAnimationView knowledge_collection;
    private LottieAnimationView interview_collection;
    private LottieAnimationView my_post_interview;
    private LottieAnimationView my_post_knowledge;
    private Context context;

    private TextView email_text;
    private TextView username_text;
    private TextView school_text;
    private TextView type_text;
    private TextView date_text;
    private TextView company_text;
    private TextView YoE_text;
    private AvatarImageView avatar;

    private List<InterviewBean> interviewBeans;
    private List<KnowledgeBean> knowledgeBeans;

    RequestQueue requestQueue;
    private userProfile profile;

    private FragmentInformationBinding binding;

    private BroadcastReceiver mRefreshBroadcastReceiver;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InformationViewModel notificationsViewModel =
                new ViewModelProvider(this).get(InformationViewModel.class);

        binding = FragmentInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View v = inflater.inflate(R.layout.fragment_information, container, false);

        profile = (userProfile) this.getContext().getApplicationContext();
        requestQueue = Volley.newRequestQueue(this.getContext());

        register = root.findViewById(R.id.information_register);
        edit = root.findViewById(R.id.information_profile);
        log_in = root.findViewById(R.id.information_switch);
        alumni = root.findViewById(R.id.information_alumni);

        email_text = root.findViewById(R.id.information_email);
        username_text = root.findViewById(R.id.information_username);
        school_text = root.findViewById(R.id.information_school);
        company_text = root.findViewById(R.id.information_company);
        YoE_text = root.findViewById(R.id.information_YoE);
        date_text = root.findViewById(R.id.information_date);
        knowledge_collection = root.findViewById(R.id.information_collection);
        interview_collection = root.findViewById(R.id.information_collection_knowledge);
        my_post_interview = root.findViewById(R.id.information_my_post_interview);
        my_post_knowledge = root.findViewById(R.id.information_my_post_knowledge);
        avatar=root.findViewById(R.id.information_touxiang);
        avatar.setTextAndColorSeed("GAO","GAO");
        context = this.getContext();

        /*email_text.setText(profile.getEmail());
        username_text.setText(profile.getUsername());
        school_text.setText(profile.getSchool());
        company_text.setText(profile.getCompany());
        date_text.setText(profile.getDate());*/

        email_text.setText("123@qq.com");
        username_text.setText("Ace Coder");
        school_text.setText("Hong Kong University");
        company_text.setText("Tencent");
        date_text.setText("2022-09-01");
        YoE_text.setText("");


        interviewBeans = new ArrayList<>();
        knowledgeBeans = new ArrayList<>();

        knowledge_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_knowledge_collection.class);
                startActivity(intent);
            }
        });

        interview_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_interview_collection.class);
                startActivity(intent);
            }
        });

        my_post_interview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_post_interview.class);
                startActivity(intent);
            }
        });

        my_post_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_post_knowledge.class);
                startActivity(intent);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_register.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_profile_changing.class);

                startActivity(intent);
            }
        });


        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_login_in.class);

                startActivity(intent);
            }
        });

        alumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_alumni.class);

                startActivity(intent);
            }
        });


        mRefreshBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("action.refresh")) {
                    Toast.makeText(context, "login in success", Toast.LENGTH_SHORT).show();
                    email_text.setText(intent.getStringExtra("email"));
                    username_text.setText(intent.getStringExtra("username"));
                    school_text.setText(intent.getStringExtra("school"));
                    company_text.setText(intent.getStringExtra("company"));
                    YoE_text.setText(intent.getStringExtra("YoE"));
                    date_text.setText(intent.getStringExtra("date"));

                    log_in.setVisibility(View.GONE);
                    login_out.setVisibility(View.VISIBLE);
                    register.setVisibility(View.GONE);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refresh");
        getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        getActivity().unregisterReceiver(mRefreshBroadcastReceiver);
    }

}
