package com.ruoqian.brainidphoto.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ruoqian.lib.utils.DisplayUtils;

public class RadisView extends View {

    private int bgColor;
    private int radis = 2;

    public RadisView(Context context) {
        super(context, null);
    }

    public RadisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setBackgroundColor(int color) {
        bgColor = color;
    }

    public void setRadis(int radis) {
        this.radis = radis;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();//测量宽度
        int height = getMeasuredHeight();//测量高度
        Paint bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(0, 0, width, height, DisplayUtils.dp2px(getContext(), radis), DisplayUtils.dp2px(getContext(), radis), bgPaint);
    }
}
