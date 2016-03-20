package android.giifty.dk.giifty.web;

import org.joda.time.DateTime;

/**
 * Created by mak on 20-02-2016.
 */
public class ServerToken {

    private final String token;
    private final DateTime expirationTime;

    public ServerToken(String token, DateTime expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public String getToken() {
        return token;
    }

    public DateTime getExpirationTime() {
        return expirationTime;
    }
}
