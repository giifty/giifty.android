package android.giifty.dk.giifty.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by mak on 11-01-2016.
 */
public class HelperMethods {

    public static void setImage(ImageView image, Context context, String url){
        Picasso.with(context).load(url).into(image);
    }
}
