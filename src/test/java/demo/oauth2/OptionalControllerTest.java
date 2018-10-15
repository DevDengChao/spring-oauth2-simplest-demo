package demo.oauth2;

import org.json.JSONObject;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
     * Custom grant type
     *
     * @see TokenEndpoint#postAccessToken(Principal, Map)
     * @see TokenEndpoint#getClientId(Principal)
     */
    @Test
    public void custom() throws Exception {
        // Throw InsufficientAuthenticationException if the authorities is not set.
        // InsufficientAuthenticationException: The client is not authenticated.
        // See TokenEndpoint.getClientId(TokenEndpoint.java:148)
        List<GrantedAuthority> authorities = Collections.emptyList();
        Principal principal = new UsernamePasswordAuthenticationToken("client", "secret", authorities);

        MockHttpServletRequestBuilder requestBuilder = post("/oauth/token")
                .principal(principal)
                .param("username", "myUsername")
                .param("password", "myPassword")
                .param("appId", "myAppId")
                .param("grant_type", "custom");

        JsonPathResultMatchers jsonPath = jsonPath("$.access_token");

        String content = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath.exists())
                .andExpect(jsonPath.isString())
                .andExpect(jsonPath.isNotEmpty())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = new JSONObject(content).getString("access_token");
        mvc.perform(get("/oauth/check_token")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorities").isArray())
                .andExpect(jsonPath("$.authorities").isNotEmpty())
                .andDo(print());

        // MockHttpServletRequest:
        //       HTTP Method = POST
        //       Request URI = /oauth/token
        //        Parameters = {username=[myUsername], password=[myPassword], appId=[myAppId], grant_type=[custom]}
        //           Headers = {}
        //              Body = <no character encoding set>
        //     Session Attrs = {}
        //
        // Handler:
        //              Type = TokenEndpoint
        //            Method = public ResponseEntity<OAuth2AccessToken> TokenEndpoint.postAccessToken(Principal,Map<String, String>) throws HttpRequestMethodNotSupportedException
        //
        // Async:
        //     Async started = false
        //      Async result = null
        //
        // Resolved Exception:
        //              Type = null
        //
        // ModelAndView:
        //         View name = null
        //              View = null
        //             Model = null
        //
        // FlashMap:
        //        Attributes = null
        //
        // MockHttpServletResponse:
        //            Status = 200
        //     Error message = null
        //           Headers = {Cache-Control=[no-store], Pragma=[no-cache], Content-Type=[application/json;charset=UTF-8]}
        //      Content type = application/json;charset=UTF-8
        //              Body = {"access_token":"dde99d0f-52a4-4d5a-a405-5ce6a71a3a35","token_type":"bearer","expires_in":43199,"scope":"all"}
        //     Forwarded URL = null
        //    Redirected URL = null
        //           Cookies = []

        // MockHttpServletRequest:
        //       HTTP Method = GET
        //       Request URI = /oauth/check_token
        //        Parameters = {token=[65f08a91-4f6c-4617-b1cc-9f15128933cc]}
        //           Headers = {}
        //              Body = <no character encoding set>
        //     Session Attrs = {}
        //
        // Handler:
        //              Type = CheckTokenEndpoint
        //            Method = public Map<String, ?> CheckTokenEndpoint.checkToken(String)
        //
        // Async:
        //     Async started = false
        //      Async result = null
        //
        // Resolved Exception:
        //              Type = null
        //
        // ModelAndView:
        //         View name = null
        //              View = null
        //             Model = null
        //
        // FlashMap:
        //        Attributes = null
        //
        // MockHttpServletResponse:
        //            Status = 200
        //     Error message = null
        //           Headers = {Content-Type=[application/json;charset=UTF-8]}
        //      Content type = application/json;charset=UTF-8
        //              Body = {"active":true,"exp":1539615864,"user_name":"myUsername","authorities":["ROLE_USER"],"client_id":"client","scope":["all"]}
        //     Forwarded URL = null
        //    Redirected URL = null
        //           Cookies = []
    }

}