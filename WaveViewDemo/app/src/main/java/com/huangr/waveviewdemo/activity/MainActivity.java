package com.huangr.waveviewdemo.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.huangr.waveviewdemo.R;
import com.huangr.waveviewdemo.widget.VoiceWaveView;

/**
 * Created by huangr on 2018/8/6.
 * ClassName  : MainActivity
 * Description  : 两个按钮分别实现模拟声音变化的波纹和规律增长的水波
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private VoiceWaveView voiceWaveView;
    private Button btnVoice;
    private int progress = 0;

    Handler handlerVoice = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handlerVoice.postDelayed(runnableVoice,0);
        }
    };

    Runnable runnableVoice = new Runnable() {
        @Override
        public void run() {
            int voice = (int) (Math.random() * 80 + 60);
            Log.d(TAG, "onTouch() called with: , voice = [" + voice + "]");
            voiceWaveView.setWaveHeight(voice);
            handlerVoice.postDelayed(runnableVoice,500);
        }
    };

    Handler handlerProgress = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handlerProgress.postDelayed(runnableProgress,0);
        }
    };

    Runnable runnableProgress = new Runnable() {
        @Override
        public void run() {
            if (progress >= 100) {
                progress = 0;
            } else {
                progress++;
            }
            Log.d(TAG, "onTouch() called with: , progress = [" + progress + "]");
            voiceWaveView.setProgress(progress);
            handlerProgress.postDelayed(runnableProgress,100);
        }
    };
    private Button btnProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVoice = (Button) findViewById(R.id.btn_voice);
        btnProgress = (Button) findViewById(R.id.btn_progress);
        voiceWaveView = (VoiceWaveView) findViewById(R.id.voiceWaveView);

        btnVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //模拟声音音量变化
                    handlerVoice.sendEmptyMessage(0);
                    btnVoice.setText("松开停止说话");
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    handlerVoice.removeCallbacksAndMessages(null);
                    //voiceWaveView.resetWaveHeight();
                    btnVoice.setText("按下开始说话");
                }
                return false;
            }
        });

        btnProgress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //进度逐渐变化，触发progress由0到100的循环
                    voiceWaveView.setWaveHeight(100);
                    voiceWaveView.setProgress(0);
                    handlerProgress.sendEmptyMessage(0);
                    btnProgress.setText("松开停止增长");
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    handlerProgress.removeCallbacksAndMessages(null);
                    voiceWaveView.resetProgress();
                    btnProgress.setText("按下开始增长");
                }
                return false;
            }
        });
    }
}
