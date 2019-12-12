package com.example.kira.videoplayer2;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    public MediaPlayer mediaPlayer;
    Button btn;
    int i;
    boolean b=true;
    LinearLayout linearLayout;
    LinearLayout linearLayout2;
    TextView starttime,endtime;
    SeekBar seekBar;
    int kara=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView=findViewById(R.id.surfaceView);
        surfaceHolder=surfaceView.getHolder();
        linearLayout=findViewById(R.id.lina);
        linearLayout2=findViewById(R.id.lina2);
        btn=findViewById(R.id.button2);
        starttime =findViewById(R.id.starttime);
        endtime =findViewById(R.id.endtime);
        seekBar=findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        btn.setOnClickListener(this);
        surfaceView.setKeepScreenOn(true);
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b) {
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    b=false;
                }else if(mediaPlayer.isPlaying()) {
                    linearLayout.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    b=true;
                }
            }
        });
        linearLayout.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.GONE);
        mediaPlayer=new MediaPlayer();
        if(savedInstanceState!=null){
           kara=savedInstanceState.getInt("media");
         //   seekBar.setProgress(savedInstanceState.getInt("media"));
        }
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer.setDisplay(holder);
                try {
                   Intent i=getIntent();
                    String value=i.getExtras().getString("pass");
//String value="/storage/emulated/0/video/Hall.mp4";
                    mediaPlayer.setDataSource(value);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        mediaPlayer.seekTo(kara);
                        new MyThread().start();
                        seekBar.setMax(mediaPlayer.getDuration());
                        endtime.setText(String.format("%02d",mediaPlayer.getDuration()/1000/60)+":"+String.format("%02d",mediaPlayer.getDuration()/1000%60));
                    }
                });
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });}



    @Override
    public void onClick(View v) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            btn.setBackgroundResource(R.drawable.play);
        }
        else{
            mediaPlayer.start();
            btn.setBackgroundResource(R.drawable.pause);}
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        i=progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(i);
    }

    
    class MyThread extends Thread{
        public void run(){
            while(mediaPlayer!=null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seekBar.post(new Runnable() {
                    @Override
                    public void run() {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        gettime(mediaPlayer.getCurrentPosition());
                    }
                });
            }
        }
    };

    void gettime(int y){

        int x=y/1000/60;

        y=y/1000%60;
        starttime.setText(String.format("%02d",x)+":"+String.format("%02d",y));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("media",mediaPlayer.getCurrentPosition());
    }

}
