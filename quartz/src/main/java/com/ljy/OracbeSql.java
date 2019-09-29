package com.ljy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.select.Elements;

import com.ljy.crawler.page.Page;
import com.ljy.crawler.page.PageParserTool;

public class OracbeSql {
	
	public static void main(String[] args) {
		String sql = "select c.transno||','||a.orderformno||','||decode(a.ordertype,1,'货物贸易',2,'留学',3,'机票',4,'酒店',5,'话费充值服务',6,'旅游',7,'运输',8,'软件服务',9,'国际展览')||','||decode(a.transsubtype, 1, '服装', 2, '视频', 3, '电子', 4, '其他')||','||'其他'||','||d.currencyname||','||a.rmbamount||','||a.foreignamount||','||decode(a.REFUNDMENTSTATUS, 1, '退费', 0, '正交易')  || ',' ||e.supplierno||','||e.suppliername||','||'原路原币种'||','||a.payname||','||a.payercertificateno||','||a.postscript||','||a.note  from cbe_orderform            a, cbe_suppliermerchantacct b, cbe_paymentinstruction   c, cbe_currencysetting      d, cbe_suppliersetting      e where a.buyfeinstractionid = b.buyfeinstractionid and b.paymentinstractionid = c.id and a.currencyid = d.id and a.supplierid = e.id and e.supplierno = '50468' and to_char(c.bankdate,'yyyymmdd') = '20190530'";
		System.out.println("执行sql = " + sql);
		Page page = getContext(sql);
		System.out.println("获取响应数据成功");
		String contect = "";
		List<String> contectList = new ArrayList<String>();
		List<String> contectList2 = new ArrayList<String>();
		//对page进行处理： 访问DOM的某个标签
        Elements es = PageParserTool.select(page, "table");
        if(!es.isEmpty()){
            contect = es.toString();
        }
        String head = "共查出 " + PageParserTool.select(page,"span").text() + " 条数据";
        System.out.println(head);
        contectList2.add(head);
        String[] contects = contect.split("=================================================================================");
        for (String string : contects) {
        	contectList.add(string);
		}
        for (String string : contectList) {
        	String pattern = "\\s第\\s*[0-9]*\\s*条数据\\s";
        	string = string.replace("-----", "").replace("&gt;", ">");
        	string = string.replace("<table border=\"1\" width=\"100%\" align=\"center\">", "").replace("</table>", "").replace("  共查询出了：（） 条数据", "");
        	contectList2.add(string.replaceAll(pattern, ""));
		}
		try {
			String fileUrl = "D:/customs/0530sql.txt";
			writeFileContext(contectList2, fileUrl);
			System.out.println("数据保存完毕，保存地址：" + fileUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Page getContext(String sql){
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("sql", "");
		reqMap.put("sql2", sql);
		return sendPostRequstAndGetResponse("http://172.16.31.151:7799/CBE_Service/UMP/UMPTestController.web", reqMap);
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
        
        // postMethod.addRequestHeader("", "");
        
        postMethod.addRequestHeader("accept", "text/html, application/xhtml+xml, */*"); 
        postMethod.addRequestHeader("connection", "Keep-Alive"); 
        postMethod.addRequestHeader("Accept-Language", "zh-CN"); 
        postMethod.addRequestHeader("Accept-Encoding", "gzip, deflate"); 
        	
        postMethod.addRequestHeader("Content-type","application/x-www-form-urlencoded; charset=UTF-8");
//        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
//        postMethod.addRequestHeader("charset", "UTF-8"); 
        postMethod.addRequestHeader("Cookie", "USERID=44035670; MODULEID=0; MENUSTRING=%3Cdiv+id%3D%22qm0%22+class%3D%22qmmc%22%3E%3Ca+href%3D%22javascript%3Avoid%280%29%22+id%3D%22TopM4_100%22%3E%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2%3C%2Fa%3E%3Cdiv+id%3D%22TopM4_100_div%22%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Finformationquery%2FsupplierGatherQuery.jsp%27%2C%27SubM4_100_200%27%29%22+id%3D%22SubM4_100_200%22%3E%E5%95%86%E6%88%B7%E6%B1%87%E6%80%BB%E6%9F%A5%E8%AF%A2%3C%2Fa%3E%3Ca+href%3D%22javascript%3Avoid%280%29%22+id%3D%22SubM4_100_600%22%3E%E8%B5%84%E9%87%91%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2%3C%2Fa%3E%3Cdiv+id%3D%22SubM4_100_600_div%22%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Finformationquery%2FqueryCapitalReconciliation.jsp%27%2C%27SubM4_100_600_100%27%29%22+id%3D%22SubM4_100_600_100%22%3E%E8%B5%84%E9%87%91%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2%28%E4%BB%98%E6%B1%87%29%3C%2Fa%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Finformationquery%2FqueryCapitalReconciliationbuy.jsp%27%2C%27SubM4_100_600_200%27%29%22+id%3D%22SubM4_100_600_200%22%3E%E8%B5%84%E9%87%91%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2%28%E8%B4%AD%E6%B1%87%29%3C%2Fa%3E%3C%2Fdiv%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Fumpstatistics%2FqueryDailyIncomeStatistics.jsp%27%2C%27SubM4_100_700%27%29%22+id%3D%22SubM4_100_700%22%3E%E5%88%86%E6%97%A5%E6%94%B6%E5%85%A5%E7%BB%9F%E8%AE%A1%E6%9F%A5%E8%AF%A2%3C%2Fa%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Fumpstatistics%2FqueryChannelStatistics.jsp%27%2C%27SubM4_100_800%27%29%22+id%3D%22SubM4_100_800%22%3E%E9%80%9A%E9%81%93%E7%BB%9F%E8%AE%A1%E6%9F%A5%E8%AF%A2%3C%2Fa%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Fumpstatistics%2FqueryMerchant.jsp%27%2C%27SubM4_100_810%27%29%22+id%3D%22SubM4_100_810%22%3E%E5%95%86%E6%88%B7%E5%BA%94%E4%BB%98%E5%AE%9E%E4%BB%98%E6%9F%A5%E8%AF%A2%3C%2Fa%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Fumpstatistics%2FsupplierCountQuery.jsp%27%2C%27SubM4_100_840%27%29%22+id%3D%22SubM4_100_840%22%3E%E5%95%86%E6%88%B7%E7%BB%9F%E8%AE%A1%E6%9F%A5%E8%AF%A2%3C%2Fa%3E%3Ca+href%3D%22javascript%3Avoid%280%29%22+id%3D%22SubM4_100_900%22%3E%E6%94%B6%E5%85%A5%E6%88%90%E6%9C%AC%E6%9F%A5%E8%AF%A2%3C%2Fa%3E%3Cdiv+id%3D%22SubM4_100_900_div%22%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Fumpstatistics%2FincomeCostQueryPay.jsp%27%2C%27SubM4_100_900_100%27%29%22+id%3D%22SubM4_100_900_100%22%3E%E6%94%B6%E5%85%A5%E6%88%90%E6%9C%AC%E6%9F%A5%E8%AF%A2%EF%BC%88%E4%BB%98%E6%B1%87%EF%BC%89%3C%2Fa%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Fumpstatistics%2FincomeCostQuery.jsp%27%2C%27SubM4_100_900_200%27%29%22+id%3D%22SubM4_100_900_200%22%3E%E6%94%B6%E5%85%A5%E6%88%90%E6%9C%AC%E6%9F%A5%E8%AF%A2%EF%BC%88%E8%B4%AD%E6%B1%87%EF%BC%89%3C%2Fa%3E%3C%2Fdiv%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Finformationquery%2FcustomDetailQuery.jsp%27%2C%27SubM4_100_920%27%29%22+id%3D%22SubM4_100_920%22%3E%E6%B5%B7%E5%85%B3%E6%8A%A5%E9%80%81%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2%3C%2Fa%3E%3Ca+href%3D%22javascript%3Anavigation%28%27%2FCBE_Service%2Fcbeservice%2Finformationquery%2FMerchantSettlementQuery.jsp%27%2C%27SubM4_100_950%27%29%22+id%3D%22SubM4_100_950%22%3E%E6%94%B6%E9%93%B6%E5%8F%B0%E5%95%86%E6%88%B7%E7%BB%93%E7%AE%97%E6%9F%A5%E8%AF%A2%3C%2Fa%3E%3C%2Fdiv%3E%3Cspan+class%3D%22qmclear%22%3E%26nbsp%3B%3C%2Fspan%3E%3C%2Fdiv%3E; MODULESTRING=%E5%9F%BA%E7%A1%80%E8%AE%BE%E7%BD%AE%3B%3B%E4%B8%9A%E5%8A%A1%E5%A4%84%E7%90%86%3B%3B%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2%3A%3A2%3B%3B3%3B%3B4%3A%3AuserMenu.web%3FmoduleId%3D2%3B%3BuserMenu.web%3FmoduleId%3D3%3B%3BuserMenu.web%3FmoduleId%3D4; SYSAGENCYNAME=%E8%81%94%E5%8A%A8%E4%BC%98%E5%8A%BF%E7%94%B5%E5%AD%90%E5%95%86%E5%8A%A1%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8; USERNAME=%E6%9D%8E%E9%9B%AA%E6%95%8F; JSESSIONID=23AB10BCE3FB76032B92F624DBB8DED0"); 
        
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
    /**
	 * 将list按行写入到txt文件中
	 * @param strings
	 * @param path
	 * @throws Exception
	 */
	public static void writeFileContext(List<String>  strings, String path) throws Exception {
		File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (String l:strings){
            writer.write(l + "\r\n");
        }
        writer.close();
    }
}
