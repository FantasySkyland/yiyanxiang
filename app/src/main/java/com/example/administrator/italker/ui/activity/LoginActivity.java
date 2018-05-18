package com.example.administrator.italker.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.common.ui.Activity;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.util.SpUtils;
import com.example.administrator.italker.ui.util.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "LoginMainAvt";

    ImageView iv_cancel;
    EditText et_password;
    EditText et_username;
    Button btn_login;
    TextView tv_register;
    boolean isUserNameEmpty = true;
    boolean isPasswordEmpty = true;
    ImageView iv_cancel_username;


    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >=23){
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.BLUETOOTH,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                        },
                        0);
            }

        }
//        //设置无标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //设置全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_login_main);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        StatusBarUtils.statusbar(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        iv_cancel_username = (ImageView) findViewById(R.id.iv_cancel_username);
        iv_cancel_username.setOnClickListener(this);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(this);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    iv_cancel.setVisibility(View.VISIBLE);
                    isPasswordEmpty = false;
                    if (!isUserNameEmpty) {
                        btn_login.setBackgroundResource(R.drawable.login_btn_red);
                        btn_login.setClickable(true);
                        btn_login.setTag("canClick");
                    } else {
                        btn_login.setBackgroundResource(R.drawable.login_btn_gray);
                        btn_login.setClickable(false);
                        btn_login.setTag("cannotClick");
                    }
                } else {
                    iv_cancel.setVisibility(View.GONE);
                    isPasswordEmpty = true;
                    btn_login.setClickable(false);
                    btn_login.setTag("cannotClick");
                    btn_login.setBackgroundResource(R.drawable.login_btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_username = (EditText) findViewById(R.id.et_username);
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    isUserNameEmpty = false;
                    iv_cancel_username.setVisibility(View.VISIBLE);
                    if (!isPasswordEmpty) {
                        btn_login.setBackgroundResource(R.drawable.login_btn_red);
                        btn_login.setClickable(true);
                        btn_login.setTag("canClick");
                    } else {
                        btn_login.setClickable(false);
                        btn_login.setTag("cannotClick");
                        btn_login.setBackgroundResource(R.drawable.login_btn_gray);
                    }
                } else {
                    isUserNameEmpty = true;
                    iv_cancel_username.setVisibility(View.GONE);
                    btn_login.setClickable(false);
                    btn_login.setTag("cannotClick");
                    btn_login.setBackgroundResource(R.drawable.login_btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView iv_return = (ImageView) findViewById(R.id.iv_return);
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        if (btn_login.getTag().equals("canClick")) {
                           //判断用户名密码是否正确
                if (SpUtils.getString(this,"user",null) ==null){
                    Toast.makeText(this,"用户名不存在",Toast.LENGTH_SHORT).show();
                }else {
                    if (et_username.getText().toString().equals(SpUtils.getString(this,"user",null))&&
                            et_password.getText().toString().equals(SpUtils.getString(this,"password",null) )  ){
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }



        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                et_password.setText("");
                break;
            case R.id.tv_register:
                RegisterActivity.start(this);
                break;
            case R.id.iv_cancel_username:
                et_username.setText("");
                break;
        }
    }


}
