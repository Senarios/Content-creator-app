package com.senarios.coneqtlive.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class TouchBlackHoleView extends View {
    private boolean touchDisable=true;
    public TouchBlackHoleView(Context context) {
        super(context);
    }
    public TouchBlackHoleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchBlackHoleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return touchDisable;
    }

    public void disableTouch(boolean value){
        touchDisable = value;
    }
}


