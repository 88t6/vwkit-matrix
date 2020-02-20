package matrix.module.pay.controller;

import matrix.module.based.utils.WebUtil;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.enums.PayNotify;
import matrix.module.pay.templates.AbstractTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangcheng
 * @date 2019/4/26
 */
@RestController
public class PayNotifyController {

    @Value("${pay.notify-domain}")
    private String notifyDomain;

    @RequestMapping(value = PayConstant.NOTIFY_PAY_URL_PREFIX + "{id}", method = RequestMethod.POST)
    public String payNotify(@PathVariable(value = "id") String id,
                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        AbstractTemplate template = getBeanById(id, request, response);
        assert template != null;
        return template.parsePayNotify(request);
    }

    @RequestMapping(value = PayConstant.NOTIFY_RETURN_URL_PREFIX + "{id}", method = RequestMethod.POST)
    public String refundNotify(@PathVariable(value = "id") String id,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        AbstractTemplate template = getBeanById(id, request, response);
        assert template != null;
        return template.parseRefundNotify(request);
    }

    private AbstractTemplate getBeanById(String id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String beanName = PayNotify.getBeanNameById(id);
        if (StringUtils.isEmpty(beanName)) {
            request.getRequestDispatcher("/404.html").forward(request, response);
            return null;
        }
        return WebUtil.getBean(beanName, AbstractTemplate.class);
    }
}
