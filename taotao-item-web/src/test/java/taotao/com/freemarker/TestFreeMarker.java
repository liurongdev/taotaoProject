package taotao.com.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;


public class TestFreeMarker {

	@Test
	public void testFreeMarker()throws Exception{
		//穿件模板文件
		//创建Configuration对象
		Configuration configuration=new Configuration(Configuration.getVersion());
		configuration.setDirectoryForTemplateLoading(new File("E:/SoftWareWorkSpace/javaEEWorkSpace/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
		configuration.setDefaultEncoding("UTF-8");
		//创建Template对象
		Template template=configuration.getTemplate("student.ftl");
		//创建Writer对象
		Writer writer=new FileWriter(new File("E:/student.html"));
		//创建数据对象
		Student student=new Student(1,23,"湖南攸县","liurong");
		List<Student>list=new ArrayList<Student>();
		list.add(new Student(2,23,"湖南攸县-1","liurong-1"));
		list.add(new Student(3,24,"湖南攸县-1","liurong-2"));
		list.add(new Student(4,25,"湖南攸县-1","liurong-3"));
		list.add(new Student(5,26,"湖南攸县-1","liurong-3"));
		list.add(new Student(6,27,"湖南攸县-7","liurong-39"));
		Map map=new HashMap();
		map.put("student",student);
		map.put("list", list);
		//输出
		template.process(map, writer);
		//关闭流
		writer.close();
	}
}
