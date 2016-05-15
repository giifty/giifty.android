package dk.android.giifty.signin;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by mak on 15-05-2016.
 */
public class SignInParams implements Parcelable {
    public final String email;
    public final String password;


    public SignInParams(String email, String password) {
        this.email = email.trim();
        this.password = password.trim();
    }

    public String createAuthenticationHeader() {
        byte[] plain = new byte[0];
        try {
            plain = createAuthText().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format("Basic %s", Base64.encodeToString(plain, Base64.DEFAULT)).trim();
    }

    private String createAuthText() {
        return (email + ":" + password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
    }

    protected SignInParams(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
    }

    public static final Parcelable.Creator<SignInParams> CREATOR = new Parcelable.Creator<SignInParams>() {
        @Override
        public SignInParams createFromParcel(Parcel source) {
            return new SignInParams(source);
        }

        @Override
        public SignInParams[] newArray(int size) {
            return new SignInParams[size];
        }
    };
}
