package com.ljy.crawler.page;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


import java.io.IOException;
import java.util.Map;

public class RequestAndResponseTool {

	/**
	 * 发送get请求并处理得到相应
	 * @param url
	 * @return
	 */
    public static Page  sendRequstAndGetResponse(String url) {
        Page page = null;
        // 1.生成 HttpClinet 对象并设置参数
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY); // 设置浏览器相同的cookie接收策略
        // 设置 HTTP 连接超时 5s
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
        // 2.生成 GetMethod 对象并设置参数
        GetMethod getMethod = new GetMethod(url);
        // 设置 get 请求超时 5s
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
        // 设置请求重试处理
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        // 3.执行 HTTP GET 请求
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            // 判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
            }
            // 4.处理 HTTP 响应内容
            byte[] responseBody = getMethod.getResponseBody();// 读取为字节 数组
            String contentType = getMethod.getResponseHeader("Content-Type").getValue(); // 得到当前返回类型
            page = new Page(responseBody, url, contentType); //封装成为页面
        } catch (HttpException e) {
        	// 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
        	// 发生网络异常
            e.printStackTrace();
        } finally {
        	// 释放连接
            getMethod.releaseConnection();
        }
        return page;
    }
    
    /**
	 * 发送post请求并处理得到相应
	 * @param url
	 * @return
	 */
    public static Page  sendPostRequstAndGetResponse(String url, Map<String, Object> params) {
        Page page = null;
        // 1.生成 HttpClinet 对象并设置参数
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY); // 设置浏览器相同的cookie接收策略
        // 设置 HTTP 连接超时 5s
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
        // 2.生成 PostMethod 对象并设置参数
        PostMethod postMethod = new PostMethod(url);
        for(String key : params.keySet()){
        	postMethod.addParameter( new NameValuePair(key, params.get(key).toString()) );
        }
        // 设置 Post 请求超时 5s
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
        // 设置请求重试处理
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        // 3.执行 HTTP post 请求
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            // 判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + postMethod.getStatusLine());
            }
            // 4.处理 HTTP 响应内容
            byte[] responseBody = postMethod.getResponseBody();// 读取为字节 数组
            
            String contentType = postMethod.getResponseHeader("Content-Type").getValue(); // 得到当前返回类型
            page = new Page(responseBody, url, contentType); //封装成为页面
        } catch (HttpException e) {
        	// 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
        	// 发生网络异常
            e.printStackTrace();
        } finally {
        	// 释放连接
        	postMethod.releaseConnection();
        }
        return page;
    }
}