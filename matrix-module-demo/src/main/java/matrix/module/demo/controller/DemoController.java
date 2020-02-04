package matrix.module.demo.controller;

import matrix.module.common.bean.Result;
import matrix.module.common.exception.ServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangCheng
 * @date 2020/2/4
 */
@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/demo")
    public Result demo() {
        throw new ServiceException("aaaa");
    }
}
