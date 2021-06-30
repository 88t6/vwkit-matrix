package com.matrix.demo.controller;

import matrix.module.common.bean.Result;
import matrix.module.common.utils.RandomUtil;
import matrix.module.pay.enums.PayMode;
import matrix.module.pay.templates.AlipayTemplate;
import matrix.module.pay.templates.WepayTemplate;
import matrix.module.pay.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author wangcheng
 */
@RestController
@RequestMapping("/pay")
public class PayController {

//    @Autowired
//    private AlipayTemplate alipayTemplate;
//
//    @Autowired
//    private WepayTemplate wepayTemplate;
//
//    @GetMapping("/alipay")
//    public Result alipay() {
//        PayVo payVo = new PayVo()
//                .setOrderId(RandomUtil.getUUID())
//                .setDesc("测试商品")
//                .setTitle("测试商品")
//                .setPrice(new BigDecimal(0.01));
//        return Result.success(alipayTemplate.doPay(PayMode.QrCode, payVo));
////        return Result.success(alipayTemplate.doQueryPayByOrderId("6ea2c6ad-93fe-46fc-bc3f-816f3efd8635"));
////        RefundVo refundVo = new RefundVo()
////                .setOutTradeNo("2020022722001470471002638070")
////                .setRefundAmount(new BigDecimal(0.01));
////        alipayTemplate.doRefund(refundVo);
////        return Result.success(null);
////        alipayTemplate.doQueryRefundByPayId("6D016E32E59E15702E250E79A4781564");
////        return Result.success(null);
//    }
//
//    @GetMapping("/wepay")
//    public Result wepay() {
//        PayVo payVo = new PayVo()
//                .setOrderId(RandomUtil.getUUID())
//                .setDesc("测试商品")
//                .setTitle("测试商品")
//                .setPrice(new BigDecimal(0.01));
//        return Result.success(wepayTemplate.doPay(PayMode.WEJSAPI, payVo));
////        return Result.success(wepayTemplate.doQueryPayByPayId("6742045E0C12823575037C67FFBC8E17"));
////        RefundVo refundVo = new RefundVo()
////                .setPayId("6742045E0C12823575037C67FFBC8E17")
////                .setRefundAmount(new BigDecimal(0.01));
////        wepayTemplate.doRefund(refundVo);
////        return Result.success(null);
////        wepayTemplate.doQueryRefundByPayId("6742045E0C12823575037C67FFBC8E17");
////        return Result.success(null);
//    }

}
