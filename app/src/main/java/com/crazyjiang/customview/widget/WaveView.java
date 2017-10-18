package com.crazyjiang.customview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.crazyjiang.customview.R;

/**
 * Created by Jiangwenjin on 2017/10/18.
 */

public class WaveView extends View {
    private int width; // 画布宽度
    private int height; // 画布高度

    private int cycle = 100; // sin曲线 1／4周期宽度
    private int waveHeight = 200;
    private Point startPoint;

    private Path path;
    private Paint paint;
    private Paint textPaint; // percent
    private Paint circlePaint;

    private int progress; // 0 - 100
    private ValueAnimator valueAnimator;

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);

        circlePaint = new Paint();
        circlePaint.setStrokeWidth(5);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor("#FF4081"));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(10);
        textPaint.setColor(Color.BLACK);

        valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(20 * 1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE); // loading
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取view的宽度
        width = getViewSize(400, widthMeasureSpec);
        //获取view的高度
        height = getViewSize(400, heightMeasureSpec);

        startPoint = new Point(-cycle * 4, height / 2);
    }

    private int getViewSize(int defaultSize, int measureSpec) {
        int viewSize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED: //如果没有指定大小，就设置为默认大小
                viewSize = defaultSize;
                break;
            case MeasureSpec.AT_MOST: //如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                viewSize = size;
                break;
            case MeasureSpec.EXACTLY: //如果是固定的大小，那就不要去改变它
                viewSize = size;
                break;
        }
        return viewSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorPrimary)); // bg
        canvas.save();

        Path circlePath = new Path();
        circlePath.addCircle(width / 2, height / 2, width / 2, Path.Direction.CW);
        canvas.clipPath(circlePath); // 裁剪
        canvas.drawPaint(circlePaint);
        canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint); // 画圆

        // 根据percent，动态修改startPoint的y值
        startPoint.y = (int) (height - (progress / 100.0 * height));

        // wave
        path.moveTo(startPoint.x, startPoint.y);
        int j = 1;
        for (int i = 1; i <= 8; i++) {
            if (i % 2 == 0) { //
                path.quadTo(startPoint.x + (cycle * j), startPoint.y + waveHeight,
                        startPoint.x + cycle * 2 * i, startPoint.y);
            } else {
                path.quadTo(startPoint.x + (cycle * j), startPoint.y - waveHeight,
                        startPoint.x + (cycle * 2) * i, startPoint.y);
            }

            j += 2;
        }

        path.lineTo(width, height);
        path.lineTo(startPoint.x, height);
        path.lineTo(startPoint.x, startPoint.y);
        path.close();

        canvas.drawPath(path, paint);

        // percent
        Rect targetRect = new Rect(0, 0, width, height);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(progress + "%", targetRect.centerX(), baseline, paint);

        // 循环设置startPoint
        if (startPoint.x + 5 > 0) {
            startPoint.x = -cycle * 4;
        }
        startPoint.x += 5;
        postInvalidateDelayed(10);
        path.reset();
    }
}
