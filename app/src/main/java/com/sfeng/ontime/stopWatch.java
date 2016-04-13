package com.sfeng.ontime;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirby on 2016-01-19.
 */
public class stopWatch extends Service {
    //private Button start, stop, reset;
    private WindowManager windowManager;
    private View watch = null;
    private LayoutInflater inflater;
    private WindowManager.LayoutParams params;

    //
    private Button tempBtn; //Temporary Button
    private Handler mHandler = new Handler();
    private long startTime;
    private long elapsedTime;
    private final int REFRESH_RATE = 100;
    private String hours,minutes,seconds,milliseconds;
    private long secs,mins,hrs,msecs;
    private boolean stopped = false;
    private Runnable startTimer;
    //
    final private int WIDTH = 720;
    final private int HEIGHT = 120;

    private boolean active = false;

    private record records;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (watch == null) {

            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            inflater = LayoutInflater.from(this);

            startTimer = new Runnable() {
                @Override
                public void run() {
                    elapsedTime = System.currentTimeMillis() - startTime;
                    updateTimer(elapsedTime);
                    mHandler.postDelayed(this, REFRESH_RATE);
                }
            };
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (watch == null) {
            if (intent != null) {

                watch = inflater.inflate(R.layout.stop_watch, null);

                //TextView txt_title = (TextView) chatHead.findViewById(R.id.txt_title);
                //TextView txt_text = (TextView) chatHead.findViewById(R.id.txt_text);

                //txt_title.setText(intent.getStringExtra("title"));
                //txt_text.setText(intent.getStringExtra("text"));

                watch.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        windowManager.removeView(watch);
                        watch = null;
                    }
                });

                final WindowManager.LayoutParams params =
                        new WindowManager.LayoutParams(
                                WIDTH,
                                HEIGHT,
                                WindowManager.LayoutParams.TYPE_PHONE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                PixelFormat.TRANSLUCENT);

                params.gravity = Gravity.TOP | Gravity.LEFT;
                params.x = 0;
                params.y = 100;

                watch.findViewById(R.id.time).setOnTouchListener(new View.OnTouchListener() {
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
                                return true;
                            case MotionEvent.ACTION_UP:
                                return true;
                            case MotionEvent.ACTION_MOVE:
                                params.x = initialX + (int) (event.getRawX() - initialTouchX);
                                params.y = initialY + (int) (event.getRawY() - initialTouchY);
                                windowManager.updateViewLayout(watch, params);
                                return true;
                        }
                        return false;
                    }
                });

                watch.findViewById(R.id.timems).setOnTouchListener(new View.OnTouchListener() {
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
                                return true;
                            case MotionEvent.ACTION_UP:
                                return true;
                            case MotionEvent.ACTION_MOVE:
                                params.x = initialX + (int) (event.getRawX() - initialTouchX);
                                params.y = initialY + (int) (event.getRawY() - initialTouchY);
                                windowManager.updateViewLayout(watch, params);
                                return true;
                        }
                        return false;
                    }
                });

                windowManager.addView(watch, params);
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (watch != null) {
            windowManager.removeView(watch);
        }
    }

    public void start(View view) {
        if (!active) {
            if (stopped) {
                startTime = System.currentTimeMillis() - elapsedTime;
            } else {
                startTime = System.currentTimeMillis();
            }
            mHandler.removeCallbacks(startTimer);
            mHandler.postDelayed(startTimer, 0);
            active = true;
        } else {
            mHandler.removeCallbacks(startTimer);
            stopped = true;
            active = false;
        }
    }

    public void stop(View view) {
        mHandler.removeCallbacks(startTimer);
        stopped = true;
    }

    public void reset(View view) {
        if (!active) {
            stopped = false;
            ((TextView) watch.findViewById(R.id.time)).setText("00:00:00");
            ((TextView) watch.findViewById(R.id.timems)).setText(".0");
        }
    }

    public void save(View view) {
        records.add((int) startTime);
    }

    private void updateTimer (float time) {
        secs = (long) (time / 1000);
        mins = (long) ((time / 1000) / 60);
        hrs = (long) (((time / 1000) / 60) / 60);

		/* Convert the seconds to String
		 * and format to ensure it has
		 * a leading zero when required
		 */
        secs = secs % 60;
        seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        }

		/* Convert the minutes to String and format the String */

        mins = mins % 60;
        minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        }

    	/* Convert the hours to String and format the String */

        hours = String.valueOf(hrs);
        if (hrs == 0) {
            hours = "00";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours;
        }

    	/* Although we are not using milliseconds on the timer in this example
    	 * I included the code in the event that you wanted to include it on your own
    	 */
        milliseconds = String.valueOf((long) time);
        if (milliseconds.length() == 2) {
            milliseconds = "0" + milliseconds;
        }
        if ( milliseconds.length() <=1){
            milliseconds = "00" + milliseconds;
        }
        milliseconds = milliseconds.substring(milliseconds.length()-3, milliseconds.length()-2);

		/* Setting the timer text to the elapsed time */
        ((TextView) watch.findViewById(R.id.time)).setText(hours + ":" + minutes + ":" + seconds);
        ((TextView) watch.findViewById(R.id.timems)).setText("." + milliseconds);
    }



}
