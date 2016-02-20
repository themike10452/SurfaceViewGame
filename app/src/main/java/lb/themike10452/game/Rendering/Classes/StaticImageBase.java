package lb.themike10452.game.Rendering.Classes;

import android.graphics.Rect;

import lb.themike10452.game.Rendering.Animation.IDrawable;
import lb.themike10452.game.Rendering.Animation.Sprite;
import lb.themike10452.game.Rendering.Animation.SpriteBatch;
import lb.themike10452.game.Rendering.Animation.SpriteSheet;

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
public class StaticImageBase implements IDrawable {
    protected Sprite mSprite;
    protected Rect mRect;

    public int getLeft() {
        return mRect.left;
    }

    public int getTop() {
        return mRect.top;
    }

    public int getRight() {
        return mRect.right;
    }

    public int getBottom() {
        return mRect.bottom;
    }

    public int getWidth() {
        return mRect.width();
    }

    public int getHeight() {
        return mRect.height();
    }

    public StaticImageBase(SpriteSheet spriteSheet, String spriteLabel) {
        mSprite = spriteSheet.getSprite(spriteLabel);
        mRect = new Rect(0, 0,
                (int) (mSprite.Rect.width() * SpriteSheet.DRAW_SCALE),
                (int) (mSprite.Rect.height() * SpriteSheet.DRAW_SCALE));
    }

    public void update(int left, int top) {
        mRect.set(left, top, left + mRect.width(), top + mRect.height());
    }

    public void update(int left, int top, int width, int height) {
        mRect.set(left, top, left + width, top + height);
    }

    public void update(Rect rect) {
        mRect.set(rect);
    }

    public void move(int dx, int dy) {
        update(mRect.left + dx, mRect.top + dy);
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        batch.add(mSprite, mRect);
    }
}
