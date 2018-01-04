package taotao.com.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import taotao.com.common.pojo.SearchItem;
import taotao.com.search.mapper.SearchItemMapper;



public class ItemAddMessageListener implements MessageListener{
	
	@Autowired
	private SearchItemMapper SearchItemMapper;
	@Autowired
	private SolrServer SolrServer;
	
	@Override
	public void onMessage(Message message) {
		try {
			//根据商品id来取得商品信息
			TextMessage textMessage=(TextMessage)message;
			long itemId=Long.parseLong(textMessage.getText());
			//等待事务提交
			Thread.sleep(1000);
			//查询数据库
			SearchItem searchItem=SearchItemMapper.getItemById(itemId);
			//创建文档对象
			SolrInputDocument document=new SolrInputDocument();
			//向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			//文件提交索引库
			SolrServer.add(document);
			//提交
			SolrServer.commit();
			System.out.println("使用ActiveMq接受消息，并且添加索引库成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
