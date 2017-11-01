package com.crazyjiang.customview.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.crazyjiang.customview.R;

import static com.crazyjiang.customview.Utils.dp2px;

/**
 * Created by Jiangwenjin on 2017/10/30.
 */

public class LikeView extends View {
    private final static int OFFSET = 20;

    private int curOffset;
    private ObjectAnimator animator;

    private int centerX;
    private int centerY;

    private Paint paint;
    private Paint oldNumberPaint;
    private Paint newNumberPaint;

    private boolean isLike = false;
    private Bitmap likeBitmap;
    private Bitmap likedBitmap;
    private Bitmap shiningBitmap;

    private int total = 1099;
    private int newTotal = total;
    StringBuilder fixedNumber = new StringBuilder();
    String oldNumber;
    String newNumber;

    public LikeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // init
    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(dp2px(12));
        paint.setColor(Color.parseColor("#c3c4c3"));

        oldNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldNumberPaint.setTextSize(dp2px(12));
        oldNumberPaint.setColor(Color.parseColor("#c3c4c3"));

        newNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        newNumberPaint.setTextSize(dp2px(12));
        newNumberPaint.setColor(Color.parseColor("#c3c4c3"));

        likeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.like);
        likedBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.liked);
        shiningBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.liked_shining);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animator.isRunning()) {
                    return;
                }

                if (isLike) {
                    newTotal = total - 1;
                } else {
                    newTotal = total + 1;
                }
                isLike = !isLike;
                animator.start();
            }
        });

        animator = ObjectAnimator.ofInt(this, "curOffset", 0, OFFSET);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                total = newTotal;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.setDuration(500);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        // draw like icon
        int likeLeft = centerX - likeBitmap.getWidth() / 2;
        int likeTop = centerY - likeBitmap.getHeight() / 2;
        drawLike(canvas, likeLeft, likeTop);

        // draw shining
        drawShining(canvas, likeLeft, likeTop);

        // draw total
        int textLeft = centerX + likeBitmap.getWidth() / 2;
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int baseline = (getHeight() - fontMetricsInt.bottom + fontMetricsInt.top) / 2 - fontMetricsInt.top;

        fixedNumber.delete(0, fixedNumber.length());
        newNumber = "";
        oldNumber = "";
        for (int i = 0; i < Math.min(String.valueOf(total).length(), String.valueOf(newTotal).length()); i++) {
            if (String.valueOf(total).charAt(i) == String.valueOf(newTotal).charAt(i)) {
                fixedNumber.append(String.valueOf(newTotal).charAt(i));
            } else {
                newNumber = String.valueOf(newTotal).substring(i);
                oldNumber = String.valueOf(total).substring(i);
                break;
            }
        }

        canvas.drawText(fixedNumber.toString(), textLeft, baseline, paint);
        drawNumber(canvas, textLeft + paint.measureText(fixedNumber.toString()), baseline);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.end();
    }

    private void drawLike(Canvas canvas, int likeLeft, int likeTop) {
        if (isLike && getPercent() > 0.9 && getPercent() < 1) {
            canvas.save();
            canvas.translate((float) (likeLeft - likedBitmap.getWidth() * 0.05), (float) (likeTop - likedBitmap.getHeight() * 0.05));
            canvas.scale(1.1f, 1.1f);
            canvas.drawBitmap(likedBitmap, 0, 0, paint);
            canvas.restore();
        } else if (isLike && getPercent() > 0.5) {
            canvas.drawBitmap(likedBitmap, likeLeft, likeTop, paint);
        } else {
            canvas.drawBitmap(likeBitmap, likeLeft, likeTop, paint);
        }

//        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 1.8f);
//        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 1.8f);
//        AnimatorSet set = new AnimatorSet();
//        set.setDuration(500);
//        set.setInterpolator(new ScaleInterpolator(0.4f));
//        set.playTogether(animatorX, animatorY);
//        set.start();
    }

    private void drawShining(Canvas canvas, int likeLeft, int likeTop) {
        if (isLike) {
            canvas.save();

            float scale = getPercent();
            int shiningTop = (int) (likeTop - scale * shiningBitmap.getHeight() / 2);
            int shiningLeft = (int) (likeLeft + (likeBitmap.getWidth() - scale * shiningBitmap.getWidth()) / 2);

            canvas.translate(shiningLeft, shiningTop);
            canvas.scale(scale, scale);

            canvas.drawBitmap(shiningBitmap, 0, 0, paint);
            canvas.restore();
        }
    }

    private void drawNumber(Canvas canvas, float left, int baseline) {
        if (null == oldNumber || oldNumber.isEmpty()) {
            return;
        }

        int alpha = (int) ((1 - getPercent()) * 255);
        oldNumberPaint.setAlpha(alpha);
        newNumberPaint.setAlpha(255 - alpha);
        if (isLike) {
            canvas.drawText(oldNumber, left, baseline - curOffset, oldNumberPaint);
            canvas.drawText(newNumber, left, baseline + OFFSET - curOffset, newNumberPaint);
        } else {
            canvas.drawText(oldNumber, left, baseline + curOffset, oldNumberPaint);
            canvas.drawText(newNumber, left, baseline - OFFSET + curOffset, newNumberPaint);
        }
    }

    public float getPercent() {
        return 1.0F * curOffset / OFFSET;
    }

    public int getCurOffset() {
        return curOffset;
    }

    public void setCurOffset(int curOffset) {
        this.curOffset = curOffset;
    }
}
