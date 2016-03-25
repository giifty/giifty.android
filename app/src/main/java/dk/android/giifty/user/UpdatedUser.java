package dk.android.giifty.user;


import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import dk.android.giifty.model.User;
import dk.android.giifty.utils.Utils;

/**
 * Created by mak on 21-02-2016.
 */
public class UpdatedUser {
    public int userId;
    public int facebookId;
    public String password;
    public String name;
    public String email;
    public Boolean emailConfirmed;
    public String phone;
    public Boolean phoneConfirmed;
    public String accountNumber;
    public Boolean accountConfirmed;
    public String facebookProfileImageUrl;

    public RequestBody createUpdateRequest(User user) throws JSONException {
        updateUser(user);
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("password", password);
        json.put("name", name);
        json.put("email", email);
        json.put("emailConfirmed", emailConfirmed);
        json.put("phone", phone);
        json.put("phoneConfirmed", phoneConfirmed);
        json.put("accountNumber", accountNumber);
        json.put("accountConfirmed", accountConfirmed);
        json.put("facebookId", facebookId);
        json.put("facebookProfileImageUrl", facebookProfileImageUrl);
        return  Utils.createRequestBodyFromJson(json);
    }

    private void updateUser(User user) {

//        this.userId = user.getUserId();
//        if (facebookId == null) {
//            this.facebookId = user.getFacebookId();
//        }
//        if (password == null) {
//            this.password = user.getPassword();
//        }
//        if (name == null) {
//            this.name = user.getName();
//        }
//        if (email == null) {
//            this.email = user.getEmail();
//        }
//        if (emailConfirmed == null) {
//            this.emailConfirmed = user.isEmailConfirmed();
//        }
//        if (phone == null) {
//            this.phone = user.getPhone();
//        }
//        if (phoneConfirmed == null) {
//            this.phoneConfirmed = user.isPhoneConfirmed();
//        }
//        if (accountNumber == null) {
//            this.accountNumber = user.getAccountNumber();
//        }
//        if (accountConfirmed == null) {
//            this.accountConfirmed = user.isAccountConfirmed();
//        }
//        if (facebookProfileImageUrl == null) {
//            this.facebookProfileImageUrl = user.getFacebookProfileImageUrl();
//        }

    }

}
