package dk.android.giifty.purchase.purchasefragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.R;
import dk.android.giifty.busevents.GiftcardPurchasedEvent;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.purchase.PurchaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPurchaseFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link CardPaymentFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardPaymentFrag extends PurchaseFragment implements TextWatcher {

    private EditText cardNr, cardholderName, expiryDate, securityNr;
    private int giftcardId, price;
    private String orderId;

    public CardPaymentFrag() {
        // Required empty public constructor
    }

//    public static PurchaseFragment newInstance(int giftcardId, int price) {
//        PurchaseFragment fragment = new CardPaymentFrag();
//        Bundle args = new Bundle();
//        args.putInt(GIFTCARD_ID, giftcardId);
//        args.putInt(PRICE, price);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            giftcardId = getArguments().getInt(GIFTCARD_ID);
            price = getArguments().getInt(PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_card_payment, container, false);
        cardNr = (EditText) root.findViewById(R.id.card_nr_id);
        cardholderName = (EditText) root.findViewById(R.id.cardholder_name_id);
        expiryDate = (EditText) root.findViewById(R.id.expiry_date_id);
        expiryDate.addTextChangedListener(this);
        securityNr = (EditText) root.findViewById(R.id.security_nr_id);
        securityNr.addTextChangedListener(this);
        return root;
    }


    @Override
    public void onResume() {
        super.onStart();
        GiiftyApplication.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onStop();
        GiiftyApplication.getBus().unregister(this);
    }

    @Subscribe
    public void onGiftcardPurchased(GiftcardPurchasedEvent event) {
        if (event.isSuccessFul) {
            Giftcard giftcard = event.giftcard;
            onPurchaseSuccess(giftcard);
        } else {
            // handler error
            onPurchaseFailed();
        }
    }

    @Override
    public void startTransaction() {
        super.startTransaction();
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(expiryDate.hasFocus()){
            if(s.length() == 2){
                s.append("/");
            } else if(s.length() == 5){
                securityNr.requestFocus();
            }
        } else if(securityNr.hasFocus() ){
            if(s.length() == 3){
                securityNr.clearFocus();
            }
        }

    }
}
