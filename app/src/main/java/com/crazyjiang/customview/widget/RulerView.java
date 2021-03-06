package com.crazyjiang.customview.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import static com.crazyjiang.customview.Utils.dp2px;

/**
 * Created by Jiangwenjin on 2017/10/31.
 */

public class RulerView extends View {
    private final static int LENGTH = 100;

    private int value = 30;
    private Paint paint;
    private float padding;
    private float bigStrokeWidth;
    private float smallStrokeWidth;
    private float bigHeight;
    private float smallHeight;

    private float rulerWidth;
    private float rulerHeight;

    private ObjectAnimator animator;

    private GestureDetector gestureDetector;
    private int distance;

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        setClickable(true);

        rulerHeight = dp2px(80);
        padding = dp2px(10);
        bigStrokeWidth = dp2px(2);
        smallStrokeWidth = dp2px(1);
        bigHeight = dp2px(40);
        smallHeight = dp2px(20);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);

        rulerWidth = padding * LENGTH;

        animator = ObjectAnimator.ofInt(this, "value", 30, LENGTH);
        animator.setDuration(5 * 1000L);
//        animator.start();

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                distance -= distanceX;
//                invalidate();

                int temp = value + (int) (distanceX * 100 / rulerWidth);
                temp = temp < 0 ? 0 : temp;
                temp = temp > 100 ? 100 : temp;
                setValue(temp);

                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(ContextCompat.getColor(getContext(), android.R.color.white)); // bg
        canvas.save();

        float startX = 1.0f * getWidth() / 2 - rulerWidth * value / LENGTH;

        paint.setColor(Color.parseColor("#f7f9f6"));
        canvas.drawRect(startX, getHeight() / 2, startX + rulerWidth, getHeight() / 2 + rulerHeight, paint);

        paint.setColor(Color.parseColor("#dbdeda"));
        canvas.drawLine(startX, getHeight() / 2, startX + rulerWidth, getHeight() / 2, paint);

        for (int i = 0; i <= LENGTH; i++) {
            float position = startX + padding * i;
            if (i % 5 == 0) {
                paint.setStrokeWidth(bigStrokeWidth);
                canvas.drawLine(position, getHeight() / 2, position, getHeight() / 2 + bigHeight, paint);

                paint.setTextSize(dp2px(16));
                canvas.drawText(String.valueOf(i), position, getHeight() / 2 + dp2px(60), paint);
            } else {
                paint.setStrokeWidth(smallStrokeWidth);
                canvas.drawLine(position, getHeight() / 2, position, getHeight() / 2 + smallHeight, paint);
            }
        }

        canvas.save();

        paint.setColor(Color.parseColor("#67b77b"));
        paint.setStrokeWidth(dp2px(3));
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 + dp2px(42), paint);

        paint.setTextSize(dp2px(32));
        canvas.drawText(String.valueOf(value), getWidth() / 2, getHeight() / 2 - dp2px(20), paint);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    public void setValue(int value) {
        this.value = value;
        invalidate();
    }
}
