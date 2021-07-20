package com.ruoqian.lib.listener;

import android.view.View;

public interface OnRecyclerViewItemClickListener {
    void OnItemClick(View view, int position);
    void OnItemLongClick(View view, int position);
}
