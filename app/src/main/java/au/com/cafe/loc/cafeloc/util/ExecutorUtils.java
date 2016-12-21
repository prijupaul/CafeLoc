package au.com.cafe.loc.cafeloc.util;

import android.os.Handler;
import android.os.Process;

import au.com.cafe.loc.cafeloc.CafeApplication;

/**
 * Created by priju.jacobpaul on 21/12/16.
 */
public class ExecutorUtils {

    static Handler handler = new Handler(CafeApplication.getContext().getMainLooper());

    public static void runOnUIThread(Runnable runnable,long delay){
        if(delay == 0) {
         handler.post(runnable);
        }
        else {
            handler.postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable){
        handler.removeCallbacks(runnable);
    }

    public static void runInBackgroundThread(final Runnable runnable){

        new Thread(runnable){
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                runnable.run();
            }
        }.start();
    }

    public static void runInBackgroundThreadWithDelay(final Runnable runnable,final int delay){

        new Thread(runnable){
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    runnable.run();
                }
                catch (InterruptedException e){

                }
            }
        }.start();
    }
}
