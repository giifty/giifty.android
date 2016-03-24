package android.giifty.dk.giifty;


import android.giifty.dk.giifty.giftcard.GiftcardAdapter1;
import android.giifty.dk.giifty.giftcard.GiftcardRepository;
import android.giifty.dk.giifty.model.Giftcard;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchasedGiftcardsFrag extends Fragment {


    private GiftcardRepository controller;
    private GiftcardAdapter1 adapter;

    public PurchasedGiftcardsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_giftcards, container, false);
        controller = GiftcardRepository.getInstance();

        List<Giftcard> list = controller.getMyGiftcardPurchased();
        adapter = new GiftcardAdapter1(getActivity(), list);
        TextView emptyText = (TextView) root.findViewById(R.id.no_giftcards_text_id);

        if(list.isEmpty()){
            emptyText.setText(getText(R.string.msg_no_puchased_gc));
        }

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return root;
    }

}
