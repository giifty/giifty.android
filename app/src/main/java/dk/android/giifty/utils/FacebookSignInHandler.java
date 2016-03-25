package dk.android.giifty.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mak on 25-03-2016.
 */
public class FacebookSignInHandler {
    private static final List<String> PERMISSION_LIST = Arrays.asList("public_profile", "email");
    private final CallbackManager callbackManager = CallbackManager.Factory.create();


    public CallbackManager signInWithFb(Fragment fragment, FacebookCallback<LoginResult> resultFacebookCallback) {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(fragment, PERMISSION_LIST);
        loginManager.registerCallback(callbackManager, resultFacebookCallback);
        return callbackManager;
    }

    public CallbackManager signInWithFb(Activity activity, FacebookCallback<LoginResult> resultFacebookCallback) {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(activity, PERMISSION_LIST);
        loginManager.registerCallback(callbackManager, resultFacebookCallback);
        return callbackManager;
    }

    public void fetchUserData(AccessToken accessToken, GraphRequest.GraphJSONObjectCallback jsonCallback) {

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken, jsonCallback);

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email,id,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
