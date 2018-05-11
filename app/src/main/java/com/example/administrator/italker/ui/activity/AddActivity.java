package com.example.administrator.italker.ui.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.common.ui.Activity;
import com.example.administrator.common.widget.Medicine;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.fragments.MedicineView;
import com.example.administrator.italker.ui.util.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZDY
 * on 2017/jinbao2/10
 */

public class AddActivity extends Activity {
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.tv_detail)
    TextView mTvDetail;
    @BindView(R.id.et_detail)
    EditText mEtDetail;
    @BindView(R.id.tv_useTime)
    TextView mTvUseTime;
    @BindView(R.id.et_useTime)
    EditText mEtUseTime;
    @BindView(R.id.tv_dosage)
    TextView mTvDosage;
    @BindView(R.id.et_dosage)
    EditText mEtDosage;
    @BindView(R.id.tv_taboo)
    TextView mTvTaboo;
    @BindView(R.id.et_taboo)
    EditText mEtTaboo;
    @BindView(R.id.tv_effect)
    TextView mTvEffect;
    @BindView(R.id.et_effect)
    EditText mEtEffect;
    @BindView(R.id.bt_delete)
    Button mBtDelete;
    SQLiteDatabase mSQLiteDatabase;
    DbHelper mDbHelper;
    @BindView(R.id.et_useTime_minute)
    EditText mEtUseTimeMinute;
    private Medicine newMedicine;

    @Override
    protected int getContentLayoutId() {
        return R.layout.add_activity;
    }

    @Override
    protected void initData() {
        mSQLiteDatabase = MedicineView.mDatabase;
        mDbHelper = MedicineView.mHelper;
        super.initData();
    }

    @OnClick(R.id.bt_delete)
    public void onViewClicked() {
        newMedicine = new Medicine();
        newMedicine.setName(mEtName.getText().toString());
        newMedicine.setDetail(mEtDetail.getText().toString());
        if (mEtUseTime.getText().toString()!= null&&mEtUseTimeMinute.getText().toString()!=null){
            newMedicine.setUseTime(mEtUseTime.getText().toString()+mEtUseTimeMinute.getText().toString());

        }else {
            newMedicine.setUseTime("1212");
            Toast.makeText(this,"请输入完整的24小时制时间",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        newMedicine.setDosage(mEtDosage.getText().toString());
        newMedicine.setTaboo(mEtTaboo.getText().toString());
        newMedicine.setEffect(mEtEffect.getText().toString());
        mDbHelper.add(mSQLiteDatabase, newMedicine);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
