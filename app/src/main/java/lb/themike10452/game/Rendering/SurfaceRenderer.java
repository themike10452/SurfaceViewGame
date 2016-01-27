package lb.themike10452.game.Rendering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.HashMap;

import lb.themike10452.game.R;

/**
 * Created by DELL on 1/27/2016.
 */
public class SurfaceRenderer implements IRenderer, View.OnTouchListener {

    private static final boolean DEBUG = true;
    private static final String TAG = "DEBUG";

    public static void adopt(SurfaceView surfaceView) {
        SurfaceRenderer sr = new SurfaceRenderer(surfaceView.getContext());
        surfaceView.getHolder().addCallback(sr);
        surfaceView.setOnTouchListener(sr);
    }

    private ContinuousRenderingThread mRenderingThread;
    private Bitmap mCrossHair;
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundMap;

    private Paint mPaint1;
    private Paint mPaint2;
    private int mTouchX;
    private int mTouchY;
    private boolean mTouchActive;

    private SurfaceRenderer(Context context) {
        mCrossHair = BitmapFactory.decodeResource(context.getResources(), R.drawable.crosshair);
        mCrossHair = Bitmap.createScaledBitmap(mCrossHair, 150, 150, false);

        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setColor(Color.WHITE);
        mPaint1.setTextSize(20);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.BLUE);

        loadSounds(context);
    }

    private void loadSounds(Context context) {
        mSoundMap = new HashMap<>(1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build())
                    .build();
        } else {
            mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        }

        mSoundMap.put(R.raw.gunshot, mSoundPool.load(context, R.raw.gunshot, 0));
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (DEBUG) {
            canvas.drawRect(50f, 30f, 200f, 30f + 25f, mPaint2);
            String debugText = String.format("X:%d Y:%d", mTouchX, mTouchY);
            canvas.drawText(debugText, 50, 50, mPaint1);
        }

        if (mTouchActive) {
            canvas.drawBitmap(mCrossHair, mTouchX - mCrossHair.getWidth() / 2, mTouchY - mCrossHair.getHeight() / 2, null);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mRenderingThread = new ContinuousRenderingThread(holder);
        mRenderingThread.startThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mRenderingThread != null) {
            mRenderingThread.stopThread();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = (int) event.getRawX();
                mTouchY = (int) event.getRawY();
                mTouchActive = true;
                if (DEBUG) {
                    Log.d(TAG, "ACTION_DOWN");
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                mTouchX = (int) event.getRawX();
                mTouchY = (int) event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
                mTouchActive = false;
                if (DEBUG) {
                    Log.d(TAG, "ACTION_UP");
                }
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                if (pointerIndex == 1) {
                    mSoundPool.play(mSoundMap.get(R.raw.gunshot), 0.99f, 0.99f, 0, 0, 1);
                }

                if (DEBUG) {
                    Log.d(TAG, "ACTION_POINTER_DOWN " + pointerIndex);
                }
                return false;
            default:
                return false;
        }
    }

    private class ContinuousRenderingThread extends Thread {
        private final SurfaceHolder mSurfaceHolder;
        private boolean mRunning;

        public ContinuousRenderingThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }

        public void startThread() {
            mRunning = true;
            super.start();
        }

        public void stopThread() {
            mRunning = false;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            while (mRunning) {
                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    if (canvas != null) {
                        synchronized (mSurfaceHolder) {
                            canvas.drawColor(Color.parseColor("#87ceeb"));
                            onDraw(canvas);
                        }
                    }
                    sleep(0);
                } catch (InterruptedException e) {
                    if (DEBUG) {
                        Log.d(TAG, "Rendering thread interrupted");
                    }
                } finally {
                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
