package demo.oauth2;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // nothing need to do here, token endpoint is public to anonymous user by default.
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Throw StackOverflowError when calling '/oauth/token' if 'super.configure(auth);' is not blocked
        // StackOverflowError: null
        // {
        //     "error": "server_error",
        //     "error_description": "Internal Server Error"
        // }
        // super.configure(auth);

        auth.eraseCredentials(true)
                .inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser("myUsername")
                .password(passwordEncoder.encode("myPassword"))
                .roles("USER");
    }
}