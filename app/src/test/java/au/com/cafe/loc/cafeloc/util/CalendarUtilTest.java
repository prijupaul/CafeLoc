package au.com.cafe.loc.cafeloc.util;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by priju.jacobpaul on 21/12/2016.
 */
public class CalendarUtilTest extends TestCase{

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGetDate(){
        int calendar = CalendarUtil.getDate();
        assert(calendar != 0);
    }
}