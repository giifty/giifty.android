package dk.android.giifty.components;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import dk.android.giifty.R;
import dk.android.giifty.databinding.GiftcardInformationLayoutBinding;
import dk.android.giifty.model.GiftcardProperties;
import dk.android.giifty.utils.Utils;

public class GiftcardInformationView extends RelativeLayout implements DatePickerDialog.OnDateSetListener {
    private DatePickerDialog datePicker;
    private DateTime selectedExpiryTime;
    private GiftcardInformationLayoutBinding binding;
    private final LayoutInflater inflater;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.giftcard_information_layout, this, true);
        DateTime date = new DateTime();
        datePicker = new DatePickerDialog(inflater.getContext(), this, date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        binding.selectExpiryDateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });
    }

    //keep for databinding
    public void setProperties(GiftcardProperties properties) {
        binding.setProperties(properties);
    }

    public boolean validateInput() {
        return validateIsNonEmpty(binding.valueId)
                && validateIsNonEmpty(binding.salesPriceId)
                && validateIsNonEmpty(binding.expiryDateId);
    }

    private boolean validateIsNonEmpty(TextView view) {
        boolean valid = !TextUtils.isEmpty(view.getText());
        if (!valid) {
            view.requestFocus();
            view.setError(getContext().getString(R.string.missing_input));
        }
        return valid;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        selectedExpiryTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        binding.expiryDateId.setText(Utils.formatTime(selectedExpiryTime));
        binding.getProperties().setExpirationDate(selectedExpiryTime);
    }
}
