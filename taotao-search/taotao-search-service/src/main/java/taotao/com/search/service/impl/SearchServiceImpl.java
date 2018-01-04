package taotao.com.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import taotao.com.common.pojo.SearchResult;
import taotao.com.search.dao.SearchDao;
import taotao.com.search.service.SearchService;


@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String queryString, int page, int rows)  {
		// TODO Auto-generated method stub
		System.out.println("SearchServiceImpl->search:"+queryString+";"+page+";"+rows);
		//创建查询对象
		SolrQuery query=new SolrQuery();
		//设置查询条件
		query.setQuery(queryString);
		//设置分页条件
		if(page<1) page=1;
		query.setStart((page-1)*rows);
		if(rows<1) rows=10;
		query.setRows(rows);
		//设置默认搜索域
		query.set("df", "item_title");
		//设置高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<font color='red'>");
		query.setHighlightSimplePost("</font>");
		//根据dao执行查询
		SearchResult result=null;
		try {
			result = searchDao.search(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long recordCounts=result.getRecordCount();
		long pages=recordCounts/rows;
		if(recordCounts % rows>0){
			pages++;
		}
		result.setTotalPages(pages);
		return result;
	}

}
