package lb.themike10452.game.Rendering.Animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 1/31/2016.
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
