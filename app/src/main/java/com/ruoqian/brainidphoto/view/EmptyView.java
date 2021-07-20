package com.ruoqian.brainidphoto.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longtu.base.util.StringUtils;
import com.ruoqian.brainidphoto.R;
import com.ruoqian.lib.activity.BaseApplication;

public class EmptyView extends RelativeLayout {

    private ImageView ivEmptyIcon;
    private TextView tvEmptyTxt;

    public EmptyView(Context context) {
        super(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.emptyview, this);
        ivEmptyIcon = findViewById(R.id.ivEmptyIcon);
        tvEmptyTxt = findViewById(R.id.tvEmptyTxt);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, BaseApplication.height / 5);
        tvEmptyTxt.setLayoutParams(lp);
    }

    public void setEmptyIcon(int res) {
        if (ivEmptyIcon != null) {
            ivEmptyIcon.setImageResource(res);
        }
    }

    public void setEmptyTxt(String txt) {
        if (tvEmptyTxt != null && !StringUtils.isEmpty(txt)) {
            tvEmptyTxt.setText(txt);
        }
    }
}
