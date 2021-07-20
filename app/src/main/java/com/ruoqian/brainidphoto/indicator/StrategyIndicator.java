package com.ruoqian.brainidphoto.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.ruoqian.lib.utils.DisplayUtils;
import com.youth.banner.indicator.BaseIndicator;

public class StrategyIndicator extends BaseIndicator {
    private int mNormalRadius;
    private int mSelectedRadius;
    private int maxRadius;

    public StrategyIndicator(Context context) {
        this(context, null);
    }

    public StrategyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrategyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mNormalRadius = config.getNormalWidth() / 2;
        mSelectedRadius = config.getSelectedWidth() / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = config.getIndicatorSize();
        if (count <= 1) {
            return;
        }

        mNormalRadius = config.getNormalWidth() / 2;
        mSelectedRadius = config.getSelectedWidth() / 2;
        //考虑当 选中和默认 的大小不一样的情况
        maxRadius = Math.max(mSelectedRadius, mNormalRadius);
        //间距*（总数-1）+选中宽度+默认宽度*（总数-1）
        int width = (count - 1) * config.getIndicatorSpace() + config.getSelectedWidth() + config.getNormalWidth() * (count - 1);
        setMeasuredDimension(width, Math.max(config.getNormalWidth(), config.getSelectedWidth()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = config.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        float left = 0;
        for (int i = 0; i < count; i++) {
            mPaint.setColor(config.getCurrentPosition() == i ? Color.parseColor("#1977ff") : Color.parseColor("#999999"));
            int indicatorWidth = config.getNormalWidth();
            int radius = (int) DisplayUtils.dp2px(getContext(), 3) - 1;
            canvas.drawCircle(left + radius, maxRadius, radius, mPaint);
            left += indicatorWidth + DisplayUtils.dp2px(getContext(), 7) - 2;
        }
    }
}
