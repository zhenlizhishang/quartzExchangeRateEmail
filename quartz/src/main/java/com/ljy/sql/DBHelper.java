package com.ljy.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ljy.util.DBUtils;
import com.ljy.util.NumberUtils;

public class DBHelper {

	public static void insertData(Map<String, Object> map){
		String OldExchangeRate = "";
		String bank = "";
		SimpleDateFormat df = new SimpleDateFormat("yyMMdd");//设置日期格式
		String data = df.format(new Date());
		for (String str : map.keySet()) {
			if ("BOCHKUSDSell".equals(str)) {
				OldExchangeRate = map.get("BOCHKUSDSell").toString();
				bank = "BOCHKUSDSell";
				DBUtils.insertSql("insert into DOLLARHISDATA (EXCHANGERATE, NAME, GETDATE, INTIME, MODTIME) values (" + OldExchangeRate + ", '"+ bank + "', '" + data + "', now(), now());");
			} else if("CITIUSDcstexcSellPrice".equals(str)) {
				OldExchangeRate = map.get("CITIUSDcstexcSellPrice").toString();
				bank = "CITIUSDcstexcSellPrice";
				DBUtils.insertSql("insert into DOLLARHISDATA  (EXCHANGERATE, NAME, GETDATE, INTIME, MODTIME) values (" + OldExchangeRate + ", '"+ bank + "', '" + data + "', now(), now());");
			} else if("USDPABSell".equals(str)) {
				OldExchangeRate = map.get("USDPABSell").toString();
				bank = "USDPABSell";
				DBUtils.insertSql("insert into DOLLARHISDATA (EXCHANGERATE, NAME, GETDATE, INTIME, MODTIME) values (" + OldExchangeRate + ", '"+ bank + "', '" + data + "', now(), now());");
			}
		}
	}
	
	/**
	 * 添加中国银行数据
	 * @param list
	 */
	public static void insertBOCBankData(List<Map<String, Object>> list){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		String data = df.format(new Date());
		for (int i = 0; i < list.size(); i++) {
			for (String key : list.get(i).keySet()) {
				DBUtils.insertSql("insert into BOCBANKHISDATA (EXCHANGERATE, NAME, GETDATE, INTIME, MODTIME) values (" + list.get(i).get(key) + ", '"+ key + "', '" + data + "', now() ,now());");
			}
		}
	}
	
	public static List<Map<String, Object>> findData() throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "SELECT EXCHANGERATE, NAME, GETDATE FROM DOLLARHISDATA ORDER BY GETDATE DESC LIMIT 21";
		ResultSet rs = DBUtils.getAll(sql);
		while(rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("prices", NumberUtils.getSub2(rs.getDouble("EXCHANGERATE")));
			map.put("name", changeName(rs.getString("NAME")));
			map.put("time", rs.getString("GETDATE"));
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 获取最大最小值
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, Double> getMinAndMax() throws SQLException {
		Map<String, Double> map = new HashMap<String, Double>();
		String sql ="SELECT MIN(s.EXCHANGERATE) minnum, MAX(s.EXCHANGERATE) maxnum FROM (SELECT EXCHANGERATE FROM DOLLARHISDATA ORDER BY GETDATE DESC LIMIT 21) s";
		ResultSet rs = DBUtils.getAll(sql);
		while(rs.next()) {
			map.put("minNum", Double.parseDouble(rs.getString("minnum")));
			map.put("maxNum", Double.parseDouble(rs.getString("maxnum")));
		}
		return map;
	}
	
	
	/**
	 * 改一下名字
	 * @return
	 */
	private static String changeName(String name) {
		if("CITIUSDcstexcSellPrice".equals(name)){
			return "中信现汇卖出价";
		} else if("BOCHKUSDSell".equals(name)) {
			return "中行离岸卖出价";
		} else if("USDPABSell".equals(name)) {
			return "平安现汇卖出价";
		}
		return name;
	}
	
	public static void main(String[] args) throws SQLException {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("BOCHKUSDSell", "6.9823");
//		map.put("CITIUSDcstexcSellPrice", "6.9587");
//		map.put("USDPABSell", "6.9541");
//		insertData(map);
		System.out.println(findData());
	}
}
