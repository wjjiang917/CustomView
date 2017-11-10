package com.crazyjiang.customview.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.crazyjiang.customview.R;

/**
 * Created by Jiangwenjin on 2017/11/10.
 */

public class ItemGestureView extends LinearLayout {
    private GestureDetector gestureDetector;

    private int contentWidth;

    public ItemGestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        setClickable(true);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                int scrollX = getScrollX();
                int minScrollX = -scrollX;
                int maxScrollX = contentWidth - scrollX;

                // 判断还原还是滑动
                if (distanceX > maxScrollX) {
                    distanceX = maxScrollX;
                } else if (distanceX < minScrollX) {
                    distanceX = minScrollX;
                }

                scrollBy((int) distanceX, 0);

                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (velocityX < 0) { // show right view
                    showRight();
                } else { // show left view
                    showLeft();
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        LayoutInflater.from(getContext()).inflate(R.layout.item_gesture, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
            if (i == 1) {
                contentWidth = getChildAt(i).getMeasuredWidth();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            int scrollX = getScrollX();
            if (scrollX > contentWidth / 2) {
                showRight();
            } else {
                showLeft();
            }
        }

        return super.onTouchEvent(event);
    }

    private void showRight() {
        scrollTo(contentWidth, 0);
    }

    private void showLeft() {
        scrollTo(0, 0);
    }
}
