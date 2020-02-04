package matrix.module.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author WangCheng
 * @date 2020/2/4
 */
@Controller
@RequestMapping("/ftl")
public class FtlController {

    @GetMapping("/demo.html")
    public String demo() {
        return "demo";
    }
}
