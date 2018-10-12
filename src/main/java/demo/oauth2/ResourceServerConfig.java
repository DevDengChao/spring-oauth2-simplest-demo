package demo.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    // Throw RuntimeException when accessing resource (even with token) if this is not configured
    // {
    //     "timestamp": "2018-10-09T14:20:22.580+0000",
    //     "status": 401,
    //     "error": "Unauthorized",
    //     "message": "Unauthorized",
    //     "path": "/"
    // }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // expose login page, otherwise user cannot login
        http.requestMatchers()
                // language=RegExp
                .regexMatchers("/(?!login)")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }
}