package lb.themike10452.game.Rendering.Classes;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import java.lang.ref.WeakReference;
import java.util.Random;

import lb.themike10452.game.R;
import lb.themike10452.game.Rendering.Constants;
import lb.themike10452.game.Rendering.GameResources;

/**
 * Created by DELL on 1/28/2016.
 */
public class Duck extends InteractiveBase {
    private static final int ANIMATION_RATE = 16;
    private static final int QUACK_RATE = 1;
    private static final int DELAY_BEFORE_FALL = 500;
    private static final int RESET_TIME = 3000;
    private static final int DEFAULT_MOVEMENT_RATE = 700;

    private static int MovementRate;

    private Bitmap mBitmapFlyAnim1;
    private Bitmap mBitmapFlyAnim2;
    private Bitmap mBitmapShot;
    private Bitmap mBitmapShotAnim1;
    private Bitmap mBitmapShotAnim2;
    private Bitmap mBitmapShotAnim3;
    private Bitmap mVisibleBitmap;
    private GameResources mGameRes;
    private Random mRandom;
    private Rect mCanvasRect;

    private int mAnimationPhase;
    private int mMovementDirection;
    private boolean mShot;
    private boolean mFalling;

    private long _lastAnimationTime;
    private long _lastQuackTime;
    private long _shotTime;

    public Duck(WeakReference<Context> ctr, GameResources gameRes) {
        super(BitmapFactory.decodeResource(ctr.get().getResources(), R.drawable.duck_1));
        Resources resources = ctr.get().getResources();
        mBitmapFlyAnim1 = BitmapFactory.decodeResource(resources, R.drawable.duck_2);
        mBitmapFlyAnim2 = BitmapFactory.decodeResource(resources, R.drawable.duck_3);
        mBitmapShot = BitmapFactory.decodeResource(resources, R.drawable.duck_shot);
        mBitmapShotAnim1 = BitmapFactory.decodeResource(resources, R.drawable.duck_shot1);
        mBitmapShotAnim2 = BitmapFactory.decodeResource(resources, R.drawable.duck_shot2);
        mBitmapShotAnim3 = BitmapFactory.decodeResource(resources, R.drawable.duck_shot3);
        mGameRes = gameRes;
        mRandom = new Random();

        DisplayMetrics dm = ctr.get().getResources().getDisplayMetrics();
        mCanvasRect = new Rect(0, 0, dm.widthPixels, dm.heightPixels);

        reset();

        MovementRate = 700;
        mVisibleBitmap = mBitmap;
    }

    public void update() {
        long gameTime = mGameRes.getGameClock().getGameTime();

        mFalling = mShot && gameTime >= _shotTime + DELAY_BEFORE_FALL;

        if (mFalling && gameTime >= _shotTime + DELAY_BEFORE_FALL + RESET_TIME) {
            reset();
            MovementRate += 50;
            return;
        }

        //TODO
        if (!mCanvasRect.contains(mLeft, mTop) && !mShot) {
            shoot();
            MovementRate -= 50;
        }

        if (!mShot) {
            if (gameTime >= _lastQuackTime + 1000 / QUACK_RATE) {
                //quack
                mGameRes.playSound(R.raw.quack);
                _lastQuackTime = gameTime;
            }
        }

        int dx = 0;
        int dy = 0;

        if (mShot) {
            dy += mFalling ? mGameRes.getGameClock().getFrameTime() * DEFAULT_MOVEMENT_RATE / 1000 : 0;
        } else {
            int step = (int) (mGameRes.getGameClock().getFrameTime() * MovementRate / 1000);

            if (isFlyingEast()) {
                dx += step;
            }

            if (isFlyingWest()) {
                dx -= step;
            }

            if (isFlyingNorth()) {
                dy -= step;
            }

            if (isFlyingSouth()) {
                dy += step;
            }
        }

        move(dx, dy);
    }

    @Override
    public void update(int left, int top) {
        super.update(left, top);
        updateBitmap();
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
        updateBitmap();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(mVisibleBitmap, mLeft, mTop, null);
    }

    public void shoot() {
        if (!mShot) {
            mShot = true;
            _shotTime = mGameRes.getGameClock().getGameTime();
        }
    }

    public void reset() {
        mShot = false;
        mFalling = false;
        mVisibleBitmap = mBitmap;
        mAnimationPhase = 0;
        mMovementDirection = Constants.DIRECTION_EAST;
        _lastAnimationTime = 0;
        _shotTime = 0;

        int height = mCanvasRect.bottom;
        int ran = Math.abs(mRandom.nextInt(height) - height / 5);

        update(0, ran);
    }

    protected void updateBitmap() {
        if (mGameRes.getGameClock().getGameTime() >= _lastAnimationTime + (1000 / ANIMATION_RATE)) {
            _lastAnimationTime = mGameRes.getGameClock().getGameTime();
            mAnimationPhase = (mAnimationPhase + 1) % 3;

            if (mShot && !mFalling) {
                mVisibleBitmap = mBitmapShot;
            } else {
                switch (mAnimationPhase) {
                    case 0:
                        mVisibleBitmap = mFalling ? mBitmapShotAnim1 : mBitmap;
                        break;
                    case 1:
                        mVisibleBitmap = mFalling ? mBitmapShotAnim2 : mBitmapFlyAnim1;
                        break;
                    case 2:
                        mVisibleBitmap = mFalling ? mBitmapShotAnim3 : mBitmapFlyAnim2;
                        if (!mFalling)
                            mGameRes.playSound(R.raw.flap);
                        break;
                }
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (!mBitmapFlyAnim1.isRecycled()) mBitmapFlyAnim1.recycle();
        if (!mBitmapFlyAnim2.isRecycled()) mBitmapFlyAnim2.recycle();
        if (!mBitmapShot.isRecycled()) mBitmapShot.recycle();
        if (!mBitmapShotAnim1.isRecycled()) mBitmapShotAnim1.recycle();
        if (!mBitmapShotAnim2.isRecycled()) mBitmapShotAnim2.recycle();
        if (!mBitmapShotAnim3.isRecycled()) mBitmapShotAnim3.recycle();
    }

    public boolean isFlyingNorth() {
        return (mMovementDirection & Constants.DIRECTION_NORTH) == Constants.DIRECTION_NORTH;
    }

    public boolean isFlyingSouth() {
        return (mMovementDirection & Constants.DIRECTION_SOUTH) == Constants.DIRECTION_SOUTH;
    }

    public boolean isFlyingEast() {
        return (mMovementDirection & Constants.DIRECTION_EAST) == Constants.DIRECTION_EAST;
    }

    public boolean isFlyingWest() {
        return (mMovementDirection & Constants.DIRECTION_WEST) == Constants.DIRECTION_WEST;
    }
}
