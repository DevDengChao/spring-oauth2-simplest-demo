package demo.oauth2.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:qq2325690622@gmail.com">邓超</a> on 2018/10/11
 */
@Component
public class CustomUserDetailsService {
    @NonNull
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(@NonNull PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @NonNull
    public UserDetails findUserByUsernameAndAppId(@NonNull String username, @NonNull String appId) throws UsernameNotFoundException {
        // locate the unique user by username and appId
        boolean find = username.equals("myUsername") && appId.equals("myAppId");

        if (!find) {
            MessageSourceAccessor messageSource = SpringSecurityMessageSource.getAccessor();
            String message = messageSource.getMessage("JdbcDaoImpl.notFound", username + "@" + appId);
            throw new UsernameNotFoundException(message);
        }

        return User.withUsername(username)
                // Throw IllegalArgumentException: There is no PasswordEncoder mapped for the id "null" if passwordEncoder is not configured
                .passwordEncoder(passwordEncoder::encode)// setup password encoder
                // Throw NullPointerException: null if password not present. See UserBuilder.build();
                .password("myPassword")// setup password for later usage
                .roles("USER")
                .disabled(false)// enable the user
                .build();
    }
}
