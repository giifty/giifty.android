package android.giifty.dk.giifty.model;

import com.google.gson.annotations.Expose;

/**
 * Created by mak on 16-01-2016.
 */
public class User {

    @Expose
    private int userId;
    @Expose
    private String facebookId;
    @Expose
    private String password;
    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private boolean emailConfirmed;
    @Expose
    private String phone;
    @Expose
    private boolean phoneConfirmed;
    @Expose
    private String accountNumber;
    @Expose
    private String facebookProfileImageUrl;

    private boolean signedOut;

    public User(int userId, String facebookId, String password, String name, String email, boolean emailConfirmed, String phone, boolean phoneConfirmed, String accountNumber) {
        this.userId = userId;
        this.facebookId = facebookId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.emailConfirmed = emailConfirmed;
        this.phone = phone;
        this.phoneConfirmed = phoneConfirmed;
        this.accountNumber = accountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isPhoneConfirmed() {
        return phoneConfirmed;
    }

    public String getAccountNumber() {
        return accountNumber;
    }


    @Override
    public String toString() {
        return "{ userId:" + userId + ", password:" + password + ", name:" + name + ", email:" + email +
                ", emailConfirmed:" + emailConfirmed + ", phone:" + phone + ", phoneConfirmed:" + phoneConfirmed +
                ", accountNumber:" + accountNumber + "}";
    }

    public String getFacebookProfileImageUrl() {
        return facebookProfileImageUrl;
    }

    public boolean hasSignedIn() {
        return signedOut;
    }

    public void setSignedIn(boolean signedOut) {
        this.signedOut = signedOut;
    }
}
