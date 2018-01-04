package taotao.com.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.compiler.ast.DoubleConst;
import taotao.com.common.pojo.SearchItem;
import taotao.com.common.pojo.TaotaoResult;
import taotao.com.search.mapper.SearchItemMapper;
import taotao.com.search.service.SearchItemService;


@Service
public class SearchItemServiceImpl implements SearchItemService {
	
	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Autowired
	private SolrServer SolrServer;
	
	@Override
	public TaotaoResult importItemsToIndex() {
		try{
			List<SearchItem>list=searchItemMapper.getItemList();
			for(SearchItem searchItem:list){
				SolrInputDocument document=new SolrInputDocument();
				document.setField("id", searchItem.getId());
				document.setField("item_price", searchItem.getPrice());
				document.setField("item_title", searchItem.getTitle());
				document.setField("item_desc", searchItem.getItem_desc());
				document.setField("item_image", searchItem.getImage());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_category_name", searchItem.getCategory_name());
				SolrServer.add(document);
			}
			SolrServer.commit();
		}catch(Exception e){
			e.printStackTrace();
			return TaotaoResult.build(500, "文档索引库增加失败");
		}
		return TaotaoResult.ok();
	}

}
