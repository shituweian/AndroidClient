package com.example.androidclient;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_profile_changing extends Activity {

    private Button data;
    private Button confirm;

    private RadioButton student;
    private RadioButton employee;
    private RadioButton HR;

    private EditText email;
    private EditText username;
    private EditText YoE;
    private EditText company;
    private EditText school;

    private RelativeLayout student_l;
    private LinearLayout employee_l;
    private RelativeLayout HR_l;

    private int Year=1;
    private int Month=1;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_changing);
        data = findViewById(R.id.profile_graduate_time);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(activity_profile_changing.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        data.setText(String.format("%d-%d-%d", year, monthOfYear+1, dayOfMonth));
                        Year = year;
                        Month = monthOfYear+1;
                    }
                }, 2000, 1, 2).show();
            }
        });

        student = findViewById(R.id.radioButton_student);
        student_l = findViewById(R.id.student_state);

        employee = findViewById(R.id.radioButton_employee);
        employee_l = findViewById(R.id.employee_state);

        HR = findViewById(R.id.radioButton_HR);
        HR_l = findViewById(R.id.hr_state);

        school = findViewById(R.id.profile_school_extend);
        email = findViewById(R.id.profile_email_extend);
        username = findViewById(R.id.profile_username_extend);
        company = findViewById(R.id.profile_company_extend);
        YoE = findViewById(R.id.profile_YOE_extend);

        confirm = findViewById(R.id.profile_confirm);

        requestQueue = Volley.newRequestQueue(this);


        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student_l.setVisibility(View.VISIBLE);
                employee_l.setVisibility(View.GONE);
                HR_l.setVisibility(View.GONE);
            }
        });

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student_l.setVisibility(View.GONE);
                employee_l.setVisibility(View.VISIBLE);
                HR_l.setVisibility(View.GONE);
            }
        });

        HR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student_l.setVisibility(View.GONE);
                employee_l.setVisibility(View.VISIBLE);
                HR_l.setVisibility(View.GONE);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                volleyPost(confirm);
            }

        });

    }

    public void volleyPost(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);

        Map<String, Object> map = new HashMap<String, Object>();
        if(student_l.getVisibility()==View.VISIBLE) {
            map.put("email", email.getText().toString());
            map.put("username", username.getText().toString());
            map.put("identity", "student");
            map.put("school",school.getText().toString());
            map.put("year",Year);
            map.put("month",Month);
            map.put("company",null);
            map.put("YoE",null);
        }else if(employee_l.getVisibility()==View.VISIBLE){
            map.put("email", email.getText().toString());
            map.put("username", username.getText().toString());
            map.put("identity", "student");
            map.put("school",school.getText().toString());
            map.put("year",Year);
            map.put("month",Month);
            map.put("company",company.getText().toString());
            map.put("YoE",YoE.getText().toString());
        }/*else if(HR_l.getVisibility()==View.VISIBLE){
            map.put("email", email.getText().toString());
            map.put("username", username.getText().toString());
            map.put("identity", "student");
            map.put("school",school.getText().toString());
            map.put("year",Year);
            map.put("month",Month);
            map.put("company",null);
            map.put("YoE",null);
        }*/


        JSONObject jsonObject = new JSONObject(map);
        JsonRequest<JSONObject> str = new JsonObjectRequest(Request.Method.POST, "http://192.168.31.242:8080/login_register/profile_setting", jsonObject,
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
                            Toast.makeText(activity_profile_changing.this, "success", Toast.LENGTH_SHORT).show();

                        } else if (code.equals("02")) {
                            Toast.makeText(activity_profile_changing.this, "account not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_profile_changing.this, error.toString(), Toast.LENGTH_SHORT).show();
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
