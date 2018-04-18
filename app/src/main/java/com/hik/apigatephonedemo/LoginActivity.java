package com.hik.apigatephonedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hik.apigatephonedemo.utils.ToastUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText etHost;
    private EditText etUserName;
    private EditText etPassword;
    private String host = "";
    private String userName = "";
    private String password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etHost = (EditText) findViewById(R.id.etHost);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);

    }

    public void onLoginClick(View v){
        host = etHost.getText().toString().trim();
        userName = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(host) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            ToastUtil.showToast(this, "参数不完整，请输入host、appkey和appSecret");
        } else {
            HttpClient.init(host, userName, password);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
