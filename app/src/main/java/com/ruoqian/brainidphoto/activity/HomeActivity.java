package com.ruoqian.brainidphoto.activity;

import android.Manifest;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;
import com.ruoqian.brainidphoto.R;
import com.ruoqian.brainidphoto.dao.DaoManager;
import com.ruoqian.brainidphoto.event.EventType;
import com.ruoqian.brainidphoto.event.IdphotoEvent;
import com.ruoqian.brainidphoto.fragment.HomeFragment;
import com.ruoqian.brainidphoto.fragment.MyFragment;
import com.ruoqian.brainidphoto.utils.UserContact;
import com.ruoqian.lib.activity.BaseActivity;
import com.ruoqian.lib.bean.IdphotoColorListsBean;
import com.ruoqian.lib.bean.IdphotoListsBean;
import com.ruoqian.lib.bean.IdphotoPrintListsBean;
import com.ruoqian.lib.bean.IdphotoStrategyBean;
import com.ruoqian.lib.utils.LoadingUtils;
import com.ruoqian.lib.utils.SharedUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    private Button btnHome;
    private Button btnMy;
    private Button[] Tabs;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private HomeFragment homeFragment;
    private MyFragment myFragment;
    private Fragment[] fragments;

    private boolean back = false;
    private int currentTabIndex;

    private DaoManager daoManager;

    private boolean isHandleIdphoto = false;

    private final static int IDPHOTO_LISTS = 20001;
    private final static int IDPHOTO_COLOR_LISTS = 20002;
    private final static int IDPHOTO_PRINT_LISTS = 20003;
    private final static int IDPHOTO_UPDATE_UI = 20004;
    private final static int BACKSYS = 20005;
    private final static int HANDLE_IDPHOTO = 20006;
    private final static int IDPHOTO_STRATEGY = 20007;

    private IdphotoListsBean idphotoListsBean;
    private IdphotoColorListsBean idphotoColorListsBean;
    private IdphotoPrintListsBean idPhotoPrintListsBean;
    private IdphotoStrategyBean idphotoStrategyBean;

    private IdphotoEvent idphotoEvent;

    private Message msg;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case IDPHOTO_LISTS:
                    idphotoListsBean = (IdphotoListsBean) msg.obj;
                    if (idphotoListsBean != null) {
                        if (idphotoListsBean.getStateCode() == 0 && idphotoListsBean.getData() != null) {
                            setIdphotoLists();
                        }
                    }
                    isHandleIdphoto = false;
                    removeMessages(HANDLE_IDPHOTO);
                    break;
                case IDPHOTO_COLOR_LISTS:
                    idphotoColorListsBean = (IdphotoColorListsBean) msg.obj;
                    if (idphotoColorListsBean != null) {
                        if (idphotoColorListsBean.getStateCode() == 0 && idphotoColorListsBean.getData() != null) {
                            setIdphotoColorLists();
                        }
                    }
                    break;
                case IDPHOTO_PRINT_LISTS:
                    idPhotoPrintListsBean = (IdphotoPrintListsBean) msg.obj;
                    if (idPhotoPrintListsBean != null) {
                        if (idPhotoPrintListsBean.getStateCode() == 0 && idPhotoPrintListsBean.getData() != null) {
                            setIdphotoPrintLists();
                        }
                    }
                    break;
                case IDPHOTO_STRATEGY:
                    idphotoStrategyBean = (IdphotoStrategyBean) msg.obj;
                    if (idphotoStrategyBean != null) {
                        if (idphotoStrategyBean.getStateCode() == 0 && idphotoStrategyBean.getData() != null) {
                            SharedUtils.setStrategy(HomeActivity.this, (new Gson()).toJson(idphotoStrategyBean.getData()));
                            UserContact.listStrategys = idphotoStrategyBean.getData();
                        }
                    }
                    break;
                case IDPHOTO_UPDATE_UI:
                    if (idphotoEvent != null) {
                        sendUpdateUI();
                        idphotoEvent = null;
                    }
                    break;
                case BACKSYS:
                    back = false;
                    break;
                case HANDLE_IDPHOTO:
                    isHandleIdphoto = false;
                    break;
            }
        }
    };

    private void sendUpdateUI() {
        switch (idphotoEvent.getType()) {
            case EventType.HOME_RECOMMEND:
                if (homeFragment != null) {
                    homeFragment.updateUI();
                }
                break;
            case EventType.IDPHOTO_LISTS:
                IdphotoEvent idphotoEvent = new IdphotoEvent();
                idphotoEvent.setType(EventType.IDPHOTO_LISTS_UI);
                EventBus.getDefault().post(idphotoEvent);
                break;
        }
    }

    /**
     * 设置更新数据库证件照打印数据
     */
    private void setIdphotoPrintLists() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    daoManager.setIdphotoPrintData(idPhotoPrintListsBean.getData());
                    handler.sendEmptyMessageDelayed(IDPHOTO_UPDATE_UI, 100);
                } catch (Exception e) {
                }
            }
        }).start();
    }

    /**
     * 设置更新数据证件照颜色数据
     */
    private void setIdphotoColorLists() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    daoManager.setIdphotoColorData(idphotoColorListsBean.getData());
                } catch (Exception e) {
                }
            }
        }).start();
    }

    /**
     * 设置更新数据库证件照数据
     */
    private void setIdphotoLists() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    daoManager.setIdphotoData(idphotoListsBean.getData());

                } catch (Exception e) {
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHome:
                setReplace(0);
                break;
            case R.id.btnMy:
                setReplace(1);
                break;
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    public void initViews() {
        btnHome = findViewById(R.id.btnHome);
        btnMy = findViewById(R.id.btnMy);
    }

    @Override
    public void initDatas() {
        daoManager = DaoManager.getInstance(this);
        EventBus.getDefault().register(this);
        fragmentManager = getSupportFragmentManager();
        Tabs = new Button[]{btnHome, btnMy};
        homeFragment = new HomeFragment();
        myFragment = new MyFragment();
        fragments = new Fragment[]{homeFragment, myFragment};
//        initPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    @Override
    public void setDatas() {
        setReplace(0);
        if (!StringUtils.isEmpty(SharedUtils.getStrategy(this))) {
            UserContact.listStrategys = (new Gson()).fromJson(SharedUtils.getStrategy(this),  new TypeToken<List<String>>() {
            }.getType());
        }
        getIdphotoLists();
        getIdphotoColorLists();
        getIdphotoPrintLists();
        getIdphotoStrategys();
    }

    @Override
    public void setListener() {
        btnHome.setOnClickListener(this);
        btnMy.setOnClickListener(this);
    }

    public void setReplace(int index) {
        try {
            back = false;

            transaction = fragmentManager.beginTransaction();
            if (!fragments[index].isAdded()) {
                transaction.add(R.id.rlContainer, fragments[index]);
            }

            if (currentTabIndex > -1) {
                transaction.hide(fragments[currentTabIndex]);
                Tabs[currentTabIndex].setSelected(false);
                Tabs[currentTabIndex].setTextColor(getResources().getColor(R.color.tab_unselect));
            }

            transaction.show(fragments[index]).commitAllowingStateLoss();
            Tabs[index].setSelected(true);
            Tabs[index].setTextColor(getResources().getColor(R.color.tab_selected));
            currentTabIndex = index;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取证件照列表
     */
    private void getIdphotoLists() {
        if (isHandleIdphoto) {
            return;
        }
        Call<IdphotoListsBean> call = apiAskService.idphotoLists();
        sendParams(call, LoadingUtils.NOLOAD);
        isHandleIdphoto = true;
        handler.sendEmptyMessageDelayed(HANDLE_IDPHOTO, 5000);
    }

    /**
     * 获取证件照颜色列表
     */
    private void getIdphotoColorLists() {
        Call<IdphotoColorListsBean> call = apiAskService.idphotoColorLists();
        sendParams(call, LoadingUtils.NOLOAD);
    }

    /**
     * 获取证件照打印列表
     */
    private void getIdphotoPrintLists() {
        Call<IdphotoPrintListsBean> call = apiAskService.idphotoPrintLists();
        sendParams(call, LoadingUtils.NOLOAD);
    }

    /**
     * 获取拍照攻略列表
     */
    private void getIdphotoStrategys() {
        Call<IdphotoStrategyBean> call = apiAskService.idphotoStrategys();
        sendParams(call, LoadingUtils.NOLOAD);
    }

    @Override
    public void onSuccess(Response response, int loading) {
        super.onSuccess(response, loading);
        msg = new Message();
        if (response.body() instanceof IdphotoListsBean) {
            msg.what = IDPHOTO_LISTS;
        } else if (response.body() instanceof IdphotoColorListsBean) {
            msg.what = IDPHOTO_COLOR_LISTS;
        } else if (response.body() instanceof IdphotoPrintListsBean) {
            msg.what = IDPHOTO_PRINT_LISTS;
        } else if (response.body() instanceof IdphotoStrategyBean) {
            msg.what = IDPHOTO_STRATEGY;
        }
        msg.obj = response.body();
        handler.sendMessage(msg);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(Object event) {
        if (event instanceof IdphotoEvent) {
            idphotoEvent = (IdphotoEvent) event;
            handleIdphotoEvent();
        }
    }

    /**
     * 处理获取证件照数据
     */
    private void handleIdphotoEvent() {
        if (idphotoEvent == null) {
            return;
        }
        switch (idphotoEvent.getType()) {
            case EventType.HOME_RECOMMEND:
                getIdphotoLists();
                break;
            case EventType.IDPHOTO_LISTS:
                getIdphotoLists();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (back) {
                moveTaskToBack(false);
                return super.onKeyDown(keyCode, event);
            } else {
                back = true;
                handler.sendEmptyMessageDelayed(BACKSYS, 5000);
                ToastUtils.show(this, "再按一次退出");
            }
            //do something...
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}