package com.ruoqian.brainidphoto.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.ruoqian.brainidphoto.R;
import com.ruoqian.brainidphoto.config.SearchTypeConfig;
import com.ruoqian.brainidphoto.dao.DaoManager;
import com.ruoqian.brainidphoto.dao.Idphoto;
import com.ruoqian.brainidphoto.dao.SearchRecord;
import com.ruoqian.brainidphoto.view.EmptyView;
import com.ruoqian.lib.utils.DisplayUtils;
import com.ruoqian.lib.view.ClearEditText;
import com.ruoqian.lib.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索页面
 */
public class SearchActivity extends IdphotoBaseActivity {

    private RelativeLayout rlTop;
    private ImageButton ibtnSearch;
    private ClearEditText etSearch;
    private TextView tvHistoryTitle;
    private TextView tvHotTitle;
    private ImageButton ibtnClearHistory;
    private FlowLayout flHistorys;
    private FlowLayout flSpecs;
    private LinearLayout llHistory;

    private DaoManager daoManager;

    private List<Idphoto> listHotIdphotos;
    private List<TextView> listHotViews;

    private List<SearchRecord> listSearchRecords;
    private List<TextView> listHistoryViews;

    private Handler handler = new Handler(Looper.myLooper());

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnSearch:
                if (!StringUtils.isEmpty(etSearch.getText().toString())) {
                    addKeyWord(etSearch.getText().toString());
                    etSearch.setText("");
                } else {
                    ToastUtils.show(SearchActivity.this, "搜索关键字不能为空！");
                }
                break;
            case R.id.ibtnClearHistory:
                clearHistory();
                break;
            default:
                String tag = v.getTag().toString();
                if (!StringUtils.isEmpty(tag) && tag.equals("search")) {
                    goSearch(v);
                }
                break;
        }
    }

    private void goSearch(View v) {
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            if (tv != null && !StringUtils.isEmpty(tv.getText().toString())) {
                addKeyWord(tv.getText().toString());
            }
        }
    }

    private void clearHistory() {
        new XPopup.Builder(this).asConfirm("操作提醒", "确定要清空所有搜索历史记录？", new OnConfirmListener() {
            @Override
            public void onConfirm() {
                daoManager.delAllSearchRecord(SearchTypeConfig.IDPHOTO);
                llHistory.setVisibility(View.GONE);
                flHistorys.removeAllViews();
            }
        }).show();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public void initViews() {
        super.initViews();
        rlTop = findViewById(R.id.rlTop);
        ibtnSearch = findViewById(R.id.ibtnSearch);
        etSearch = findViewById(R.id.etSearch);
        tvHistoryTitle = findViewById(R.id.tvHistoryTitle);
        tvHotTitle = findViewById(R.id.tvHotTitle);
        ibtnClearHistory = findViewById(R.id.ibtnClearHistory);
        flHistorys = findViewById(R.id.flHistorys);
        flSpecs = findViewById(R.id.flSpecs);
        llHistory = findViewById(R.id.llHistory);
    }

    @Override
    public void initDatas() {
        setTitle(getString(R.string.idphoto_search));
        setBgVisible(View.GONE);
        setTopHeight(rlTop);
        daoManager = DaoManager.getInstance(this);
        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etSearch.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        etSearch.requestFocus();
    }

    @Override
    public void setDatas() {
        setFakeBoldText(tvHistoryTitle);
        setFakeBoldText(tvHotTitle);
        setHotSpecs();
        setHistorys();
    }

    @Override
    public void setListener() {
        ibtnSearch.setOnClickListener(this);
        ibtnClearHistory.setOnClickListener(this);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!StringUtils.isEmpty(etSearch.getText().toString())) {
                        addKeyWord(etSearch.getText().toString());
                        etSearch.setText("");
                    } else {
                        ToastUtils.show(SearchActivity.this, "搜索关键字不能为空！");
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });
    }

    private void addKeyWord(String keyWord) {
        daoManager.addSearckKeyWord(keyWord, SearchTypeConfig.IDPHOTO);
        intent = new Intent(this, IdphotoListsActivity.class);
        intent.putExtra("keyWord", keyWord);
        Jump(intent);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setHistorys();
            }
        }, 100);
    }

    private void setHotSpecs() {
        listHotIdphotos = daoManager.getIdphotoLists(true, null);
        if (listHotIdphotos == null || listHotIdphotos.size() == 0) {
            tvHotTitle.setVisibility(View.GONE);
            flSpecs.setVisibility(View.GONE);
            return;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins((int) DisplayUtils.dp2px(this, 7), (int) DisplayUtils.dp2px(this, 7), (int) DisplayUtils.dp2px(this, 8), (int) DisplayUtils.dp2px(this, 7));
        int len = listHotIdphotos.size();
        listHotViews = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            TextView textView = new TextView(this);
            textView.setText(listHotIdphotos.get(i).getName());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setTextColor(ContextCompat.getColor(this, R.color.color555));
            textView.setPadding((int) DisplayUtils.dp2px(this, 15), (int) DisplayUtils.dp2px(this, 5), (int) DisplayUtils.dp2px(this, 15), (int) DisplayUtils.dp2px(this, 5));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.btn_tag_selector);
            textView.setLayoutParams(lp);
            textView.setTag("search");
            textView.setOnClickListener(this);
            flSpecs.addView(textView);
            listHotViews.add(textView);
        }
    }

    private void setHistorys() {
        listSearchRecords = daoManager.getSearchRecords(SearchTypeConfig.IDPHOTO);
        llHistory.setVisibility(View.VISIBLE);
        if (listSearchRecords == null || listSearchRecords.size() == 0) {
            llHistory.setVisibility(View.GONE);
            return;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins((int) DisplayUtils.dp2px(this, 7), (int) DisplayUtils.dp2px(this, 7), (int) DisplayUtils.dp2px(this, 8), (int) DisplayUtils.dp2px(this, 7));
        int len = listSearchRecords.size();
        listHistoryViews = new ArrayList<>();
        flHistorys.removeAllViews();
        for (int i = 0; i < len; i++) {
            TextView textView = new TextView(this);
            textView.setText(listSearchRecords.get(i).getKeyWord());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setTextColor(ContextCompat.getColor(this, R.color.color555));
            textView.setPadding((int) DisplayUtils.dp2px(this, 15), (int) DisplayUtils.dp2px(this, 5), (int) DisplayUtils.dp2px(this, 15), (int) DisplayUtils.dp2px(this, 5));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.btn_tag_selector);
            textView.setLayoutParams(lp);
            textView.setTag("search");
            textView.setOnClickListener(this);
            flHistorys.addView(textView);
            listHistoryViews.add(textView);
        }
    }
}