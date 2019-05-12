package com.example.root.dronoid;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameController extends View {
    private JoystickL j1;
    private JoystickR j2;
    private Paint mFillpaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mStrokepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean count;

    public GameController(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.j1 = new JoystickL(context);
        this.j2 = new JoystickR(context);
        init();
    }

    private void init() {
        mFillpaint1.setStyle(Paint.Style.FILL);
        mFillpaint1.setColor(0xffffffff);
        mStrokepaint.setStyle(Paint.Style.STROKE);
        mStrokepaint.setColor(0xffdddddd);
        mStrokepaint.setStrokeWidth(40);
    }

    public void onDraw(Canvas canvas)
    {
        canvas.drawCircle(j1.getmX(), j1.getmY(), j1.getRayon(), mFillpaint1);
        canvas.drawCircle(j2.getmX(), j2.getmY(), j2.getRayon(), mFillpaint1);
        canvas.drawCircle(j1.getmX(), j1.getmY(), j1.getRayon(), mStrokepaint);
        canvas.drawCircle(j2.getmX(), j2.getmY(), j2.getRayon(), mStrokepaint);
        //Log.i("info", ""+ j2.getDirectionY());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean onTouchEvent(MotionEvent event)
    {
        int pointerIndexLeft;
        int pointerIndexRight;
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        if (event.getX() < j1.getCenterX() * 2) // Initializing pointers to recognize which finger is touching the screen
        {
            pointerIndexLeft = 0;
            pointerIndexRight = 1;
        }
        else
        {
            pointerIndexRight = 0;
            pointerIndexLeft = 1;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > j1.getCenterX() * 2) {
                    j2.setPointerBeginX(event.getX() - j2.getCenterX());
                    j2.setPointerBeginY(event.getY() - j2.getCenterY());
                }
                else
                    j1.setPointerBeginY(event.getY() - j1.getCenterY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (pointerIndexRight == 1 && event.getPointerId(pointerId) == 1) {
                    Log.i("caca", ""+event.getPointerId(pointerId));
                    j2.setPointerBeginX(event.getX(pointerIndexRight) - j2.getCenterX());
                    j2.setPointerBeginY(event.getY(pointerIndexRight) - j2.getCenterY());
                }
                else
                    j1.setPointerBeginY(event.getY(pointerIndexLeft) - j1.getCenterY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) { // If only one finger is touching the screen
                    pointerIndexLeft = 0;
                    pointerIndexRight = 0;
                }
                if (event.getX(pointerIndexLeft) < j1.getCenterX() * 2 ) {
                    j1.setmY(event.getY(pointerIndexLeft) - j1.getPointerBeginY());
                }
                if (event.getX(pointerIndexRight) > j1.getCenterX() * 2) {
                    j2.setmX(event.getX(pointerIndexRight) - j2.getPointerBeginX());
                    j2.setmY(event.getY(pointerIndexRight) - j2.getPointerBeginY());
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (pointerId == pointerIndexLeft) {
                    j1.setmY(j1.getCenterY());
                    j1.setPointerBeginY(0);
                }
                else if (pointerId == pointerIndexRight) {
                    j2.setmX(j2.getCenterX());
                    j2.setmY(j2.getCenterY());
                    j2.setPointerBeginX(0);
                    j2.setPointerBeginY(0);
                }

                break;
            case MotionEvent.ACTION_UP:
                j1.setmY(j1.getCenterY());
                j2.setmX(j2.getCenterX());
                j2.setmY(j2.getCenterY());
                j2.setPointerBeginX(0);
                j2.setPointerBeginY(0);
                j1.setPointerBeginY(0);
                break;
        }
        if (MainActivity.mBluetoothCaracteristic != null)
        {
            MainActivity.mBluetoothCaracteristic.setValue(convert(j1.getDirectionZ()) + convert(j2.getDirectionX() + 40) + convert(j2.getDirectionY() + 80));
            MainActivity.mBluetoothGatt.writeCharacteristic(MainActivity.mBluetoothCaracteristic);
        }
        count = false;
        invalidate();
        return true;
    }

    public String convert(int value) {
        return Character.toString((char)(value + 10));
    }

    public void onResume() {
        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }
}
