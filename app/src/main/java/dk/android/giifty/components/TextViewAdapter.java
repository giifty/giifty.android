package dk.android.giifty.components;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dk.android.giifty.R;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Company;
import dk.android.giifty.utils.ActivityStarter;

/**
 * Created by mak on 26-03-2016.
 */
public class TextViewAdapter extends RecyclerView.Adapter<TextViewAdapter.ViewHolder> {

    private List<Company> companyList;

    public TextViewAdapter() {
        companyList = GiftcardRepository.getInstance().getCompanyList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recyclerItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_list_item_layout, parent, false);
        return new ViewHolder(recyclerItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.companyName.setText(companyList.get(position).getName());
        holder.companyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityStarter.startScannerActivity();
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;

        public ViewHolder(View itemView) {
            super(itemView);
            companyName = (TextView) itemView.findViewById(R.id.company_name_id);
        }
    }
}
