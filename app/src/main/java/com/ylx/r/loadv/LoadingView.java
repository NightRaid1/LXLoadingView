package com.ylx.r.loadv;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by yanglixin on 2017/3/31.
 */
public class LoadingView extends View {
    //控件大小 Math.min(w,h) 取正方形
    private int size;
    //四个点之间的间隔
    private int interval = 5;
    //颜色
    private static final int[] COLORS = new int[]{0xFFEF800b, 0xFFFAEA0b, 0xFFFF0000, 0xFFAAEAFF};
    //画笔
    private Paint mPaint;
    //当前正在画的点
    private int currentIndex = 0;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.size = Math.min(w, h);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //半径
        canvas.rotate(-90, size / 2, size / 2);
        //每个圆所在正方形的长度
        int length = (size / 2 - interval);
        //圆的半径
        int radius = length / 2;

        for (int i = 0; i < 4; i++) {
            mPaint.setColor(COLORS[i]);
            //控制半径的变量 （0f - 1f）
            float tempValue = animValue;
            if (currentIndex < 4) {
                //当currentIndex 大于 当前圆的下标时证明这个下标所代表的圆已经被绘制，所以直接画出来
                if (currentIndex > i) {
                    tempValue = 1;
                }
                //当currentIndex 小于 当前圆的下标时证明这个下标所代表的圆已经被绘制，隐藏掉
                if (currentIndex < i) {
                    tempValue = 0;
                }
                //当前下标所代表的圆 动态变化
                if (currentIndex == i) {
                    tempValue = animValue;
                }
                //PS上面的判断是我按照我自己的思路来进行的渐显（一个圆一个圆的显示），
                // 你如果有更好的思路，请告诉我
                //共同进步✧(≖ ◡ ≖✿)
                //加油~

            } else {
                //当currentIndex == 5 的时候 走一个旋转动画
                canvas.rotate(90 * tempValue, size / 2, size / 2);
                tempValue = Math.abs(0.7f - tempValue);
            }
            //顺时针旋转90° 画出每一个圆
            canvas.rotate(90, size / 2, size / 2);
            canvas.drawCircle(radius, radius, radius * tempValue, mPaint);


        }


    }

    private float animValue = 0;

    public void startAnim() {
        //半径 * （0f-1f）从而实现从小到大
        ValueAnimator v = ValueAnimator.ofFloat(0, 1f);
        v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        v.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                currentIndex++;
                //current<=4的时候是画4个圆 等于5的时候是圆缩小的动画 onDraw里面有判断
                if (currentIndex == 5) {
                    currentIndex = 0;
                }

            }
        });
        v.setDuration(400);
        v.setRepeatCount(ValueAnimator.INFINITE);
        v.setInterpolator(new AccelerateDecelerateInterpolator());
        v.start();


    }


}

