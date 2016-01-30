package lb.themike10452.game.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import lb.themike10452.game.Rendering.SurfaceRenderer;

/**
 * Created by DELL on 1/27/2016.
 */
public class MainActivity extends Activity {

    SurfaceRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SurfaceView surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        mRenderer = new SurfaceRenderer(surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRenderer.release();
    }
}
