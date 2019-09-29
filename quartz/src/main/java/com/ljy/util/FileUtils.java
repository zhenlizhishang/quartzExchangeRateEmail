package com.ljy.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FileUtils {

	public static String getFtlContext(String fileName, Map<String, Object> map) throws IOException, TemplateException {
		//1.创建配置实例Cofiguration
		Configuration cfg = new Configuration();
		//2.设置模板文件目录
		cfg.setDirectoryForTemplateLoading(new File(XMLData.FTLURL));
//		cfg.setDirectoryForTemplateLoading(new File("src/main/resources"));
		//获取模板（template）
		Template template = cfg.getTemplate(fileName);
		StringWriter out = new StringWriter();
		//数据与模板合并（数据+模板）
		template.process(map, out);
		out.flush();
		return out.toString();
	}
}
