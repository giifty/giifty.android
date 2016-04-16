package dk.android.giifty.drawerfragments;

import android.content.Context;
import android.support.v4.app.Fragment;


public class DrawerFragment extends Fragment {

    private OnDrawerFragmentInteraction mListener;

    public DrawerFragment() {
        // Required empty public constructor
    }

    public void setToolbarTitle(String  title) {
        if (mListener != null) {
            mListener.setToolbarTitle(title);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrawerFragmentInteraction) {
            mListener = (OnDrawerFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDrawerFragmentInteraction {
        void setToolbarTitle(String title);
    }
}
