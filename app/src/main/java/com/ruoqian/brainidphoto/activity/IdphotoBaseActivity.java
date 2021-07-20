package com.ruoqian.brainidphoto.activity;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.longtu.base.util.StringUtils;
import com.ruoqian.brainidphoto.R;
import com.ruoqian.brainidphoto.view.TopHeaderView;
import com.ruoqian.lib.activity.BaseActivity;
import com.ruoqian.lib.activity.BaseApplication;
import com.ruoqian.lib.utils.DisplayUtils;

import static com.ruoqian.brainidphoto.view.TopHeaderView.TOPHEIGHT;
import static com.ruoqian.brainidphoto.view.TopHeaderView.TOPWIDTH;

public abstract class IdphotoBaseActivity extends BaseActivity implements TopHeaderView.OnTopHeaderListener {

    private TopHeaderView topHeader;

    @Override
    public void initViews() {
        topHeader = findViewById(R.id.topHeader);
        if (topHeader != null) {
            topHeader.setOnTopHeaderListener(this);
        }
    }

    protected void setTopHeight(RelativeLayout rlTop) {
        int h = BaseApplication.width * TOPHEIGHT / TOPWIDTH + (int) DisplayUtils.dp2px(this, 63);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
        rlTop.setLayoutParams(layoutParams);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        if (!StringUtils.isEmpty(title) && topHeader != null) {
            topHeader.setTitle(title);
        }
    }

    public void setBackVisible(int visible) {
        if (topHeader!= null) {
            topHeader.setBack(visible);
        }
    }

    public void setBgVisible(int visible) {
        if (topHeader != null) {
            topHeader.setBgVisible(visible);
        }
    }

    @Override
    public void onBack() {
        finish();
    }
}
