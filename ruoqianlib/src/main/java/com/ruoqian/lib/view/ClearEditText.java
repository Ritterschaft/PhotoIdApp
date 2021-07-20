package com.ruoqian.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.DrawableRes;

import com.ruoqian.lib.R;


public class ClearEditText extends androidx.appcompat.widget.AppCompatEditText
{
    /** 默认的清除按钮图标资源 */
    private static final int ICON_CLEAR_DEFAULT = R.mipmap.ic_search_clear;

    /** 清楚按钮的图标 */
    private Drawable drawableClear;

    private Context context;
    private AttributeSet attrs;

    public ClearEditText(Context context)
    {
        super(context);
        init(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setClearIco(int res) {
        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        // 获取清除按钮图标资源
        int iconClear =
                typedArray.getResourceId(R.styleable.ClearEditText_iconClear, res);
        drawableClear = getResources().getDrawable(iconClear);
        updateIconClear();
        typedArray.recycle();
    }

    private void init(Context context, AttributeSet attrs)
    {
        this.context = context;
        this.attrs = attrs;
        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        // 获取清除按钮图标资源
        int iconClear =
                typedArray.getResourceId(R.styleable.ClearEditText_iconClear, ICON_CLEAR_DEFAULT);
        drawableClear = getResources().getDrawable(iconClear);
        updateIconClear();
        typedArray.recycle();

        // 设置TextWatcher用于更新清除按钮显示状态
        addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                updateIconClear();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // 点击是的 x 坐标
            int xDown = (int) event.getX();
            // 清除按钮的起始区间大致为[getWidth() - getCompoundPaddingRight(), getWidth()]，
            // 点击的x坐标在此区间内则可判断为点击了清除按钮
            if (xDown >= (getWidth() - getCompoundPaddingRight()) && xDown < getWidth())
            {
                // 清空文本
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 更新清除按钮图标显示
     */
    public void updateIconClear()
    {
        // 获取设置好的drawableLeft、drawableTop、drawableRight、drawableBottom
        Drawable[] drawables = getCompoundDrawables();
        if (length() > 0) //输入框有输入内容才显示删除按钮
        {
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawableClear,
                    drawables[3]);
        }
        else
        {
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], null,
                    drawables[3]);
        }
    }

    /**
     * 设置清除按钮图标样式
     * @param resId 图标资源id
     */
    public void setIconClear(@DrawableRes int resId)
    {
        drawableClear = getResources().getDrawable(resId);
        updateIconClear();
    }
}
