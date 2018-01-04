package taotao.com.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import taotao.com.common.pojo.SearchItem;
import taotao.com.common.pojo.SearchResult;
import taotao.com.search.service.SearchService;

@Controller
public class SearchController {
		
	@Autowired
	private SearchService searchService;
	
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;
	
	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryString,
			@RequestParam(defaultValue="1") Integer page,Model model)throws Exception{
		    //int a=1/0;手动抛出异常
			System.out.println("queryString:"+queryString+" ;page:"+page);
			queryString=new String(queryString.getBytes("iso8859-1"),"utf-8");
			System.out.println("new QueryString:"+queryString);
			SearchResult result=searchService.search(queryString, page, SEARCH_RESULT_ROWS);
			System.out.println(result.getTotalPages());
			System.out.println("size:"+result.getItemList().size());
			List<SearchItem>itemList=result.getItemList();
			for(int i=0;i<itemList.size();i++){
				SearchItem item=itemList.get(i);
				System.out.println(item.getId()+";"+item.getTitle());
			}
			model.addAttribute("query", queryString);
			model.addAttribute("totalPages", result.getTotalPages());
			model.addAttribute("itemList", result.getItemList());
			model.addAttribute("page", page);
		return "search";
	}
}
