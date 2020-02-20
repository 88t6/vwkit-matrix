package matrix.module.pay.sdk;

import com.alibaba.druid.util.StringUtils;
import matrix.module.based.utils.JacksonUtil;
import matrix.module.common.bean.CertInfo;
import matrix.module.common.enums.HttpParamEnum;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.helper.encrypt.MD5;
import matrix.module.common.utils.HttpClientUtil;
import matrix.module.common.utils.RandomUtil;
import matrix.module.common.utils.StreamUtil;
import matrix.module.common.utils.StringUtil;
import matrix.module.pay.builder.request.wepay.*;
import matrix.module.pay.builder.response.wepay.*;
import matrix.module.pay.constants.WepayConstant;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * @author wangcheng
 * @date 2019/4/30
 */
public class WepayClient {

    private static final Logger logger = LogManager.getLogger(WepayClient.class);

    private String appId;

    private String mchId;

    private String key;

    private String certPwd;

    private String certPath;

    public WepayClient(String appId, String mchId, String key, String certPwd, String certPath) {
        this.appId = appId;
        this.mchId = mchId;
        this.key = key;
        this.certPwd = certPwd;
        this.certPath = certPath;
    }

    public WepayPayResponse execute(WepayPayRequest request) {
        String result = send(WepayConstant.PAY_URL, null, request);
        return parseResponse(result, WepayPayResponse.class);
    }

    public WepayRefundResponse execute(WepayRefundRequest request) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(certPath));
            CertInfo certInfo = new CertInfo(certPwd, fis);
            String result = send(WepayConstant.REFUND_URL, certInfo, request);
            return parseResponse(result, WepayRefundResponse.class);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fis);
        }
    }

    public WepayQueryPayResponse execute(WepayQueryPayRequest request) {
        String result = send(WepayConstant.QUERY_PAY_URL, null, request);
        return parseResponse(result, WepayQueryPayResponse.class);
    }

    public WepayQueryRefundResponse execute(WepayQueryRefundRequest request) {
        String result = send(WepayConstant.QUERY_REFUND_URL, null, request);
        return parseResponse(result, WepayQueryRefundResponse.class);
    }

    /**
     * 请求微信支付
     *
     * @param url
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private String send(String url, CertInfo certInfo, WepayBaseRequest request) {
        Assert.isNotNull(url, "url");
        Assert.isNotNull(request, "request");
        request.setAppId(appId).setMchId(mchId).setSignType("MD5")
                .setNonceStr(MD5.get32(RandomUtil.getUUID()))
                .setSign(sign(JacksonUtil.parseJson(JacksonUtil.toJsonString(request), HashMap.class)));
        String params = XML.toString(new JSONObject(JacksonUtil.parseJson(JacksonUtil.toJsonString(request), HashMap.class)), "xml");
        logger.info(String.format("请求url:%s, params:%s", url, params));
        String result = HttpClientUtil.sendPost(url, params, HttpParamEnum.XML, certInfo, new HttpClientUtil.CallBack<String>() {
            @Override
            public String execute(HttpEntity httpEntity) {
                try {
                    return StringUtil.decodeStr(EntityUtils.toString(httpEntity));
                } catch (Exception e) {
                    throw new ServiceException(e);
                }
            }
        });
        logger.info(String.format("返回结果:%s", result));
        return result;
    }

    /**
     * 解析返回参数
     *
     * @param xml
     * @param tClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends WepayBaseResponse> T parseResponse(String xml, Class<T> tClass) {
        JSONObject json = XML.toJSONObject(xml).getJSONObject("xml");
        WepayBaseResponse response = null;
        if (WepayPayResponse.class.getSimpleName().equals(tClass.getSimpleName())) {
            response = JacksonUtil.parseJson(json.toString(), WepayPayResponse.class);
        } else if (WepayRefundResponse.class.getSimpleName().equals(tClass.getSimpleName())) {
            response = WepayRefundResponse.parse(json.toString());
        } else if (WepayQueryPayResponse.class.getSimpleName().equals(tClass.getSimpleName())) {
            response = WepayQueryPayResponse.parse(json.toString());
        } else if (WepayQueryRefundResponse.class.getSimpleName().equals(tClass.getSimpleName())) {
            response = WepayQueryRefundResponse.parse(json.toString());
        }
        Assert.state(response != null, "返回类型暂不支持");
        Assert.state(WepayConstant.RETURN_CODE.equals(response.getReturnCode()), response.getReturnMsg());
        Assert.state(WepayConstant.RETURN_CODE.equals(response.getResultCode()), response.getErrCode() + ":" + response.getErrCodeDes());
        Map<String, Object> params = JacksonUtil.parseJson(json.toString(), HashMap.class);
        params.remove("sign");
        Assert.state(response.getSign().equals(sign(params)), "签名错误");
        return (T) response;
    }

    /**
     * 参数签名
     *
     * @param params
     * @return
     */
    private String sign(Map<String, Object> params) {
        Iterator<String> keyIterator = params.keySet().iterator();
        List<String> keys = new ArrayList<>();
        while (keyIterator.hasNext()) {
            keys.add(keyIterator.next());
        }
        Collections.sort(keys);
        List<String> values = new ArrayList<>();
        for (String key : keys) {
            String value = String.valueOf(params.get(key));
            if (!StringUtils.isEmpty(value)) {
                values.add(key + "=" + value);
            }
        }
        return MD5.get32(String.join("&", values) + "&key=" + key).toUpperCase();
    }
}
