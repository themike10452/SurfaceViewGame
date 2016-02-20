package lb.themike10452.game.Rendering.Animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Michael Mouawad
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SpriteBatch {
    private class DrawParameters {
        public Rect DestRect;
        public Sprite Sprite;

        public DrawParameters(Sprite sprite, Rect destRect) {
            this.Sprite = sprite;
            this.DestRect = destRect;
        }
    }

    private static final boolean DEBUG = true;

    private SpriteSheet mSpriteSheet;
    private List<DrawParameters> mBatch;
    private Paint mDbgPaint;

    public SpriteBatch(SpriteSheet spriteSheet) {
        mSpriteSheet = spriteSheet;
        mBatch = new ArrayList<>();
        mDbgPaint = new Paint();
        mDbgPaint.setColor(Color.WHITE);
    }

    public void begin() {
        mBatch.clear();
    }

    public void add(Sprite sprite, Rect destRect) {
        mBatch.add(new DrawParameters(sprite, destRect));
    }

    public void add(IDrawable drawable) {
        drawable.onDraw(SpriteBatch.this);
    }

    public void draw(Canvas canvas) {
        for (DrawParameters dp : mBatch) {
            Sprite sprite = dp.Sprite;
            Rect srcRect = new Rect(sprite.Rect);
            Rect destRect = new Rect(dp.DestRect);

            if (DEBUG) {
                //canvas.drawRect(destRect, mDbgPaint);
                canvas.drawLines(new float[]{
                                destRect.left, destRect.top,
                                destRect.right, destRect.top,
                                destRect.right, destRect.top,
                                destRect.right, destRect.bottom,
                                destRect.right, destRect.bottom,
                                destRect.left, destRect.bottom,
                                destRect.left, destRect.bottom,
                                destRect.left, destRect.top
                        },
                        mDbgPaint);
            }

            if (sprite.Rotation != 0 || sprite.Flip != null) {
                canvas.save();

                if (sprite.Flip != null) {
                    canvas.scale(sprite.Flip[0], sprite.Flip[1], destRect.left + destRect.width() / 2, destRect.top + destRect.height() / 2);
                }

                if (sprite.Rotation != 0) {
                    canvas.rotate(sprite.Rotation, destRect.left + destRect.width() / 2, destRect.top + destRect.height() / 2);
                }

                canvas.drawBitmap(mSpriteSheet.Bitmap, srcRect, destRect, null);
                canvas.restore();
            } else {
                canvas.drawBitmap(mSpriteSheet.Bitmap, srcRect, destRect, null);
            }
        }
    }
}
