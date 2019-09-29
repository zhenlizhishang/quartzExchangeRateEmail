package com.ljy.crawler.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json工具类
 * @author LiuJiaYu
 */
public class JsonUtil {

	/**
	 * json转为实体类
	 * @param retJsonString json串
	 * @param t 实体类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T  jsonToEntry(String retJsonString, T t){
		String jsonString = retJsonString;
		JSONObject jsonobject = JSON.parseObject(jsonString);
		JSONArray array = jsonobject.getJSONArray("");
		List<T> rules = new ArrayList<T>();
		T result = null;
		if (rules != null && array.get(0) != null) {
			JSONObject object = (JSONObject) array.get(0);
			result = (T) JSONObject.toJavaObject(object, t.getClass());
		}
		return result;
		
		// return new Gson().fromJson(retJsonString, object.getClass());
	}
	
}
