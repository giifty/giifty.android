package dk.android.giifty;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.model.CreateGiftcardRequest;
import dk.android.giifty.model.Holder;


public class PriceAndDescriptionActivity extends BaseActivity {

    private Holder holder;
    private CreateGiftcardRequest request = new CreateGiftcardRequest();
    private EditText valueText, priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_and_description);
        //  holder = getIntent().getParcelableExtra(Constants.EXTRA_HOLDER);
        valueText = (EditText) findViewById(R.id.value_id);
        priceText = (EditText) findViewById(R.id.sales_price_id);
        View selectExpiryDate = findViewById(R.id.select_expiry_date_id);
        selectExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start calendarwidget
            }
        });
        holder = new Holder();
        holder.setRequest(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_gc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_done) {
            if (!validateTextFields()) {
                return true;
            }
            //    ActivityStarter.startPriceAndDescriptionActivity(this, holder);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateTextFields() {
        return validateIsNonEmpty(valueText) && validateIsNonEmpty(priceText);
    }

    private boolean validateIsNonEmpty(EditText view) {
        boolean valid = !TextUtils.isEmpty(view.getText());
        if (!valid) {
            view.requestFocus();
            view.setError("Må ikke være tomt");
        }
        return valid;
    }
}
