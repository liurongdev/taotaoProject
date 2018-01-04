package taotao.com.search.dao;



import java.awt.im.spi.InputMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import taotao.com.common.pojo.SearchItem;
import taotao.com.common.pojo.SearchResult;
/*
 * 
 * Reposity 注解回创建Spring 对象
 */
@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;
	
    public SearchResult search(SolrQuery solrQuery)throws Exception{
       SearchResult result=new SearchResult();
        //根据条件执行查询
       QueryResponse response=solrServer.query(solrQuery);
       SolrDocumentList documentList=response.getResults();
       long numFound=documentList.getNumFound();
       result.setRecordCount(numFound);
       List<SearchItem>itemList=new ArrayList<SearchItem>();
       for(SolrDocument document :documentList){
    	   SearchItem item=new SearchItem();
    	   item.setId((String) document.get("id"));
    	   //要是有多张图片则只显示第一张
    	   String image=(String) document.get("item_image");
    	   if(StringUtils.isNotBlank(image)){
    		   image=image.split(",")[0];
    	   }
    	   item.setImage(image);
    	   item.setSell_point((String) document.get("item_sell_point"));
    	   item.setCategory_name((String) document.get("item_category_name"));
    	   item.setPrice((long) document.get("item_price"));
    	   //取得高亮显示
    	   Map<String,Map<String, List<String>>> list=response.getHighlighting();
    	   List<String>list2=list.get(document.get("id")).get("item_title");
    	   String title="";
    	   if(list2.size()>0 && list2!=null){
    		   title=list2.get(0);
    	   }else{
    		   title=(String) document.get("item_title");
    	   }
    	   item.setTitle(title);
    	   itemList.add(item);
       }
	   result.setItemList(itemList);
	   return result;
    }
}
