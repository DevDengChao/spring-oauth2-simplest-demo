package demo.oauth2;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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


}