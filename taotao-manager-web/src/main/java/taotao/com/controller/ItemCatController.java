package taotao.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import taotao.com.common.pojo.EasyUITreeNode;
import taotao.com.service.ItemCatService;
/**
 * 
 * 商品管理Controller
 * @author liurong
 *
 */
@Controller
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList( @RequestParam(name="id" ,defaultValue="0") long parentId){
		List<EasyUITreeNode> result=this.itemCatService.getItemCatList(parentId);
		return result;
	}
}
