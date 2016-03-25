package dk.android.giifty;


import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.android.giifty.broadcastreceivers.MyBroadcastReceiver;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.giftcard.company.CompanyAdapter;
import dk.android.giifty.utils.Broadcasts;


public class BuyGiftcardFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GiftcardRepository controller;
    private CompanyAdapter adapter;
    private MyReceiver myReceiver;

    public BuyGiftcardFrag() {
    }

    public static BuyGiftcardFrag newInstance(String param1, String param2) {
        BuyGiftcardFrag fragment = new BuyGiftcardFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buy_giftcard, container, false);

        controller = GiftcardRepository.getInstance();

        adapter = new CompanyAdapter(this, controller.getMainView(getContext()));

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        myReceiver = new MyReceiver();
        getContext().registerReceiver(myReceiver, new IntentFilter(Broadcasts.NEW_DOWNLOADS_FILTER));
        return root;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(myReceiver);
    }

    class MyReceiver extends MyBroadcastReceiver {

        @Override
        public void downloadCompleted(boolean isSucces) {
            if (isSucces) {
                adapter.updateData(controller.getMainView(getContext()));
            }
        }
    }
}
