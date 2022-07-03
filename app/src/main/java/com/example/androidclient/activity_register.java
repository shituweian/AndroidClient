package com.example.androidclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidclient.ui.customButton.verifyCodeButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class activity_register extends Activity {

    private Button confirm;

    private ExtendedEditText email;

    private ExtendedEditText  username;

    private ExtendedEditText password;

    private ExtendedEditText password_confirm;

    private ExtendedEditText verification;

    private verifyCodeButton code_button;

    private Button check_code;

    private boolean email_ver;

    private int verify_state=-1;


    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        confirm = findViewById(R.id.confirm);

        email = findViewById(R.id.register_email_extend);

        username = findViewById(R.id.register_username_extend);

        password = findViewById(R.id.register_password_extend);

        password_confirm = findViewById(R.id.register_password_confirm_extend);

        code_button = findViewById(R.id.register_verification_code);

        verification = findViewById(R.id.register_verification_extend);

        check_code = findViewById(R.id.code);

        requestQueue = Volley.newRequestQueue(this);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pw = password.getText().toString();
                String pw_confirm = password_confirm.getText().toString();
                if (pw.equals(pw_confirm)) {
                    volleyGet(confirm);
                    if(verify_state==1) {
                        volleyPost(confirm);
                    }
                } else {
                    Toast.makeText(activity_register.this, "two times password not equal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        code_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code_button.waited();
                volleyPost_code(check_code);
            }
        });

        check_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volleyGet(check_code);
            }
        });

    }


    public void volleyPost(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", email.getText().toString());
        map.put("user_name", username.getText().toString());
        map.put("password", password.getText().toString());


        JSONObject jsonObject = new JSONObject(map);

        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/login_register/register", jsonObject,
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
                            Toast.makeText(activity_register.this, "success", Toast.LENGTH_SHORT).show();

                            finish();
                        } else if (code.equals("03")) {
                            Toast.makeText(activity_register.this, "account already exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_register.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    public void volleyPost_code(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", email.getText().toString());


        JSONObject jsonObject = new JSONObject(map);


        //Toast.makeText(activity_register.this, "Timeout", Toast.LENGTH_SHORT).show();

        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://120.77.98.16:8080/email_verification", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //Toast.makeText(activity_register.this, "receive", Toast.LENGTH_SHORT).show();
                        String code = "";
                        try {
                            code = (String) response.get("code");
                            Toast.makeText(activity_register.this, code, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code.equals("00")) {
                            Toast.makeText(activity_register.this, "success", Toast.LENGTH_SHORT).show();
                            code_button.start();
                        } else if (code.equals("04")) {
                            Toast.makeText(activity_register.this, "please check email address", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_register.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        str.setRetryPolicy(new DefaultRetryPolicy(16000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(str);
    }

    public void volleyGet(View v) {

        String url = "/email_verification?email=" + email.getText().toString() + "&code=" + verification.getText().toString();

        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.GET, "http://120.77.98.16:8080" + url, null,
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
                            Toast.makeText(activity_register.this, "get code", Toast.LENGTH_SHORT).show();
                            verify_state=1;
                            finish();
                        } else if (code.equals("07")) {
                            Toast.makeText(activity_register.this, "not matched", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_register.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) /*{
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");

                return headers;
            }
        }*/;
        requestQueue.add(str);
    }


}
