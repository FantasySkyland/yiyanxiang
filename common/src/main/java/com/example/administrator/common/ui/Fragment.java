package com.example.administrator.common.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/18.
 */

public abstract class Fragment extends android.support.v4.app.Fragment{
    protected View mRoot;
    protected Unbinder mRootUnbinder;
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null){
            int layId = getContentLayoutId();
            // 初始化当前的跟布局，但是不在创建时就添加到container里边
            View root = inflater.inflate(layId,container,false);
            initWidget(root);
            mRoot = root;
        }else {
            if (mRoot.getParent() != null){
                // 把当前Root从其父控件中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        super.onViewCreated(view, savedInstanceState);
    }
    /**
     * 初始化相关参数
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 得到当前界面的资源文件id
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View root){
        mRootUnbinder = ButterKnife.bind(this,root);

    }
    /**
     * 初始化数据
     */
    protected  void initData(){

    }
    /**
     * 返回按键触发时调用
     *
     * @return 返回True代表我已处理返回逻辑，Activity不用自己finish。
     * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }
}
