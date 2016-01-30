package android.giifty.dk.giifty.Utils1;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by mak on 11-01-2016.
 */
public class HelperMethods {


    public static void setImage(Context context, ImageView imageView, String imageUrl) {
        Picasso.with(context).load(imageUrl).into(imageView);
    }
}
