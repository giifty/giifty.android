package android.giifty.dk.giifty.model;

import com.google.gson.annotations.Expose;

/**
 * Created by mak on 16-01-2016.
 */
public class User {

    @Expose
    private final int userId;
    @Expose
    private  final int facebookId;
    @Expose
    private final String password;
    @Expose
    private final String name;
    @Expose
    private final String email;
    @Expose
    private boolean emailConfirmed;
    @Expose
    private final String phone;
    @Expose
    private boolean phoneConfirmed;
    @Expose
    private final String accountNumber;
    @Expose
    private boolean accountConfirmed;
    @Expose
    private String facebookProfileImageUrl;

    private boolean termsAccepted;


    public User(int facebookId, String password, String name, String email, boolean emailConfirmed, String phone, boolean phoneConfirmed, String accountNumber) {
        this.facebookId = facebookId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.emailConfirmed = emailConfirmed;
        this.phone = phone;
        this.phoneConfirmed = phoneConfirmed;
        this.accountNumber = accountNumber;
        this.userId = -1;
    }

    public void setFacebookProfileImageUrl(String facebookProfileImageUrl) {
        this.facebookProfileImageUrl = facebookProfileImageUrl;
    }

    public void setAccountConfirmed(boolean accountConfirmed) {
        this.accountConfirmed = accountConfirmed;
    }

    public void setPhoneConfirmed(boolean phoneConfirmed) {
        this.phoneConfirmed = phoneConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public int getUserId() {
        return userId;
    }

    public int getFacebookId() {
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

    public boolean isAccountConfirmed() {
        return accountConfirmed;
    }

    public String getFacebookProfileImageUrl() {
        return facebookProfileImageUrl;
    }

    public boolean isSignedIn() {
        return password != null && password.isEmpty();
    }

    @Override
    public String toString() {
        return "{ userId:" + userId + ", password:" + password + ", name:" + name + ", email:" + email +
                ", emailConfirmed:" + emailConfirmed + ", phone:" + phone + ", phoneConfirmed:" + phoneConfirmed +
                ", accountNumber:" + accountNumber + "}";
    }

    public boolean isTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }
}
