package com.ruoqian.brainidphoto.activity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longtu.base.util.StringUtils;
import com.ruoqian.brainidphoto.R;
import com.ruoqian.brainidphoto.adapter.IdphotoRecyclerAdapter;
import com.ruoqian.brainidphoto.dao.DaoManager;
import com.ruoqian.brainidphoto.dao.Idphoto;
import com.ruoqian.brainidphoto.event.EventType;
import com.ruoqian.brainidphoto.event.IdphotoEvent;
import com.ruoqian.brainidphoto.view.EmptyView;
import com.ruoqian.lib.listener.OnRecyclerViewItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class IdphotoListsActivity extends IdphotoBaseActivity implements OnRecyclerViewItemClickListener {

    private RelativeLayout rlTop;
    private RelativeLayout rlSearch;
    private TextView tvSearch;
    private RecyclerView recyclerIdphotos;
    private LinearLayout llMain;

    private EmptyView emptyView;

    private List<Idphoto> listIdphotos;
    private DaoManager daoManager;

    private String keyWord;

    private IdphotoRecyclerAdapter idphotoRecyclerAdapter;

    private final static int UPDATE_UI_DATA = 20001;

    private Message msg;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_UI_DATA:
                    llMain.setVisibility(View.VISIBLE);
                    getIdphotoLists();
                    titleDisMiss();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlSearch:
                Jump(SearchActivity.class);
                break;
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_idphoto_lists);
    }

    @Override
    public void initViews() {
        super.initViews();
        rlTop = findViewById(R.id.rlTop);
        rlSearch = findViewById(R.id.rlSearch);
        tvSearch = findViewById(R.id.tvSearch);
        llMain = findViewById(R.id.llMain);
        recyclerIdphotos = findViewById(R.id.recyclerIdphotos);
        emptyView = findViewById(R.id.emptyView);
    }

    @Override
    public void initDatas() {
        keyWord = getIntent().getStringExtra("keyWord");
        setTitle(getString(R.string.idphoto_specs));
        setBgVisible(View.GONE);
        setTopHeight(rlTop);
        emptyView.setEmptyIcon(R.mipmap.icon_search_empty);
        emptyView.setEmptyTxt(getString(R.string.idphoto_search_empty));
        daoManager = DaoManager.getInstance(this);
        EventBus.getDefault().register(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerIdphotos.setLayoutManager(linearLayoutManager);
        recyclerIdphotos.setItemAnimator(new DefaultItemAnimator());
        listIdphotos = new ArrayList<>();
        idphotoRecyclerAdapter = new IdphotoRecyclerAdapter(listIdphotos, this, this);
        recyclerIdphotos.setAdapter(idphotoRecyclerAdapter);
    }

    @Override
    public void setDatas() {
        if (!StringUtils.isEmpty(keyWord)) {
            tvSearch.setText(keyWord);
        }
        getIdphotoLists();
    }

    private void getIdphotoLists() {
        emptyView.setVisibility(View.GONE);
        List<Idphoto> lists = daoManager.getIdphotoLists(false, null);
        if (lists == null || lists.size() == 0) {
            //发送消息进行网络请求数据
            llMain.setVisibility(View.GONE);
            showLoadingTitle("");
            //发送请求接口获取数据
            IdphotoEvent idphotoEvent = new IdphotoEvent();
            idphotoEvent.setType(EventType.IDPHOTO_LISTS);
            EventBus.getDefault().post(idphotoEvent);
            return;
        }
        lists = daoManager.getIdphotoLists(false, keyWord);
        lists = lists == null ? new ArrayList<>() : lists;
        listIdphotos.clear();
        listIdphotos.addAll(lists);
        idphotoRecyclerAdapter.notifyDataSetChanged();
        if (listIdphotos == null || listIdphotos.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setListener() {
        rlSearch.setOnClickListener(this);
    }

    @Override
    public void OnItemClick(View view, int position) {

    }

    @Override
    public void OnItemLongClick(View view, int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(Object event) {
        if (event instanceof IdphotoEvent) {
            IdphotoEvent idphotoEvent = (IdphotoEvent) event;
            if (idphotoEvent != null) {
                if (idphotoEvent.getType() == EventType.IDPHOTO_LISTS_UI) {
                    handler.sendEmptyMessage(UPDATE_UI_DATA);
                }
            }
        }
    }
}