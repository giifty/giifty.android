package android.giifty.dk.giifty.User;

import com.google.gson.annotations.Expose;

/**
 * Created by mak on 16-01-2016.
 */
public class User {

    @Expose
    private final String userId;
    @Expose
    private final String facebookId;
    @Expose
    private  final String password;
    @Expose
    private final String name;
    @Expose
    private final String email;
    @Expose
    private final boolean emailConfirmed;
    @Expose
    private final String phone;
    @Expose
    private final boolean phoneConfirmed;
    @Expose
    private final String accountNumber;

    public User(String userId, String facebookId, String password, String name, String email, boolean emailConfirmed, String phone, boolean phoneConfirmed, String accountNumber) {
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
        return "{ userId:"+ userId + ", password:"+ password + ", name:"+name + ", email:"+ email +
                ", emailConfirmed:"+ emailConfirmed + ", phone:"+ phone + ", phoneConfirmed:"+ phoneConfirmed +
                ", accountNumber:"+ accountNumber + "}";
    }

}
