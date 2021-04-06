package matrix.module.common.utils;

import com.alibaba.fastjson.JSONObject;
import matrix.module.common.bean.CertInfo;
import matrix.module.common.enums.HttpParamEnum;
import matrix.module.common.exception.ServiceException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;

/**
 * http请求工具
 * @author wangcheng
 */
public class HttpClientUtil {

    /**
     * get请求
     * @param url 连接地址
     * @param callBack 回调方法
     * @return T
     */
    public static <T> T sendGet(String url, CallBack<T> callBack) {
        return sendGet(url, null, null, null, callBack);
    }

    /**
     * get请求
     * @param url 连接地址
     * @param params 参数
     * @param callBack 回调方法
     * @return T
     */
	public static <T> T sendGet(String url, Map<String, ?> params, CallBack<T> callBack) {
		return sendGet(url, null, params, null, callBack);
	}

    /**
     * get请求
     * @param url 连接地址
     * @param headers 请求头
     * @param params 参数
     * @param callBack 回调方法
     * @return T
     */
    public static <T> T sendGet(String url, Map<String, String> headers, Map<String, ?> params, CallBack<T> callBack) {
        return sendGet(url, headers, params, null, callBack);
    }

    /**
     * get请求
     * @param url 连接地址
     * @param certInfo 证书（https需要）
     * @param callBack 回调方法
     * @return T
     */
    public static <T> T sendGet(String url, CertInfo certInfo, CallBack<T> callBack) {
        return sendGet(url, null, null, certInfo, callBack);
    }

    /**
     * post请求
     * @param url 连接地址
     * @param body 参数
     * @param paramType 参数类型
     * @param callBack 回调方法
     * @return T
     */
    public static <T> T sendPost(String url, String body, HttpParamEnum paramType, CallBack<T> callBack) {
    	return sendPost(url, null, body, paramType, null, callBack);
    }

    /**
     * post请求
     * @param url 连接地址
     * @param headers 请求头
     * @param body 参数
     * @param paramType 参数类型
     * @param callBack 回调方法
     * @return T
     */
    public static <T> T sendPost(String url, Map<String, String> headers, String body, HttpParamEnum paramType, CallBack<T> callBack) {
        return sendPost(url, headers, body, paramType, null, callBack);
    }

    /**
     * post请求
     * @param url 连接地址
     * @param headers 请求头
     * @param params 参数
     * @param paramType 参数类型
     * @param callBack 回调方法
     * @return T
     */
    public static <T> T sendPost(String url, Map<String, String> headers, Map<String, ?> params, HttpParamEnum paramType, CallBack<T> callBack) {
    	return sendPost(url, headers, params, paramType, null, callBack);
    }

    /**
     * post请求
     * @param url 连接地址
     * @param headers 请求头
     * @param params 参数
     * @param paramType 参数类型
     * @param certInfo 证书（https需要）
     * @param callBack 回调方法
     * @return T
     */
    public static <T> T sendPost(String url, Map<String, String> headers, Map<String, ?> params, HttpParamEnum paramType, CertInfo certInfo, CallBack<T> callBack) {
    	if (HttpParamEnum.JSON.name().equals(paramType.name())) {
    		return sendPost(url, headers, JSONObject.toJSONString(params), paramType, certInfo, callBack);
    	} else if (HttpParamEnum.XML.name().equals(paramType.name())) {
    		throw new ServiceException("please invoke sendPost(String url, String body, HttpParamEnum paramType, CallBack<T> callBack)");
    	} else if (HttpParamEnum.FORM.name().equals(paramType.name())){
    		return sendPost(url, headers, toParams(params, null), paramType, certInfo, callBack);
    	}
    	throw new ServiceException("not support paramType");
    }

    /**
     * 基础get方法
     * @param url 连接地址
     * @param headers 请求头
     * @param params 参数
     * @param certInfo 证书（https需要）
     * @param callBack 回调方法
     * @return T
     */
    public static <T> T sendGet(String url, Map<String, String> headers, Map<String, ?> params, CertInfo certInfo, CallBack<T> callBack) {
        HttpGet httpGet = new HttpGet(toParams(params, url));
        return invoke(httpGet, headers, certInfo, callBack);
    }

    /**
     * 基础post方法
     * @param url 连接地址
     * @param headers 请求头
     * @param body 参数
     * @param paramType 参数类型
     * @param certInfo 证书（https需要）
     * @param callBack 回调方法
     * @return T
     */
    public static <T> T sendPost(String url, Map<String, String> headers, String body, HttpParamEnum paramType, CertInfo certInfo, CallBack<T> callBack) {
    	try {
            HttpPost httpPost = new HttpPost(url);
            if (!StringUtil.isEmpty(body)) {
            	if (HttpParamEnum.JSON.name().equals(paramType.name())) {
            		httpPost.addHeader("Content-type", "application/json; charset=UTF-8");
            	} else if (HttpParamEnum.XML.name().equals(paramType.name())) {
            		httpPost.addHeader("Content-type", "application/xml; charset=UTF-8");
            	}
            	httpPost.setEntity(new StringEntity(body));
            }
            return invoke(httpPost, headers, certInfo, callBack);
    	} catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 调用
     * @param request 请求体
     * @param headers http头
     * @param certInfo 证书
     * @param callBack 调用函数
     * @return T
     */
    private static <T> T invoke(final HttpRequestBase request, final Map<String, String> headers, final CertInfo certInfo, final CallBack<T> callBack) {
        CloseableHttpClient client = null;
        try {
            if (request.getConfig() == null) {
                request.setConfig(RequestConfig.custom()
                        .setSocketTimeout(20000)
                        .setConnectTimeout(10000)
                        .build());
            }
            //加入请求头
            if (headers != null && headers.size() > 0) {
                headers.forEach(request::addHeader);
            }
            if (certInfo != null) {
            	client = HttpClients.custom()
            			.setConnectionManager(getCertConnectionManager(certInfo))
            			.build();
            } else {
            	client = HttpClients.createDefault();
            }
            CloseableHttpResponse response = client.execute(request);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                if (callBack != null) {
                    return callBack.execute(response.getEntity());
                }
            }
            return null;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(client);
        }
    }

    /**
     * map -> a=1&b=1
     */
    private static String toParams(Map<String, ?> params, String url) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder result = new StringBuilder();
        for (String key : params.keySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            String value = String.valueOf(params.get(key));
            result.append(StringUtil.encodeUrl(key));
            if (value != null && !"".equals(value)) {
                result.append("=");
                result.append(StringUtil.encodeUrl(value));
            }
        }
        if (url == null) {
            return result.toString();
        }
        if (url.indexOf("?") <= 0) {
            return url + "?" + result.toString();
        } else {
            return url + "&" + result.toString();
        }
    }

    private static BasicHttpClientConnectionManager getCertConnectionManager(CertInfo certInfo) {
    	try {
    		// 证书
        	char[] password = certInfo.getPassword().toCharArray();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certInfo.getCertStream(), password);
            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);
            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext, new String[]{"TLSv1"},
                    null, new DefaultHostnameVerifier());
            return new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslConnectionSocketFactory)
                            .build(), null, null, null);
    	} catch (Exception e) {
    		throw new ServiceException(e);
    	}
    }

    /**
     * 回调函数
     */
    @FunctionalInterface
    public interface CallBack<T> {
        T execute(HttpEntity entity);
    }
}
