package dk.android.giifty.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.math.BigInteger;


public class User implements Serializable {

    @Expose
    private int userId;
    @Expose
    private BigInteger facebookId;
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
    private String cardholderName;
    @Expose
    private String facebookProfileImageUrl;
    @Expose
    private boolean termsAccepted;

    public User(BigInteger facebookId, String password, String name, String email, boolean emailConfirmed, String phone, boolean phoneConfirmed, String accountNumber, String cardholderName) {
        this.facebookId = facebookId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.emailConfirmed = emailConfirmed;
        this.phone = phone;
        this.phoneConfirmed = phoneConfirmed;
        this.accountNumber = accountNumber;
        this.cardholderName = cardholderName;
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


    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public void setFacebookProfileImageUrl(String facebookProfileImageUrl) {
        this.facebookProfileImageUrl = facebookProfileImageUrl;
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

    public BigInteger getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(BigInteger facebookId) {
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
        return "{ sellerId:" + userId + ", password:" + password + ", name:" + name + ", email:" + email +
                ", emailConfirmed:" + emailConfirmed + ", phone:" + phone + ", phoneConfirmed:" + phoneConfirmed +
                ", accountNumber:" + accountNumber + ", cardholderName:" + cardholderName +"}";
    }
}
