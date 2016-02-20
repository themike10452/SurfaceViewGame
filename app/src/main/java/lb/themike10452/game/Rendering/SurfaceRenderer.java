package lb.themike10452.game.Rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import java.lang.ref.WeakReference;

import lb.themike10452.game.R;
import lb.themike10452.game.Rendering.Animation.SpriteBatch;
import lb.themike10452.game.Rendering.Animation.SpriteSheet;
import lb.themike10452.game.Rendering.Classes.Grass;
import lb.themike10452.game.Rendering.Classes.GunSight;
import lb.themike10452.game.Rendering.Classes.Rock;
import lb.themike10452.game.Rendering.Classes.SceneController;
import lb.themike10452.game.Rendering.Classes.Tree;

/**
 * Copyright 2016 Michael Mouawad
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SurfaceRenderer implements IRenderer, View.OnTouchListener {
    private static final boolean DEBUG = true;
    private static final String TAG = "DEBUG";
    private static final String BG_COLOR = "#87ceeb";

    private RenderingThread mRenderingThread;
    private GunSight mCrossHair;
    private Grass mGrass;
    private Rock mRock;
    private Tree mTree;
    private GameResources mGameRes;
    private TextureView mTextureView;
    private SpriteBatch mSpriteBatch;
    private SpriteSheet mSpriteSheet;
    private SceneController mSceneController;

    private Paint mPaint1;
    private Paint mPaint2;

    private int mTouchX;
    private int mTouchY;
    private boolean mTouchActive;
    private boolean mSurfaceReady;

    public SurfaceRenderer(TextureView textureView) {
        Context context = textureView.getContext();
        mTextureView = textureView;
        mGameRes = new GameResources(context);

        mSpriteSheet = new SpriteSheet(context.getResources(), R.drawable.spritesheet, context.getAssets(), "sprites.json");
        mSpriteBatch = new SpriteBatch(mSpriteSheet);

        mSceneController = new SceneController(new WeakReference<>(context), mGameRes);

        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setColor(Color.WHITE);
        mPaint1.setTextSize(20);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.BLUE);

        intiSceneElements();

        mTextureView.setSurfaceTextureListener(this);
        mTextureView.setOnTouchListener(this);

        if (DEBUG) {
            Log.d(TAG, "SurfaceRenderer initialized");
        }
    }

    private void intiSceneElements() {
        mCrossHair = new GunSight(mSpriteSheet);
        mGrass = new Grass(mSpriteSheet);
        mRock = new Rock(mSpriteSheet);
        mTree = new Tree(mSpriteSheet);

        DisplayMetrics dm = mGameRes.getDisplayMetrics();
        int hpadding = dm.widthPixels / 15;

        int width = dm.widthPixels;
        int height = dm.heightPixels / 5;

        mGrass.update(0, dm.heightPixels - height, width, height);

        height = dm.heightPixels / 3;
        float ratio = (float) mRock.getHeight() / height;
        width = (int) (mRock.getWidth() / ratio);

        mRock.update(dm.widthPixels - width - hpadding, dm.heightPixels - height, width, height);

        height = (int) (dm.heightPixels * (7 / 8f));
        ratio = (float) mTree.getHeight() / height;
        width = (int) (mTree.getWidth() / ratio);

        mTree.update(hpadding, dm.heightPixels - height, width, height);

        width = 150;
        ratio = (float) mCrossHair.getWidth() / width;
        height = (int) (mCrossHair.getHeight() / ratio);

        mCrossHair.update(0, 0, width, height);
    }

    public void release() {
        mGameRes.dispose();
    }

    @Override
    public void onUpdate() {
        mGameRes.getGameClock().update();
        mSceneController.update();
    }

    @Override
    public void onDraw(Canvas canvas) {
        mSpriteBatch.begin();

        //draw background elements
        mSpriteBatch.add(mTree);
        mSpriteBatch.add(mRock);

        //Draw moving elements
        mSpriteBatch.add(mSceneController);

        //draw foreground elements
        mSpriteBatch.add(mGrass);

        if (mTouchActive) {
            mCrossHair.update(mTouchX, mTouchY);
            mSpriteBatch.add(mCrossHair);
        }

        mSpriteBatch.draw(canvas);

        if (DEBUG) {
            canvas.drawRect(50f, 30f, 200f, 30f + 25f, mPaint2);
            String debugText = mGameRes.getGameClock().getFrameTime() + " ms";
            canvas.drawText(debugText, 50, 50, mPaint1);
            canvas.drawLines(new float[]{mTouchX, 0, mTouchX, 1080}, mPaint1);
            canvas.drawLines(new float[]{0, mTouchY, 1920, mTouchY}, mPaint1);
        }
    }

    public void start() {
        if (mRenderingThread != null
                && mRenderingThread.isRunning()
                && !mRenderingThread.isStopping()) {
            return;
        }

        mRenderingThread = new RenderingThread(new Surface(mTextureView.getSurfaceTexture()));
        mRenderingThread.startThread();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (DEBUG) {
            Log.d(TAG, "Surface Ready");
        }

        mSurfaceReady = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //System.gc();
                start();
            }
        }, 1000);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        //
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (DEBUG) {
            Log.d(TAG, "Surface Destroyed");
        }

        mSurfaceReady = false;

        if (mRenderingThread != null) {
            mRenderingThread.stopThread();
        }

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mSceneController.onTouch(v, event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = (int) event.getRawX();
                mTouchY = (int) event.getRawY();
                mTouchActive = true;
                return true;
            case MotionEvent.ACTION_UP:
                mTouchActive = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                mTouchX = (int) event.getRawX();
                mTouchY = (int) event.getRawY();
                return true;
            default:
                return false;
        }
    }

    private class RenderingThread extends Thread {
        private final Surface mSurface;
        private boolean mRunning;
        private boolean mStopping;

        public RenderingThread(Surface surface) {
            mSurface = surface;
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
                    canvas = mSurface.lockCanvas(null);
                    if (canvas != null && mSurfaceReady) {
                        synchronized (mSurface) {
                            canvas.drawColor(Color.parseColor(BG_COLOR));
                            onUpdate();
                            onDraw(canvas);
                        }
                    }
                } catch (Exception e) {
                    if (DEBUG) {
                        e.printStackTrace();
                    }
                } finally {
                    if (canvas != null && mSurfaceReady) {
                        try {
                            mSurface.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
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
