package lb.themike10452.game.Rendering;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by DELL on 1/27/2016.
 */
public interface IRenderer extends SurfaceHolder.Callback {
    void onDraw(Canvas canvas);
    void onUpdate();
}
