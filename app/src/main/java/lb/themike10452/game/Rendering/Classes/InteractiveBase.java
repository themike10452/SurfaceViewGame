package lb.themike10452.game.Rendering.Classes;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by DELL on 1/28/2016.
 */
public class InteractiveBase extends StaticImageBase {

    protected Rect mRect;

    public InteractiveBase(Bitmap bitmap) {
        super(bitmap);
        mRect = new Rect(mLeft, mTop, mRight, mBottom);
    }

    @Override
    public void update(int left, int top) {
        super.update(left, top);
        mRect.set(mLeft, top, mRight, mBottom);
    }

    public boolean hit(int x, int y) {
        return mRect.contains(x, y);
    }
}
