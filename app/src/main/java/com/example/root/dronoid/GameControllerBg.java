package com.example.root.dronoid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class GameControllerBg extends View {
    private float r;
    private float r1;
    private JoystickL j1;
    private JoystickR j2;
    private RectF rect1;
    private RectF rect2;
    private Paint mFillPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mFillPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float count = 0;
    private int stroke = 20;
    private int strokeR = stroke / 2;
    Timer t;
    private static final int PERIOD = 500;

    public GameControllerBg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.j1 = new JoystickL(context);
        this.j2 = new JoystickR(context);
        this.r = j1.getCenterX() * 0.4f;
        this.r1 = j1.getCenterX() * 0.7f;
        init();
    }

    private void init() {
        mFillPaint1.setStyle(Paint.Style.FILL);
        mFillPaint1.setColor(0xffffffff);
        mFillPaint2.setStyle(Paint.Style.FILL);
        mFillPaint2.setColor(0xffa592f1);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(0xffffaa00);
        mStrokePaint.setStrokeWidth(stroke);
        rect1 = new RectF(j1.getCenterX() - r1, j1.getCenterY() - r1, j1.getCenterX() + r1, j1.getCenterY() + r1);
        rect2 = new RectF(j2.getCenterX() - r1, j2.getCenterY() - r1, j2.getCenterX() + r1, j2.getCenterY() + r1);

        t=new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                    if (count == 360)
                        t.cancel();
                count++;
            }
        }, 1000, 2);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 0, 0,0);
        canvas.drawArc(rect1, 90, count, false, mStrokePaint);
        canvas.drawArc(rect2, 0, count, false, mStrokePaint);
        canvas.drawLine(j1.getCenterX() - r, j1.getCenterY() - r, j1.getCenterX() - r, j1.getCenterY() - r * (1 - (count * 2 / 360)), mStrokePaint);
        canvas.drawLine(j1.getCenterX() - r, j1.getCenterY() - r, j1.getCenterX() - r * (1 - (count * 2 / 360)), j1.getCenterY() - r, mStrokePaint);
        canvas.drawLine(j1.getCenterX() + r, j1.getCenterY() + r, j1.getCenterX() + r, j1.getCenterY() + r * (1 - (count * 2 / 360)), mStrokePaint);
        canvas.drawLine(j1.getCenterX() + r, j1.getCenterY() + r, j1.getCenterX() + r * (1 - (count * 2 / 360)), j1.getCenterY() + r, mStrokePaint);
        canvas.drawLine(j2.getCenterX() + r, j2.getCenterY() - r, j2.getCenterX() + r, j2.getCenterY() - r * (1 - (count * 2 / 360)), mStrokePaint);
        canvas.drawLine(j2.getCenterX() + r, j2.getCenterY() - r, j2.getCenterX() + r * (1 - (count * 2 / 360)), j2.getCenterY() - r, mStrokePaint);
        canvas.drawLine(j2.getCenterX() - r, j2.getCenterY() + r, j2.getCenterX() - r, j2.getCenterY() + r * (1 - (count * 2 / 360)), mStrokePaint);
        canvas.drawLine(j2.getCenterX() - r, j2.getCenterY() + r, j2.getCenterX() - r * (1 - (count * 2 / 360)), j2.getCenterY() + r, mStrokePaint);

        invalidate();
    }

    public void onResume() {
        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }
}
