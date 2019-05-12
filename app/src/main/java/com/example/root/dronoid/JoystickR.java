package com.example.root.dronoid;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;

public class JoystickR extends View {
    private float centerX;
    private float centerY;
    private float rayon;
    private float pointerBeginX;
    private float pointerBeginY;
    private float mX;
    private float mY;
    private int directionX;
    private int directionY;

    public JoystickR(Context context) {
        super(context);
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        this.centerX = dm.widthPixels * 3 / 4 + getStatusBarHeight();
        this.centerY = dm.heightPixels / 2;
        this.rayon = dm.heightPixels / 2 * 0.3f;
        this.mX = dm.widthPixels * 3 / 4 + getStatusBarHeight();
        this.mY = dm.heightPixels / 2;
    }

    private float getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getRayon() {
        return rayon;
    }

    public float getmX() {
        return mX;
    }

    public float getmY() {
        return mY;
    }

    public void setmX(float pPosX) {
        if (pPosX >= centerX + rayon)
            this.mX = centerX + rayon;
        else if (pPosX <= centerX - rayon)
            this.mX = centerX - rayon;
        else
            this.mX = pPosX;
    }

    public void setmY(float pPosY) {
        if (pPosY >= centerY + rayon)
            this.mY = centerY + rayon;
        else if (pPosY <= centerY - rayon)
            this.mY = centerY - rayon;
        else
            this.mY = pPosY;
    }

    public void setPointerBeginX(float pointerBeginX) {
        this.pointerBeginX = pointerBeginX;
    }

    public void setPointerBeginY(float pointerBeginY) {
        this.pointerBeginY = pointerBeginY;
    }

    public float getPointerBeginX() {
        return pointerBeginX;
    }

    public float getPointerBeginY() {
        return pointerBeginY;
    }

    public int getDirectionX() {
        return (int)((getmX() - getCenterX()) * 11 / getRayon());
    }

    public int getDirectionY() {
        return (int)(-(getmY() - getCenterY()) * 11 / getRayon());
    }
}
