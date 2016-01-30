package lb.themike10452.game.Rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.lang.ref.WeakReference;

import lb.themike10452.game.R;
import lb.themike10452.game.Rendering.Classes.GunSight;
import lb.themike10452.game.Rendering.Classes.Duck;
import lb.themike10452.game.Rendering.Classes.Grass;
import lb.themike10452.game.Rendering.Classes.Stone;
import lb.themike10452.game.Rendering.Classes.Tree;

/**
 * Created by DELL on 1/27/2016.
 */
public class SurfaceRenderer implements IRenderer, View.OnTouchListener {
    private static final boolean DEBUG = true;
    private static final String TAG = "DEBUG";
    private static final String BG_COLOR = "#87ceeb";

    private RenderingThread mRenderingThread;
    private GunSight mCrossHair;
    private Duck mDuck1;
    private Duck mDuck2;
    private Grass mGrass;
    private Stone mStone;
    private Tree mTree;
    private GameResources mGameRes;
    private SurfaceView mSurfaceView;

    private Paint mPaint1;
    private Paint mPaint2;
    private int mTouchX;
    private int mTouchY;
    private boolean mTouchActive;
    private boolean mSurfaceReady;

    public SurfaceRenderer(SurfaceView surfaceView) {
        mSurfaceView = surfaceView;
        mGameRes = new GameResources(surfaceView.getContext());

        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setColor(Color.WHITE);
        mPaint1.setTextSize(20);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.BLUE);

        intiSceneElements(surfaceView.getContext());

        mSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mSurfaceView.getHolder().addCallback(this);
        mSurfaceView.setOnTouchListener(this);

        if (DEBUG) {
            Log.d(TAG, "SurfaceRenderer initialized");
        }
    }

    private void intiSceneElements(Context context) {
        WeakReference<Context> ctr = new WeakReference<>(context);
        mCrossHair = new GunSight(ctr);
        mGrass = new Grass(ctr);
        mStone = new Stone(ctr);
        mTree = new Tree(ctr);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int hpadding = dm.widthPixels / 15;

        mGrass.update(0, dm.heightPixels - mGrass.getBitmap().getHeight());
        mStone.update(dm.widthPixels - mStone.getBitmap().getWidth() - hpadding, dm.heightPixels - mStone.getBitmap().getHeight());
        mTree.update(hpadding, dm.heightPixels - mTree.getBitmap().getHeight());

        mDuck1 = new Duck(ctr, mGameRes);
        mDuck2 = new Duck(ctr, mGameRes);
    }

    public void release() {
        mCrossHair.dispose();
        mDuck1.dispose();
        mDuck2.dispose();
        mGameRes.dispose();
        mGrass.dispose();
        mStone.dispose();
        mTree.dispose();
    }

    @Override
    public void onUpdate() {
        mGameRes.getGameClock().update();
        mDuck1.update();
        mDuck2.update();
    }

    @Override
    public void onDraw(Canvas canvas) {
        //draw background elements
        mTree.draw(canvas);
        mStone.draw(canvas);

        //draw ducks
        mDuck1.draw(canvas);
        mDuck2.draw(canvas);

        //draw foreground elements
        mGrass.draw(canvas);

        if (mTouchActive) {
            mCrossHair.update(mTouchX, mTouchY);
            mCrossHair.draw(canvas);
        }

        if (DEBUG) {
            canvas.drawRect(50f, 30f, 200f, 30f + 25f, mPaint2);
            String debugText = mGameRes.getGameClock().getFrameTime() + " ms";
            canvas.drawText(debugText, 50, 50, mPaint1);
        }
    }

    public void start() {
        if (mRenderingThread != null && mRenderingThread.isRunning() && !mRenderingThread.isStopping()) {
            return;
        }

        mRenderingThread = new RenderingThread(mSurfaceView.getHolder());
        mRenderingThread.startThread();
    }

    private void onShoot(int x, int y) {
        if (mDuck1.hit(x, y)) {
            mDuck1.shoot();
            if (DEBUG) {
                Log.d(TAG, String.format("Duck1 shot %d %d", x, y));
            }
        }

        if (mDuck2.hit(x, y)) {
            mDuck2.shoot();
            if (DEBUG) {
                Log.d(TAG, String.format("Duck2 shot %d %d", x, y));
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
                    mGameRes.playSound(R.raw.shoot);
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

    private class RenderingThread extends Thread {
        private final SurfaceHolder mSurfaceHolder;
        private boolean mRunning;
        private boolean mStopping;

        public RenderingThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }

        public void startThread() {
            mRunning = true;
            mGameRes.getGameClock().start();
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
                            canvas.drawColor(Color.parseColor(BG_COLOR));
                            onUpdate();
                            onDraw(canvas);
                        }
                    }
                } catch (IllegalStateException e) {
                    if (DEBUG) {
                        e.printStackTrace();
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
