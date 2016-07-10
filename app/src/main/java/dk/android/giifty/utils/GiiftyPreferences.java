package dk.android.giifty.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.model.User;
import dk.android.giifty.web.TypeAdapterYodaTime;

/**
 * Created by mak on 11-12-2015.
 */
public class GiiftyPreferences {

    private static final String KEY_AUTO_PASSWORD = "hasAutoPassword";
    private static GiiftyPreferences myPrefs;
    private Context context;
    private static final String NAME_SPACE = "namespace_giifty";
    private Gson gson;
    private static final String KEY_USER = "user_user";
    public static final String KEY_MY_GIFTCARDS = "giftcardsOnSale";
    public static final String KEY_MY_GC_PURCHASED = "giftcardsPurchased";

    public static GiiftyPreferences getInstance() {
        if (myPrefs == null) {
            myPrefs = new GiiftyPreferences();
        }
        return myPrefs;
    }

    public GiiftyPreferences() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new TypeAdapterYodaTime());
        gson = gsonBuilder.create();
        this.context = GiiftyApplication.getMyApplicationContext();
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences(NAME_SPACE, Context.MODE_PRIVATE);
    }

    public void persistPurchasedGiftcards(List<Giftcard> giftcardList) {
        String result = gson.toJson(giftcardList, new TypeToken<List<Giftcard>>() {
        }.getType());
        getPrefs().edit().putString(KEY_MY_GC_PURCHASED, result).commit();
    }

    public void persistMyGiftcards(List<Giftcard> giftcardList) {
        String result = gson.toJson(giftcardList, new TypeToken<List<Giftcard>>() {
        }.getType());
        getPrefs().edit().putString(KEY_MY_GIFTCARDS, result).commit();
    }

    public Giftcard getPurchasedGiftcard(int id) {
        Toast.makeText(context, "TODO getPurchasedGiftcard", Toast.LENGTH_LONG).show();
        return null;
    }

    public void addMyGiftcard(Giftcard giftcard) {
        List<Giftcard> list = getMyGiftcards();
        list.add(giftcard);
        persistMyGiftcards(list);
    }
    public void addPurchased(Giftcard giftcard) {
        List<Giftcard> list = getPurchasedGiftcards();
        list.add(giftcard);
        persistPurchasedGiftcards(list);
    }

    public List<Giftcard> getMyGiftcards() {
        List<Giftcard> list = getObject(KEY_MY_GIFTCARDS, new TypeToken<List<Giftcard>>() {
        });

        if(list == null){
            list = new ArrayList<>();
        }

        return list;
    }

    public List<Giftcard> getPurchasedGiftcards() {
        List<Giftcard> list = getObject(KEY_MY_GC_PURCHASED, new TypeToken<List<Giftcard>>() {
        });

        if(list == null){
            list = new ArrayList<>();
        }

        return list;
    }

    public User getUser() {
        return getObject(KEY_USER, new TypeToken<User>() {
        });
    }

    public boolean hasUser() {
        return getPrefs().contains(KEY_USER);
    }

    public void clearUser() {
        deleteKey(KEY_USER);
        deleteKey(KEY_MY_GIFTCARDS);
        deleteKey(KEY_MY_GC_PURCHASED);
    }

    public void persistUser(User user) {
        persistObject(KEY_USER, user);
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
