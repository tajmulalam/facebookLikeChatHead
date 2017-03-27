package com.example.chatheads;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ChatHeadService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;
    private final IBinder binder = new LocalBinder();

    long clickTimeInterval = 200;
    long startTime = 500;


    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.face1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        chatHead.setLayoutParams(layoutParams);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        //this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startTime = java.util.Calendar.getInstance().getTimeInMillis();
                        return true;
                    case MotionEvent.ACTION_UP:
                        startTime = java.util.Calendar.getInstance().getTimeInMillis() - startTime;
                        if (startTime < clickTimeInterval) {
                            if (onClickCustomListner != null) {
                                onClickCustomListner.viewClicked();
                            }

                        }
                        startTime = 0;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);

                        return true;
                }
                return false;
            }
        });
        windowManager.addView(chatHead, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null)
            windowManager.removeView(chatHead);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return binder;
    }

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        ChatHeadService getService() {
            // Return this instance of MyService so clients can call public methods
            return ChatHeadService.this;
        }
    }

    private onClickCustomListner onClickCustomListner;

    public void setListner(onClickCustomListner listner) {
        this.onClickCustomListner = listner;
    }

    public interface onClickCustomListner {

        public void viewClicked();
    }
}