package dk.android.giifty.drawer.drawerfragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.R;
import dk.android.giifty.busevents.CompaniesFetchedEvent;
import dk.android.giifty.drawer.DrawerFragment;
import dk.android.giifty.giftcard.company.CompanyAdapter;


public class BuyGiftcardFrag extends DrawerFragment {

    private CompanyAdapter adapter;
    private RecyclerView recyclerView;

    public BuyGiftcardFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buy_giftcard, container, false);

        adapter = new CompanyAdapter(this);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        GiiftyApplication.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GiiftyApplication.getBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbarTitle(getString(R.string.buy_giftcard));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onCompaniesFetched(CompaniesFetchedEvent fetchedEvent) {
        if(fetchedEvent != null){
            if (fetchedEvent.isSuccessful) {
                adapter.updateData(fetchedEvent.companyList);
            } else {
                Snackbar.make(recyclerView, "Hov, noget fejlet", Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
