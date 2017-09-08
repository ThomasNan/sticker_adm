package com.xjy.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final int  TIME_OUT = 1000 * 20;

    private static final int  TIME_OUT_DOWNLOAD = 1000 * 60 * 5;

    private CloseableHttpClient httpClient;

    public HttpClientUtil(){
        httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
    }

    //GET请求
    public  String get(String url) {

        Map<Object, Object> map = null;
        HttpGet getMethod = new HttpGet(url);

        // 设置请求与数据处理的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT).build();
        getMethod.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        String result = "";
        try {

            response = httpClient.execute(getMethod);

            HttpEntity httpentity = response.getEntity();
            if (httpentity != null) {
                result = EntityUtils.toString(httpentity, "UTF-8");
                /*ObjectMapper mapper = new ObjectMapper();
                map = mapper.readValue(, Map.class);*/
            }

        }catch (IOException e){
            logger.error(e.getMessage(), e);
        }finally {
            try {
                if(response != null){
                    response.close();
                }
               /* httpClient.close();
                getMethod.releaseConnection();*/
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                logger.warn("httpClient.close失败");
            }
        }

        return  result;
    }
    //post请求
    public String post(String url, Map<String, String> params) {

        HttpPost httppost = new HttpPost(url);

        // 设置请求与数据处理的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT).build();
        httppost.setConfig(requestConfig);

        //放入请求参数
        List<NameValuePair> list = new ArrayList<>();
        Iterator<?> iterator = params.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
            list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
        }

        CloseableHttpResponse response = null;
        String result = null;

        try {

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
            httppost.setEntity(entity);

            response = httpClient.execute(httppost);
            HttpEntity httpentity = response.getEntity();

            if (httpentity != null) {
                result = EntityUtils.toString(httpentity, "UTF-8");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            try {
                if(response != null){
                    response.close();
                }
                //httpClient.close();
                //httppost.releaseConnection();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                logger.warn("httpClient.close失败");
            }
        }

        return result;
    }

    public String postRequest(String url, String Str){

        HttpPost httpPost = new HttpPost(url);

        // 设置请求与数据处理的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT).build();
        httpPost.setConfig(requestConfig);

        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setEntity(new StringEntity(Str, "UTF-8"));

        CloseableHttpResponse response = null;
        String result = null;

        try{

            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if(entity !=null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }finally {
            try {
                if(response != null){
                    response.close();
                }
                //httpClient.close();
                //httpPost.releaseConnection();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                logger.warn("httpClient.close失败");
            }
        }

        return result;
    }

    // 下载远程文件到指定目录
    public int downloadRemoteFile(String urlStr,String path) {

        HttpGet getReq = new HttpGet(urlStr);

        // 设置请求与数据处理的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT_DOWNLOAD).setConnectTimeout(TIME_OUT_DOWNLOAD).setConnectionRequestTimeout(TIME_OUT_DOWNLOAD).build();
        getReq.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        int result = 0;

        try {

            response = httpClient.execute(getReq);

            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                response.close();
                //httpClient.close();
                return 0;
            }

            Header contentHeader = response.getFirstHeader("Content-Disposition");
            if(contentHeader==null || contentHeader.getValue()==null){
                response.close();
                //httpClient.close();
                return 0;
            }

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                response.close();
                //httpClient.close();
                return 0;
            }

            //获取文件名称
            String contentDes = contentHeader.getValue();
            String fileName =contentDes.substring(contentDes.indexOf("filename") + 10, contentDes.length() - 1);
            if(fileName == null){
                response.close();
                //httpClient.close();
                return 0;
            }

            //拷贝文件到指定目录
            byte[] apply_file_doc = EntityUtils.toByteArray(entity);
            FileCopyUtils.copy(apply_file_doc, new File(path + fileName));

            //关闭资源
            response.close();
            //httpClient.close();

            return 1;

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }finally {
            try {
                if(response != null){
                    response.close();
                }
                //httpClient.close();
                //getReq.releaseConnection();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                logger.warn("httpClient.close失败");
            }
        }

        return result;
    }



}
