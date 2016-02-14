package android.giifty.dk.giifty.user;

import android.content.Context;
import android.giifty.dk.giifty.Constants;
import android.giifty.dk.giifty.model.NullResponse;
import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.utils.MyPrefrences;
import android.giifty.dk.giifty.utils.Utils;
import android.giifty.dk.giifty.web.ServiceCreator;
import android.giifty.dk.giifty.web.WebApi;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mak on 16-01-2016.
 */
public class UserController implements Callback {

    private static final String TAG = UserController.class.getSimpleName();
    private static UserController instance;
    private Context context;
    private WebApi webService;
    private User user;
    MyPrefrences myPrefrences;

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public UserController() {
    }

    public void initController(Context applicationContext) {
        Log.d(TAG, "initController()");
        this.context = applicationContext;
        myPrefrences = MyPrefrences.getInstance();
        webService = ServiceCreator.creatService();
        if (myPrefrences.hasKey(Constants.KEY_USER)) {
            user = myPrefrences.getObject(Constants.KEY_USER, new TypeToken<User>() {
            });
            loginWithUser();
        }
    }

    public void loginWithUser() {
        String auth = Utils.createAuthenticationHeader(createAuthText());
        webService.loginUser(auth);
    }

    public void createUser(User user) throws JSONException {
        String auth = "APP:so8Zorro";
        JSONObject json = new JSONObject();
        json.put("password", user.getPassword());
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("phone", user.getPhone());
        json.put("accountNumber", user.getAccountNumber());

        auth = Utils.createAuthenticationHeader(auth);

        Log.d(TAG, "createUser() auth:" + auth);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString());
        Call<User> request = webService.createUser(auth, requestBody);
        request.enqueue(this);
    }

    public void deleteUser() {
        Call<Boolean> request = webService.deleteUser("");
        request.enqueue(this);
    }


    public User getUser() {
        return user;
    }

    public Boolean hasUser() {
        return user != null;
    }

    public String createAuthText() {
        return (user.getName() + ":" + user.getPassword()).trim();
    }

    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        Log.d(TAG, "onResponse() state:" + response.isSuccess() + "  code:" + response.code() + "  msg:" + response.message());

        if (response.code() == 200) {
            Object responseBody = response.body();

            if (User.class.isInstance(responseBody)) {
                user = (User) responseBody;
                persistUser(user);
            } else if (Boolean.class.isInstance(responseBody)) {
                boolean isVerified = (boolean) responseBody;
            }else if (NullResponse.class.isInstance(responseBody)) {
                boolean isVerified = (boolean) responseBody;
            }

            String tokenResponse = response.headers().get("token");
        }
    }

    private void persistUser(User user) {
        myPrefrences.persistObject(Constants.KEY_USER, user);
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d(TAG, "onFailure() msg: " + t.getMessage());
    }

}
