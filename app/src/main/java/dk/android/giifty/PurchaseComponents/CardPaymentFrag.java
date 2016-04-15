package dk.android.giifty.PurchaseComponents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.android.giifty.R;
import dk.android.giifty.web.RequestHandler;
import dk.android.giifty.web.WebApi;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardPaymentFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardPaymentFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardPaymentFrag extends PurchaseFragment {


    private int giftcardId, price;
    private WebApi webService;
    private RequestHandler requestHandler;
    private String orderId;

    public CardPaymentFrag() {
        // Required empty public constructor
    }

    public static PurchaseFragment newInstance(int giftcardId, int price, String orderId) {
        PurchaseFragment fragment = new CardPaymentFrag();
        Bundle args = new Bundle();
        args.putInt(GIFTCARD_ID, giftcardId);
        args.putInt(PRICE, price);
        args.putString(ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            giftcardId = getArguments().getInt(GIFTCARD_ID);
            price = getArguments().getInt(PRICE);
            orderId = getArguments().getString(ORDER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_payment, container, false);
    }

}
