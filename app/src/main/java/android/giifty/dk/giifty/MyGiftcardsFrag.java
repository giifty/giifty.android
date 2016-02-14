package android.giifty.dk.giifty;


import android.giifty.dk.giifty.giftcard.GiftcardAdapter1;
import android.giifty.dk.giifty.giftcard.GiftcardController;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyGiftcardsFrag extends Fragment {


    private GiftcardController controller;
    private GiftcardAdapter1 adapter;

    public MyGiftcardsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_giftcards, container, false);
        controller = GiftcardController.getInstance();
        adapter = new GiftcardAdapter1(getActivity(), null);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return root;
    }

}
