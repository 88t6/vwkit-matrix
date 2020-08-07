package matrix.module.common.utils;

import com.alibaba.fastjson.JSONObject;
import matrix.module.common.exception.ServiceException;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wangcheng
 */
public class CustomHttpUtil {

    public static String accept = "*/*";

    public static String connection = "keep-alive";

    public static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.75 Safari/537.36";

    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static String mapToParams(Map<String, Object> params, String contentType) {
        if (params != null && !params.isEmpty()) {
            if ("application/json".equals(contentType)) {
                return JSONObject.toJSONString(params);
            } else {
                Iterator<String> iterator = params.keySet().iterator();
                StringBuilder result = new StringBuilder();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = String.valueOf(params.get(key));
                    result.append(key).append("=").append(value).append("&");
                }
                return result.toString().length() > 0 ? (result.toString().substring(0, result.toString().length() - 1)) : "";
            }
        }
        return "";
    }

    public static InputStream send(String url, String params, String cookie, boolean isPost, String contentType) {
        URLConnection conn;
        try {
            conn = (new URL(url + (!isPost ? ("?" + params) : ""))).openConnection();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
        conn.setRequestProperty("Connection", connection);
        conn.setRequestProperty("Accept", accept);
        conn.setRequestProperty("User-Agent", userAgent);
        conn.setRequestProperty("Cookie", cookie != null ? cookie : "");
        if (contentType != null && !"".equals(contentType)) {
            conn.setRequestProperty("Content-Type", contentType);
        }
        if (!isPost) {
            try {
                conn.connect();
            } catch (IOException e) {
                throw new ServiceException(e);
            }
        } else {
            conn.setDoOutput(true);
            conn.setDoInput(true);
            try (PrintWriter out = new PrintWriter(conn.getOutputStream())) {
                out.print(params);
                out.flush();
            } catch (Exception e) {
                throw new ServiceException(e);
            }
        }
        try {
            return conn.getInputStream();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    public static InputStream sendSSL(String url, String params, String cookie, boolean isPost, String contentType) {
        SSLContext sc;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
        try {
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()},
                    new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            throw new ServiceException(e);
        }
        HttpsURLConnection conn;
        try {
            conn = (HttpsURLConnection) (new URL(url + (!isPost ? ("?" + params) : ""))).openConnection();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.setRequestProperty("Accept", accept);
        conn.setRequestProperty("Connection", connection);
        conn.setRequestProperty("User-Agent", userAgent);
        conn.setRequestProperty("Cookie", cookie != null ? cookie : "");
        if (contentType != null && !"".equals(contentType)) {
            conn.setRequestProperty("Content-Type", contentType);
        }
        if (!isPost) {
            try {
                conn.connect();
            } catch (IOException e) {
                throw new ServiceException(e);
            }
        } else {
            conn.setDoOutput(true);
            conn.setDoInput(true);
            PrintWriter out = null;
            try {
                try {
                    out = new PrintWriter(conn.getOutputStream());
                } catch (IOException e) {
                    throw new ServiceException(e);
                }
                out.print(params);
                out.flush();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
        try {
            return conn.getInputStream();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }
}
