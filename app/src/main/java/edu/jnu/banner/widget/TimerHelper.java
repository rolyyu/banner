package edu.jnu.banner.widget;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by roly on 2016/11/15.
 * 支持暂停和恢复的Timer
 * 由于Timer和TimerTask类的对象只能用一次，所以如果想多次调度同一样的任务，必须重新实例化。
 */
public abstract class TimerHelper {

    private Timer timer = null;

    public void start(long delay,long period){
        stop();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerHelper.this.run();
            }
        },delay,period);
    }
    public void stop(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    public abstract void run();
}