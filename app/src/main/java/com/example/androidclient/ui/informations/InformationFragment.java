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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.Bean.InterviewBean;
import com.example.androidclient.Bean.KnowledgeBean;
import com.example.androidclient.R;
import com.example.androidclient.activity_login_in;
import com.example.androidclient.activity_register;
import com.example.androidclient.activity_profile_changing;
import com.example.androidclient.applicationContent.userProfile;
import com.example.androidclient.databinding.FragmentInformationBinding;
import com.example.androidclient.mycollection.activity_interview_collection;
import com.example.androidclient.mycollection.activity_knowledge_collection;
import com.example.androidclient.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformationFragment extends Fragment {

    private Button register;
    private Button edit;
    private Button log_in;
    private Button login_out;
    private ImageButton collection;
    private ImageButton knowledge_collection;
    private Context context;

    private TextView email_text;
    private TextView username_text;
    private TextView school_text;
    private TextView type_text;
    private TextView date_text;
    private TextView company_text;
    private TextView YoE_text;

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

        register = (Button) root.findViewById(R.id.register);
        edit = (Button) root.findViewById(R.id.edit);
        log_in = (Button) root.findViewById(R.id.login_in);

        email_text = root.findViewById(R.id.information_email);
        username_text = root.findViewById(R.id.information_username);
        school_text = root.findViewById(R.id.information_school);
        company_text = root.findViewById(R.id.information_company);
        YoE_text = root.findViewById(R.id.information_YoE);
        date_text = root.findViewById(R.id.information_date);
        login_out = root.findViewById(R.id.login_out);
        collection = root.findViewById(R.id.information_collection);
        knowledge_collection=root.findViewById(R.id.information_collection_knowledge);
        context=this.getContext();

        interviewBeans=new ArrayList<>();
        knowledgeBeans=new ArrayList<>();

        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, activity_interview_collection.class);
                startActivity(intent);
            }
        });

        knowledge_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, activity_knowledge_collection.class);
                startActivity(intent);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_profile_changing.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), test.class);

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

        login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_out.setVisibility(View.GONE);
                log_in.setVisibility(View.VISIBLE);
                register.setVisibility(View.VISIBLE);
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
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
            getActivity().unregisterReceiver(mRefreshBroadcastReceiver);
        }

    }
