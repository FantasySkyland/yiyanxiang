package com.example.administrator.italker.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.administrator.common.ui.Activity;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.fragments.ActiveFragment;
import com.example.administrator.italker.ui.fragments.ContactFragment;
import com.example.administrator.italker.ui.fragments.GroupFragment;
import com.example.administrator.italker.ui.util.NavHelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer> {

    @BindView(R.id.appbar)
    AppBarLayout mAppbar;

    @BindView(R.id.navigation_bottom)
    BottomNavigationView mNavigationBottom;

    @BindView(R.id.txt_title)
    TextView mTxtTitle;
    @BindView(R.id.im_search)
    ImageView mImSearch;
    @BindView(R.id.bt_action)
    FloatActionButton mBtAction;
    @BindView(R.id.content_layout)
    FrameLayout mLayContainer;

    private NavHelper mNavHelper;
    private int serachType = 1;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    //    @Override
    protected void initData() {
        super.initData();

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.content_layout,
                getSupportFragmentManager(), this);

        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
//                .add(R.id.action_contact,new NavHelper.Tab<>(ContactFragment.class,R.string.title_contact))
        ;

        mNavHelper.performClickMenu(R.id.action_home);
        Glide.with(this).
                load(R.drawable.xingkong).
                centerCrop().
                into(new ViewTarget<View, GlideDrawable>(mAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });

        mNavigationBottom.setOnNavigationItemSelectedListener(this);


    }


    @OnClick({R.id.im_search, R.id.bt_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_search:

            case R.id.bt_action:

        }
    }

    /**
     * 底部导航栏点击事件触发时调用
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //转接事件到工具类中

        return mNavHelper.performClickMenu(item.getItemId());

    }


    /**
     * 处理后回调的方法
     *
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        //从额外字段中获取title的资源id
        //mTxtTitle.setText(newTab.extra);

        //对浮动按钮进行隐藏与显示的动画
        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 76);
            serachType = 1;
        } else {
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                mBtAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
                serachType = 0;
            } else {
                transY = Ui.dipToPx(getResources(), 76);
                serachType = 1;
            }


        }
        //开始动画
        mBtAction.animate().
                rotation(rotation).
                translationY(transY).
                setInterpolator(new AnticipateOvershootInterpolator()).
                setDuration(480).start();

    }

    private static final String TAG = "MainActivity";

}
