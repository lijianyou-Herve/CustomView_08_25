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
 * Created           :Herve on 2016/8/29.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/8/29
 * @ projectName     :CustomView_08_25
 * @ version
 */
public class SlingshotView extends View {


    private Path mPath;
    private Paint mPaint;

    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float supX;
    private float supY;
    private String TAG = getClass().getSimpleName();

    public SlingshotView(Context context) {
        super(context);
        init();
    }

    public SlingshotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlingshotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

        mPaint = new Paint();

        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPath = new Path();

    }


    private float scrlloBall = -200;
    private boolean showCcrlloBall = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        startX = (float) (getWidth() * 0.2);
        startY = getHeight() / 2;
        endX = (float) (getWidth() * 0.8);
        endY = getHeight() / 2;

        supX = getWidth() / 2;

        mPath.reset();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.moveTo(startX, startY);
        mPath.quadTo(supX, supY, endX, endY);

        canvas.drawPath(mPath, mPaint);

        Log.i(TAG, "onDraw:startAni=" + startAni);
        Log.i(TAG, "onDraw: getHeight=" + getHeight() / 2);


        if (drawBall) {
            ballValue -= 10;
            mPaint.setColor(Color.GREEN);
            mPaint.setStyle(Paint.Style.FILL);

            canvas.drawCircle(supX, ballValue / 2 + getHeight() / 4 - 20, 20, mPaint);

        }

        if (startAni) {
            if (isUpAni) {
                supY += 8;
                Log.i(TAG, "onDraw:小于0 supY=" + supY);
            } else {
                supY -= 8;
                Log.i(TAG, "onDraw:大于一半 supY=" + supY);
            }

        }

        Log.i(TAG, "onDraw: 绘制初始化小球");
        if (scrlloBall < supX) {
            scrlloBall += 2;
            mPaint.setColor(Color.GREEN);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(scrlloBall, getHeight() / 2-20, 20, mPaint);
        }else {
            if(!drawBall){
                Log.i(TAG, "onDraw: 绘制初始化小球");
                mPaint.setColor(Color.GREEN);
                mPaint.setStyle(Paint.Style.FILL);

                canvas.drawCircle(supX, supY / 2 + getHeight() / 4 - 20, 20, mPaint);
            }


        }

        if (startAni | drawBall|(scrlloBall < supX)) {
            Log.i(TAG, "onDraw: 冲洗绘制");
            if (supY < 0) {

                isUpAni = true;
            } else if (supY < getHeight() / 2) {
                supY = getHeight() / 2;
                isUpAni = false;
                startAni = false;
                scrlloBall=-200;

            }
            if (ballValue < -1000) {
                drawBall = false;
            }
            postInvalidate();


        }




    }

    private boolean startAni = false;
    private boolean isUpAni = false;


    private float height = 0;

    private boolean drawBall = false;
    private float ballValue = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                supY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                supY = event.getY();
                break;
            case MotionEvent.ACTION_UP:

                if (supY > getHeight() / 2) {
                    isUpAni = false;
                    height = getHeight() / 2 - supY;

                } else {
                    height = supY - getHeight() / 2;

                    isUpAni = true;

                }
                startAni = true;
                ballValue = supY;
                if (ballValue < 0) {
                    ballValue = 0;
                }
                if (ballValue > getHeight()) {
                    ballValue = getHeight();
                }
                drawBall = true;

                break;
            case MotionEvent.ACTION_CANCEL:
                startAni = false;


                break;
        }


        invalidate();

        return true;
    }
}
