package android.giifty.dk.giifty.model;

import com.google.gson.annotations.Expose;

/**
 * Created by mak on 16-01-2016.
 */
public class User {

    @Expose
    private int userId;
    @Expose
    private int facebookId;
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
    private boolean accountConfirmed;
    @Expose
    private String facebookProfileImageUrl;
    @Expose
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
    }

    public User(User newUser) {
        this.facebookId = newUser.getFacebookId();
        this.password = newUser.getPassword();
        this.name = newUser.getName();
        this.email = newUser.getEmail();
        this.emailConfirmed = newUser.isEmailConfirmed();
        this.phone = newUser.getPhone();
        this.phoneConfirmed = newUser.isPhoneConfirmed();
        this.accountNumber = newUser.getAccountNumber();
        userId = newUser.getUserId();
    }

    public User() {

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(int facebookId) {
        this.facebookId = facebookId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneConfirmed() {
        return phoneConfirmed;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isAccountConfirmed() {
        return accountConfirmed;
    }

    public String getFacebookProfileImageUrl() {
        return facebookProfileImageUrl;
    }

    public boolean isTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    @Override
    public String toString() {
        return "{ userId:" + userId + ", password:" + password + ", name:" + name + ", email:" + email +
                ", emailConfirmed:" + emailConfirmed + ", phone:" + phone + ", phoneConfirmed:" + phoneConfirmed +
                ", accountNumber:" + accountNumber + "}";
    }
}
