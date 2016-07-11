package dk.android.giifty.components;

import android.databinding.ObservableBoolean;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dk.android.giifty.R;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Company;
import dk.android.giifty.signin.SignInDialogHandler;
import dk.android.giifty.utils.ActivityStarter;


public class TextViewAdapter extends RecyclerView.Adapter<TextViewAdapter.ViewHolder> {

    private List<Company> companyList;
    private final Fragment parent;
    private final ObservableBoolean hasSignedIn;

    public TextViewAdapter(Fragment parent, ObservableBoolean hasSignedIn) {
        this.parent = parent;
        this.hasSignedIn = hasSignedIn;
        companyList = GiftcardRepository.getInstance().getCompanyList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recyclerItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_list_item_layout, parent, false);
        return new ViewHolder(recyclerItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Company company = companyList.get(position);
        holder.companyName.setText(company.getName());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasSignedIn.get()) {
                    new SignInDialogHandler().startDialog(parent.getContext());
                    return;
                }
                ActivityStarter.startCreateGiftcardActivity(parent.getActivity(), company);

            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        View root;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            companyName = (TextView) itemView.findViewById(R.id.company_name_id);
        }
    }
}
