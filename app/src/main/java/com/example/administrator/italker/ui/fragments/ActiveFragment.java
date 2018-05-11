package com.example.administrator.italker.ui.fragments;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.common.ui.Fragment;
import com.example.administrator.common.widget.Medicine;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.activity.MainActivity;
import com.example.administrator.italker.ui.util.App;
import com.example.administrator.italker.ui.util.DbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends Fragment {

    SQLiteDatabase mSQLiteDatabase;
    DbHelper mDbHelper;
    private static final String TAG = "ActiveFragment";
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
    @BindView(R.id.ref)
    Button mRef;
    @BindView(R.id.start)
    Button mStart;
    @BindView(R.id.call)
    Button mCall;


    private int count = 0;
    private Medicine medicine;
    private ArrayList<Medicine> mArrayList;
    public static SoundPool mSoundPool;
    private int mSoundId;
    private int delay;
    private String mStr;
    private SimpleDateFormat mFormatter;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }


    @Override
    protected void initData() {

        mFormatter = new SimpleDateFormat("HHmm");
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,0);

        mSQLiteDatabase = MainActivity.mSQLiteDatabase;
        mDbHelper = MainActivity.mDbHelper;
        mArrayList = new ArrayList<Medicine>();
        SimpleDateFormat formatter  = new SimpleDateFormat("HHmm");
        Date  curDate  =  new Date(System.currentTimeMillis());
        mStr = formatter.format(curDate);
        mArrayList = mDbHelper.queryByTime(mSQLiteDatabase, mStr);
        if (mArrayList.size()>0 && count < mArrayList.size()){
            medicine = mArrayList.get(count);
            delay = timeDelay(medicine.getUseTime(), mStr);
            setUi();
        }

        super.initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("fragment","  "+resultCode);
    }

    @OnClick({R.id.ref, R.id.start, R.id.call})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.ref:

                Date  curDate2  =  new Date(System.currentTimeMillis());
                mStr = mFormatter.format(curDate2);
                mArrayList = mDbHelper.queryByTime(mSQLiteDatabase, mStr);
                Log.e("mArrayList",""+mArrayList.size());
                if (mArrayList.size()!=0){
                    count++;
                    if (count >= mArrayList.size()){
                        count = 0;
                        medicine = mArrayList.get(count);
                        setUi();
                    }else {
                        medicine = mArrayList.get(count);
                        setUi();
                    }
                }


                break;
            case R.id.start:

                Date  curDate  =  new Date(System.currentTimeMillis());
                mStr = mFormatter.format(curDate);

                mArrayList = mDbHelper.queryByTime(mSQLiteDatabase, mStr);
                if (mArrayList.size()>0){
                    delay = timeDelay(medicine.getUseTime(),mStr);
                    startTimer(delay);
                }


//                //mSoundId = mSoundPool.load(getActivity(),R.raw.jinbao2,1);
//                mSoundPool.play(1,1.0f,1.0f,1,0,1.0f);
                break;
            case R.id.call:
                call("110");
                break;
        }
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private int timeDelay(String meTime,String currentTime){
        int aaa = Integer.valueOf(meTime.substring(0,1)) -Integer.valueOf(currentTime.substring(0,1));
        int delay = aaa*3600;
        int bbb = Integer.valueOf(meTime.substring(1,3)) -Integer.valueOf(currentTime.substring(1,3));
        delay = delay+bbb*60;
        delay = delay*1000;
        return delay;
    }


    private void setUi(){

        mTvName.setText("药品名称:"+medicine.getName());
        mTvDetail.setText("药品详情:"+medicine.getDetail());
        if (!medicine.getUseTime().isEmpty()){
            mTvUseTime.setText("用药时间:"+medicine.getUseTime().substring(0,2)+":"
                    +medicine.getUseTime().substring(2,4));
        }else {
            mTvUseTime.setText("用药时间:");
        }

        mTvDosage.setText("剂量:"+medicine.getDosage());
        mTvTaboo.setText("用药禁忌:"+medicine.getTaboo());
        mTvEffect.setText("不良反应:"+medicine.getEffect());
    }


    private void startTimer(int delay){
        Timer timer = new Timer();
        timer.schedule(new MyTask(),delay,5000);
    }



    static class MyTask extends TimerTask {


        public MyTask(){

        }
        @Override
        public void run() {

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        mSoundPool.load(App.getInstance(),R.raw.jinbao2,1);
                        sleep(1000);
                        mSoundPool.play(1,1.0f,1.0f,1,0,1.0f);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.run();


        }

    }
}
