package com.crazyjiang.customview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Jiangwenjin on 2017/11/1.
 */

public class RadarView extends View {
    private Paint paint;

    private float radius;
    private float lineLength;

    private int degree;
    private ObjectAnimator animator;

    private Shader shader;
    private Paint radarPaint;
    private Matrix matrix;

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        setWillNotDraw(false);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);

        shader = new SweepGradient(0, 0, Color.TRANSPARENT, Color.GREEN);
        radarPaint = new Paint();
        matrix = new Matrix();

        animator = ObjectAnimator.ofInt(this, "degree", 0, 360);
        animator.setDuration(4000L);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        radius = 1.0f * getWidth() / 4;
        lineLength = radius + 20;

        canvas.translate(centerX, centerY);

        paint.setStrokeWidth(5);
        canvas.drawLine(-lineLength, 0, lineLength, 0, paint);
        canvas.drawLine(0, -lineLength, 0, lineLength, paint);
        canvas.drawCircle(0, 0, radius, paint);
        canvas.drawCircle(0, 0, radius / 2, paint);
        canvas.save();

        radarPaint.reset();
        matrix.setRotate(degree);
        shader.setLocalMatrix(matrix);
        radarPaint.setShader(shader);
        canvas.drawCircle(0, 0, radius, radarPaint);
        canvas.restore();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.end();
    }

    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }
}
