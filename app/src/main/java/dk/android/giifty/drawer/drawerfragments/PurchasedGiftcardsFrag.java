package dk.android.giifty.drawer.drawerfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dk.android.giifty.utils.MyDialogBuilder;
import dk.android.giifty.R;
import dk.android.giifty.drawer.DrawerFragment;
import dk.android.giifty.giftcard.GiftcardAdapter1;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.user.UserRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchasedGiftcardsFrag extends DrawerFragment {


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
        TextView emptyText = (TextView) root.findViewById(R.id.no_giftcards_text_id);


        int userId = -1;
        List<Giftcard> immutableList = controller.getMyGiftcardPurchased();

        if (UserRepository.getInstance().hasUser()) {
            userId = UserRepository.getInstance().getUser().getUserId();
            if (immutableList.isEmpty()) {
                emptyText.setVisibility(View.VISIBLE);
                emptyText.setText(getText(R.string.msg_no_puchased_gc));
            }
        }
        adapter = new GiftcardAdapter1(getActivity(), immutableList, userId);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbarTitle(getString(R.string.buy_giftcard));
        if (!UserRepository.getInstance().hasUser()) {
            MyDialogBuilder.createNoUserDialog(getActivity()).show();
        }
    }

}
