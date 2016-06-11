package dk.android.giifty.components;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> images = new ArrayList<>();

    public ImagePagerAdapter(Context context, List<String> images) {
        mContext = context;
        this.images = images;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        //  LayoutInflater inflater = LayoutInflater.from(mContext);
        // ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.property_image, collection, false);
        ImageView image = new ImageView(mContext); //(ImageView) layout.findViewById(R.id.image);
        Picasso.with(mContext).load(images.get(position)).into(image);
        collection.addView(image);
        return image;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.format("(%s/%s)", position, images.size());
    }

}
