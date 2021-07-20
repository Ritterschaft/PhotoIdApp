package com.ruoqian.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ruoqian.lib.R;
import com.ruoqian.lib.listener.WifiListener;

/**
 * 加载圈
 */
public class LoadingView extends RelativeLayout implements View.OnClickListener{

    private ImageView ivLoading;
    private RotateAnimation rotateAnimation;
    private LinearLayout llNoWifi;
    private RelativeLayout rlLoading;
    private WifiListener wifiListener;

    public void setWifiListener(WifiListener wifiListener){
        this.wifiListener=wifiListener;
    }

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.loadingview,this);
        ivLoading= findViewById(R.id.ivLoading);
        llNoWifi = findViewById(R.id.llNoWifi);
        rlLoading = findViewById(R.id.rlLoading);
        llNoWifi.setOnClickListener(this);
        Loading(ivLoading);
    }

    public void Loading(ImageView iv){
        rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());// 不停顿
        rotateAnimation.setDuration(3000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setRepeatCount(-1);
        iv.setAnimation(rotateAnimation);
        rotateAnimation.startNow();
    }

    public void setLoadingVisible(){
        rlLoading.setVisibility(GONE);
        llNoWifi.setVisibility(VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.llNoWifi){
            llNoWifi.setVisibility(GONE);
            rlLoading.setVisibility(VISIBLE);
            if (wifiListener!=null){
                wifiListener.Refresh();
            }
        }
    }
}
