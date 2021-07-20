package com.ruoqian.lib.utils;

import android.content.Context;
import android.util.TypedValue;

public class DisplayUtils {
    public static float dp2px(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
