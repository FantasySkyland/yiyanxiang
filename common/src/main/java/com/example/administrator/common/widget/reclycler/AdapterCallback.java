package com.example.administrator.common.widget.reclycler;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
