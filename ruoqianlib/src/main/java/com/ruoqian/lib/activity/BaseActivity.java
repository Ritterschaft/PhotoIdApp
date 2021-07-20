package com.ruoqian.lib.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.longtu.base.notice.InitListener;
import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.ruoqian.lib.R;
import com.ruoqian.lib.api.ApiAskService;
import com.ruoqian.lib.http.ReceiveListener;
import com.ruoqian.lib.http.RequestPresenter;
import com.ruoqian.lib.listener.WifiListener;
import com.ruoqian.lib.permissions.RxPermissions;
import com.ruoqian.lib.utils.LoadingUtils;
import com.ruoqian.lib.utils.SharedUtils;
import com.ruoqian.lib.view.LoadingView;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseActivity<T> extends AppCompatActivity implements View.OnClickListener, InitListener,
        ReceiveListener, WifiListener {
    protected Intent intent;
    protected ActionBar actionBar;

    protected Map<String, String> params;
    private OkHttpClient client;
    private Retrofit retrofit;
    protected ApiAskService apiAskService;
    private RequestPresenter requestPresenter;

    protected LinearLayoutManager linearLayoutManager;

    public ApiAskService otherApiService;

    private LoadingView loadingView;
    private LoadingPopupView loadingPopupView;
    protected int permissionType = 0;

    protected final static int TOAST = 1001;
    protected final static int TITLEDISMISS = 1002;

    private InputMethodManager inputMethodManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStackImmediate(null, 1);
        }
        /**
         * 去除透明状态栏
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        requestPresenter = new RequestPresenter(this);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(mTokenInterceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        initApi();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        loadingPopupView = new XPopup.Builder(this).dismissOnBackPressed(false).dismissOnTouchOutside(false).asLoading("加载中...");
        setContentView();
        initViews();
//        setBar();
        initLoading();
        initDatas();
        setDatas();
        setListener();
    }

    protected void initApi() {

        retrofit = new Retrofit.Builder()
                .baseUrl(SharedUtils.getBaseUrl(this))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiAskService = retrofit.create(ApiAskService.class);
    }

    public void initOtherApi(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        otherApiService = retrofit.create(ApiAskService.class);
    }


    private void initLoading() {
        loadingView = findViewById(R.id.loadingView);
        if (loadingView != null) {
            loadingView.setWifiListener(this);
        }
    }

    protected void setBar(Drawable upArrow) {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorbar)));
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                actionBar.setElevation(0);
            }
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
//            Drawable upArrow = getResources().getDrawable(R.mipmap.icon_excel_save);
//            upArrow.setColorFilter(getResources().getColor(R.color.color363636), PorterDuff.Mode.SRC_ATOP);
//            actionBar.setHomeAsUpIndicator(upArrow);
//            tvTitle.set
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("");
        }
    }


    protected void setBar() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorbar)));
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                actionBar.setElevation(0);
            }
            Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.color282828), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
//            Drawable upArrow = getResources().getDrawable(R.mipmap.icon_excel_save);
//            upArrow.setColorFilter(getResources().getColor(R.color.color363636), PorterDuff.Mode.SRC_ATOP);
//            actionBar.setHomeAsUpIndicator(upArrow);
//            tvTitle.set
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("");
        }
    }

    protected void setBar(View view) {
        if (actionBar != null) {
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            actionBar.setCustomView(view, lp);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    protected void setTitle(String title) {
        if (!StringUtils.isEmpty(title) && actionBar != null) {
            actionBar.setTitle(title);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ResumeDatas();
    }

    @Override
    public void ResumeDatas() {
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void hideInput() {
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            try {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
            }
        }
    }


    protected void toggleInput() {
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(0, 0);
        }
    }

    /**
     * 跳转
     */
    public void Jump(Intent intent) {
        startActivity(intent);
    }

    /***
     * 跳转无参数
     */
    public void Jump(Class cls) {
        intent = new Intent(this, cls);
        startActivity(intent);
    }

    /***
     * 跳转有返回
     */
    public void Jump(Intent intent, int code) {
        startActivityForResult(intent, code);
    }

    @Override
    public void Refresh() {

    }

    public void sendParams(Call<T> call, int loading) {
        if (loading == LoadingUtils.SUBMITTITLELOAD) {
            showTitleLoading();
        } else if (loading == LoadingUtils.SUBMITLOAD) {
            showLoadingTitle("");
        }else if (loading == LoadingUtils.PAGELOAD) {
            showLoading();
        }
        requestPresenter.sendParams(call, loading);
    }

    @Override
    public void onSuccess(Response response, int loading) {
        if (loading == LoadingUtils.PAGELOAD) {
            disMissLoading();
        } else if (loading == LoadingUtils.SUBMITTITLELOAD || loading == LoadingUtils.SUBMITLOAD) {
            titleDisMiss();
        }
        isExpiredToken(response);
    }

    private void isExpiredToken(Response response) {

    }

    @Override
    public void onFailure(String error, int loading) {
        if (loading == LoadingUtils.PAGELOAD) {
            NoLoading();
        } else if (loading == LoadingUtils.SUBMITTITLELOAD || loading == LoadingUtils.SUBMITLOAD) {
            titleDisMiss();
        }
    }

    private HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Log.e("RetrofitLog", message);
        }
    });
    private Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("token", "")
                    .build();
            return chain.proceed(request);
        }
    };

    private void showLoading() {
        if (loadingView != null && loadingView.getVisibility() == View.GONE) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    protected void disMissLoading() {
        if (loadingView != null && loadingView.getVisibility() == View.VISIBLE) {
            loadingView.setVisibility(View.GONE);
        }
    }

    private void NoLoading() {
        if (loadingView != null) {
            loadingView.setLoadingVisible();
        }
    }

    protected void showTitleLoading() {
        if (loadingPopupView != null && loadingPopupView.isDismiss()) {
            loadingPopupView.show();
        }
    }

    protected void showLoadingTitle(String loading) {
        if (!StringUtils.isEmpty(loading)) {
            loadingPopupView = new XPopup.Builder(this).dismissOnBackPressed(false).dismissOnTouchOutside(false).asLoading(loading);
        } else {
            loadingPopupView = new XPopup.Builder(this).dismissOnBackPressed(false).dismissOnTouchOutside(false).asLoading();
        }
        showTitleLoading();
    }

    protected void titleDisMiss() {
        if (loadingPopupView != null && loadingPopupView.isShow()) {
            loadingPopupView.dismiss();
        }
    }

    protected void initPermissions(String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissions).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    onPermission();
                } else {
                    ToastUtils.show(BaseActivity.this, "授权被拒绝");
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                onCompletePermission();
            }
        });
    }

    protected void onCompletePermission() {
        permissionType = 0;
    }

    protected void onPermission() {

    }

    protected void setFakeBoldText(TextView tv) {
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
    }

    protected void setStautsHeight(View viewStatus) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, XPopupUtils.getStatusBarHeight());
        viewStatus.setLayoutParams(layoutParams);
    }

}
