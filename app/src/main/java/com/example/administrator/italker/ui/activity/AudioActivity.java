package com.example.administrator.italker.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.italker.R;

public class AudioActivity extends AppCompatActivity {

    public static void start(Context context){
        Intent intent = new Intent(context,AudioActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

    }
}
