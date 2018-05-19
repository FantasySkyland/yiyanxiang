package com.example.administrator.italker.ui.view;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.italker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class AudioListView extends RecyclerView {
    private static final int LOADER_ID = 0x0200;
    private static final int MIN_IMAGE_FILE_SIZE = 1024 * 1024; // 最小的音乐大小
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private Adapter mAdapter = new Adapter();
    private List<Audio> mSelectedAudios = new LinkedList<>();
    private AudioListener mListener;


    public AudioListView(Context context) {
        super(context);
        init();
    }

    public AudioListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(mAdapter);

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Audio>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Audio audio) {
                // Cell点击操作，如果说我们的点击是允许的，那么更新对应的Cell的状态
                // 然后更新界面，同理；如果说不能允许点击（已经达到最大的选中数量）那么就不刷新界面
//                if (onItemSelectClick(image)) {
//                    //noinspection unchecked
//                    holder.updateData(image);
//                }
                if (mListener!=null){
                    mListener.onClick(audio,holder);
                }
            }
        });
    }

    /**
     * 初始化方法
     *
     * @param loaderManager Loader管理器
     * @return 任何一个LOADER_ID，可用于销毁Loader
     */
    public int setup(LoaderManager loaderManager, AudioListener listener) {
        mListener = listener;
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        return LOADER_ID;
    }


    /**
     * 得到选中的图片的全部地址
     *
     * @return 返回一个数组
     */
    public String[] getSelectedPath() {
        String[] paths = new String[mSelectedAudios.size()];
        int index = 0;
        for (Audio audio : mSelectedAudios) {
            paths[index++] = audio.path;
        }
        return paths;
    }

    /**
     * 可以进行清空选中的文件
     */
    public void clear() {
        for (Audio audio : mSelectedAudios) {
            // 一定要先重置状态
            audio.isSelect = false;
        }
        mSelectedAudios.clear();
        // 通知更新
        mAdapter.notifyDataSetChanged();



    }

    /**
     * 通知Adapter数据更改的方法
     *
     * @param audios 新的数据
     */
    private void updateSource(List<Audio> audios) {
        mAdapter.replace(audios);
    }

    /**
     * 用于实际的数据加载的Loader Callback
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Audio.Media._ID, // Id
                MediaStore.Audio.Media.DATA, // 图片路径
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST

        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // 创建一个Loader
            if (id == LOADER_ID) {
                // 如果是我们的ID则可以进行初始化
                return new CursorLoader(getContext(),
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC"); // 倒序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // 当Loader加载完成时
            List<Audio> audios = new ArrayList<>();
            // 判断是否有数据
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    // 移动游标到开始
                    data.moveToFirst();

                    // 得到对应的列的Index坐标
                    int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    int indexName = data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]);
                    int indexArtist = data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]);

                    do {
                        // 循环读取，直到没有下一条数据
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long dateTime = data.getLong(indexDate);
                        String name = data.getString(indexName);
                        String artist = data.getString(indexArtist);

                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE) {
                            // 如果没有图片，或者图片大小太小，则跳过
                            continue;
                        }


                        // 添加一条新的数据
                        Audio audio = new Audio();
                        audio.id = id;
                        audio.path = path;
                        audio.date = dateTime;
                        audio.name = name;
                        audio.artist = artist;
                        audios.add(audio);


                    } while (data.moveToNext());
                }
            }
            updateSource(audios);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // 当Loader销毁或者重置了, 进行界面清空
            updateSource(null);
        }
    }



    public static class Audio {
        int id; // 数据的ID
        String path; // 音乐的路径
        long date; // 图片的创建日期
        String name; //音乐名字
        String artist; //歌手
        boolean isSelect;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Audio audio = (Audio) o;

            return path != null ? path.equals(audio.path) : audio.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }
    /**
     * 适配器
     */
    private class Adapter extends RecyclerAdapter<Audio> {


        @Override
        protected int getItemViewType(int position, Audio audio) {
            return R.layout.audio_list_item;
        }

        @Override
        protected ViewHolder<Audio> onCreateViewHolder(View root, int viewType) {
            return new AudioListView.ViewHolder(root);
        }
    }

    /**
     * Cell 对应的Holder
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Audio> {
        TextView name;
        TextView artist;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            artist =itemView.findViewById(R.id.artist);
        }

        @Override
        protected void onBind(Audio audio) {
            name.setText(audio.name);
            artist.setText(audio.artist);
        }

    }

    /**
     * 对外的一个监听器
     */
    public interface AudioListener {
        void onSelectedCountChanged(int count);

        void onClick(Audio audio,RecyclerAdapter.ViewHolder holder);
    }


}
