package au.com.cafe.loc.cafeloc.network;

import au.com.cafe.loc.cafeloc.network.dto.ExploreVenueDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by priju.jacobpaul on 20/12/2016.
 */

public interface NetworkServices {

    @GET(NetworkConstants.FOURSQUARE_POPULAR_VENUE)

    Call<ExploreVenueDto> getPopularVenues(@Query("ll") String location,
                                           @Query("client_id")String clientId,
                                           @Query("client_secret")String clientSecret,
                                           @Query("v")String date,
                                           @Query("section")String section,
                                           @Query("sortByDistance")int sortByDistance);
}
