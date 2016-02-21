package android.giifty.dk.giifty.utils;

import android.content.Context;
import android.giifty.dk.giifty.model.User;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;


/**
 * Created by mak on 11-01-2016.
 */
public class Utils {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd MMMM, yyyy");

    private static PeriodFormatter hourFormatter = new PeriodFormatterBuilder()
            .appendHours()
            .printZeroNever()
            .toFormatter();

    private static PeriodFormatter minuteFormatter = new PeriodFormatterBuilder()
            .appendMinutes()
            .printZeroNever()
            .toFormatter();

    private static final String TAG = Utils.class.getSimpleName();

    public static void setImage(Context context, ImageView imageView, String imageUrl) {
        Picasso.with(context).load(imageUrl).into(imageView);
    }

    public static String calculateTime(DateTime createdDate) {

        if (createdDate.plusDays(1).isBeforeNow()) {
            return dateTimeFormatter.print(createdDate);
        }
        Period period = new Period(createdDate, DateTime.now());
        if (createdDate.plusHours(1).isBeforeNow()) {
            if (createdDate.plusHours(1).isAfterNow()) {
                return hourFormatter.print(period) + " time siden";
            } else {
                return hourFormatter.print(period) + " timer siden";
            }
        }
        return createdDate.plusMinutes(1).isAfterNow() ? "1 minut siden" : minuteFormatter.print(period) + " minuter siden";

    }

    public static User createFakeUser(){
        return new User(-1, "", "12345678", "zaza onhorse", "mulle@gmail.com", false, "40845650", false, "sfdssfsd");
    }

}
