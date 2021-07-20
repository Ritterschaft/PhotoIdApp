package com.ruoqian.brainidphoto.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruoqian.brainidphoto.R;
import com.ruoqian.brainidphoto.view.TopHeaderView;
import com.ruoqian.lib.fragment.BaseFragment;

public class MyFragment extends BaseFragment {

    private TopHeaderView topHeader;
    private RelativeLayout rlUserInfo;
    private ImageView ivAvatar;
    private TextView tvNickName;
    private TextView tvMobile;

    private TextView tvAccount;
    private TextView tvOrder;
    private TextView tvCustomer;
    private TextView tvFeedback;
    private TextView tvSetting;

    @Override
    public void onClick(View view) {

    }

    @Override
    public void setContentView() {
        layout = R.layout.fragment_my;
    }

    @Override
    public void initViews() {
        topHeader = view.findViewById(R.id.topHeader);
        rlUserInfo = view.findViewById(R.id.rlUserInfo);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvNickName = view.findViewById(R.id.tvNickName);
        tvMobile = view.findViewById(R.id.tvMobile);
        tvAccount = view.findViewById(R.id.tvAccount);
        tvOrder = view.findViewById(R.id.tvOrder);
        tvCustomer = view.findViewById(R.id.tvCustomer);
        tvFeedback = view.findViewById(R.id.tvFeedback);
        tvSetting = view.findViewById(R.id.tvSetting);
    }

    @Override
    public void initDatas() {
        topHeader.setBack(View.GONE);
        topHeader.setTitle(getString(R.string.title_my));
        tvNickName.setText("点击登录");
        setFakeBoldText(tvNickName);
        setFakeBoldText(tvAccount);
        setFakeBoldText(tvOrder);
        setFakeBoldText(tvCustomer);
        setFakeBoldText(tvFeedback);
        setFakeBoldText(tvSetting);
        tvMobile.setVisibility(View.GONE);
    }

    @Override
    public void setDatas() {

    }

    @Override
    public void setListener() {
        rlUserInfo.setOnClickListener(this);
        tvAccount.setOnClickListener(this);
        tvOrder.setOnClickListener(this);
        tvCustomer.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
    }

    @Override
    public void ResumeDatas() {

    }
}
