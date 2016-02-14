package android.giifty.dk.giifty.giftcard;

import android.app.Activity;
import android.giifty.dk.giifty.R;
import android.giifty.dk.giifty.model.Giftcard;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mak on 30-01-2016.
 */
public class GiftcardAdapter1 extends RecyclerView.Adapter<GiftcardAdapter1.ViewHolder> {


    private List<Giftcard> giftcardList;
    private final Activity parent;

    public GiftcardAdapter1(Activity giftcardActivity, List<Giftcard> data) {
        this.parent = giftcardActivity;
        giftcardList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.giftcard_gridview_item1, parent, false));
    }

    @Override
    public void onBindViewHolder(final GiftcardAdapter1.ViewHolder holder, int position) {
        final Giftcard giftcard = giftcardList.get(position);

        holder.title.setText(giftcard.getCompanyName());
        holder.bodyValue.setText(giftcard.getValue());
        holder.bodySales.setText(giftcard.getPrice());
        //   Utils.setImage(parent, holder.imageView, company.getFacebookProfileImageUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(giftcard);
                //TODO start new activity, either edit own giftcard or se giftcard info
            }
        });
    }

    private void startActivity(Giftcard giftcard) {
        // if(giftcard.getOwner().getName() == giftcard.getUserID())
        //   ActivityStarter.startGiftCardDetails(parent, holder.imageView, giftcard.getGiftcardId());
    }

    @Override
    public int getItemCount() {
        return giftcardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title, bodySales, bodyValue;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.company_image_id);
            title = (TextView) itemView.findViewById(R.id.title_id);
            bodySales = (TextView) itemView.findViewById(R.id.body_salesprice_id);
            bodyValue = (TextView) itemView.findViewById(R.id.body_value_id);
        }
    }
}
