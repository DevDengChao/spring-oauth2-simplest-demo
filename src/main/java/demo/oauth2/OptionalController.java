package demo.oauth2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * An optional controller used to test token
 *
 * @author <a href="mailto:qq2325690622@gmail.com">邓超</a> on 2018/10/9
 */
@RestController
@RequestMapping
public class OptionalController {

    @GetMapping
    public String root() {
        return "Hello world!~";
    }
}
