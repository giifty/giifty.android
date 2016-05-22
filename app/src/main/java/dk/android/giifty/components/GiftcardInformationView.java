package dk.android.giifty.components;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import dk.android.giifty.R;
import dk.android.giifty.utils.Utils;

/**
 * Created by mak on 22-05-2016.
 */
public class GiftcardInformationView extends RelativeLayout implements DatePickerDialog.OnDateSetListener {
    private final LayoutInflater inflater;
    private EditText valueText, priceText;
    private DatePickerDialog datePicker;
    private TextView expiryDateText;
    private DateTime selectedExpiryTime;

    public GiftcardInformationView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        initView();
    }

    public GiftcardInformationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        initView();
    }

    public GiftcardInformationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        inflater.inflate(R.layout.giftcard_information_layout, this, true);

        DateTime date = new DateTime();

        valueText = (EditText) findViewById(R.id.value_id);
        priceText = (EditText) findViewById(R.id.sales_price_id);
        View selectExpiryDate = findViewById(R.id.select_expiry_date_id);
        datePicker = new DatePickerDialog(inflater.getContext(), this, date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        expiryDateText = (TextView) findViewById(R.id.expiry_date_id);
        assert selectExpiryDate != null;
        selectExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });
    }

    public boolean validateTextFields() {
        return validateIsNonEmpty(valueText) && validateIsNonEmpty(priceText) && validateIsNonEmpty(expiryDateText);
    }

    private boolean validateIsNonEmpty(TextView view) {
        boolean valid = !TextUtils.isEmpty(view.getText());
        if (!valid) {
            view.requestFocus();
            view.setError("Skal udfyldes");
        }
        return valid;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        selectedExpiryTime = new DateTime(year, monthOfYear  + 1, dayOfMonth, 0, 0);
        expiryDateText.setText(Utils.formatTime(selectedExpiryTime));
    }

    public String getSelectedExpiryTime() {
        return selectedExpiryTime.toString();
    }

    public int getPrice() {
        return Integer.parseInt(priceText.getText().toString());
    }

    public int getValue() {
        return Integer.parseInt(valueText.getText().toString());
    }

    public void setValueText(int value) {
        this.valueText.setText(String.valueOf(value));
    }

    public void setPriceText(int price) {
        this.priceText.setText(String.valueOf(price));
    }

    public void setExpiryDate(String expiryDateText) {
        selectedExpiryTime = new DateTime(expiryDateText);
        this.expiryDateText.setText(Utils.formatTime(selectedExpiryTime));
    }
}
