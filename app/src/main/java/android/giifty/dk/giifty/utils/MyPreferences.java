package android.giifty.dk.giifty.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by mak on 11-12-2015.
 */
public class MyPreferences {

    private static MyPreferences myPrefs;
    private Context context;
    private static final String NAME_SPACE = "namespace_giifty";
    private Gson gson = new Gson();

    public void setContext(Context context) {
        this.context = context;
    }

    public static MyPreferences getInstance() {
        if (myPrefs == null) {
            myPrefs = new MyPreferences();
        }
        return myPrefs;
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences(NAME_SPACE, Context.MODE_PRIVATE);
    }

    public boolean hasKey(String key) {
        return getPrefs().contains(key);
    }

    public void persistBool(String key, boolean value) {
        getPrefs().edit().putBoolean(key, value).commit();
    }

    public void persistLong(String key, long value) {
        getPrefs().edit().putLong(key, value).commit();
    }

    public void persistInt(String key, int value) {
        getPrefs().edit().putInt(key, value).commit();
    }

    public <T> T getObject(String key, TypeToken token) {
        String result = getPrefs().getString(key, "");
        if ("".contentEquals(result)) {
            return null;
        }
        return gson.fromJson(result, token.getType());
    }

    public long getLong(String key) {
        return getPrefs().getLong(key, 0);
    }

    public int getInt(String key) {
        return getPrefs().getInt(key, -1);
    }

    public boolean getBoolean(String key) {
        return getPrefs().getBoolean(key, false);
    }

    public void persistObject(String key, Object value) {
        String result = gson.toJson(value);
        getPrefs().edit().putString(key, result).commit();
    }


    public void persistString(String key, String value) {
        getPrefs().edit().putString(key, value).commit();
    }


    public String getString(String key) {
        return getPrefs().getString(key, "");
    }

    public void deleteKey(String keyToDelete) {
        getPrefs().edit().remove(keyToDelete).commit();
    }
}
