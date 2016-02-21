package android.giifty.dk.giifty.user;

import android.giifty.dk.giifty.SignInListener;
import android.giifty.dk.giifty.model.User;
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
        synchronized (lockUser) {
            GlobalObserver.currentUser = currentUser;
        }
    }

    public static boolean hasCurrentUser() {
        synchronized (lockUser) {
            return GlobalObserver.currentUser != null;
        }
    }

    public static boolean hasSignedIn() {
        synchronized (lockUser) {
            return currentUser.hasSignedIn();
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

    public static void fireSignInEvent() {
        getCurrentUser().setSignedIn(true);
        synchronized (lockSingIn) {
            for (SignInListener listener : signInListeners) {
                listener.onSignedIn();
            }
        }
    }

    public static void fireSignOutEvent() {
        setServerToken(null);
        getCurrentUser().setSignedIn(false);
        synchronized (lockSingIn) {
            for (SignInListener listener : signInListeners) {
                listener.onSignedOut();
            }
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

    public void fireUserUpdatedEvent() {
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
