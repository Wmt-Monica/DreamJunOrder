package com.dreamplume.sell.util;

import com.dreamplume.sell.configure.PictureConfig;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname ImgtuUtil
 * @Description TODO
 * @Date 2022/5/14 15:38
 * @Created by 翊
 */
public class ImgtuUtil {
    static final String IMGTU_USER_NAME = "monica0501";

    static final String IMGTU_PASSWORD = "mengmengmeng0501";

    static final Logger log = LogManager.getLogger(ImgtuUtil.class);

    static final private String IMGTU_INIT_URL = "https://imgtu.com/init";

    static final private String IMGTU_LOGIN_URL = "https://imgtu.com/login";

    static final private String IMGTU_OPERATE_URL = "https://imgtu.com/json";

    static final private Pattern SESSION_ID_PATTERN = Pattern.compile("PHPSESSID=([^;]*); path=/; HttpOnly");

    static final private Pattern AUTH_TOKEN_PATTERN = Pattern.compile("PF\\.obj\\.config\\.auth_token = \"([0-9a-f]{40})\";");

    static final private Pattern KEEP_LOGIN_PATTERN = Pattern.compile("KEEP_LOGIN=([^;]*);");

    static final private long INIT_VALID_DURATION = 15L * 60 * 1000;

    static final private long LOGIN_VALID_DURATION = 30L * 24 * 60 * 60 * 1000;

    @Getter
    static private String sessionId;

    @Getter
    static private String authToken;

    @Getter
    static private String keepLogin;

    @Getter
    static private long initTimestamp = 0;

    @Getter
    static private long loginTimestamp = 0;

    public static Boolean initSession() {
        return initSession(true);
    }

    public static Boolean initSession(boolean forceAction) {
        if (!forceAction && !isSessionIdExpired()) {
            log.info("【初始化】成功：会话有效期内，无需重新初始化。");
            return null;
        }
        synchronized (ImgtuUtil.class) {
            // 初始化
            sessionId = null;
            authToken = null;

            // 请求登录页
            String httpRawString;
            CloseableHttpResponse httpResponse;
            try {
                httpResponse = HttpUtil.get(IMGTU_INIT_URL, new HashMap<>(0), new HashMap<>(0));
                HttpEntity httpEntity = httpResponse.getEntity();
                httpRawString = EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                log.error("【初始化】失败：请求页面失败。（{}）", e.getLocalizedMessage());
                e.printStackTrace();
                return false;
            }

            // 获取SessionId
            Header[] responseHeaders = httpResponse.getAllHeaders();
            for (Header header : responseHeaders) {
                if ("Set-Cookie".equalsIgnoreCase(header.getName())) {
                    String cookies = header.getValue();
                    Matcher matcher = SESSION_ID_PATTERN.matcher(cookies);
                    if (matcher.find(0)) {
                        sessionId = matcher.group(1);
                    }
                }
            }

            if (sessionId == null) {
                log.error("【初始化】失败：获取SessionId失败。");
                return false;
            }

            // 获取AuthToken
            Matcher matcher = AUTH_TOKEN_PATTERN.matcher(httpRawString);
            if (matcher.find(0)) {
                authToken = matcher.group(1);
            } else {
                log.error("【初始化】失败：获取AuthToken失败。");
                return false;
            }

            log.info("【初始化】√ SessionId:" + sessionId);
            log.info("【初始化】√ AuthToken:" + authToken);

            initTimestamp = System.currentTimeMillis();
            return true;
        }
    }

    public static Boolean login() throws IOException {
        return login(false);
    }

    public static Boolean login(boolean forceAction) throws IOException {
        if (!forceAction && !isLoginExpired()) {
            log.info("【登录】成功：登录状态有效期内，无需重新登录。");
            return null;
        }
        synchronized (ImgtuUtil.class) {
            // 初始化会话
            if (isSessionIdExpired()) {
                Boolean b = initSession();
                if (!(b == null || b)) {
                    log.error("【登录】失败：初始化会话受阻。");
                }
            }

            // 设置请求头
            Map<String, String> headers = new HashMap<>(3);
            headers.put("cookie", "PHPSESSID=" + sessionId + ";");
            headers.put("content-type", "application/x-www-form-urlencoded");
            headers.put("connection", "keep-alive");

            CloseableHttpResponse httpResponse = HttpUtil.post(IMGTU_LOGIN_URL, new HashMap<>(0), headers, "login-subject=" + IMGTU_USER_NAME + "&password=" + IMGTU_PASSWORD + "&auth_token=" + authToken);

            Header[] responseHeaders = httpResponse.getAllHeaders();
            for (Header header : responseHeaders) {
                if ("Set-Cookie".equalsIgnoreCase(header.getName())) {
                    String cookies = header.getValue();
                    Matcher matcher = KEEP_LOGIN_PATTERN.matcher(cookies);
                    if (matcher.find(0)) {
                        keepLogin = matcher.group(1);
                    }
                }
            }

            if (keepLogin != null) {
                loginTimestamp = System.currentTimeMillis();
                log.info("【登录】√ KeepLogin:" + keepLogin);
                return true;
            } else {
                log.error("【登录】× StatusCode:" + httpResponse.getStatusLine().getStatusCode());
                return false;
            }
        }
    }

    public static boolean ensureLogin() throws IOException {
        Boolean loginRes = login();
        if (loginRes == null) {
            Boolean initRes = initSession();
            return initRes == null || initRes;
        } else {
            return loginRes;
        }
    }

    public static Map<String, String> upload(byte[] bytes, String fileName, ContentType fileType) {
        try {
            log.info("-------->>>> 图床·上传 <<<<--------");
            if (!ensureLogin()) {
                log.error("【上传】失败：服务不可用。");
                return null;
            }

            Map<String, String> headers = new HashMap<>(3);
            headers.put("Cookie", "PHPSESSID=" + sessionId + "; KEEP_LOGIN=" + keepLogin);
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36");
            headers.put("Connection", "keep-alive");

            Map<String, ContentBody> params = new HashMap<>(6);
            params.put("source", new ByteArrayBody(bytes, fileType, fileName));
            params.put("type", new StringBody("file", ContentType.MULTIPART_FORM_DATA));
            params.put("action", new StringBody("upload", ContentType.MULTIPART_FORM_DATA));
            params.put("timestamp", new StringBody(Long.toString(System.currentTimeMillis()), ContentType.MULTIPART_FORM_DATA));
            params.put("auth_token", new StringBody(authToken, ContentType.MULTIPART_FORM_DATA));
            params.put("nsfw", new StringBody("0", ContentType.MULTIPART_FORM_DATA));

            CloseableHttpResponse httpResponse = HttpUtil.multipart(IMGTU_OPERATE_URL, new HashMap<>(0), headers, params);
            String httpRawString = EntityUtils.toString(httpResponse.getEntity());
            log.info("【上传】成功：上传成功！");
            log.info("test={}",new Gson().fromJson(httpRawString, JsonObject.class));
            JsonObject image = new Gson().fromJson(new Gson().fromJson(httpRawString, JsonObject.class).get("image"), JsonObject.class);
            Map<String, String> map = new HashMap<>();
            String imageUrl = image.get("display_url").toString();
            String imageName = image.get("name").toString();
            imageUrl = imageUrl.substring(1, imageUrl.length() - 1);
            imageName = imageName.substring(1, imageName.length() - 1);
            map.put(PictureConfig.PICTURE_URL, imageUrl);
            map.put(PictureConfig.PICTURE_FILE_NAME, imageName);
            return map;
        } catch (IOException e) {
            log.error("【上传】失败：{}", e.getLocalizedMessage());
            throw new SellException(SellErrorCode.PICTURE_UP_LOAD_FAIL);
        } catch (Exception e) {
            log.error("受图层限制，上传图片频率受限");
            throw new SellException(SellErrorCode.PICTURE_UPLOAD_TIMES_LIMIT);
        }
    }

    public static JsonObject delete(String deleteId) {
        try {

            log.info("-------->>>> 图床·删除 <<<<--------");
            if (!ensureLogin()) {
                log.error("【删除】失败：服务不可用。");
                return null;
            }
            Map<String, String> headers = new HashMap<>(3);
            headers.put("Cookie", "PHPSESSID=" + sessionId + "; KEEP_LOGIN=" + keepLogin);
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36");
            headers.put("Connection", "keep-alive");

            Map<String, ContentBody> params = new HashMap<>(5);
            params.put("auth_token", new StringBody(authToken, ContentType.MULTIPART_FORM_DATA));
            params.put("action", new StringBody("delete", ContentType.MULTIPART_FORM_DATA));
            params.put("single", new StringBody("true", ContentType.MULTIPART_FORM_DATA));
            params.put("delete", new StringBody("image", ContentType.MULTIPART_FORM_DATA));
            params.put("deleting[id]", new StringBody(deleteId, ContentType.MULTIPART_FORM_DATA));

            CloseableHttpResponse httpResponse = HttpUtil.multipart(IMGTU_OPERATE_URL, new HashMap<>(0), headers, params);
            String httpRawString = EntityUtils.toString(httpResponse.getEntity());
            log.info("【删除】成功：删除成功！");
            return new Gson().fromJson(httpRawString, JsonObject.class);
        } catch (IOException e) {
            log.error("【删除】失败：{}", e.getLocalizedMessage());
            throw new SellException(SellErrorCode.PICTURE_DELETE_FAIL);
        }
    }

    static boolean isSessionIdExpired() {
        return initTimestamp + INIT_VALID_DURATION < System.currentTimeMillis();
    }

    static boolean isLoginExpired() {
        return loginTimestamp + LOGIN_VALID_DURATION < System.currentTimeMillis();
    }
}
