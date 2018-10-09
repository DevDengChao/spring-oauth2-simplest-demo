package demo.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServeConfig extends AuthorizationServerConfigurerAdapter {
    // Unable to perform authorization if this is not configured
    // Throw Oauth2Exception when calling '/oauth2/token' if this is not configured
    // {
    //     "error": "unauthorized",
    //     "error_description": "Full authentication is required to access this resource"
    // }

}