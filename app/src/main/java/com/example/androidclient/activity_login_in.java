package com.example.androidclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.applicationContent.userProfile;
import com.example.androidclient.ui.informations.InformationFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class activity_login_in extends Activity {
    RequestQueue requestQueue;
    private userProfile profile;
    private TextView email_text;
    private TextView username_text;


    private ExtendedEditText password;
    private ExtendedEditText email;

    private LottieAnimationView atom;
    private LottieAnimationView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        email = findViewById(R.id.login_email_extend);
        password = findViewById(R.id.login_password_extend);
        login = findViewById(R.id.login_login);
        email_text = findViewById(R.id.information_email);
        username_text = findViewById(R.id.information_username);
        atom=findViewById(R.id.lottieAnimationView);
        //atom.setRepeatCount(-1);
        //atom.playAnimation();
        requestQueue = Volley.newRequestQueue(this);
        profile = (userProfile) getApplicationContext();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setRepeatCount(1);
                login.playAnimation();
                volleyPost(login);
            }
        });
    }

    public void volleyPost(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", email.getText().toString());
        map.put("password", password.getText().toString());
        JSONObject jsonObject = new JSONObject(map);
        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/login_register/login", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = "";
                        JSONObject data = new JSONObject();
                        try {
                            code = (String) response.get("code");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code.equals("00")) {
                            try {
                                data = (JSONObject) response.get("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                profile.setEmail(data.get("email").toString());
                                profile.setUsername(data.get("name").toString());
                                profile.setCompany(data.get("company").toString());
                                profile.setDate(data.get("date").toString());
                                profile.setSchool(data.get("school").toString());
                                profile.setType(data.get("type").toString());
                                Intent intent = new Intent();
                                intent.putExtra("email", data.get("email").toString());
                                intent.putExtra("username", data.get("name").toString());
                                intent.putExtra("school", data.get("school").toString());
                                intent.putExtra("date", data.get("date").toString());
                                intent.putExtra("type", data.get("type").toString());
                                intent.putExtra("company", data.get("company").toString());
                                intent.setAction("action.refresh");
                                sendBroadcast(intent);
                                //activity_login_in.this.setResult(110,intent);
                                //startActivity(intent);
                                //Toast.makeText(activity_login_in.this, data.get("email").toString(), Toast.LENGTH_SHORT).show();

                                finish();

                            } catch (Exception e) {
                                Toast.makeText(activity_login_in.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (code.equals("01")) {
                            Toast.makeText(activity_login_in.this, "incorrect password", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("02")) {
                            Toast.makeText(activity_login_in.this, "no such account", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_login_in.this, error.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");

                return headers;
            }
        };
        requestQueue.add(str);
    }

}
