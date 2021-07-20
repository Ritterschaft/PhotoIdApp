package com.ruoqian.brainidphoto.view;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longtu.base.util.StringUtils;
import com.lxj.xpopup.util.XPopupUtils;
import com.ruoqian.brainidphoto.R;
import com.ruoqian.lib.activity.BaseApplication;

public class TopHeaderView extends RelativeLayout implements View.OnClickListener {

    private View view;
    private LayoutParams layoutParams;
    public final static int TOPWIDTH = 1125;
    public final static int TOPHEIGHT = 264;

    private int statusAndTitleHeight = 0;

    private View viewStatus;
    private RelativeLayout rlTitle;
    private ImageButton ibtnBack;
    private TextView tvTitle;
    private ImageView ivBg;

    private OnTopHeaderListener onTopHeaderListener;

    public void setOnTopHeaderListener(OnTopHeaderListener onTopHeaderListener) {
        this.onTopHeaderListener = onTopHeaderListener;
    }

    public TopHeaderView(Context context) {
        super(context, null);
    }

    public TopHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.top_header, null);
        statusAndTitleHeight = BaseApplication.width * TOPHEIGHT / TOPWIDTH;
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, statusAndTitleHeight);
        view.setLayoutParams(layoutParams);
        viewStatus = view.findViewById(R.id.viewStatus);
        rlTitle = view.findViewById(R.id.rlTitle);
        ibtnBack = view.findViewById(R.id.ibtnBack);
        tvTitle = view.findViewById(R.id.tvTitle);
        ivBg = view.findViewById(R.id.ivBg);
        setStautsHeight();
        setTitleHeight();
        setFakeBoldText(tvTitle);
        setListener();
        addView(view);
    }

    private void setListener() {
        ibtnBack.setOnClickListener(this);
    }

    /**
     * 设置背景是否显示
     */
    public void setBgVisible(int visible) {
        if (ivBg != null) {
            ivBg.setVisibility(visible);
        }
    }

    /**
     * 设置是否显示返回按钮
     */
    public void setBack(int visible) {
        if (ibtnBack != null) {
            ibtnBack.setVisibility(visible);
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        if (tvTitle != null && !StringUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    private void setTitleHeight() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusAndTitleHeight - XPopupUtils.getStatusBarHeight());
        rlTitle.setLayoutParams(layoutParams);
    }

    private void setStautsHeight() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, XPopupUtils.getStatusBarHeight());
        viewStatus.setLayoutParams(layoutParams);
    }

    private void setFakeBoldText(TextView tv) {
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
    }

    @Override
    public void onClick(View v) {
        if (onTopHeaderListener == null) {
            return;
        }
        if (v.getId() == R.id.ibtnBack) {
            onTopHeaderListener.onBack();
        }
    }

    public interface OnTopHeaderListener {
        void onBack();
    }
}
