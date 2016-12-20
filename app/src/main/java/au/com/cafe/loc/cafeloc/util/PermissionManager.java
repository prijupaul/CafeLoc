package au.com.cafe.loc.cafeloc.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by priju.jacobpaul on 26/05/16.
 */
public class PermissionManager {


    public static boolean checkPermission(Context context, String permission) {

        if (BuildUtils.isVersionLesserThanM()) {
            return true;
        }

        int permissionStatus = ContextCompat.checkSelfPermission(context, permission);
        return (permissionStatus == PackageManager.PERMISSION_GRANTED) ? true : false;
    }

    public static boolean checkPermissions(Context context, String[] permissions) {

        if (BuildUtils.isVersionLesserThanM()) {
            return true;
        }

        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }


    public static String[] getLocationPermissions(){

        ArrayList<String> permissons = new ArrayList<>();
        permissons.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissons.add(Manifest.permission.ACCESS_FINE_LOCATION);
        String[] items = permissons.toArray(new String[permissons.size()]);
        return items;

    }

}
