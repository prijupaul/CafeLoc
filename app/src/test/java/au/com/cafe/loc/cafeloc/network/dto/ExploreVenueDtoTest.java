package au.com.cafe.loc.cafeloc.network.dto;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by priju.jacobpaul on 22/12/2016.
 */
public class ExploreVenueDtoTest {
    @Test
    public void sortOnDistance() throws Exception {
        ExploreVenueDto exploreVenueDto = new ExploreVenueDto();
        ArrayList<ExploreVenueDto.items> itemsArrayList = new ArrayList<>();

        ExploreVenueDto.items items1 = new ExploreVenueDto.items();
        ExploreVenueDto.venue venue1 = new ExploreVenueDto.venue();
        ExploreVenueDto.location location1 = new ExploreVenueDto.location();
        location1.distance = "1000";
        venue1.location = location1;
        items1.venue = venue1;
        itemsArrayList.add(items1);

        ExploreVenueDto.items items2 = new ExploreVenueDto.items();
        ExploreVenueDto.venue venue2 = new ExploreVenueDto.venue();
        ExploreVenueDto.location location2 = new ExploreVenueDto.location();
        location2.distance = "500";
        venue2.location = location2;
        items2.venue = venue2;
        itemsArrayList.add(items2);

        ExploreVenueDto.items items3 = new ExploreVenueDto.items();
        ExploreVenueDto.venue venue3 = new ExploreVenueDto.venue();
        ExploreVenueDto.location location3 = new ExploreVenueDto.location();
        location3.distance = "900";
        venue3.location = location3;
        items3.venue = venue3;
        itemsArrayList.add(items3);

        exploreVenueDto.sortOnDistance(itemsArrayList);

        assertEquals(Integer.valueOf(itemsArrayList.get(0).getVenue().getLocation().distance), Integer.valueOf(500));
        assertEquals(Integer.valueOf(itemsArrayList.get(1).getVenue().getLocation().distance), Integer.valueOf(900));
        assertEquals(Integer.valueOf(itemsArrayList.get(2).getVenue().getLocation().distance), Integer.valueOf(1000));
    }

}