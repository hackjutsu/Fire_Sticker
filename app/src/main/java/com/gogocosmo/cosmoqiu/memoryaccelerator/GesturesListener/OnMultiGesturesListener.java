package com.gogocosmo.cosmoqiu.memoryaccelerator.GesturesListener;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class OnMultiGesturesListener implements View.OnTouchListener {

    final private String TAG = "MEMORY-ACC";

    private final GestureDetector _gestureDetector;

    public OnMultiGesturesListener(Context context) {
        _gestureDetector = new GestureDetector(context, new GestureListener());
    }

    abstract public void onSwipeLeft();

    abstract public void onSwipeRight();

    abstract public void onSwipeUp();

    abstract public void onSwipeDown();

    abstract public void onDoubleTapped();

    public void onTouchDown() {

    };

    public void onTouchUp() {

    };


    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown();
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp();
                break;
            default:
        }
        return _gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 50;
        private static final int SWIPE_VELOCITY_THRESHOLD = 50;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight();
                else
                    onSwipeLeft();
                return true;
            } else if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceY > 0)
                    onSwipeDown();
                else
                    onSwipeUp();
                return true;
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            float x = e.getX();
            float y = e.getY();

            onDoubleTapped();
            Log.d(TAG, "Tapped at: (" + x + "," + y + ")");

            return true;
        }
    }
}