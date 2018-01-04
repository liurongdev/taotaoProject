package taotao.com.solrj;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	
	@Test
	public void testSolrCloud()throws Exception{
		//创建CloudSolrServer对象
		CloudSolrServer cloudSolrServer=new CloudSolrServer("192.168.25.175:2181,192.168.25.175:2182,192.168.25.175:2183");
	    cloudSolrServer.setDefaultCollection("collection2");
		SolrInputDocument document=new SolrInputDocument();
		document.setField("id", "testCloud001");
		document.setField("item_title", "测试CloudSolrServer");
		cloudSolrServer.add(document);
		//提交
		cloudSolrServer.commit();
	}
}
