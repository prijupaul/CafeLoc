package au.com.cafe.loc.cafeloc.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by priju.jacobpaul on 17/06/16.
 */
public class SharedPreferenceManager {

    private static String mLatitude;
    private static String mLongitude;
    private static String mFile;

    private static String TAG = SharedPreferenceManager.class.getName();

    public static void setFilePath(Context context) {

        mFile = context.getPackageName();
        mLatitude = mFile + "." + "latitude";
        mLongitude = mFile = "." + "longitude";

    }

    public static void saveLatitude(String latitude, Context context) {
        saveStringValue(mLatitude, latitude, context);
    }

    public static String getLatitude(Context context) {
        return getStringValue(mLatitude , context, "");
    }

    public static void saveLongitude(String longitude, Context context) {
        saveStringValue(mLongitude, longitude, context);
    }

    public static String getLongitude(Context context) {
        return getStringValue(mLongitude , context, "");
    }


    private static void saveStringValue(String key, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(mFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getStringValue(String key, Context context, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(mFile, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

}
