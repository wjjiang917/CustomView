package com.crazyjiang.customview.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.crazyjiang.customview.R;

/**
 * Created by Jiangwenjin on 2017/10/31.
 */

public class FlipboardView extends View {
    private Paint paint;
    private Bitmap bitmap;
    private Camera camera;

    private AnimatorSet animatorSet;
    private ObjectAnimator animator1;
    private int degree1;
    private ObjectAnimator animator2;
    private int degree2;
    private ObjectAnimator animator3;
    private int degree3;

    public FlipboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        camera = new Camera();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.flipboard);

        animator1 = ObjectAnimator.ofInt(this, "degree1", 0, 30);
        animator1.setDuration(1000L);
        animator1.setInterpolator(new LinearInterpolator());

        animator2 = ObjectAnimator.ofInt(this, "degree2", 0, 270);
        animator2.setDuration(2000L);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());

        animator3 = ObjectAnimator.ofInt(this, "degree3", 0, 30);
        animator3.setDuration(1000L);
        animator3.setInterpolator(new LinearInterpolator());

        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator1, animator2, animator3);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                degree1 = 0;
                degree2 = 0;
                degree3 = 0;
                animatorSet.start();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2; // upper-left
        int y = centerY - bitmapHeight / 2; // upper-left

        canvas.save();
        canvas.rotate(-degree2, centerX, centerY);
        canvas.clipRect(0, 0, centerX, getHeight());
        canvas.rotate(degree2, centerX, centerY);

        camera.save();
        camera.rotateX(-degree3);
        canvas.translate(centerX, centerY);
        camera.applyToCanvas(canvas);
        canvas.translate(-centerX, -centerY);
        camera.restore();

        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(-degree2, centerX, centerY);
        canvas.clipRect(centerX, 0, getWidth(), getHeight());

        camera.save();
        camera.rotateY(-degree1);
        canvas.translate(centerX, centerY);
        camera.applyToCanvas(canvas);
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.rotate(degree2, centerX, centerY);

        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();
    }

    public int getDegree1() {
        return degree1;
    }

    public void setDegree1(int degree1) {
        this.degree1 = degree1;
        invalidate();
    }

    public int getDegree2() {
        return degree2;
    }

    public void setDegree2(int degree2) {
        this.degree2 = degree2;
        invalidate();
    }

    public int getDegree3() {
        return degree3;
    }

    public void setDegree3(int degree3) {
        this.degree3 = degree3;
        invalidate();
    }
}
