package taotao.com.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;
import taotao.com.item.pojo.Item;
import taotao.com.pojo.TbItem;
import taotao.com.pojo.TbItemDesc;
import taotao.com.service.ItemService;

public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemService ItemService;
	
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;
	@Override
	public void onMessage(Message message) {
		try {
			//从消息中取出id
			TextMessage textMessage=(TextMessage)message;
			Long itemId=Long.parseLong(textMessage.getText());
			//查询数据库取得商品信息，商品描述信息
			System.out.println("itemId="+itemId);
		    TbItem tbItem=ItemService.getItemById(itemId);
		    System.out.println("tbItem:"+tbItem);
		    Thread.sleep(1000);
		    Item item=new Item(tbItem);
		    TbItemDesc tbItemDesc=ItemService.getItemDescById(itemId);
		    //设置数据
		    Map map=new HashMap();
		    map.put("item", item);
		    map.put("itemDesc",tbItemDesc);
		    //设置Configuration 
		    Configuration configuration=freeMarkerConfig.getConfiguration();
		    configuration.setDefaultEncoding("GBK");
		    //创建模板
			//设置生成静态页面的目录和文件
		    Template template=configuration.getTemplate("item.ftl");
		    Writer out=new FileWriter(new File(HTML_OUT_PATH+itemId+".html"));
		    template.process(map, out);
		    out.close();
		    System.out.println("静态页面生成成功："+itemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

	}
}
