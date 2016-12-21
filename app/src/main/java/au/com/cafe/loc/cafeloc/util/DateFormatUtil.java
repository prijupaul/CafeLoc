package au.com.cafe.loc.cafeloc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by priju.jacobpaul on 20/12/2016.
 */

public class DateFormatUtil {

    public static String getDateYYYYMMDD(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDateandTime = sdf.format(date);
        return currentDateandTime;
    }
}
