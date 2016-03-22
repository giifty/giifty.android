package android.giifty.dk.giifty.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.giifty.dk.giifty.R;
import android.os.AsyncTask;

import java.io.IOException;

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

    public RequestHandler(Callback callback) {
        signInHandler = new SignInHandler();
        this.callback = callback;
    }


    public void enqueueRequest(Call request, Context context) {
        if (context != null) {
            showProgressDialog(context);
        }
        cloneRequest(request);

        request.enqueue(this);
    }

    private void finishWork(boolean isSucces, Response response, Retrofit retrofit) {
        dismissDialog();
        if (isSucces) {
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
            if(response.code() != 401){
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
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                try {
                    if (signInHandler.refreshTokenSynchronous()) {
                        Call c = getClonedRequest();
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