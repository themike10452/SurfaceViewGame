package lb.themike10452.game.Rendering.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.ref.WeakReference;

import lb.themike10452.game.R;

/**
 * Created by DELL on 1/30/2016.
 */
public class GunSight extends StaticImageBase {
    public GunSight(WeakReference<Context> ctr) {
        super(BitmapFactory.decodeResource(ctr.get().getResources(), R.drawable.gunsight));
        mBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200, false);
    }

    @Override
    public void update(int left, int top) {
        super.update(left - mBitmap.getWidth() / 2, top - mBitmap.getHeight() / 2);
    }
}
