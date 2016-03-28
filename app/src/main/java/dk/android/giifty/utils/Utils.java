package dk.android.giifty.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dk.android.giifty.MyApp;
import dk.android.giifty.R;
import dk.android.giifty.model.User;


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

    public static void setUserImage(Context context, ImageView imageView, String imageUrl) {
        Picasso.with(context).load(imageUrl).placeholder(R.drawable.avatar).into(imageView);
    }

    public static void printHasH(Context context) {
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "dk.android.giifty",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
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

    public static User createFakeUser() {
        return new User(-1, "12345678", "zaza onhorse", "mulle@gmail.com", false, "40845650", false, "sfdssfsd");
    }

    public static RequestBody createRequestBodyFromJson(JSONObject json) {
        return RequestBody.create(MediaType.parse("application/json"), json.toString());
    }

    public static void makeToast(String msg) {
        Toast.makeText(MyApp.getMyApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void initFacebookSdk() {
        if(!FacebookSdk.isInitialized()){
            FacebookSdk.sdkInitialize(MyApp.getMyApplicationContext());
            FacebookSdk.setIsDebugEnabled(true);
        }
    }
}