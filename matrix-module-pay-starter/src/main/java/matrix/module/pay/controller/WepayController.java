package matrix.module.pay.controller;

import matrix.module.common.bean.Result;
import matrix.module.common.exception.ServiceException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangcheng
 * @date 2020-02-27
 */
@RestController
@RequestMapping("/wepay")
public class WepayController {

    @Autowired
    private WxMpService wxMpService;

    @GetMapping("/exchangeOpenId")
    public Result exchangeOpenId(@RequestParam("code") String code) {
        try {
            return Result.success(wxMpService.oauth2getAccessToken(code).getOpenId());
        } catch (Exception e) {
            throw new ServiceException("置换openId失败");
        }
    }
}
