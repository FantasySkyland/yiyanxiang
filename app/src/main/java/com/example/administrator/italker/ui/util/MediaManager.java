package com.example.administrator.italker.ui.util;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by zhangfang on 2018/4/16.
 * 播放音频的Manager
 */

public class MediaManager {

    private static MediaPlayer mMediaPlayer;
    private AudioListener audioListener; //播放状态的监听
    private static boolean isPause; //是否暂停播放 （状态值）
    private int progress = 0;
    private Thread playThread;
    private String url;
    private boolean isPrepared = false;
    private boolean isPlaying;
    private Activity act;

    public MediaManager(Activity act) {
        this.act = act;
    }

    public void setAudioListener(AudioListener audioListener) {
        this.audioListener = audioListener;
    }

    //播放录音
    public void play(String url) {
        this.url = url;
        if (isPlaying  && mMediaPlayer != null) {
            stop();
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            //播放错误 防止崩溃
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
            isPrepared = false;
        }
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audioListener.complete();
                    isPrepared = false;
                    isPlaying = false;
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    audioListener.onError(what, extra);
                    return false;
                }
            });
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
            isPrepared = true;
            isPlaying = true;
            mMediaPlayer.start();
            audioListener.start();
            if (mMediaPlayer.isPlaying()) {
                progress = 0;
                playThread = new Thread(getProgressRunnable);
                playThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前播放时间的Runnable
     */


    /**
     * 暂停播放
     */
    public void pause() {
        if (mMediaPlayer != null && isPlaying) {
            isPause = true;
            isPlaying = false;
            mMediaPlayer.pause();
            if (audioListener != null) {
                audioListener.audioPause();
            }
            if (playThread != null) {
                playThread.interrupt();
            }

        }
    }

    /**
     * 重新播放（如果是同一个音频文件暂停播放后需要调用此方法重新播放）
     */
    public void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
            isPlaying = true;
            if (audioListener != null) {
                audioListener.onResume(mMediaPlayer.getCurrentPosition());
            }
            if (mMediaPlayer.isPlaying()) {
                playThread = new Thread(getProgressRunnable);
                progress = mMediaPlayer.getCurrentPosition();
                playThread.start();
            }
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 播放到当前进度
     *
     * @param progress 毫秒值
     */
    public void seekToPosition(int progress) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(progress);
        }
    }

    //获取时长的Runnable
    private Runnable getProgressRunnable = new Runnable() {
        @Override
        public void run() {
            while (null != mMediaPlayer && isPlaying) {
                try {
                    Thread.sleep(60);
                    if (null != mMediaPlayer && isPlaying) {
                        progress = mMediaPlayer.getCurrentPosition();
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (null != mMediaPlayer && isPlaying) {
                                    audioListener.onPlaying(progress, getDuration());
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public interface AudioListener {
        void audioPause();

        void complete();

        void start();

        void onPlaying(int progress, float duration);

        void onResume(int progress);

        void stop();

        void onError(int what, int extra);
    }

    /**
     * 返回当前的url
     */
    public String getCurrentUrl() {
        return url;
    }


    /**
     * 停止播放
     */
    public void stop() {
        isPrepared = false;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            isPlaying = false;
            isPrepared = false;
            release();
            if (playThread != null) {
                playThread.interrupt();
            }

            if (audioListener != null) {
                audioListener.stop();
                audioListener.onPlaying(1000, getDuration());
            }
        }
    }

    /**
     * 获取当前的进度
     */
    public float getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 1000;
    }
}
