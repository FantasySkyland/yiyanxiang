package com.example.administrator.italker.ui.activity;

import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.common.ui.Activity;
import com.example.administrator.common.widget.Medicine;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.fragments.MedicineView;
import com.example.administrator.italker.ui.util.DbHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZDY
 * on 2017/jinbao2/10
 */

public class QueryActivity extends Activity {
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_detail)
    TextView mTvDetail;
    @BindView(R.id.tv_useTime)
    TextView mTvUseTime;
    @BindView(R.id.tv_dosage)
    TextView mTvDosage;
    @BindView(R.id.tv_taboo)
    TextView mTvTaboo;
    @BindView(R.id.tv_effect)
    TextView mTvEffect;
    @BindView(R.id.et_query)
    EditText mEtQuery;
    @BindView(R.id.bt_query)
    Button mBtQuery;
    SQLiteDatabase mSQLiteDatabase;
    DbHelper mDbHelper;
    @Override
    protected int getContentLayoutId() {
        return R.layout.query_activity;
    }

    @Override
    protected void initData() {
        mSQLiteDatabase = MedicineView.mDatabase;
        mDbHelper = MedicineView.mHelper;
        super.initData();
    }

    @OnClick(R.id.bt_query)
    public void onViewClicked() {
        Medicine medicine =mDbHelper.queryByName(mSQLiteDatabase,mEtQuery.getText().toString());
        mTvName.setText("药品名称:"+medicine.getName());
        mTvDetail.setText("药品详情:"+medicine.getDetail());
        if (!medicine.getUseTime().isEmpty()){
            mTvUseTime.setText("用药时间:"+medicine.getUseTime().substring(0,1)+":"
                    +medicine.getUseTime().substring(1,3));
        }else {
            mTvUseTime.setText("用药时间:");
        }

        mTvDosage.setText("剂量:"+medicine.getDosage());
        mTvTaboo.setText("用药禁忌:"+medicine.getTaboo());
        mTvEffect.setText("不良反应:"+medicine.getEffect());
    }
}
