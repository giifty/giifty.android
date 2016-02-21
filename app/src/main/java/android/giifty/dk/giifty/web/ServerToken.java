package android.giifty.dk.giifty.web;

import org.joda.time.DateTime;

/**
 * Created by mak on 20-02-2016.
 */
public class ServerToken {

    private final String token;
    private final DateTime experationTime;

    public ServerToken(String token, DateTime experationTime) {
        this.token = token;
        this.experationTime = experationTime;
    }

    public String getToken() {
        return token;
    }

    public DateTime getExperationTime() {
        return experationTime;
    }
}
