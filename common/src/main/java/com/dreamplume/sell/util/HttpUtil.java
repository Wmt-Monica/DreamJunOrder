package com.dreamplume.sell.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.util.Map;

/**
 * @Classname HttpUtil
 * @Description TODO
 * @Date 2022/5/14 15:55
 * @Created by 翊
 */
public class HttpUtil {
    private static Header[] parseHeaders(Map<String, String> headers) {
        Header[] headersArray = new Header[headers.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headersArray[i] = new BasicHeader(entry.getKey(), entry.getValue());
            i++;
        }
        return headersArray;
    }

    private static String parseParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sb.toString();
    }

    /**
     * 发送Get请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果，若请求失败则返回null。
     */
    public static CloseableHttpResponse get(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        CloseableHttpClient httpClient;

        //通过默认配置创建一个httpClient实例
        httpClient = HttpClients.createDefault();

        //创建httpGet远程连接实例
        String param = parseParams(params);
        if (param != null) {
            url = url + "?" + param;
        }
        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeaders(parseHeaders(headers));

        //配置请求参数
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000)
                .setConnectionRequestTimeout(30 * 1000)
                .setSocketTimeout(60 * 1000)
                .build();
        httpGet.setConfig(requestConfig);

        //执行get请求得到返回对象
        return httpClient.execute(httpGet);
    }

    /**
     * 发送Post请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @param entity  请求实体
     * @return 响应结果
     */
    public static CloseableHttpResponse post(String url, Map<String, String> params, Map<String, String> headers, HttpEntity entity) throws IOException {
        //创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建http请求
        String param = parseParams(params);
        if (param != null) {
            url = url + "?" + param;
        }
        HttpPost httpPost = new HttpPost(url);

        //设置请求头
        httpPost.setHeaders(parseHeaders(headers));

        //配置请求参数
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000)
                .setConnectionRequestTimeout(30 * 1000)
                .setSocketTimeout(60 * 1000)
                .build();
        httpPost.setConfig(requestConfig);

        //设置请求内容
        httpPost.setEntity(entity);

        //执行get请求得到返回对象
        return httpClient.execute(httpPost);
    }

    /**
     * 发送Post请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @param data    请求数据
     * @return 响应结果
     */
    public static CloseableHttpResponse post(String url, Map<String, String> params, Map<String, String> headers, String data) throws IOException {
        return post(url, params, headers, new StringEntity(data));
    }

    /**
     * 发送Multipart请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @param data    请求数据
     * @return 响应结果
     */
    public static CloseableHttpResponse multipart(String url, Map<String, String> params, Map<String, String> headers, Map<String, ContentBody> data) throws IOException {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for (Map.Entry<String, ContentBody> dataPair : data.entrySet()) {
            multipartEntityBuilder.addPart(dataPair.getKey(), dataPair.getValue());
        }
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        HttpEntity reqEntity = multipartEntityBuilder.build();

        return post(url, params, headers, reqEntity);
    }
}
