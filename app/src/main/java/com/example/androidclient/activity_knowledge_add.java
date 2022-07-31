package com.example.androidclient;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.classichu.lineseditview.LinesEditView;
import com.example.androidclient.applicationContent.userProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.tiagohm.markdownview.MarkdownView;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class activity_knowledge_add extends Activity {

    RequestQueue requestQueue;
    private userProfile profile;
    private MarkdownView md;
    private LinesEditView content;
    private TextFieldBoxes company_box;
    private TextFieldBoxes tag_box;
    private ExtendedEditText company;
    private ExtendedEditText tag;
    private Button upload;
    private Button refresh;
    private LottieAnimationView upload_lottie;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_add);
        company_box = findViewById(R.id.knowledge_add_company_box);
        tag_box = findViewById(R.id.knowledge_add_tag_box);
        company = findViewById(R.id.knowledge_add_company_extend);
        tag = findViewById(R.id.knowledge_add_tag_extend);
        upload = findViewById(R.id.knowledge_add_upload);
        md=findViewById(R.id.knowledge_add_md);
        md.loadMarkdown("**<center>Here will display markdown text</center>**\n\n**_<center>Enter the context at the bottom edittext</center>_**\n\n**<center>Then" +
                " click <font color=#1976D2> REFRESH button </font> to see</center>**");
        content =findViewById(R.id.knowledge_add_content);
        refresh=findViewById(R.id.knowledge_add_refresh);
        upload_lottie=findViewById(R.id.knowledge_add_upload_lottie);
        profile = (userProfile) getApplicationContext();
        requestQueue= Volley.newRequestQueue(this);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(content.getContentText()!=null&&!content.getContentText().equals("")) {
                    md.loadMarkdown(content.getContentText());
                }else{
                    Toast.makeText(activity_knowledge_add.this, "no text entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!content.getContentText().toString().equals("")&&!company.getText().toString().equals("")&&!tag.getText().toString().equals("")){
                    volleySendComment(upload);
                }
            }
        });

        upload_lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!content.getContentText().toString().equals("")){
                    upload_lottie.setRepeatCount(-1);
                    upload_lottie.playAnimation();
                    volleySendComment(upload);
                }else{
                    Toast.makeText(activity_knowledge_add.this, "no content entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void volleySendComment(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("question_content", content.getContentText().toString());
        map.put("answer_list", "");
        map.put("userid", profile.getEmail());
        map.put("interview_id", "");
        map.put("comment", "");
        map.put("company", company.getText().toString());
        map.put("tag", tag.getText().toString());
        JSONObject jsonObject = new JSONObject(map);
        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/knowledge_service", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = "";
                        try {
                            code = (String) response.get("code");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code.equals("00")) {

                            try {
                                Toast.makeText(activity_knowledge_add.this, "finish", Toast.LENGTH_SHORT).show();
                                Thread thread=new Thread(){
                                    public void run(){
                                        try {
                                            sleep(1500);
                                            finish();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                thread.start();
                            } catch (Exception e) {
                                Toast.makeText(activity_knowledge_add.this, e.toString(), Toast.LENGTH_SHORT).show();
                                upload_lottie.cancelAnimation();
                            }
                        } else if (code.equals("99")) {
                            Toast.makeText(activity_knowledge_add.this, "incorrect password", Toast.LENGTH_SHORT).show();
                            upload_lottie.cancelAnimation();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_knowledge_add.this, error.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                upload_lottie.cancelAnimation();
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
