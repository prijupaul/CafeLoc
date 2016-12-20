package au.com.cafe.loc.cafeloc.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

    public class meta {
        @SerializedName("code")
        public String code;
        @SerializedName("requestId")
        public String requestId;
        @SerializedName("errorDetail")
        public String errorDetail;

    }

    public class response{

        @SerializedName("groups")
        ArrayList<groups> groupsArrayList = new ArrayList<>();


        public ArrayList<groups> getGroupsArrayList(){
            return groupsArrayList;
        }
    }

    public class groups{
        @SerializedName("type")
        public String type;

        @SerializedName("items")
        ArrayList<items> itemsArrayList = new ArrayList<>();

        public ArrayList<items> getItemsArrayList(){
            return itemsArrayList;
        }

    }

    public class items{
        @SerializedName("venue")
        public venue venue;

        @SerializedName("tips")
        ArrayList<tips> tipsArrayList = new ArrayList<>();

        public ArrayList<tips> getTipsArrayList() {
            return tipsArrayList;
        }


    }

    class venue {

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("contact")
        public contact contact;

        @SerializedName("location")
        public location location;

        @SerializedName("url")
        String url;

        @SerializedName("rating")
        String rating;

        @SerializedName("ratingColor")
        String ratingColor;


    }

    class contact{
        @SerializedName("phone")
        public String phone;

        @SerializedName("formattedPhone")
        public String formattedPhone;
    }

    class location{
        @SerializedName("address")
        public String address;

        @SerializedName("lat")
        public String lat;

        @SerializedName("lng")
        public String lng;
    }

    class tips{
        @SerializedName("canonicalUrl")
        public String canonicalUrl;
    }
}
