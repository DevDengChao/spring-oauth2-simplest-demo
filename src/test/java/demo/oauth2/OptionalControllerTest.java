package demo.oauth2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author <a href="mailto:qq2325690622@gmail.com">邓超</a> on 2018/10/9
 */
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class OptionalControllerTest {

    @Autowired
    protected WebApplicationContext wac;
    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Password grant type
     *
     * @see TokenEndpoint#postAccessToken(Principal, Map)
     * @see TokenEndpoint#getClientId(Principal)
     */
    @Test
    public void password() throws Exception {
        // Throw InsufficientAuthenticationException if the authorities is not set.
        // InsufficientAuthenticationException: The client is not authenticated.
        // See TokenEndpoint.getClientId(TokenEndpoint.java:148)
        List<GrantedAuthority> authorities = Collections.emptyList();
        Principal principal = new UsernamePasswordAuthenticationToken("client", "secret", authorities);

        MockHttpServletRequestBuilder requestBuilder = post("/oauth/token")
                .principal(principal)
                .param("username", "myUsername")
                .param("password", "myPassword")
                .param("grant_type", "password");

        JsonPathResultMatchers jsonPath = jsonPath("$.access_token");

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath.exists())
                .andExpect(jsonPath.isString())
                .andExpect(jsonPath.isNotEmpty())
                .andDo(print());
        // MockHttpServletRequest:
        // HTTP Method = POST
        // Request URI = /oauth/token
        // Parameters = {username=[myUsername], password=[myPassword], grant_type=[password]}
        // Headers = {}
        // Body = <no character encoding set>
        // Session Attrs = {}
        //
        // Handler:
        // Type = TokenEndpoint
        // Method = public ResponseEntity<OAuth2AccessToken> TokenEndpoint.postAccessToken(Principal,Map<String, String>) throws HttpRequestMethodNotSupportedException
        //
        // Async:
        // Async started = false
        // Async result = null
        //
        // Resolved Exception:
        // Type = null
        //
        // ModelAndView:
        // View name = null
        // View = null
        // Model = null
        //
        // FlashMap:
        // Attributes = null
        //
        // MockHttpServletResponse:
        // Status = 200
        // Error message = null
        // Headers = {Cache-Control=[no-store], Pragma=[no-cache], Content-Type=[application/json;charset=UTF-8]}
        // Content type = application/json;charset=UTF-8
        // Body = {"access_token":"d714961f-f4a4-4e1d-b947-9357ad8cb2e7","token_type":"bearer","expires_in":43199,"scope":"all"}
        // Forwarded URL = null
        // Redirected URL = null
        // Cookies = []

        // Request received for POST '/oauth/token?username=myUsername&password=myPassword&grant_type=password':
        //
        // org.apache.catalina.connector.RequestFacade
        //
        // servletPath:/oauth/token
        // pathInfo:null
        // headers:
        // cache-control: no-cache
        // postman-token: f9c6723a-2c78-457a-b49d-1efbc0722c7f
        // authorization: Basic Y2xpZW50OnNlY3JldA==
        // user-agent: PostmanRuntime/7.3.0
        // accept: */*
        // host: localhost
        // cookie: JSESSIONID=B36E5B7696621177A14C41CCBC94FD8D
        // accept-encoding: gzip, deflate
        // content-length: 0
        // connection: keep-alive
        //
        // Security filter chain: [
        //   WebAsyncManagerIntegrationFilter
        //   SecurityContextPersistenceFilter
        //   HeaderWriterFilter
        //   LogoutFilter
        //   BasicAuthenticationFilter
        //   RequestCacheAwareFilter
        //   SecurityContextHolderAwareRequestFilter
        //   AnonymousAuthenticationFilter
        //   SessionManagementFilter
        //   ExceptionTranslationFilter
        //   FilterSecurityInterceptor
        // ]
    }
}