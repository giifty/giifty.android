package dk.android.giifty.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import dk.android.giifty.R;
import dk.android.giifty.signin.SignInHandler;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mak on 16-02-2016.
 */
public class RequestHandler implements Callback {

    private Callback callback;
    private Call savedRequest;
    private int retryCounter;
    private static final int RETRY_MAX = 3;
    private ProgressDialog mProgressDialog;
    private SignInHandler signInHandler;
    private static final String TAG = RequestHandler.class.getSimpleName();

    public RequestHandler(Callback callback) {
        signInHandler = new SignInHandler();
        this.callback = callback;
    }

    public void enqueueRequest(Call request, Context context) {
        Log.d(TAG, "enqueueRequest()");
        if (context != null) {
            showProgressDialog(context);
        }
        request.enqueue(this);
    }

    private void finishWork(Response response, Retrofit retrofit) {
        Log.d(TAG, "finishWork()");
        dismissDialog();
        callback.onResponse(response, retrofit);

    }

    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        Log.d(TAG, "onResponse() success:" + response.isSuccess());
        finishWork(response, retrofit);
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        finishWork(null, null);
    }

    private void showProgressDialog(Context context) {
        //TODO customize that spinner damnit
        if (context != null) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage(context.getString(R.string.msg_loading));
                mProgressDialog.setIndeterminate(true);
            }
            mProgressDialog.show();
        }
    }

    private void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}