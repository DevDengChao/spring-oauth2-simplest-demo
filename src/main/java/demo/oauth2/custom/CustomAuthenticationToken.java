package demo.oauth2.custom;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Extend UsernamePasswordAuthenticationToken, add <code>appId</code> as a custom authorization factor.<br/>
 * This token help us locate a {@link org.springframework.security.core.userdetails.UserDetails UserDetails} by {@link #principal username} and {@link #appId}
 *
 * @author <a href="mailto:qq2325690622@gmail.com">邓超</a> on 2018/10/11
 */
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    @NonNull
    private final String appId;

    /**
     * Construct a un-authorized token
     *
     * @param username username
     * @param password password
     * @param appId    appId
     * @see UsernamePasswordAuthenticationToken#setAuthenticated(boolean)
     */
    public CustomAuthenticationToken(@NonNull String username, @NonNull String appId, @NonNull String password) {
        // this(username, appId, password, null);// do not use this()
        super(username, password, null);
        this.appId = appId;
    }

    @NonNull
    public String getAppId() {
        return appId;
    }

    @Override
    public String toString() {
        return "CustomAuthenticationToken{" +
                "appId='" + appId + '\'' +
                "} " + super.toString();
    }
}
