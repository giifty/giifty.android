package dk.android.giifty.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import dk.android.giifty.R;
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
        cloneRequest(request);

        request.enqueue(this);
    }

    private void finishWork(boolean isSuccess, Response response, Retrofit retrofit) {
        Log.d(TAG, "finishWork()");
        dismissDialog();
        if (isSuccess) {
            callback.onResponse(response, retrofit);
        } else {
            callback.onFailure(null);
        }
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

    private Call getClonedRequest() {
        return savedRequest;
    }

    private void cloneRequest(Call request) {
        savedRequest = request.clone();
    }

    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        if (response.isSuccess()) {
            finishWork(true, response, retrofit);
        } else {
            if (response.code() != 401) {
                if (retryCounter < RETRY_MAX) {
                    retryCounter++;
                    retryRequest();
                } else {
                    finishWork(false, response, retrofit);
                }
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        finishWork(false, null, null);
    }

    private void retryRequest() {
        Log.d(TAG, "retryRequest()");
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                try {
                    if (signInHandler.refreshTokenSynchronous()) {
                        Call c = getClonedRequest();
                        c.enqueue(callback);
                    } else {
                        finishWork(false, null, null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute();
    }
}