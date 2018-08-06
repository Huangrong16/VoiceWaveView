package com.huangr.waveviewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.huangr.waveviewdemo.R;

/**
 * Created by huangr on 2018/8/6.
 * ClassName  : VoiceWaveView
 * Description  : 实现长按说话时的一个水波动效，水波的progress随着声音分贝大小变化
 */


public class VoiceWaveView extends View{

    private static final String TAG = "VoiceWaveView";

    /**
     * 波纹的个数
     */
    private int number;

    /**
     * 水波基数的高度
     */
    private int progress;
    /**
     * 水波波纹的高度
     */
    private float waveHeight;
    /**
     * 是否一直浮动，true代表一直动，false代表停止动
     */
    private boolean isFloating;
    /**
     * 波纹的颜色
     */
    private int waveColor;
    /**
     * 透明度
     */
    private int alpha;

    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;
    /**
     * 每隔波纹的宽度，总宽度的1/6
     */
    private int waveWidth;
    /**
     * 每次水平偏移量，视觉上反映了波动速度
     */
    private int offset = 30;
    /**
     * 水波刷新时间间隔
     */
    private int duration = 150;
    /**
     * 绘制的起点
     */
    private Point mStartPoint;

    public VoiceWaveView(Context context) {
        super(context, null);
    }

    public VoiceWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VoiceWaveView);
        number = typedArray.getInteger(R.styleable.VoiceWaveView_number, 2);
        progress = typedArray.getInteger(R.styleable.VoiceWaveView_progress, 50);
        waveHeight = typedArray.getDimension(R.styleable.VoiceWaveView_waveHeight, 40);
        isFloating = typedArray.getBoolean(R.styleable.VoiceWaveView_isFloating, true);
        waveColor = typedArray.getColor(R.styleable.VoiceWaveView_waveColor, context.getResources().getColor(R.color.colorPrimary));
        alpha = typedArray.getInteger(R.styleable.VoiceWaveView_waveAlpha, 50);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        waveWidth = mWidth/6;
        if(0 == waveWidth){
            waveWidth = 180;
        }
        mStartPoint = new Point(-waveWidth*4, mHeight / 2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        Paint wavePaint = new Paint();
        wavePaint.setColor(waveColor);
        wavePaint.setAlpha(alpha);
        wavePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //根据progress改变起点坐标的Y值,progress波动变化范围：由控件的一半高度到充满总高度
        //mStartPoint.y = (int) ((mHeight / 2 + mWidth / 4) - (progress / 100.0 * mWidth / 2));
        mStartPoint.y = (int) ((mHeight * 9 / 10 ) - (progress / 100.0 * mHeight / 4));
        //mStartPoint.y = mHeight / 2 ;

        //设置好起点
        Path[] paths = new Path[number];
        for (int i = 0; i < number; i++) {
            paths[i] = new Path();
            paths[i].moveTo(mStartPoint.x - i * mWidth/10, mStartPoint.y);
        }

        int j = 1;
        for (int i = 1; i <= 8; i++) {
            if (i % 2 == 0) {
                //波谷
                for (int k = 0; k < number; k++) {
                    paths[k].quadTo(mStartPoint.x - k * mWidth/10 + (waveWidth * j), mStartPoint.y + waveHeight ,
                            mStartPoint.x - k * mWidth/10 + (waveWidth * 2) * i, mStartPoint.y);
                }
            } else {
                //波峰
                for (int k = 0; k < number; k++) {
                    paths[k].quadTo(mStartPoint.x - k * mWidth/10 + (waveWidth * j), mStartPoint.y - waveHeight ,
                            mStartPoint.x - k * mWidth/10 + (waveWidth * 2) * i, mStartPoint.y);
                }
            }
            j += 2;
        }

        //绘制封闭的曲线
        for (int i = 0; i < number; i++) {
            paths[i].lineTo(mWidth, mHeight);//右下角
            paths[i].lineTo(mStartPoint.x, mHeight);//左下角
            paths[i].lineTo(mStartPoint.x, mStartPoint.y);//起点
            paths[i].close();
        }
        for (int i = 0; i < number; i++) {
            canvas.drawPath(paths[i], wavePaint);
        }

        //画出进度的text
        Paint textPaint = new Paint();
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(progress + "%", mWidth / 2, mHeight / 2, textPaint);

        //一个周期平移完了
        if (mStartPoint.x + offset >= 0) {
            //恢复默认起点继续平移
            mStartPoint.x = -waveWidth*4;
        }
        mStartPoint.x += offset;//每次偏移offset
        Log.d(TAG, "onDraw() called with: waveHeight = [" + waveHeight + "]");
        if(isFloating){
            postInvalidateDelayed(duration);
        }

        for (int i = 0; i < number; i++) {
            paths[i].reset();
        }
    }

    public void setIsFloating(boolean isFloating) {
        this.isFloating = isFloating;
    }

    public void setWaveHeight(int waveHeight) {
        this.waveHeight = waveHeight;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
