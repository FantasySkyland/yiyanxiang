package com.example.administrator.italker.ui.fragments;



import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.common.ui.Fragment;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.activity.AudioActivity;
import com.example.administrator.italker.ui.activity.LoginActivity;
import com.example.administrator.italker.ui.util.SpUtils;
import com.example.administrator.italker.ui.util.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends Fragment {
    @BindView(R.id.tv_id)
    TextView mTvId;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.alarm)
    LinearLayout mAlarm;

    @Override
    protected void initData() {
        super.initData();
        String username = SpUtils.getString(getActivity(),"user",null);
        if (username == null) {
            mTvId.setText("未登陆");
        } else {
            mTvId.setText(username);
        }

        StatusBarUtils.statusbar(getActivity());
    }

    @OnClick({R.id.tv_id, R.id.tv_login, R.id.alarm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_id:
                break;
            case R.id.tv_login:
                LoginActivity.start(getActivity());
                break;
            case R.id.alarm:
               AlarmActivity.start(getActivity());

            case R.id.music:

                AudioActivity.start(getActivity());
                break;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }


}
