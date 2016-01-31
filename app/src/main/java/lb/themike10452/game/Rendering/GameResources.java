package lb.themike10452.game.Rendering;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;

import java.util.HashMap;

import lb.themike10452.game.R;

/**
 * Created by DELL on 1/30/2016.
 */
public class GameResources {
    private DisplayMetrics mDisplayMetrics;
    private GameClock mGameClock;
    private HashMap<Integer, MediaPlayer> mSoundMap;

    public GameClock getGameClock() {
        return mGameClock;
    }
    
    public DisplayMetrics getDisplayMetrics() {
        return mDisplayMetrics;
    }

    public void playSound(int id) {
        MediaPlayer mp = mSoundMap.get(id);
        if (mp.isPlaying()) {
            mp.seekTo(0);
        } else {
            mp.start();
        }
    }

    public GameResources(Context context) {
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        mGameClock = new GameClock();
        loadSounds(context);
    }

    private void loadSounds(Context context) {
        mSoundMap = new HashMap<>(2);
        mSoundMap.put(R.raw.flap, MediaPlayer.create(context, R.raw.flap));
        mSoundMap.put(R.raw.quack, MediaPlayer.create(context, R.raw.quack));
        mSoundMap.put(R.raw.shoot, MediaPlayer.create(context, R.raw.shoot));
    }

    public void dispose() {
        for (MediaPlayer mp : mSoundMap.values()) {
            mp.release();
        }
    }
}
