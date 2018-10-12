package demo.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @NonNull
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(@NonNull PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // nothing need to do here, token endpoint is public to anonymous user by default.
    }

    /**
     * The {@link AuthenticationManagerBuilder#inMemoryAuthentication() inMemoryAuthentication()} setup the default
     * {@link org.springframework.security.core.userdetails.UserDetailsService UserDetailsService} provide by
     * {@link #userDetailsService()} and/or {@link #userDetailsServiceBean()}
     *
     * @see #userDetailsService()
     * @see #userDetailsServiceBean()
     * @see WebSecurityConfigurerAdapter.UserDetailsServiceDelegator
     * @see AuthenticationManagerBuilder#getDefaultUserDetailsService()
     * @see AuthenticationManagerBuilder#inMemoryAuthentication()
     */
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

    /**
     * provide an AuthenticationManager for {@link AuthorizationServeConfig} to enable password mode
     *
     * @see AuthorizationServeConfig#configure(AuthorizationServerEndpointsConfigurer)
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}