package dk.android.giifty.giftcard;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dk.android.giifty.R;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.utils.ActivityStarter;

/**
 * Created by mak on 30-01-2016.
 */
public class GiftcardAdapter1 extends RecyclerView.Adapter<GiftcardAdapter1.ViewHolder> {


    private final int userId;
    private List<Giftcard> giftcardList;
    private final Activity parent;

    public GiftcardAdapter1(Activity giftcardActivity, int userId) {
        this.parent = giftcardActivity;
        giftcardList = new ArrayList<>();
        this.userId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.giftcard_gridview_item1, parent, false));
    }

    @Override
    public void onBindViewHolder(final GiftcardAdapter1.ViewHolder holder, int position) {
        final Giftcard giftcard = giftcardList.get(position);

        holder.title.setText(giftcard.getCompany());
//        holder.bodyValue.setText(giftcard.getValueDataBinding());
//        holder.bodySales.setText(giftcard.getPriceDataBinding());
//        Utils.setImage(parent, holder.imageView, giftcard.getImages().get(0).getUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(giftcard, holder.imageView);
            }
        });
    }

    private void startActivity(Giftcard giftcard, ImageView view) {
        if (giftcard.getSellerId() == userId) {
            //TODO start Edit Giftcard
        } else if (giftcard.getBuyerId() == userId) {
            ActivityStarter.startGiftCardDetails(parent, view, giftcard.getGiftcardId());
        }
    }

    public void updateData(List<Giftcard> data){
        giftcardList = data;
        notifyDataSetChanged();
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
            bodySales = (TextView) itemView.findViewById(R.id.test2_id);
            bodyValue = (TextView) itemView.findViewById(R.id.test1_id);
        }
    }
}
