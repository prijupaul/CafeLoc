package au.com.cafe.loc.cafeloc.network.handlers;

import android.content.Context;

import au.com.cafe.loc.cafeloc.R;
import au.com.cafe.loc.cafeloc.network.NetworkConstants;
import au.com.cafe.loc.cafeloc.network.NetworkServices;
import au.com.cafe.loc.cafeloc.network.dto.ExploreVenueDto;
import au.com.cafe.loc.cafeloc.util.DateFormatUtil;

import java.security.InvalidParameterException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by priju.jacobpaul on 20/12/2016.
 */

public final class ExploreVenueNetworkHandler extends BaseNetwork {

    public interface ExploreVenueNetworkHandlerListener {
        void onVenueDetailsFetched(ExploreVenueDto response);

        void onError(String errorReason);
    }

    private String latitude;
    private String longitude;
    private String section;
    private int sortByDistance;
    private String client_id;
    private String client_secret;

    private ExploreVenueNetworkHandler() {

    }

    public static class ExploreVenueBuilder {

        private String lat = "";
        private String lon = "";
        private String sec = "";
        private boolean sortByDist = false;
        private String clientId = "";
        private String clientSecret = "";

        public ExploreVenueBuilder setLatitude(String lat) {
            this.lat = lat;
            return this;
        }

        public ExploreVenueBuilder setLongitude(String lon) {
            this.lon = lon;
            return this;
        }

        public ExploreVenueBuilder setSection(String sec) {
            this.sec = sec;
            return this;
        }

        public ExploreVenueBuilder setSortByDistance(boolean sortByDist) {
            this.sortByDist = sortByDist;
            return this;
        }

        public ExploreVenueBuilder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ExploreVenueBuilder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public ExploreVenueNetworkHandler build(Context context) {

            if (lat.isEmpty() || lon.isEmpty() || sec.isEmpty() || clientId.isEmpty() || clientSecret.isEmpty()) {
                throw new InvalidParameterException(context.getString(R.string.error_mandatory_param_empty));
            }
            ExploreVenueNetworkHandler exploreVenueNetworkHandler = new ExploreVenueNetworkHandler();
            exploreVenueNetworkHandler.latitude = lat;
            exploreVenueNetworkHandler.longitude = lon;
            exploreVenueNetworkHandler.section = sec;
            exploreVenueNetworkHandler.sortByDistance = sortByDist ? 1 : 0;
            exploreVenueNetworkHandler.client_id = clientId;
            exploreVenueNetworkHandler.client_secret = clientSecret;
            exploreVenueNetworkHandler.setBaseUrl(NetworkConstants.FOURSQUARE_URL);
            exploreVenueNetworkHandler.init();
            return exploreVenueNetworkHandler;
        }
    }

    public void getPopularVenues(final ExploreVenueNetworkHandlerListener listener) {

        if (listener == null) {
            throw new NullPointerException("Listener cannot be null");
        }

        NetworkServices networkServices = getRetrofit().create(NetworkServices.class);

        String location = latitude + "," + longitude;

        String date = DateFormatUtil.getDateYYYYMMDD(new Date());
        final Call<ExploreVenueDto> call = networkServices.getPopularVenues(location, client_id, client_secret, date, section, sortByDistance);
        call.enqueue(new Callback<ExploreVenueDto>() {
            @Override
            public void onResponse(Call<ExploreVenueDto> call, Response<ExploreVenueDto> response) {

                if (response.body().getMeta().code.compareToIgnoreCase("200") == 0) {
                    listener.onVenueDetailsFetched(response.body());
                } else {
                    listener.onError(response.body().getMeta().errorDetail);
                }

            }

            @Override
            public void onFailure(Call<ExploreVenueDto> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }


}
