package au.com.cafe.loc.cafeloc.network.handlers;

import android.content.Context;

import au.com.cafe.loc.cafeloc.network.NetworkServices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.InvalidParameterException;

import retrofit2.Retrofit;

import static org.mockito.Mockito.mock;

/**
 * Created by priju.jacobpaul on 22/12/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ExploreVenueNetworkHandler.class
})

public class ExploreVenueNetworkHandlerTest {

    ExploreVenueNetworkHandler.ExploreVenueNetworkHandlerListener mockExploreVenueListener;
    ExploreVenueNetworkHandler testExploreVenueHandler;
    Context mockContext;
    Retrofit mockRetrofit;
    NetworkServices mockNetworkServices;

    @Before
    public void setUp(){
        mockExploreVenueListener = mock(ExploreVenueNetworkHandler.ExploreVenueNetworkHandlerListener.class);
        mockContext = mock(Context.class);
//        mockRetrofit = PowerMockito.mock(Retrofit.class);
//        mockNetworkServices = mock(NetworkServices.class);
//        PowerMockito.doReturn(mockRetrofit).when(testExploreVenueHandler).getRetrofit();

    }

    @Test(expected = InvalidParameterException.class)
    public void getPopularVenuesTestException() throws Exception {

            testExploreVenueHandler = new ExploreVenueNetworkHandler.ExploreVenueBuilder()
                    .setClientId("clientid")
                    .setClientSecret("clientsecret")
                    .build(mockContext);

    }

    @Test(expected = InvalidParameterException.class)
    public void getPopularVenuesTestInvalidException() throws Exception {

        testExploreVenueHandler = new ExploreVenueNetworkHandler.ExploreVenueBuilder()
                .setClientId("clientid")
                .setClientSecret("clientsecret")
                .setLongitude("-122.2222")
                .setLongitude("60.000")
                .build(mockContext);
    }

    @Test(expected = NullPointerException.class)
    public void getPopularVenuesTestNPEException() throws Exception {

        testExploreVenueHandler = new ExploreVenueNetworkHandler.ExploreVenueBuilder()
                .setClientId("22232r3eergwesdfsdf")
                .setClientSecret("23234resfewrt654")
                .setLatitude("-33.43074490")
                .setLongitude("121.28045570")
                .setSection("coffee")
                .setSortByDistance(true)
                .build(mockContext);
        testExploreVenueHandler.getPopularVenues(null);
    }


}