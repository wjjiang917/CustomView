package com.crazyjiang.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.crazyjiang.customview.Utils;


/**
 * Created by Jiangwenjin on 2017/11/9.
 */

public class BallGestureView extends View {
    private float centerX;
    private float centerY;
    private float radius;

    private boolean touched;

    private Paint paint;
    private GestureDetector gestureDetector;

    public BallGestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_light));

        radius = Utils.dp2px(30);

        setClickable(true);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (touched) {
                    centerX -= distanceX;
                    centerY -= distanceY;

                    // keep in screen
                    if (centerX < radius) {
                        centerX = radius;
                    } else if (centerX > getWidth() - radius) {
                        centerX = getWidth() - radius;
                    }
                    if (centerY < radius) {
                        centerY = radius;
                    } else if (centerY > getHeight() - radius) {
                        centerY = getHeight() - radius;
                    }

                    postInvalidate();
                }

                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event); // touch -> GestureDetector

        // check if touch the ball
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (Utils.getDistanceBetween2Points(centerX, centerY, event.getX(), event.getY()) < radius) {
                touched = true;
            } else {
                touched = false;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w > 0) {
            centerX = w / 2.0f;
        }
        if (h > 0) {
            centerY = h / 2.0f;
        }
    }
}
