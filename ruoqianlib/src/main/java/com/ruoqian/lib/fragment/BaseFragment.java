package com.ruoqian.lib.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.longtu.base.notice.InitListener;
import com.longtu.base.util.StringUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.ruoqian.lib.R;
import com.ruoqian.lib.api.ApiAskService;
import com.ruoqian.lib.http.ReceiveListener;
import com.ruoqian.lib.http.RequestPresenter;
import com.ruoqian.lib.listener.WifiListener;
import com.ruoqian.lib.utils.LoadingUtils;
import com.ruoqian.lib.view.LoadingView;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public abstract class BaseFragment<T> extends Fragment implements InitListener, View.OnClickListener, ReceiveListener, WifiListener {

    protected Map<String, String> params;
    protected Intent intent;
    protected int layout;
    protected View view;
    private RequestPresenter requestPresenter;
    private Retrofit retrofit;
    private OkHttpClient client;
    public ApiAskService apiAskService;
    private LoadingView loadingView;
    private LoadingPopupView loadingPopupView;
    private InputMethodManager inputMethodManager;
    protected LinearLayoutManager linearLayoutManager;
    protected int dialogType = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setContentView();
        view = inflater.inflate(layout, null);
        requestPresenter = new RequestPresenter(this);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(mTokenInterceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
//                .cookieJar(new CookieManger(getActivity()))//cookie保持
                .build();

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        loadingPopupView = new XPopup.Builder(getActivity()).dismissOnBackPressed(false).dismissOnTouchOutside(false).asLoading("加载中...");
        initViews();
        initLoading();
        initDatas();
        setDatas();
        setListener();
        return view;
    }

    private void initLoading() {
        loadingView = view.findViewById(R.id.loadingView);
        if (loadingView != null) {
            loadingView.setWifiListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ResumeDatas();
    }

    public void hideInput() {
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            try {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
     *
     * @param intent
     */
    public void Jump(Intent intent) {
        startActivity(intent);
    }

    /***
     * 跳转无参数
     *
     * @param cls
     */
    public void Jump(Class cls) {
        intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    /***
     * 跳转有返回
     *
     * @param
     */
    public void Jump(Intent intent, int code) {
        startActivityForResult(intent, code);
    }


    public void sendParams(Call<T> call, int loading) {
        if (loading == LoadingUtils.SUBMITTITLELOAD) {
            showTitleLoading();
        } else if (loading == LoadingUtils.PAGELOAD) {
            showLoading();
        } else if (loading == LoadingUtils.SUBMITLOAD) {
            showLoadingTitle("");
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
            //打印retrofit日志
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

    @Override
    public void Refresh() {
    }

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
            loadingPopupView = new XPopup.Builder(getActivity()).dismissOnBackPressed(false).dismissOnTouchOutside(false).asLoading(loading);
        } else {
            loadingPopupView = new XPopup.Builder(getActivity()).dismissOnBackPressed(false).dismissOnTouchOutside(false).asLoading();
        }
        showTitleLoading();
    }

    protected void titleDisMiss() {
        if (loadingPopupView != null && loadingPopupView.isShow()) {
            loadingPopupView.dismiss();
        }
    }

    protected void showDialog(String title, String content, String cancel, String confirm, int confirmColor) {
        if (confirmColor > 0) {
            XPopup.setPrimaryColor(ContextCompat.getColor(getActivity(), confirmColor));
        }
        new XPopup.Builder(getActivity())
                .asConfirm(title, content, cancel, confirm, new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        onDialogConfirm();
                        dialogType = -1;
                    }
                }, new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        dialogType = -1;
                    }
                }, false).show();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                XPopup.setPrimaryColor(Color.parseColor("#121212"));
            }
        }, 1000);
    }

    protected void onDialogConfirm() {

    }

    protected void setFakeBoldText(TextView tv) {
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
    }
}
