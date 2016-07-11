package dk.android.giifty.drawer;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


public abstract class DrawerFragment extends Fragment {

    private OnDrawerFragmentInteraction mListener;

    public DrawerFragment() {
        // Required empty public constructor
    }

    public void setToolbarTitle(String  title) {
        if (mListener != null) {
            mListener.setToolbarTitle(title);
        }
    }

    public void showFragment(int id){
        if (mListener != null) {
            mListener.showSpecificView(id);
        }
    }

    @Nullable
    public ObservableBoolean getHasAskedToSignIn(){
        if (mListener == null) {
           return null;
        }
        return mListener.getHasAskedToSignIn();
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
        void showSpecificView(int id);
        ObservableBoolean getHasAskedToSignIn();
    }
}
