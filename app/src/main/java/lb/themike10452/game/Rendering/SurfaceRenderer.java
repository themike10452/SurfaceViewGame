package lb.themike10452.game.Rendering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import lb.themike10452.game.R;
import lb.themike10452.game.Rendering.Classes.Bird;

/**
 * Created by DELL on 1/27/2016.
 */
public class SurfaceRenderer implements IRenderer, View.OnTouchListener {

    private static final boolean DEBUG = true;
    private static final String TAG = "DEBUG";

    private ContinuousRenderingThread mRenderingThread;
    private Bitmap mCrossHair;
    private Bitmap mGrass;
    private Bird mBird1;
    private GameClock mGameClock;
    private HashMap<Integer, Integer> mSoundMap;
    private SoundPool mSoundPool;
    private SurfaceView mSurfaceView;

    private Paint mPaint1;
    private Paint mPaint2;
    private int mTouchX;
    private int mTouchY;
    private boolean mTouchActive;
    private boolean mSurfaceReady;

    public SurfaceRenderer(SurfaceView surfaceView) {
        mSurfaceView = surfaceView;
        mGameClock = new GameClock();

        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setColor(Color.WHITE);
        mPaint1.setTextSize(20);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.BLUE);

        Context context = surfaceView.getContext();
        loadResources(context);
        loadSounds(context);

        mSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mSurfaceView.getHolder().addCallback(this);
        mSurfaceView.setOnTouchListener(this);

        if (DEBUG) {
            Log.d(TAG, "SurfaceRenderer initialized");
        }
    }

    private void loadResources(Context context) {
        Bitmap originalCrossHair = BitmapFactory.decodeResource(context.getResources(), R.drawable.crosshair);
        mCrossHair = Bitmap.createScaledBitmap(originalCrossHair, 150, 150, false);
        originalCrossHair.recycle();

        Bitmap originalGrass = BitmapFactory.decodeResource(context.getResources(), R.drawable.grass);
        int height = context.getResources().getDisplayMetrics().heightPixels / 4;
        float ratio = (float) height / originalGrass.getHeight();
        mGrass = Bitmap.createScaledBitmap(originalGrass, (int) (originalGrass.getWidth() * ratio), height, false);
        originalGrass.recycle();

        mBird1 = new Bird(new WeakReference<>(context.getApplicationContext()), mGameClock);
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

        mSoundMap.put(R.raw.shoot, mSoundPool.load(context, R.raw.shoot, 0));
    }

    @Override
    public void onUpdate() {
        mGameClock.update();
        mBird1.update();
    }

    @Override
    public void onDraw(Canvas canvas) {
        mBird1.draw(canvas);
        //canvas.drawBitmap(mBackground, 0, 0, null);
        canvas.drawBitmap(mGrass, 0, canvas.getHeight() - mGrass.getHeight(), null);

        if (mTouchActive) {
            canvas.drawBitmap(mCrossHair, mTouchX - mCrossHair.getWidth() / 2, mTouchY - mCrossHair.getHeight() / 2, null);
        }

        if (DEBUG) {
            canvas.drawRect(50f, 30f, 200f, 30f + 25f, mPaint2);
            String debugText = mGameClock.getFrameTime() + " ms";
            canvas.drawText(debugText, 50, 50, mPaint1);
        }
    }

    public void start() {
        if (mRenderingThread != null && mRenderingThread.isRunning() && !mRenderingThread.isStopping()) {
            return;
        }

        mRenderingThread = new ContinuousRenderingThread(mSurfaceView.getHolder());
        mRenderingThread.startThread();
    }

    private void onShoot(int x, int y) {
        if (mBird1.hit(x, y)) {
            mBird1.shoot();
            if (DEBUG) {
                Log.d(TAG, String.format("Bird1 shot %d %d", x, y));
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (DEBUG) {
            Log.d(TAG, "surfaceCreated");
        }

        mSurfaceReady = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, 1000);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (DEBUG) {
            Log.d(TAG, "surfaceDestroyed");
        }

        mSurfaceReady = false;

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
                    mSoundPool.play(mSoundMap.get(R.raw.shoot), 0.99f, 0.99f, 0, 0, 1);
                    onShoot((int) event.getRawX(), (int) event.getRawY());
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
        private boolean mStopping;

        public ContinuousRenderingThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }

        public void startThread() {
            mRunning = true;
            mGameClock.start();
            super.start();
        }

        public void stopThread() {
            mStopping = true;
        }

        public boolean isRunning() {
            return mRunning;
        }

        public boolean isStopping() {
            return mStopping;
        }

        @Override
        public void run() {
            if (DEBUG) {
                Log.d(TAG, "Rendering thread started");
            }

            Canvas canvas = null;
            while (mRunning && !mStopping) {
                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    if (canvas != null && mSurfaceReady) {
                        synchronized (mSurfaceHolder) {
                            canvas.drawColor(Color.parseColor("#87ceeb"));
                            //canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                            onUpdate();
                            onDraw(canvas);
                        }
                    }
                } finally {
                    if (canvas != null) {
                        try {
                            mSurfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (IllegalStateException e) {
                            if (DEBUG) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            mRunning = false;

            if (DEBUG) {
                Log.d(TAG, "Rendering thread stopped");
            }
        }
    }
}
