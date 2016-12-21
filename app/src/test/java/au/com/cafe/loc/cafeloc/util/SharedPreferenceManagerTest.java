package au.com.cafe.loc.cafeloc.util;

import android.content.Context;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by priju.jacobpaul on 21/12/2016.
 */
@RunWith(PowerMockRunner.class)
public class SharedPreferenceManagerTest extends TestCase {

    Context mContext;

    @Override
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mContext = mock(Context.class);
    }

    @Test
    public void getLatitude() throws Exception {

        SharedPreferenceManager.saveLatitude("10.22111",mContext);
        String lat = SharedPreferenceManager.getLatitude(mContext);
        assertEquals(lat,"10.22111");

    }

    @Test
    public void getLongitude() throws Exception {

    }

}