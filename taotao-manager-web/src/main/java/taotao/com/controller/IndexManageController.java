package taotao.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import taotao.com.common.pojo.TaotaoResult;
import taotao.com.search.service.SearchItemService;


@Controller
public class IndexManageController {
	
	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/import")
	@ResponseBody
	public TaotaoResult ImportIndex(){
		TaotaoResult result=searchItemService.importItemsToIndex();
		return result;
	}
}
