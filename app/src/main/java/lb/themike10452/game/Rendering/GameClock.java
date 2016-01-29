package lb.themike10452.game.Rendering;

/**
 * Created by DELL on 1/28/2016.
 */
public class GameClock {
    private long _frameCount;
    private long _gameTime;
    private long _frameTime;
    private long _startTime;

    public long getFrameCount() {
        return _frameCount;
    }

    public long getGameTime() {
        return _gameTime;
    }

    public long getFrameTime() {
        return _frameTime;
    }

    public long getStartTime() {
        return _startTime;
    }

    public GameClock() {

    }

    public void start() {
        _startTime = System.currentTimeMillis();
        _gameTime = 0;
    }

    public void update() {
        _frameTime = System.currentTimeMillis() - (_startTime + _gameTime);
        _gameTime = System.currentTimeMillis() - _startTime;
        _frameCount++;
    }
}
