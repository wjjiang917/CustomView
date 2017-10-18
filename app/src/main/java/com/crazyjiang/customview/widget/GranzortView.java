package com.crazyjiang.customview.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.crazyjiang.customview.R;

/**
 * Created by Jiangwenjin on 2017/10/18.
 */

public class GranzortView extends View {
    private Paint paint;
    private PathMeasure pathMeasure;

    private Path innerCircle;
    private Path outerCircle;

    private Path triangle1;
    private Path triangle2;

    private Path dst;

    private float mViewWidth;
    private float mViewHeight;

    private float percent;

    private ValueAnimator valueAnimator;
    private State mCurrentState = State.CIRCLE_STATE;

    private enum State {
        CIRCLE_STATE, // 圆环
        TRIANGLE_LINE_STATE, // 三角形
        TRIANGLE_STATE, // 三角形
        FINISH_STATE
    }

    public GranzortView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG); // 去锯齿
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);  // 空心
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.BEVEL);
        paint.setShadowLayer(15, 0, 0, Color.WHITE); // 白色光影效果

        dst = new Path();

        innerCircle = new Path();
        RectF innerRectF = new RectF(-220, -220, 220, 220); // 矩形区域
        innerCircle.addArc(innerRectF, 150, -359.9F); // 绘制圆弧

        outerCircle = new Path();
        RectF outerRectF = new RectF(-280, -280, 280, 280);
        outerCircle.addArc(outerRectF, 60, -359.9F);

        pathMeasure = new PathMeasure();
        pathMeasure.setPath(innerCircle, false); // 三角形的点坐标

        triangle1 = new Path();
        float[] pos = new float[2];

        // 画三边
        pathMeasure.getPosTan(0, pos, null);
        triangle1.moveTo(pos[0], pos[1]); //

        pathMeasure.getPosTan((1f / 3f) * pathMeasure.getLength(), pos, null);
        triangle1.lineTo(pos[0], pos[1]);

        pathMeasure.getPosTan((2f / 3f) * pathMeasure.getLength(), pos, null);
        triangle1.lineTo(pos[0], pos[1]);
        triangle1.close();

        // 倒立
        triangle2 = new Path();
        Matrix matrix = new Matrix();
        matrix.postRotate(-180);
        triangle1.transform(matrix, triangle2);

        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                percent = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                switch (mCurrentState) {
                    case CIRCLE_STATE: // 圆 -> 三角形
                        Log.d("TAG", "circle done");
                        mCurrentState = State.TRIANGLE_LINE_STATE;
                        valueAnimator.start();
                        break;
                    case TRIANGLE_LINE_STATE: //  三角形 ->
                        Log.d("TAG", "triangle line done");
                        mCurrentState = State.TRIANGLE_STATE;
                        valueAnimator.start();
                        break;
                    case TRIANGLE_STATE: //  三角形 ->
                        Log.d("TAG", "triangle done");
                        mCurrentState = State.FINISH_STATE;
                        valueAnimator.start();
                        break;
                    case FINISH_STATE:
                        valueAnimator.cancel();
                        break;
                }
            }
        });
        valueAnimator.setDuration(3000);
//        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE); // loading
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorPrimary)); // bg
        canvas.save();
        canvas.translate(mViewWidth / 2, mViewHeight / 2); // 重新设置(0, 0)坐标

        dst.reset();
        dst.lineTo(0, 0);  // 圆心画点

        switch (mCurrentState) {
            case CIRCLE_STATE:
                // 内圆
                pathMeasure.setPath(innerCircle, false);
                pathMeasure.getSegment(0, pathMeasure.getLength() * percent, dst, true);

                // loading
//        float stop = length * percent;
//        float start = (float) (stop - ((0.5 - Math.abs(percent - 0.5)) * length));
//        pathMeasure.getSegment(start, stop, dst, true);

                canvas.drawPath(dst, paint);

                dst.reset();

                // 外圆
                pathMeasure.setPath(outerCircle, false);
                pathMeasure.getSegment(0, pathMeasure.getLength() * percent, dst, true);
                canvas.drawPath(dst, paint);
                break;
            case TRIANGLE_LINE_STATE:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);

                pathMeasure.setPath(triangle1, false);
                float stop = pathMeasure.getLength() * percent;
                float start = stop - (0.5f - Math.abs(0.5f - percent)) * 200;
                pathMeasure.getSegment(start, stop, dst, true);
                canvas.drawPath(dst, paint);

                dst.reset();

                pathMeasure.setPath(triangle2, false);
                pathMeasure.getSegment(start, stop, dst, true);
                canvas.drawPath(dst, paint);

                break;
            case TRIANGLE_STATE:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);

                pathMeasure.setPath(triangle1, false);
                pathMeasure.getSegment(0, pathMeasure.getLength() * percent, dst, true);
                canvas.drawPath(dst, paint);

                dst.reset();

                pathMeasure.setPath(triangle2, false);
                pathMeasure.getSegment(0, pathMeasure.getLength() * percent, dst, true);
                canvas.drawPath(dst, paint);
                break;
            case FINISH_STATE:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);

                canvas.drawPath(triangle1, paint);
                canvas.drawPath(triangle2, paint);
                break;
        }

        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }
}
