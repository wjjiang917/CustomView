package com.crazyjiang.customview;

import android.view.animation.Interpolator;

/**
 * Created by Jiangwenjin on 2017/10/31.
 * <p>
 * 弹性动画的实现 http://blog.csdn.net/qq_34902522/article/details/77651799
 * <p>
 * http://inloop.github.io/interpolator/
 */

public class ScaleInterpolator implements Interpolator {
    //弹性因数
    private float factor;

    public ScaleInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float getInterpolation(float x) {
        return (float) (Math.pow(2, -10 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}
