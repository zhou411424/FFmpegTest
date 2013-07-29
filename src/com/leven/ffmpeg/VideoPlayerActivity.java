package com.leven.ffmpeg;


import java.util.Timer;
import java.util.TimerTask;

import com.leven.ffmpeg.jni.VideoPlayerDecode;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VideoPlayerActivity extends Activity {

    public int playOrStop = 1;
    private Button playBtn;
    private Button stopBtn;
    private VideoSurfaceView videoSurfaceView;
    private boolean isExit = false;
    private String videopath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);    
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        setContentView(R.layout.video_player_activity);
        
        Bundle bundle = getIntent().getExtras();
        videopath = bundle.getString("videopath");
        
        videoSurfaceView = (VideoSurfaceView) findViewById(R.id.videoSurfaceView);
        playBtn = (Button) findViewById(R.id.play);
        stopBtn = (Button) findViewById(R.id.stop);
        
        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playOrStop == 1) {
                    playBtn.setText(R.string.video_play);
                    playOrStop = 2;
                }else if(playOrStop == 2){
                    playBtn.setText(R.string.video_pause);
                    playOrStop = 1;
                }
                VideoPlayerDecode.VideoPlayerPauseOrPlay(true);
            }
        });
        
        stopBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isExit = true;
                finish();
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (!isExit && playOrStop == 1) {
            Timer aTimer = new Timer();
            aTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //初始化解码器
                    VideoPlayerDecode.VideoPlayer(videopath);
                }
            }, 10);
        }
    }

    
    @Override
    protected void onStop() {
        if(!isExit){ //播放状态
            if (playOrStop == 1) {
                playOrStop = 2;
                playBtn.setText(R.string.video_play);
                VideoPlayerDecode.VideoPlayerPauseOrPlay(true);
            }
        }else{
            try {
                if (playOrStop == 2) {
                    playOrStop = 1;
                    playBtn.setText(R.string.video_pause);
                    VideoPlayerDecode.VideoPlayerPauseOrPlay(true);
                    Thread.sleep(50);
                }
                VideoPlayerDecode.VideoPlayerStop();
                Thread.sleep(550);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                        + Environment.getExternalStorageDirectory()
                                .getAbsolutePath())));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        isExit = true;
        finish();
    }

}
