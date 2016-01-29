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
import lb.themike10452.game.Rendering.GameClock;

/**
 * Created by DELL on 1/28/2016.
 */
public class Bird extends InteractiveBase {
    private static final int ANIMATION_RATE = 6;
    private static final int DELAY_BEFORE_FALL = 300;
    private static final int RESET_TIME = 3000;

    private Bitmap mVisibleBitmap;
    private Bitmap mBitmapFlyAnim1;
    private Bitmap mBitmapFlyAnim2;
    private Bitmap mBitmapShot;
    private Bitmap mBitmapShotAnim1;
    private Bitmap mBitmapShotAnim2;
    private Bitmap mBitmapShotAnim3;
    private GameClock mGameClock;
    private Random mRandomizer;
    private Rect mCanvasRect;

    private int mAnimationPhase;
    private int mMovementDirection;
    private int mMovementRate;
    private boolean mShot;
    private boolean mFalling;

    private long _lastAnimationTime;
    private long _shotTime;

    public Bird(WeakReference<Context> ctr, GameClock gameClock) {
        super(BitmapFactory.decodeResource(ctr.get().getResources(), R.drawable.duck_1));
        Resources resources = ctr.get().getResources();
        mVisibleBitmap = mBitmap;
        mBitmapFlyAnim1 = BitmapFactory.decodeResource(resources, R.drawable.duck_2);
        mBitmapFlyAnim2 = BitmapFactory.decodeResource(resources, R.drawable.duck_3);
        mBitmapShot = BitmapFactory.decodeResource(resources, R.drawable.duck_shot);
        mBitmapShotAnim1 = BitmapFactory.decodeResource(resources, R.drawable.duck_shot1);
        mBitmapShotAnim2 = BitmapFactory.decodeResource(resources, R.drawable.duck_shot2);
        mBitmapShotAnim3 = BitmapFactory.decodeResource(resources, R.drawable.duck_shot3);
        mGameClock = gameClock;
        mRandomizer = new Random();
        DisplayMetrics dm = ctr.get().getResources().getDisplayMetrics();
        mCanvasRect = new Rect(0, 0, dm.widthPixels, dm.heightPixels);
        reset();
    }

    public void update() {
        mFalling = mShot && mGameClock.getGameTime() >= _shotTime + DELAY_BEFORE_FALL;

        if (mFalling && mGameClock.getGameTime() >= _shotTime + DELAY_BEFORE_FALL + RESET_TIME) {
            reset();
            return;
        }

        //TODO
        if (!mCanvasRect.contains(mLeft, mTop) && !mShot) {
            shoot();
        }

        int dx = 0;
        int dy = 0;
        int step = (int) (mGameClock.getFrameTime() * mMovementRate / 1000);

        if (mShot) {
            dy += mFalling ? step : 0;
        } else {
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
            _shotTime = mGameClock.getGameTime();
        }
    }

    public void reset() {
        mShot = false;
        mFalling = false;
        mVisibleBitmap = mBitmap;
        mAnimationPhase = 0;
        mMovementDirection = Constants.DIRECTION_EAST;
        mMovementRate = 700;
        _lastAnimationTime = 0;
        _shotTime = 0;

        int height = mCanvasRect.bottom;
        int ran = Math.abs(mRandomizer.nextInt(height) - height / 5);

        update(0, ran);
    }

    protected void updateBitmap() {
        if (mGameClock.getGameTime() >= _lastAnimationTime + (1000 / ANIMATION_RATE)) {
            _lastAnimationTime = mGameClock.getGameTime();
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
                        break;
                }
            }
        }
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
