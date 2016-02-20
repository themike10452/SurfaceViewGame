package lb.themike10452.game.Rendering.Classes;

import java.util.Random;

import lb.themike10452.game.R;
import lb.themike10452.game.Rendering.Animation.Animation;
import lb.themike10452.game.Rendering.Animation.IDrawable;
import lb.themike10452.game.Rendering.Animation.SpriteBatch;
import lb.themike10452.game.Rendering.Animation.SpriteSheet;
import lb.themike10452.game.Rendering.Constants;
import lb.themike10452.game.Rendering.GameResources;

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
public class Duck extends AnimatedImageBase implements IDrawable {
    private static final int ANIMATION_RATE = 15;
    private static final int QUACK_RATE = 1;
    private static final int DELAY_BEFORE_FALL = 500;
    private static final int RESET_TIME = 3000;
    private static final int DEFAULT_MOVEMENT_RATE = 600;
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
                        new Animation.FrameInfo("duck111", null, 0),
                        new Animation.FrameInfo("duck112", null, 0),
                        new Animation.FrameInfo("duck113", null, 0),
                        new Animation.FrameInfo("duck112", null, 0)),

                new Animation(spriteSheet, 1, false,
                        new Animation.FrameInfo("duck1shot", null, 0)),

                new Animation(spriteSheet, ANIMATION_RATE, true,
                        new Animation.FrameInfo("duck1fall", null, 0),
                        new Animation.FrameInfo("duck131", new int[]{1, -1}, 0),
                        new Animation.FrameInfo("duck1fall", new int[]{-1, 1}, 0))
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

            if (mGameTime >= mFlapTime + 1000 / (ANIMATION_RATE / 3f)) {
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
