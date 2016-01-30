package android.giifty.dk.giifty.giftcard.company;


import android.content.Intent;
import android.giifty.dk.giifty.BuyGiftcardFrag;
import android.giifty.dk.giifty.Constants;
import android.giifty.dk.giifty.GiftcardActivity;
import android.giifty.dk.giifty.R;
import android.giifty.dk.giifty.model.Company;
import android.giifty.dk.giifty.utils.HelperMethods;
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
public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {


    private final BuyGiftcardFrag parent;
    private List<Company> companyList;

    public CompanyAdapter(BuyGiftcardFrag fragment, List<Company> companyList) {
        this.parent = fragment;
        this.companyList = companyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recyclerItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.giftcard_gridview_item, parent, false);
        return new ViewHolder(recyclerItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Company company = companyList.get(position);

        holder.title.setText(company.getName());
        String body = "Se " + company.getNumberOfCards() + " " + company.getName() + " gavekort der er til salg.";
        holder.body.setText(body);
        holder.discountText.setText("23%");

        HelperMethods.setImage(parent.getContext(), holder.imageView, company.getImageUrl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), GiftcardActivity.class);
                intent.putExtra(Constants.EKSTRA_COMPANY_ID, company.getCompanyId());
                HelperMethods.startActivityWithHero(parent.getActivity(), holder.itemView,
                        intent,parent.getString(R.string.hero_gridview_item));
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public void updateData(List<Company> newData) {
        companyList = newData;
//        for (Company c : newData) {
//            companyList.add(c);
//        }
        notifyDataSetChanged();
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
