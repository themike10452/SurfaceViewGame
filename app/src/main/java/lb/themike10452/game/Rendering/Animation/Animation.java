package lb.themike10452.game.Rendering.Animation;

import lb.themike10452.game.Rendering.GameClock;

/**
 * Created by DELL on 1/31/2016.
 */
public class Animation {
    public static class FrameInfo {
        public String SpriteName;
        public int[] Flip;
        public int Rotation;

        public FrameInfo(String spriteName, int[] flip, int rotation) {
            SpriteName = spriteName;
            Flip = flip;
            Rotation = rotation;
        }
    }

    private SpriteSheet mSpriteSheet;
    private FrameInfo[] mFrames;
    private FrameInfo mCurrentFrame;
    private int mFrameCount;
    private int mAnimationPhase;
    private int mAnimationRate;
    private long mNextAnimationTime;
    private boolean mLoop;

    public Animation(SpriteSheet spriteSheet, int animationRate, boolean loop, FrameInfo...frames) {
        mSpriteSheet = spriteSheet;
        mFrames = frames;
        mFrameCount = mFrames.length;
        mAnimationRate = animationRate;
        mLoop = loop;
        reset();
    }

    public void reset() {
        mCurrentFrame = mFrames[0];
        mAnimationPhase = 0;
        mNextAnimationTime = -1;
    }

    public void update(GameClock gameClock) {
        long gameTime = gameClock.getGameTime();

        if (mNextAnimationTime == -1) {
            mAnimationPhase = 0;
            mNextAnimationTime = gameTime + 1000 / mAnimationRate;
        } else if (gameTime >= mNextAnimationTime) {
            ++mAnimationPhase;
            if (mLoop) {
                mAnimationPhase %= mFrameCount;
            } else {
                mAnimationPhase = Math.min(mAnimationPhase, mFrameCount - 1);
            }
            mNextAnimationTime = gameTime + 1000 / mAnimationRate;
        }

        mCurrentFrame = mFrames[mAnimationPhase];
    }

    public Sprite getFrame() {
        Sprite sprite = mSpriteSheet.getSprite(mCurrentFrame.SpriteName);
        sprite.Flip = mCurrentFrame.Flip;
        sprite.Rotation = mCurrentFrame.Rotation;
        return sprite;
    }
}
