package android.giifty.dk.giifty.giftcard;

import android.app.Activity;
import android.giifty.dk.giifty.R;
import android.giifty.dk.giifty.model.Company;
import android.giifty.dk.giifty.model.Giftcard;
import android.giifty.dk.giifty.utils.ActivityStarter;
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
public class GiftcardAdapter extends RecyclerView.Adapter<GiftcardAdapter.ViewHolder> {


    private List<Giftcard> giftcardList;
    private final Activity parent;
    private Company company;
    private final String body;

    public GiftcardAdapter(Activity giftcardActivity, Company company) {
        this.parent = giftcardActivity;
        this.company = company;
        giftcardList = company.getGiftcard();
        body = "Køb gavekort til " + company.getName() + ":\n";

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.giftcard_gridview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final GiftcardAdapter.ViewHolder holder, int position) {
        final Giftcard giftcard = giftcardList.get(position);

        holder.title.setText(company.getName());
        String body1 = body + "værdi " + giftcard.getValue() + ",-  din pris " + giftcard.getPrice() + ",-";
        holder.body.setText(body1);
        holder.discountText.setText(calculateDiscount(giftcard));
     //   Utils.setImage(parent, holder.imageView, company.getFacebookProfileImageUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityStarter.startGiftCardDetails(parent, holder.imageView, giftcard.getGiftcardId());
            }
        });


    }

      private String calculateDiscount(Giftcard giftcard) {
        return String.valueOf(Math.floor(giftcard.getPrice() / giftcard.getValue())) + "%";
    }

    @Override
    public int getItemCount() {
        return giftcardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title, body, discountText;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.company_image_id);
            title = (TextView) itemView.findViewById(R.id.title_id);
            body = (TextView) itemView.findViewById(R.id.body);
            discountText = (TextView) itemView.findViewById(R.id.discount_text_id);
        }
    }
}
