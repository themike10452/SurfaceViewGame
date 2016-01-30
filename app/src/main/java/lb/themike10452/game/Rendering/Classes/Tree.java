package lb.themike10452.game.Rendering.Classes;

import android.content.Context;
import android.graphics.BitmapFactory;

import java.lang.ref.WeakReference;

import lb.themike10452.game.R;
import lb.themike10452.game.Utilities.Utils;

/**
 * Created by DELL on 1/30/2016.
 */
public class Tree extends ImageBase {
    public Tree(WeakReference<Context> ctr) {
        super(BitmapFactory.decodeResource(ctr.get().getResources(), R.drawable.tree));
        mBitmap = Utils.resizeBitmap(mBitmap, Utils.Constants.AUTO, (int) (ctr.get().getResources().getDisplayMetrics().heightPixels * (7f / 8f)));
    }
}
