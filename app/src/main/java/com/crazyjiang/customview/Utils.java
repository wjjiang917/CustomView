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

    public static double getDistanceBetween2Points(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.abs((x2 - x1) * (x2 - x1) - (y2 - y1) * (y2 - y1)));
    }
}
