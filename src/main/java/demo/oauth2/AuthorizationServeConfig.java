package demo.oauth2;

import demo.oauth2.custom.CustomAuthenticationProvider;
import demo.oauth2.custom.CustomTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServeConfig extends AuthorizationServerConfigurerAdapter {
    // Unable to perform authorization if this is not configured
    // Throw Oauth2Exception when calling '/oauth2/token' if this is not configured
    // {
    //     "error": "unauthorized",
    //     "error_description": "Full authentication is required to access this resource"
    // }
    @NonNull
    private final PasswordEncoder passwordEncoder;
    @NonNull
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    public AuthorizationServeConfig(@NonNull PasswordEncoder passwordEncoder,
                                    @NonNull CustomAuthenticationProvider customAuthenticationProvider) {
        this.passwordEncoder = passwordEncoder;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    /**
     * Setup password encoder for client password encode/match
     *
     * @see DelegatingPasswordEncoder.UnmappedIdPasswordEncoder#matches(CharSequence, String)
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
        // Throw IllegalArgumentException when calling '/oauth/token' if this is not configured
        // IllegalArgumentException: "There is no PasswordEncoder mapped for the id "null"
        security.passwordEncoder(passwordEncoder);
    }

    /**
     * Setup grant type
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        clients.inMemory()
                .withClient("client")
                .secret(passwordEncoder.encode("secret"))
                .authorizedGrantTypes(CustomTokenGranter.GRANT_TYPE)// enable custom grant type
                // Throw InvalidScopeException when calling '/oauth/token' if 'scope' is not configured
                // InvalidScopeException: Empty scope (either the client or the user is not allowed the requested scopes)
                // {
                //     "error": "invalid_scope",
                //     "error_description": "Empty scope (either the client or the user is not allowed the requested scopes)"
                // }
                .scopes("all");
    }

    /**
     * override endpoint's token granter
     *
     * @see AuthorizationServerEndpointsConfigurer#getDefaultTokenGranters()
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        CustomTokenGranter customTokenGranter = new CustomTokenGranter(customAuthenticationProvider, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
        endpoints
                .tokenGranter(customTokenGranter);
    }
}