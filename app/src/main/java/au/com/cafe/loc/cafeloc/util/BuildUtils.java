package au.com.cafe.loc.cafeloc.util;

import android.os.Build;

/**
 * Created by priju.jacobpaul on 20/12/16.
 */
public class BuildUtils {

    public static boolean isVersionLesserThanM(){
        return Build.VERSION.SDK_INT <  Build.VERSION_CODES.M;
    }

}
