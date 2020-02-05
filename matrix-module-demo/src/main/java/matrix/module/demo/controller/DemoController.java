package matrix.module.demo.controller;

import matrix.module.common.bean.Result;
import matrix.module.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/jdbc")
    public Result jdbc() {
        jdbcTemplate.execute("insert into test values (123)");
        return Result.success(true);
    }

    @GetMapping("/demo")
    public Result demo() {
        throw new ServiceException("aaaa");
    }
}
