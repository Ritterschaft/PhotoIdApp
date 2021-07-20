package com.ruoqian.brainidphoto.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ruoqian.lib.utils.DisplayUtils;

public class TrapezoidView extends View {

    private int bgColor = 0xffe3eeff;
    private Paint bgPaint;
    private float SPACE;

    public TrapezoidView(Context context) {
        super(context);
        init();
    }

    public TrapezoidView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrapezoidView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TrapezoidView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        SPACE = DisplayUtils.dp2px(getContext(), 10);
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        //实例化路径
        Path path = new Path();
        path.moveTo(0, 0);// 此点为多边形的起点
        path.lineTo(width, 0);
        path.lineTo(width - SPACE, height - 2);
        path.lineTo(SPACE, height - 3);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, bgPaint);
        canvas.drawRoundRect(SPACE, 0, width - SPACE, height, 3, 3, bgPaint);
    }
}
