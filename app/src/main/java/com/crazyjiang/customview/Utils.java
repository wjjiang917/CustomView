package com.crazyjiang.customview;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Jiangwenjin on 2017/11/1.
 */

public class Utils {
    public static float dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }
}
