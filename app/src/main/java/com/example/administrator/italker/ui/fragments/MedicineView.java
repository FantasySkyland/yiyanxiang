package com.example.administrator.italker.ui.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.common.widget.Medicine;
import com.example.administrator.common.widget.reclycler.RecyclerAdapter;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.activity.MainActivity;
import com.example.administrator.italker.ui.util.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZDY
 * on 2017/jinbao2/10
 */

public class MedicineView extends RecyclerView {

    private DbHelper mDbHelper;
    private static final int LOADER_ID = 0x0100;
    private static final int MAX_IMAGE_COUNT = 1; // 最大选中图片数量
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024; // 最小的图片大小

    private Adapter mAdapter = new Adapter();
    private List<Medicine> mMedicines = new ArrayList<>();
    private SelectedChangeListener mListener;
    public static DbHelper mHelper;
    public static SQLiteDatabase mDatabase;

    public MedicineView(Context context) {
        super(context);
        init();
    }

    public MedicineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MedicineView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mHelper = MainActivity.mDbHelper;
        mDatabase = MainActivity.mSQLiteDatabase;
        mMedicines = mHelper.queryAll(mDatabase);
        if (mMedicines != null){
            updateSource(mMedicines);
        }
        mMedicines.clear();
        setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Medicine>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Medicine medicine) {
                super.onItemClick(holder, medicine);
//                if (onItemSelectClick(medicine)) {
//                    //noinspection unchecked
//                    holder.updateData(medicine);
//                }

                notifySelectChanged(medicine);
            }

        });
    }



    /**
     * Cell点击的具体逻辑
     *
     * @param medicine Medicine
     * @return True，代表我进行了数据更改，你需要刷新；反之不刷新
     */
    private boolean onItemSelectClick(Medicine medicine) {
        // 是否需要进行刷新
        boolean notifyRefresh;
        if (mMedicines.contains(medicine)) {
            // 如果之前在那么现在就移除
            mMedicines.remove(medicine);
            medicine.setSelect(false);
            // 状态已经改变则需要更新
            notifyRefresh = true;
        } else {
            if (mMedicines.size() > MAX_IMAGE_COUNT) {
                // 得到提示文字
//                String str = getResources().getString(R.string.label_gallery_select_max_size);
//                // 格式化填充
//                str = String.format(str, MAX_IMAGE_COUNT);
//                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            } else {
                mMedicines.add(medicine);
                medicine.setSelect(true);
                notifyRefresh = true;
            }
        }

        // 如果数据有更改，
        // 那么我们需要通知外面的监听者我们的数据选中改变了
        if (notifyRefresh)
            notifySelectChanged(medicine);
        return true;
    }


//    /**
//     * 可以进行清空选中的图片
//     */
//    public void clear() {
//        for (Medicine medicine : mMedicines) {
//            // 一定要先重置状态
//            medicine.isSelect = false;
//        }
//        mMedicines.clear();
//        // 通知更新
//        mAdapter.notifyDataSetChanged();
//    }

    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged(Medicine medicine) {
        // 得到监听者，并判断是否有监听者，然后进行回调数量变化
        SelectedChangeListener listener = mListener;
        if (listener != null) {
            listener.onSelectedCountChanged(mMedicines.size(),medicine);
        }
    }

    public void setListener(SelectedChangeListener listener){
        mListener = listener;
    }
    /**
     * 通知Adapter数据更改的方法
     *
     * @param medicines 新的数据
     */
    public void updateSource(List<Medicine> medicines) {
        if (medicines.size() == 0){
            mAdapter.clear();
        }else {
            mAdapter.replace(medicines);
        }

    }





    /**
     * 适配器
     */
    private class Adapter extends RecyclerAdapter<Medicine> {

        @Override
        protected int getItemViewType(int position, Medicine medicine) {
            return R.layout.medicine_box2;
        }

        @Override
        protected ViewHolder<Medicine> onCreateViewHolder(View root, int viewType) {
            return new MedicineView.ViewHolder(root);
        }


    }

    /**
     * Cell 对应的Holder
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Medicine> {
        private TextView name;
        private TextView detail;
        private TextView useTime;
        private TextView dosage;
        private TextView taboo;
        private TextView effect;
        private CheckBox mSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.medicine_name);
            detail = itemView.findViewById(R.id.mi_detail);
            useTime =  itemView.findViewById(R.id.useTime);
            dosage =  itemView.findViewById(R.id.dosage);
            taboo =  itemView.findViewById(R.id.mi_taboo);
            effect =  itemView.findViewById(R.id.mi_effect);
        }

        @Override
        protected void onBind(Medicine medicine) {
            Log.d("onBind",medicine.getName());
            name.setText(medicine.getName());
            detail.setText("药品详情:"+ medicine.getDetail());
            if (medicine.getUseTime()!=null){
                useTime.setText("用药时间:"+medicine.getUseTime().substring(0,2)+":"
                        +medicine.getUseTime().substring(2,4));
            }else {
                useTime.setText("用药时间:");
            }
            dosage.setText("剂量:"+medicine.getDosage());
            taboo.setText("用药禁忌:"+medicine.getTaboo());
            effect.setText("不良反应:"+medicine.getEffect());
        }

    }

    /**
     * 对外的一个监听器
     */
    public interface SelectedChangeListener {
        void onSelectedCountChanged(int count,Medicine medicine);
    }

}
