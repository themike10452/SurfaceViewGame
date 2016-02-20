package lb.themike10452.game.Rendering.Classes;

import android.graphics.Rect;

import lb.themike10452.game.Rendering.Animation.Animation;
import lb.themike10452.game.Rendering.Animation.Sprite;
import lb.themike10452.game.Rendering.Animation.SpriteSheet;
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
public class AnimatedImageBase {
    protected GameResources mGameRes;
    protected Animation[] mAnimations;
    protected Animation mActiveAnimation;
    protected Rect mRect;
    protected Rect mCameraRect;

    protected int mLeft;
    protected int mRight;
    protected int mTop;
    protected int mBottom;

    public AnimatedImageBase(GameResources gameResources, Animation... animations) {
        mGameRes = gameResources;
        mAnimations = animations;
        mRect = new Rect();
        mCameraRect = new Rect(0, 0, gameResources.getDisplayMetrics().widthPixels, gameResources.getDisplayMetrics().heightPixels);
        mActiveAnimation = mAnimations[0];
        position(0, 0);
    }

    public void reset() {
        mActiveAnimation = mAnimations[0];
        mActiveAnimation.reset();
        position(0, 0);
    }

    public void update() {
        mActiveAnimation.update(mGameRes.getGameClock());
    }

    public void position(int left, int top) {
        mLeft = left;
        mTop = top;
        mRight = mLeft + (int) (getFrame().Rect.width() * SpriteSheet.DRAW_SCALE);
        mBottom = mTop + (int) (getFrame().Rect.height() * SpriteSheet.DRAW_SCALE);
        mRect.set(mLeft, mTop, mRight, mBottom);
    }

    public void move(int dx, int dy) {
        position(mLeft + dx, mTop + dy);
    }

    public Sprite getFrame() {
        return mActiveAnimation.getFrame();
    }

    public int getLeft() {
        return mLeft;
    }

    public int getRight() {
        return mRight;
    }

    public int getTop() {
        return mTop;
    }

    public int getBottom() {
        return mBottom;
    }
}
