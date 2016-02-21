package android.giifty.dk.giifty;


import android.giifty.dk.giifty.components.DataUpdateListener;
import android.giifty.dk.giifty.giftcard.company.CompanyAdapter;
import android.giifty.dk.giifty.giftcard.GiftcardController;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BuyGiftcardFrag extends Fragment implements DataUpdateListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GiftcardController controller;
    private CompanyAdapter adapter;

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

        controller = GiftcardController.getInstance();

        controller.setDataUpdateListener(this);

        adapter = new CompanyAdapter(this, controller.getMainView(getContext()));

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return root;
    }


    @Override
    public void onNewDataAvailable() {
        adapter.updateData(controller.getMainView(getContext()));
    }

    @Override
    public void onNewUpdateFailed() {

    }
}
