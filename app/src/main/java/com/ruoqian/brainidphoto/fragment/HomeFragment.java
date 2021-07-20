package com.ruoqian.brainidphoto.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxj.xpopup.util.XPopupUtils;
import com.ruoqian.brainidphoto.R;
import com.ruoqian.brainidphoto.activity.IdphotoDetailsActivity;
import com.ruoqian.brainidphoto.activity.IdphotoListsActivity;
import com.ruoqian.brainidphoto.activity.SearchActivity;
import com.ruoqian.brainidphoto.adapter.IdphotoAdapter;
import com.ruoqian.brainidphoto.dao.DaoManager;
import com.ruoqian.brainidphoto.dao.Idphoto;
import com.ruoqian.brainidphoto.event.EventType;
import com.ruoqian.brainidphoto.event.IdphotoEvent;
import com.ruoqian.lib.fragment.BaseFragment;
import com.ruoqian.lib.utils.DisplayUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView lvIdphotos;
    private View headerView;
    private View viewStatus;
    private TextView tvTitle;
    private TextView tvCrop;
    private TextView tvMatting;
    private TextView tvRefoot;
    private TextView tvRecommendTitle;
    private TextView tvHomeMore;
    private RelativeLayout rlSearch;

    private View viewTopStatus;
    private RelativeLayout rlTopSearchContainer;
    private RelativeLayout rlTopSearch;

    private int topHeight;

    private DaoManager daoManager;

    private List<Idphoto> listIdphotos;
    private IdphotoAdapter idphotoAdapter;

    private Handler handler = new Handler(Looper.myLooper());

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvHomeMore:
                Jump(IdphotoListsActivity.class);
                break;
            case R.id.rlSearch:
                Jump(SearchActivity.class);
                break;
            case R.id.rlTopSearch:
                Jump(SearchActivity.class);
                break;
        }
    }

    @Override
    public void setContentView() {
        layout = R.layout.fragment_home;
    }

    @Override
    public void initViews() {
        lvIdphotos = view.findViewById(R.id.lvIdphotos);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.home_header_view, null);
        viewStatus = headerView.findViewById(R.id.viewStatus);
        tvTitle = headerView.findViewById(R.id.tvTitle);
        tvCrop = headerView.findViewById(R.id.tvCrop);
        tvMatting = headerView.findViewById(R.id.tvMatting);
        tvRefoot = headerView.findViewById(R.id.tvRefoot);
        tvRecommendTitle = headerView.findViewById(R.id.tvRecommendTitle);
        tvHomeMore = headerView.findViewById(R.id.tvHomeMore);
        rlSearch = headerView.findViewById(R.id.rlSearch);
        viewTopStatus = view.findViewById(R.id.viewTopStatus);
        rlTopSearchContainer = view.findViewById(R.id.rlTopSearchContainer);
        rlTopSearch = view.findViewById(R.id.rlTopSearch);
        lvIdphotos.addHeaderView(headerView);
    }

    @Override
    public void initDatas() {
        daoManager = DaoManager.getInstance(getActivity());
        listIdphotos = new ArrayList<>();
        idphotoAdapter = new IdphotoAdapter(listIdphotos);
        lvIdphotos.setAdapter(idphotoAdapter);
        setStautsHeight(viewStatus);
        setStautsHeight(viewTopStatus);
        topHeight = XPopupUtils.getStatusBarHeight() + (int) DisplayUtils.dp2px(getActivity(), 40);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getIdphotos();
            }
        }, 10);
    }

    private void setStautsHeight(View viewStatus) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, XPopupUtils.getStatusBarHeight());
        viewStatus.setLayoutParams(layoutParams);
    }

    private void getIdphotos() {
        List<Idphoto> idphotos = daoManager.getIdphotoLists(true, null);
        if (idphotos == null || idphotos.size() == 0) { //发送消息请求数据
            lvIdphotos.setVisibility(View.GONE);
            showLoadingTitle("");
            //发送请求接口获取数据
            IdphotoEvent idphotoEvent = new IdphotoEvent();
            idphotoEvent.setType(EventType.HOME_RECOMMEND);
            EventBus.getDefault().post(idphotoEvent);
            return;
        }
        listIdphotos.clear();
        listIdphotos.addAll(idphotos);
        idphotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDatas() {
        setFakeBoldText(tvTitle);
        setFakeBoldText(tvCrop);
        setFakeBoldText(tvMatting);
        setFakeBoldText(tvRefoot);
        setFakeBoldText(tvRecommendTitle);
    }

    private int mCurrentfirstVisibleItem = 0;

    @Override
    public void setListener() {
        rlSearch.setOnClickListener(this);
        rlTopSearch.setOnClickListener(this);
        tvHomeMore.setOnClickListener(this);
        tvCrop.setOnClickListener(this);
        tvMatting.setOnClickListener(this);
        tvRefoot.setOnClickListener(this);
        lvIdphotos.setOnItemClickListener(this);
        lvIdphotos.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int arg1, int arg2, int arg3) {
                mCurrentfirstVisibleItem = arg1;
                View firstView = absListView.getChildAt(0);
                if (null != firstView) {
                    int h = getScrollY();//滚动距离
                    if (h > topHeight / 2) {
                        rlTopSearchContainer.setVisibility(View.VISIBLE);
                    } else {
                        rlTopSearchContainer.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public int getScrollY() {
        View c = lvIdphotos.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = lvIdphotos.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    public void updateUI() {
        lvIdphotos.setVisibility(View.VISIBLE);
        getIdphotos();
        titleDisMiss();
    }

    @Override
    public void ResumeDatas() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 0 || position > listIdphotos.size()) {
            return;
        }
        intent = new Intent(getActivity(), IdphotoDetailsActivity.class);
        intent.putExtra("id", listIdphotos.get(position - 1).getId());
        Jump(intent);
    }

}
