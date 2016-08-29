package com.example.herve.customview_08_25.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.herve.customview_08_25.R;

/**
 * Created           :Herve on 2016/8/25.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/8/25
 * @ projectName     :CustomView_08_25
 * @ version
 */
public class CircleProgressView extends View {
    private Paint mPaint;
    private RectF mRectF;
    //颜色以及宽度
    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    //进度
    private int mProgress;
    //是否到下一圈
    private boolean mChanged;


    //空间的宽度 以及 高度(两个值设为一样)
    private int mWidth;
    private boolean canDraw;

    public CircleProgressView(Context context) {
        this(context, null);
        init();
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0);

        //获取自定义属性和默认值
        mFirstColor = ta.getColor(R.styleable.CircleProgressView_firstColor, Color.RED);
        mSecondColor = ta.getColor(R.styleable.CircleProgressView_secondColor, Color.BLUE);
        mCircleWidth = ta.getDimensionPixelSize(R.styleable.CircleProgressView_circleWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
        ta.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);//绘图为描边模式
        mPaint.setStrokeWidth(mCircleWidth);//画笔宽度
        System.out.println("width" + mCircleWidth + "");
        mPaint.setAntiAlias(true);//抗锯齿
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = Math.min(widthSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 160, getResources().getDisplayMetrics()));
            setMeasuredDimension(mWidth, mWidth);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                canDraw = !canDraw;


                break;
            case MotionEvent.ACTION_UP:

                canDraw = !canDraw;

                break;
            case MotionEvent.ACTION_CANCEL:
                canDraw = false;

                break;

        }
        invalidate();
        return super.onTouchEvent(event);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //得到画布一半的宽度
        int center = getWidth() / 2;
        //定义圆的半径
        int radius = 60 ;
        //定义一个圆
        mRectF = new RectF(center - radius, center - radius, center + radius, center + radius);
//        mRectF = new RectF(0,0,0,0);
        if (!mChanged) {
            //设置画笔的颜色
            mPaint.setColor(mFirstColor);
            //画一个圆，由于画笔是描边模式，所以展现的是个圆环
            canvas.drawCircle(center, center, radius, mPaint);
            //设置画笔的颜色
            mPaint.setColor(mSecondColor);
            //绘制圆弧，从12点方向（-90度）开始绘制，偏移角度为进度
            canvas.drawArc(mRectF, -90, mProgress, false, mPaint);
        } else {
            mPaint.setColor(mSecondColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mFirstColor);
            canvas.drawArc(mRectF, -90, mProgress, false, mPaint);
        }

        //进度更新方法
        if (canDraw) {

            startProgress();
        }
    }

    private void startProgress() {
        //当view控件可见时，每50毫秒更新一次视图
        if (isShown()) {
            postDelayed(new Runnable() {
                @Override
                public void run() {

                    mProgress += 10;
                    //如果偏移角度超过360，则至为0，并且跟换绘制颜色
                    if (mProgress >= 360) {
                        mProgress = 0;
                        mChanged = !mChanged;
                    }
                    invalidate();
                }
            }, 50);
        }
    }
}
