package au.com.cafe.loc.cafeloc.util;

import java.util.Calendar;

/**
 * Created by priju.jacobpaul on 20/12/2016.
 */

public class CalendarUtil {

    public static int getDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }
}
