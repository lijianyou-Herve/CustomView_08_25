package com.example.herve.customview_08_25.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created           :Herve on 2016/8/27.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/8/27
 * @ projectName     :CustomView_08_25
 * @ version
 */
public class CostomOne extends View {


    private Path mPath;

    /*画笔*/
    private Paint mPaint;

    private float mSupChangeX = 1;
    private float mSupChangeY = 1;
    private String TAG = getClass().getSimpleName();
    private int mBallColor = Color.parseColor("#FF4081");

    public static final int DEFAULT_BALL_RADIUS = 13;
    public int BALL_RADIUS = DEFAULT_BALL_RADIUS;//小球半径
    private float freeBallDistance;
    private float freeBallTime;


    public CostomOne(Context context) {
        super(context);
        init(context);
    }

    public CostomOne(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public CostomOne(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CostomOne(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);//抗锯齿


    }

    private float startX;
    private float startY;
    private float mSupX;
    private float mSupY;
    private float endX;
    private float endY;

    private boolean isUp = false;
    private boolean startDraw = false;

    @Override
    protected void onDraw(Canvas canvas) {
        startX = getWidth() / 5;
        startY = getHeight() / 2;

        endX = (float) (getWidth() * 0.8);
        endY = getHeight() / 2;

        if (startDraw) {
            startDraw(canvas);
        }

        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        startDraw = true;
        postInvalidate();

        return super.onTouchEvent(event);
    }

    private void startDraw(Canvas canvas) {
        mSupChangeX = getWidth() / 2;

        Log.i(TAG, "onDraw:mSupChangeY= " + mSupChangeY);
        Log.i(TAG, "onDraw:getHeight()= " + getHeight());


        if (isUp) {/*上升动画*/
            mSupChangeY += 2;
            freeBallTime += 0.2;

            if (mSupChangeY > getHeight()) {
                isUp = false;
                mSupChangeY = getHeight();

            }
        } else {/*上升动画*/
            mSupChangeY -= 2;
            freeBallTime -= 0.2;
            if (mSupChangeY < 0) {
                isUp = true;
                mSupChangeY = 0;
            }
        }
        freeBallDistance = 40 * freeBallTime - 5 * freeBallTime * freeBallTime;

        Log.i(TAG, "startDraw: freeBallDistance=" + freeBallDistance);

        /*绘制贝塞尔线条的动画*/
        mPath.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
//        P0起点(x0,y0)
        mPath.moveTo(startX, startY);
//        辅助点P1(x1,y1)     重点P2(x2,y2)
        mPath.quadTo(mSupChangeX, mSupChangeY, endX, endY);

        canvas.drawPath(mPath, mPaint);



        /*绘制中心圆点*/
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBallColor);
        canvas.drawCircle(getWidth() / 2, mSupChangeY / 2 + getWidth() / 4 - 10 - freeBallDistance, BALL_RADIUS, mPaint);

        /*绘制左边圆点*/
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(startX, startY, BALL_RADIUS, mPaint);

        /*绘制右边圆点*/
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(endX, endY, BALL_RADIUS, mPaint);

        postInvalidate();
    }


}
