package android.giifty.dk.giifty.giftcard.company;


import android.content.Intent;
import android.giifty.dk.giifty.BuyGiftcardFrag;
import android.giifty.dk.giifty.Constants;
import android.giifty.dk.giifty.GiftcardActivity;
import android.giifty.dk.giifty.R;
import android.giifty.dk.giifty.model.Company;
import android.giifty.dk.giifty.utils.ActivityStarter;
import android.graphics.drawable.Drawable;
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
    private final Drawable frameDrawable1, frameDrawable2, transperant;
    private List<Company> companyList;

    public CompanyAdapter(BuyGiftcardFrag fragment, List<Company> companyList) {
        this.parent = fragment;
        this.companyList = companyList;
        frameDrawable1 = parent.getActivity().getDrawable(R.drawable.bg_sale);
        frameDrawable2 = parent.getActivity().getDrawable(R.drawable.bg_more_sales);
        transperant = parent.getActivity().getDrawable(R.drawable.transperant_background);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recyclerItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_gridview_item, parent, false);
        return new ViewHolder(recyclerItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Company company = companyList.get(position);
        holder.title.setText(company.getName());
        String body = "Se " + company.getNumberOfGiftcards() + " " + company.getName() + " gavekort der er til salg.";
        holder.body.setText(body);
        holder.discountText.setText("23%");

        //    Utils.setImage(parent.getContext(), holder.imageView, company.getFacebookProfileImageUrl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), GiftcardActivity.class);
                intent.putExtra(Constants.EKSTRA_COMPANY_ID, company.getCompanyId());
                ActivityStarter.startActivityWithSlideIn(parent.getActivity(), intent);
            }
        });

        setFrame(holder.frame1, holder.frame2, company.getNumberOfGiftcards());
    }


    public void setFrame(View frame1,View frame2, int number) {
        if (number > 1) {
            frame2.setBackground(frameDrawable2);
            frame1.setBackground(null);
        }else {
            frame2.setBackground(transperant);
            frame1.setBackground(frameDrawable1);
        }
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public void updateData(List<Company> newData) {
        companyList = newData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title, body, discountText;
        private View frame2, frame1;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.company_image_id);
            title = (TextView) itemView.findViewById(R.id.title_id);
            body = (TextView) itemView.findViewById(R.id.body);
            discountText = (TextView) itemView.findViewById(R.id.discount_text_id);
            frame2 = itemView.findViewById(R.id.frame2_id);
            frame1 = itemView.findViewById(R.id.frame1_id);
        }
    }
}
