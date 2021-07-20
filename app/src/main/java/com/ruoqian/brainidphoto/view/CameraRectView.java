package com.ruoqian.brainidphoto.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ruoqian.lib.activity.BaseApplication;
import com.ruoqian.lib.utils.DisplayUtils;

/**
 * Rect to crop
 * Created by zhouzhuo810 on 2017/6/15.
 */
public class CameraRectView extends View {

    private int width;
    private int height;
    private int bgColor = 0x3f000000;
    private Paint squaPaint;
    private int statusBarHeight;
    private final static int titleHeight = 45;
    private final static int bottomHeight = 120;
    private final static int marginTopAndBottom = 40;
    private final static int marginLeftAndRight = 20;

    public CameraRectView(Context context) {
        super(context);
        init(context, null);
    }

    public CameraRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CameraRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context ctx, AttributeSet attrs) {
        width = BaseApplication.width;
        height = BaseApplication.height;
        squaPaint = new Paint();
        squaPaint.setColor(bgColor);
        squaPaint.setStyle(Paint.Style.FILL);
        try {
            int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            statusBarHeight = resourceId > 0 ? getContext().getResources().getDimensionPixelSize(resourceId) : 105;
            statusBarHeight = statusBarHeight > 0 ? statusBarHeight : 105;
            Log.e("statusBarHeight", statusBarHeight + "");
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float top = DisplayUtils.dp2px(getContext(), titleHeight + marginTopAndBottom);
        float bottom = DisplayUtils.dp2px(getContext(), bottomHeight + marginTopAndBottom);
        float left = DisplayUtils.dp2px(getContext(), marginLeftAndRight);
        float right = DisplayUtils.dp2px(getContext(), marginLeftAndRight);
        canvas.drawRect(0, 0, width, top, squaPaint);
        canvas.drawRect(0, height - bottom, width, height, squaPaint);
        canvas.drawRect(0, top, left, height - bottom, squaPaint);
        canvas.drawRect(width - right, top, width, height - bottom, squaPaint);
    }
}
