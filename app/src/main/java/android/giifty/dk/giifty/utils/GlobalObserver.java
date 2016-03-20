package android.giifty.dk.giifty.utils;

import android.giifty.dk.giifty.SignInListener;
import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.user.UserUpdatedListener;
import android.giifty.dk.giifty.web.ServerToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mak on 20-02-2016.
 */
public class GlobalObserver {

    private final static List<UserUpdatedListener> userUpdatedListeners = new ArrayList<>();
    private final static List<SignInListener> signInListeners = new ArrayList<>();
    private static ServerToken serverToken;
    private static User currentUser;
    private static final Object lockUser = new Object(), lockSingIn = new Object(), lockToken = new Object();

    public static User getCurrentUser() {
        synchronized (lockUser) {
            return currentUser;
        }
    }

    public static void setCurrentUser(User currentUser) {
        //TODO is this really needed if all get theier user from this class
        fireUserUpdatedEvent();
        synchronized (lockUser) {
            GlobalObserver.currentUser = currentUser;
        }
    }

    public static boolean hasCurrentUser() {
        synchronized (lockUser) {
            return GlobalObserver.currentUser != null;
        }
    }

    public static boolean isSignedIn() {
        synchronized (lockUser) {
            return currentUser.isSignedIn();
        }
    }

    public static String getServerToken() {
        synchronized (lockToken) {
            return serverToken.getToken();
        }
    }

    public static void setServerToken(ServerToken token) {
        synchronized (lockToken) {
            GlobalObserver.serverToken = token;
        }
    }



    public static void registerSignInListener(SignInListener listener) {
        synchronized (lockSingIn) {
            signInListeners.add(listener);
        }
    }

    public static void unRegisterSignInListener(SignInListener listener) {
        synchronized (lockSingIn) {
            signInListeners.remove(listener);
        }
    }

    public static void fireUserUpdatedEvent() {
        synchronized (lockUser) {
            for (UserUpdatedListener listener : userUpdatedListeners) {
                listener.onUserUpdated();
            }
        }
    }

    public static void registerUserUpdateListener(UserUpdatedListener listener) {
        synchronized (lockUser) {
            userUpdatedListeners.add(listener);
        }
    }

    public static void unRegisterUserUpdateListener(UserUpdatedListener listener) {
        synchronized (lockUser) {
            userUpdatedListeners.remove(listener);
        }
    }
}
