package com.example.administrator.italker.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.common.ui.Activity;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.util.SpUtils;
import com.example.administrator.italker.ui.util.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends Activity implements View.OnClickListener{
    private Context mContext = this;
    private static final String TAG = "LoginMainAvt";

    ImageView iv_cancel;
    EditText et_password;
    EditText et_username;
    Button btn_login;

    boolean isUserNameEmpty = true;
    boolean isPasswordEmpty = true;
    ImageView iv_cancel_username;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register_main;
    }

    @Override
    protected void initWidget() {
        StatusBarUtils.statusbar(this);
        btn_login = (Button) findViewById(R.id.btn_login);
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
                register();
            }
        });
    }

    public void register() {
        if (btn_login.getTag().equals("canClick")) {
                    if (!et_username.getText().toString().isEmpty()){
            SpUtils.putString(this,"user",et_username.getText().toString());
            if (!et_password.getText().toString().isEmpty()){
                SpUtils.putString(this,"password",et_password.getText().toString());

            }else {
                Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show();
        }


        if (et_username.getText().toString().isEmpty() && et_password.getText().toString().isEmpty()){

        }else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
        }


    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                et_password.setText("");
                break;

            case R.id.iv_cancel_username:
                et_username.setText("");
                break;
        }
    }


//    @OnClick(R.id.register_bt)
//    public void onViewClicked() {
//
//        if (!mUserNameEt.getText().toString().isEmpty()){
//            SpUtils.putString(this,"user",mUserNameEt.getText().toString());
//            if (!mPassWordEt.getText().toString().isEmpty()){
//                SpUtils.putString(this,"password",mPassWordEt.getText().toString());
//
//            }else {
//                Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
//            }
//        }else {
//            Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show();
//        }
//
//
//        if (mUserNameEt.getText().toString().isEmpty() && mPassWordEt.getText().toString().isEmpty()){
//
//        }else {
//            Intent intent = new Intent(this,LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
}
