package dk.android.giifty;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.giftcard.GiftcardAdapter;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Company;

public class GiftcardActivity extends BaseActivity {

    private GiftcardRepository controller;
    private GiftcardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftcard);
        int companyId = 0;


        if (getIntent().hasExtra(Constants.EKSTRA_COMPANY_ID)) {
            companyId = getIntent().getIntExtra(Constants.EKSTRA_COMPANY_ID, -1);
        }

        controller = GiftcardRepository.getInstance();

        Company company = controller.getCompany(companyId);

        adapter = new GiftcardAdapter(this, company, controller.getCompanyGiftcards(company.getCompanyId()));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        String titleText = company.getName() + " " + getString(R.string.giftcard);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleText);
        }
    }

}
