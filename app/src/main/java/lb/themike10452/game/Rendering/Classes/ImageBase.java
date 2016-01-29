package lb.themike10452.game.Rendering.Classes;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by DELL on 1/28/2016.
 */
public class ImageBase {

    protected Bitmap mBitmap;

    protected int mLeft;
    protected int mTop;
    protected int mRight;
    protected int mBottom;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getLeft() {
        return mLeft;
    }

    public int getTop() {
        return mTop;
    }

    public int getRight() {
        return mRight;
    }

    public int getBottom() {
        return mBottom;
    }

    public ImageBase(Bitmap bitmap) {
        mBitmap = bitmap;
        mLeft = 0;
        mTop = 0;
        mRight = mLeft + bitmap.getWidth();
        mBottom = mTop + bitmap.getHeight();
    }

    public void update(int left, int top) {
        mLeft = left;
        mTop = top;
        mRight = mLeft + mBitmap.getWidth();
        mBottom = mTop + mBitmap.getHeight();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mLeft, mTop, null);
    }

    public void dispose() {
        if (mBitmap.isRecycled()) return;
        mBitmap.recycle();
    }
}
