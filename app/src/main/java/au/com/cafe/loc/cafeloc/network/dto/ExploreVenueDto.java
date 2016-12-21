package au.com.cafe.loc.cafeloc.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by priju.jacobpaul on 20/12/2016.
 */

public class ExploreVenueDto {

    @SerializedName("meta")
    ExploreVenueDto.meta meta;

    @SerializedName("response")
    ExploreVenueDto.response response;

    public ExploreVenueDto.meta getMeta(){
        return meta;
    }

    public ExploreVenueDto.response getResponse(){
        return response;
    }

    public static class meta {
        @SerializedName("code")
        String code;
        @SerializedName("requestId")
        String requestId;
        @SerializedName("errorDetail")
        String errorDetail;

        public String getCode() {
            return code;
        }

        public String getRequestId() {
            return requestId;
        }

        public String getErrorDetail() {
            return errorDetail;
        }
    }

    public static class response{

        @SerializedName("groups")
        ArrayList<groups> groupsArrayList = new ArrayList<>();

        public ArrayList<groups> getGroupsArrayList(){
            return groupsArrayList;
        }
    }

    public static class groups{
        @SerializedName("type")
        String type;

        @SerializedName("items")
        ArrayList<items> itemsArrayList = new ArrayList<>();

        public ArrayList<items> getItemsArrayList(){
            return itemsArrayList;
        }

    }

    public static class items{
        @SerializedName("venue")
        venue venue;

        @SerializedName("tips")
        ArrayList<tips> tipsArrayList = new ArrayList<>();

        public ArrayList<tips> getTipsArrayList() {
            return tipsArrayList;
        }

        public venue getVenue(){
            return venue;
        }


    }

    public static class venue {

        @SerializedName("id")
        String id;

        @SerializedName("name")
        String name;

        @SerializedName("contact")
        contact contact;

        @SerializedName("location")
        location location;

        @SerializedName("url")
        String url;

        @SerializedName("rating")
        String rating;

        @SerializedName("ratingColor")
        String ratingColor;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public ExploreVenueDto.contact getContact() {
            return contact;
        }

        public ExploreVenueDto.location getLocation() {
            return location;
        }

        public String getUrl() {
            return url;
        }

        public String getRating() {
            return rating;
        }

        public String getRatingColor() {
            return ratingColor;
        }
    }

    public static class contact{
        @SerializedName("phone")
        String phone;

        @SerializedName("formattedPhone")
        String formattedPhone;

        public String getPhone() {
            return phone;
        }

        public String getFormattedPhone() {
            return formattedPhone;
        }
    }

    public static class location{
        @SerializedName("address")
        private String address;

        @SerializedName("lat")
        private String lat;

        @SerializedName("lng")
        private String lng;

        @SerializedName("distance")
        private String distance;

        public String getAddress() {
            return address;
        }

        public String getDistance() {
            return distance;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }
    }

    public static class tips{
        @SerializedName("canonicalUrl")
        public String canonicalUrl;

        public String getCanonicalUrl() {
            return canonicalUrl;
        }
    }

    public void sortOnDistance(ArrayList<items> itemsArrayLis){

        Collections.sort(itemsArrayLis, new Comparator<items>() {
            @Override
            public int compare(items items, items t1) {

                int tDistance = Integer.valueOf(items.getVenue().getLocation().getDistance());
                int t1Distance = Integer.valueOf(t1.getVenue().getLocation().getDistance());

                if(tDistance > t1Distance){
                    return 1;
                }
                else if (tDistance == t1Distance){
                    return 0;
                }
                return -1;

            }
        });
    }
}
