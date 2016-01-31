package lb.themike10452.game.Rendering.Classes;

import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

import lb.themike10452.game.R;
import lb.themike10452.game.Rendering.Animation.Animation;
import lb.themike10452.game.Rendering.Animation.IDrawable;
import lb.themike10452.game.Rendering.Animation.SpriteBatch;
import lb.themike10452.game.Rendering.Animation.SpriteSheet;
import lb.themike10452.game.Rendering.Constants;
import lb.themike10452.game.Rendering.GameResources;

/**
 * Created by DELL on 1/31/2016.
 */
public class Duck extends AnimatedImageBase implements IDrawable {
    private static final int ANIMATION_RATE = 16;
    private static final int QUACK_RATE = 1;
    private static final int DELAY_BEFORE_FALL = 500;
    private static final int RESET_TIME = 3000;
    private static final int DEFAULT_MOVEMENT_RATE = 200;
    private static final int FALL_RATE = 700;

    protected Random mRandom;

    protected int mDirection;
    protected boolean mFalling;
    protected boolean mShot;
    protected long mGameTime;
    protected long mShotTime;
    protected long mQuackTime;
    protected long mFlapTime;

    public Duck(GameResources gameResources, SpriteSheet spriteSheet) {
        super(gameResources,
                new Animation(spriteSheet, ANIMATION_RATE, true,
                        new Animation.FrameInfo("duck1_1_1", null, 0),
                        new Animation.FrameInfo("duck1_1_2", null, 0),
                        new Animation.FrameInfo("duck1_1_3", null, 0),
                        new Animation.FrameInfo("duck1_1_2", null, 0)),

                new Animation(spriteSheet, 1, false,
                        new Animation.FrameInfo("duck1_shot", null, 0)),

                new Animation(spriteSheet, ANIMATION_RATE, true,
                        new Animation.FrameInfo("duck1_fall", null, 0),
                        new Animation.FrameInfo("duck1_3_1", new int[]{1, -1}, 0),
                        new Animation.FrameInfo("duck1_fall", new int[]{-1, 1}, 0))
        );
        mRandom = new Random();
        mDirection = Constants.DIRECTION_EAST;
        mFalling = false;
        mShot = false;
        reset();
    }

    @Override
    public void reset() {
        mShot = false;
        mActiveAnimation = mAnimations[0];
        mActiveAnimation.reset();
        position(0, Math.abs(mRandom.nextInt(mCameraRect.bottom - mCameraRect.top) + mCameraRect.top - mCameraRect.height() / 4));
    }

    @Override
    public void update() {
        int dx = 0;
        int dy = 0;

        mGameTime = mGameRes.getGameClock().getGameTime();

        if (mShot) {
            if (mGameTime >= mShotTime + RESET_TIME) {
                reset();
                return;
            } else if (mGameTime >= mShotTime + DELAY_BEFORE_FALL) {
                mActiveAnimation = mAnimations[2];
                dy += mGameRes.getGameClock().getFrameTime() * FALL_RATE / 1000;
            }
        } else {
            if (mGameTime >= mQuackTime + 1000 / QUACK_RATE) {
                mGameRes.playSound(R.raw.quack);
                mQuackTime = mGameTime;
            }

            if (mGameTime >= mFlapTime + 1000 / (ANIMATION_RATE / 3)) {
                mGameRes.playSound(R.raw.flap);
                mFlapTime = mGameTime;
            }

            int step = (int) (mGameRes.getGameClock().getFrameTime() * DEFAULT_MOVEMENT_RATE / 1000);

            if (isFlyingEast()) {
                dx += step;
            } else if (isFlyingWest()) {
                dx -= step;
            }

            if (isFlyingNorth()) {
                dy -= step;
            } else if (isFlyingSouth()) {
                dy += step;
            }
        }

        super.update();
        super.move(dx, dy);
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        batch.add(getFrame(), mRect);
    }

    public boolean hit(int x, int y) {
        return mRect.contains(x, y);
    }

    public void shoot() {
        mActiveAnimation = mAnimations[1];
        mShot = true;
        mShotTime = mGameRes.getGameClock().getGameTime();
    }

    public boolean isFlyingNorth() {
        return (mDirection & Constants.DIRECTION_NORTH) == Constants.DIRECTION_NORTH;
    }

    public boolean isFlyingSouth() {
        return (mDirection & Constants.DIRECTION_SOUTH) == Constants.DIRECTION_SOUTH;
    }

    public boolean isFlyingEast() {
        return (mDirection & Constants.DIRECTION_EAST) == Constants.DIRECTION_EAST;
    }

    public boolean isFlyingWest() {
        return (mDirection & Constants.DIRECTION_WEST) == Constants.DIRECTION_WEST;
    }
}
