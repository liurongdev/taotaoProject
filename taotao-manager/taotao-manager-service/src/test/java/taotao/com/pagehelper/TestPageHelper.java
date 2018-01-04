package taotao.com.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import taotao.com.mapper.TbItemMapper;
import taotao.com.pojo.TbItem;
import taotao.com.pojo.TbItemExample;

public class TestPageHelper {
	
	@Test
	public void testPageHelper() throws Exception{
		//在mybatis中配置pageHelper插件
		//设置查询分页条件，调用PageHelper中的静态方法
		PageHelper.startPage(1, 10);
		
		//执行查询
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		TbItemMapper itemMapper=applicationContext.getBean(TbItemMapper.class);
		
		//穿件Example对象
		TbItemExample itemExample=new TbItemExample();
		List<TbItem>list=itemMapper.selectByExample(itemExample);
		
		//取出分页信息
		PageInfo<TbItem> pageInfo=new PageInfo<>(list);
		
		System.out.println("总的页数:"+pageInfo.getPages());
		System.out.println("总的记录数使："+pageInfo.getTotal());
		System.out.println("返回的记录数使:"+list.size());
		
		
	}
}
