package au.com.cafe.loc.cafeloc.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import au.com.cafe.loc.cafeloc.R;
import au.com.cafe.loc.cafeloc.network.dto.ExploreVenueDto;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by priju.jacobpaul on 21/12/2016.
 */

public class ExploreVenueAdapter extends RecyclerView.Adapter<ExploreVenueAdapter.ViewHolder> {

    private ExploreVenueAdapterListener exploreVenueListener;
    private ArrayList<ExploreVenueDto.items> itemsArrayList;

    public interface ExploreVenueAdapterListener{
        void onClick(View view,String url);
        void onLoadMaps(String lat,String lon);
    }

    public void setExploreVenueDto(ArrayList<ExploreVenueDto.items> itemsArrayList, ExploreVenueAdapterListener listener){
        this.itemsArrayList = itemsArrayList;
        this.exploreVenueListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTVCafeName.setText(itemsArrayList.get(position).getVenue().getName());
        holder.mTVAddress.setText(itemsArrayList.get(position).getVenue().getLocation().getAddress());
        float distance = Float.valueOf(itemsArrayList.get(position).getVenue().getLocation().getDistance()) / 1000;
        holder.mTvDistance.setText(String.valueOf(distance) + " km");

        holder.mTvRating.setVisibility(View.INVISIBLE);

        if(itemsArrayList.get(position).getVenue().getRating() != null &&
                !itemsArrayList.get(position).getVenue().getRating().isEmpty()) {
            holder.mTvRating.setVisibility(View.VISIBLE);
            holder.mTvRating.setText("Rating: " + itemsArrayList.get(position).getVenue().getRating());
            holder.mTvRating.setTextColor(Color.parseColor("#" + itemsArrayList.get(position).getVenue().getRatingColor()));
        }


    }

    @Override
    public int getItemCount() {
        try {
            if (this.itemsArrayList != null) {
                return itemsArrayList.size();
            }
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        return 0;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.card_view)
        CardView mCardView;

        @BindView(R.id.tv_cafename)
        TextView mTVCafeName;

        @BindView(R.id.tv_address)
        TextView mTVAddress;

        @BindView(R.id.tv_distance)
        TextView mTvDistance;

        @BindView(R.id.imgv_maps)
        ImageButton mImageButtonMaps;

        @BindView(R.id.tv_rating)
        TextView mTvRating;

      public ViewHolder(View itemView) {
          super(itemView);
          ButterKnife.bind(this,itemView);

          this.mCardView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  try {
                      ExploreVenueDto.items items = itemsArrayList.get(getAdapterPosition());
                      String url = "";
                      String venueUrl = items.getVenue().getUrl();
                      String canonicalUrl = "";

                      if (items.getTipsArrayList() != null) {
                          canonicalUrl = items.getTipsArrayList().get(0).getCanonicalUrl();
                      }

                      if ((venueUrl != null) && !venueUrl.isEmpty()) {
                          url = items.getVenue().getUrl();
                      } else if (!canonicalUrl.isEmpty()) {
                          url = canonicalUrl;
                      }

                      if (!url.isEmpty()) {
                          exploreVenueListener.onClick(view, url);
                      }
                      else {
                          Toast.makeText(view.getContext(), view.getContext().getString(R.string.cafe_url_not_available),Toast.LENGTH_LONG).show();;
                      }

                  }
                  catch (RuntimeException e){
                      e.printStackTrace();
                      Toast.makeText(view.getContext(), view.getContext().getString(R.string.cafe_url_not_available),Toast.LENGTH_LONG).show();;
                  }
              }
          });

          mImageButtonMaps.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  ExploreVenueDto.items items = itemsArrayList.get(getAdapterPosition());
                  if(items != null) {
                      exploreVenueListener.onLoadMaps(items.getVenue().getLocation().getLat(),
                              items.getVenue().getLocation().getLng());
                  }
              }
          });
      }
  }
}
