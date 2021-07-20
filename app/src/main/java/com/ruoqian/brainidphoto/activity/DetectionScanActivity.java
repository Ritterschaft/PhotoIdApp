package com.ruoqian.brainidphoto.activity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.ruoqian.brainidphoto.R;
import com.ruoqian.brainidphoto.view.CameraProgressView;
import com.ruoqian.lib.activity.BaseActivity;

/**
 * 检测扫描照片人物
 */
public class DetectionScanActivity extends BaseActivity {

    private Animation translateAnimation;

    private CameraProgressView cameraProgress;
    private ImageView ivCameraScan;

    private int progress = 0;
    private final static int PROGRESS = 20001;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 20001:
                    progress++;
                    cameraProgress.setProgress(progress);
                    sendEmptyMessageDelayed(PROGRESS, 50);
                    break;
                case 20002:
                    animationTopToBottom();
                    sendEmptyMessageDelayed(20002, 2500);
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_detection_scan);
    }

    @Override
    public void initViews() {
        cameraProgress = findViewById(R.id.cameraProgress);
        ivCameraScan = findViewById(R.id.ivCameraScan);
    }

    @Override
    public void initDatas() {
        handler.sendEmptyMessageDelayed(PROGRESS, 200);
        translateAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 1.1f);
    }

    @Override
    public void setDatas() {
        translateAnimation.setRepeatMode(Animation.RESTART);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setDuration(2000);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setFillAfter(true);
        handler.sendEmptyMessage(20002);
    }

    private void animationTopToBottom() {
        ivCameraScan.startAnimation(translateAnimation);
    }

    @Override
    public void setListener() {

    }
}