package com.example.androidclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.applicationContent.userProfile;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class activity_knowledge_add extends Activity {

    RequestQueue requestQueue;
    private userProfile profile;

    private TextFieldBoxes content_box;
    private TextFieldBoxes company_box;
    private TextFieldBoxes tag_box;
    private ExtendedEditText content;
    private ExtendedEditText company;
    private ExtendedEditText tag;
    private Button upload;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_add);
        content_box = findViewById(R.id.knowledge_add_content_box);
        company_box = findViewById(R.id.knowledge_add_company_box);
        tag_box = findViewById(R.id.knowledge_add_tag_box);
        content = findViewById(R.id.knowledge_add_content_extend);
        company = findViewById(R.id.knowledge_add_company_extend);
        tag = findViewById(R.id.knowledge_add_tag_extend);
        upload = findViewById(R.id.knowledge_add_upload);
        profile = (userProfile) getApplicationContext();
        requestQueue= Volley.newRequestQueue(this);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!content.getText().toString().equals("")&&!company.getText().toString().equals("")&&!tag.getText().toString().equals("")){
                    volleySendComment(upload);
                }
            }
        });

    }

    public void volleySendComment(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("question_content", content.getText().toString());
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
                                //finish();
                            } catch (Exception e) {
                                Toast.makeText(activity_knowledge_add.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (code.equals("99")) {
                            Toast.makeText(activity_knowledge_add.this, "incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_knowledge_add.this, error.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
