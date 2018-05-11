package com.example.administrator.italker.ui.fragments;


import android.content.Intent;

import com.example.administrator.common.ui.Fragment;
import com.example.administrator.common.widget.Medicine;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.activity.DbActivity;

import java.util.ArrayList;

import butterknife.BindView;


public class GroupFragment extends Fragment implements MedicineView.SelectedChangeListener{

    private ArrayList<Medicine> mMedicines;

    @BindView(R.id.medicine_view)
    MedicineView mMedicineView;




    @Override
    public void onResume() {
        mMedicineView.updateSource(MedicineView.mHelper.queryAll(MedicineView.mDatabase));
        super.onResume();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initData() {
        mMedicineView.setListener(this);
        super.initData();
    }


    @Override
    public void onSelectedCountChanged(int count, Medicine medicine) {

        Intent intent = new Intent(getActivity(), DbActivity.class);
        intent.putExtra("medicine",medicine);
        startActivity(intent);

    }
}
