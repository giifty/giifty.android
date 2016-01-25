package android.giifty.dk.giifty.Giftcards;

import android.giifty.dk.giifty.R;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mak on 25-01-2016.
 */
public class GiftcardAdapter extends RecyclerView.Adapter<GiftcardAdapter.ViewHolder> {


    private List<Company> companyList;

    public GiftcardAdapter(List<Company> companyList) {
        this.companyList = companyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recyclerItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.giftcard_gridview_item, parent, false);
        return new ViewHolder(recyclerItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Company company = companyList.get(position);




    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title, description, discountText;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.company_image_id);
            title = (TextView) itemView.findViewById(R.id.title_id);
            description = (TextView) itemView.findViewById(R.id.description_id);
            discountText = (TextView) itemView.findViewById(R.id.discount_text_id);

        }
    }
}
