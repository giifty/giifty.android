package android.giifty.dk.giifty.giftcard;

import android.app.Activity;
import android.giifty.dk.giifty.R;
import android.giifty.dk.giifty.model.Giftcard;
import android.giifty.dk.giifty.utils.ActivityStarter;
import android.giifty.dk.giifty.utils.GlobalObserver;
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


    private final int userId;
    private List<Giftcard> giftcardList;
    private final Activity parent;

    public GiftcardAdapter1(Activity giftcardActivity, List<Giftcard> data) {
        this.parent = giftcardActivity;
        giftcardList = data;
        userId = GlobalObserver.getCurrentUser().getUserId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.giftcard_gridview_item1, parent, false));
    }

    @Override
    public void onBindViewHolder(final GiftcardAdapter1.ViewHolder holder, int position) {
        final Giftcard giftcard = giftcardList.get(position);

        holder.title.setText(giftcard.getCompany());
        holder.bodyValue.setText(giftcard.getValue());
        holder.bodySales.setText(giftcard.getPrice());
        //   Utils.setImage(parent, holder.imageView, company.getFacebookProfileImageUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(giftcard, holder.imageView);
            }
        });
    }

    private void startActivity(Giftcard giftcard, ImageView view) {
        if (giftcard.getSellerId() == userId) {
            //start Edit Giftcard
        } else if (giftcard.getBuyerId() == userId) {
            ActivityStarter.startGiftCardDetails(parent, view, giftcard.getGiftcardId());
        }
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
