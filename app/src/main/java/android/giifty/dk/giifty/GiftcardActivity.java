package android.giifty.dk.giifty;

import android.giifty.dk.giifty.giftcard.GiftcardAdapter;
import android.giifty.dk.giifty.giftcard.GiftcardController;
import android.giifty.dk.giifty.model.Company;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class GiftcardActivity extends AppCompatActivity {

    private GiftcardController controller;
    private GiftcardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftcard);
        int companyId = 0;
        if (getIntent().hasExtra(Constants.EKSTRA_COMPANY_ID)) {
            companyId = getIntent().getIntExtra(Constants.EKSTRA_COMPANY_ID, -1);
        }

        controller = GiftcardController.getInstance();

        Company company = controller.getCompany(companyId);

        adapter = new GiftcardAdapter(this, company);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

}
