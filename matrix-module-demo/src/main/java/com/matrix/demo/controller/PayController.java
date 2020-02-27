package com.matrix.demo.controller;

import matrix.module.common.bean.Result;
import matrix.module.common.utils.RandomUtil;
import matrix.module.pay.enums.PayMode;
import matrix.module.pay.templates.AlipayTemplate;
import matrix.module.pay.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author wangcheng
 * @date 2020-02-27
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private AlipayTemplate alipayTemplate;

    @GetMapping("/alipay")
    public Result alipay() {
        PayVo payVo = new PayVo()
                .setOrderId(RandomUtil.getUUID())
                .setDesc("aaaa")
                .setTitle("bbbb")
                .setPrice(new BigDecimal(10.6));
        return Result.success(alipayTemplate.doPay(PayMode.PC, payVo));
    }

}
