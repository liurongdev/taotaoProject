package taotao.com.solrj;

import java.util.List;

import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;


public class TestSolrj {
	
	@Test
	public void testAdditem()throws Exception{
		//创建solr服务对象
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.175:8080/solr/collection1");
		//创建文档document对象，必须要有id域，其他的域名需要再schema.xm文件中定义
		SolrInputDocument document=new SolrInputDocument();
		//给document对象添加域
		document.addField("id", "test003");
		document.addField("item_title","测试商品003");
		document.addField("item_price", 1000);
		document.addField("item_desc", "我的商品描述！");
		
		//增加并插入
		solrServer.add(document);
		solrServer.commit();
	}
	
	@Test
	public void testdeleteitemById()throws Exception{
		//创建solr服务对象
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.175:8080/solr/collection1");
		solrServer.deleteById("test003");
		solrServer.commit();
	}
	@Test
	public void testdeleteitemByQuery()throws Exception{
		//创建solr服务对象
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.175:8080/solr/collection1");
		solrServer.deleteByQuery("id:test001");
		solrServer.commit();
	}
	@Test
	public void testfinditemByQuery()throws Exception{
		//创建solr服务对象
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.175:8080/solr/collection1");
		solrServer.deleteByQuery("id:test001");
		solrServer.commit();
	}
	
	@Test
	public void searchDocument()throws Exception{
		//创建solr服务对象
	    SolrServer solrServer=new HttpSolrServer("http://192.168.25.175:8080/solr/collection1");
	    //创建solrQuery对象
	    SolrQuery query=new SolrQuery();
	    //设置查询条件，高亮显示,高亮前缀，后缀等等
	    query.setQuery("手机");
	    query.setStart(0);
	    query.setRows(10);
	    //设置默认搜索域
	    query.set("df", "item_keywords");
	    query.setHighlight(true);
	    query.addHighlightField("item_title");
	    query.setHighlightSimplePre("<div>");
	    query.setHighlightSimplePost("</div>");
	    
	    //执行查询得到一个QueryResponse对象
	    QueryResponse response=solrServer.query(query);
	    SolrDocumentList list=response.getResults();
	    //查询结果的总记录数
	    System.out.println("查询结果的总记录数："+list.getNumFound());
	    for(SolrDocument document:list){
	    	System.out.println(document.get("id"));
	    	//输出高亮显示
	        Map<String, Map<String, List<String>>> result=response.getHighlighting();
	        String item_title="";
	        List<String>resultList=result.get(document.get("id")).get("item_title");
	        if(resultList!=null && resultList.size()!=0){
	        	item_title=resultList.get(0);
	        }else{
	        	item_title=(String) document.get("item_title");
	        }
	    	System.out.println(item_title);
	    	System.out.println(document.get("item_sell_point"));
	    	System.out.println(document.get("item_price"));
	    	System.out.println(document.get("item_image"));
	    	System.out.println(document.get("item_category_name"));
	    	System.out.println("================================");
	    }
	}
	@Test
	public void searchDocument2()throws Exception{
		//创建solr服务对象
	    SolrServer solrServer=new HttpSolrServer("http://192.168.25.175:8080/solr/collection1");
	    //创建solrQuery对象
	    SolrQuery query=new SolrQuery();
	    //设置查询条件，高亮显示,高亮前缀，后缀等等
	    query.setQuery("手机");
	    query.setStart(0);
	    query.setRows(10);
	    //设置默认搜索域
	    query.set("df", "item_keywords");
	    query.setHighlight(true);
	    query.addHighlightField("item_title");
	    query.setHighlightSimplePre("<div>");
	    query.setHighlightSimplePost("</div>");
	    
	    //执行查询得到一个QueryResponse对象
	    QueryResponse response=solrServer.query(query);
	    SolrDocumentList list=response.getResults();
	    //查询结果的总记录数
	    System.out.println("查询结果的总记录数："+list.getNumFound());
	    for(SolrDocument document:list){
	    	System.out.println(document.get("id"));
	    	//输出高亮显示
	        Map<String, Map<String, List<String>>> result=response.getHighlighting();
	        String item_title="";
	        List<String>resultList=result.get(document.get("id")).get("item_title");
	        if(resultList!=null && resultList.size()!=0){
	        	item_title=resultList.get(0);
	        }else{
	        	item_title=(String) document.get("item_title");
	        }
	    	System.out.println(item_title);
	    	System.out.println(document.get("item_sell_point"));
	    	System.out.println(document.get("item_price"));
	    	System.out.println(document.get("item_image"));
	    	System.out.println(document.get("item_category_name"));
	    	System.out.println("================================");
	    }
	}
}
