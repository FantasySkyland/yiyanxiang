package com.example.administrator.italker.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.util.MediaManager;
import com.example.administrator.italker.ui.view.AudioListView;
import com.example.administrator.italker.ui.view.RecyclerAdapter;

public class AudioActivity extends AppCompatActivity {

    private MediaManager mMediaManager;

    private int state = 5;
    public static void start(Context context){
        Intent intent = new Intent(context,AudioActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        AudioListView audioListView = findViewById(R.id.audio_list);
        mMediaManager = new MediaManager(this);
        mMediaManager.setAudioListener(new MediaManager.AudioListener() {
            @Override
            public void audioPause() {
                state = 2;
            }

            @Override
            public void complete() {

            }

            @Override
            public void start() {
                state = 1;
            }

            @Override
            public void onPlaying(int progress, float duration) {
                state = 1;
            }

            @Override
            public void onResume(int progress) {
                state = 1;
            }

            @Override
            public void stop() {
                state = 5;
            }

            @Override
            public void onError(int what, int extra) {

            }
        });
        audioListView.setup(getSupportLoaderManager(), new AudioListView.AudioListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }

            @Override
            public void onClick(AudioListView.Audio audio, RecyclerAdapter.ViewHolder holder) {
                if (mAudio == null){
                    mMediaManager.play(audio.getPath());

                }else if (mAudio.getPath().equals(audio.getPath())){
                    switch (state){
                        case 1:
                            mMediaManager.pause();
                            break;
                        case 2:
                            mMediaManager.resume();
                            break;
                        case 5:
                            mMediaManager.play(audio.getPath());
                            break;
                            default:
                                mMediaManager.play(audio.getPath());
                                break;
                    }
                }else {
                    mMediaManager.play(audio.getPath());
                }
                mAudio = audio;
            }
        });
    }

    private AudioListView.Audio  mAudio;
    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaManager!=null){
            mMediaManager.stop();
        }

    }
}
