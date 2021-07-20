package com.ruoqian.brainidphoto.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ruoqian.lib.utils.DisplayUtils;

public class CameraProgressView extends View {

    private float radis;
    private int bgColor = 0xff303135;
    private int fgColor = 0xff1e57fa;
    private Paint bgPaint;
    private Paint fgPaint;

    private float progress = 0;
    private float maxProgress = 100;

    public CameraProgressView(Context context) {
        super(context);
        init();
    }

    public CameraProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CameraProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        radis = DisplayUtils.dp2px(getContext(), 5);
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);

        fgPaint = new Paint();
        fgPaint.setColor(fgColor);
        fgPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        int width = getWidth();
        int height = getHeight();
        canvas.drawRoundRect(0, 0, width, height, radis, radis, bgPaint);
        canvas.drawRoundRect(0, 0, width  * progress / maxProgress, height, radis, radis, fgPaint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }
}
