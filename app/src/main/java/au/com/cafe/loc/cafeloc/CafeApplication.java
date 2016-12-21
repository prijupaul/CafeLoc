package au.com.cafe.loc.cafeloc;

import android.app.Application;
import android.content.Context;

/**
 * Created by priju.jacobpaul on 22/12/2016.
 */

public class CafeApplication extends Application {

    static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
