package taotao.com.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;


@Controller
public class TestHtmlGen {
	
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String getHtml()throws Exception{
		Configuration configuration=freeMarkerConfig.getConfiguration();
		Template template=configuration.getTemplate("hello.ftl");
		Map map=new HashMap<>();
		map.put("hello", "hello framework");
		Writer out=new FileWriter(new File("e:/test.html"));
		template.process(map, out);
		out.close();
		return "OK";
	}
}
