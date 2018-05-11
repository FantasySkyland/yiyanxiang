package com.example.administrator.common.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author qiujuer
 */

public abstract class Activity extends AppCompatActivity {
    public FragmentManager fm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在界面未初始化之前调用的初始化窗口
        initWidows();

        if (initArgs(getIntent().getExtras())) {
            // 得到界面Id并设置到Activity界面中
            int layId = getContentLayoutId();
            setContentView(layId);
            fm = getSupportFragmentManager();
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化窗口
     */
    protected void initWidows() {

    }

    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     * @return 如果参数正确返回True，错误返回False
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * @param fragment
     * 设置内容fragment
     */
    protected void switchContentFragment(com.example.administrator.common.ui.Fragment fragment,int id){
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fragment != null){
            fragmentTransaction.replace(id,fragment);
        }
        fragmentTransaction.commit();
    }
    @Override
    public boolean onSupportNavigateUp() {
        // 当点击界面导航返回时，Finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // 得到当前Activity下的所有Fragment
        @SuppressLint("RestrictedApi")
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断是否为空
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                // 判断是否为我们能够处理的Fragment类型
                if (fragment instanceof com.example.administrator.common.ui.Fragment) {
                    // 判断是否拦截了返回按钮
                    if (((com.example.administrator.common.ui.Fragment) fragment).onBackPressed()) {
                        // 如果有直接Return
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
        finish();
    }
}
