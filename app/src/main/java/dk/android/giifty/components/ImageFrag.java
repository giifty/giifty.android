package dk.android.giifty.components;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dk.android.giifty.R;
import dk.android.giifty.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFrag extends Fragment {
    private static final String EXTRA_URL = "param1";
    private String imageUrl;


    public ImageFrag() {
        // Required empty public constructor
    }

    public static ImageFrag newInstance(String url) {
        ImageFrag fragment = new ImageFrag();
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(EXTRA_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image, container, false);
        ImageView imageView = (ImageView) root.findViewById(R.id.giftcard_image_id);
        Utils.setImage(getContext(), imageView, imageUrl);
        return root;
    }

}
