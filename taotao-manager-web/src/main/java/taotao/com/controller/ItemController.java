package taotao.com.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import taotao.com.common.pojo.EasyUIDataGridResult;
import taotao.com.common.pojo.TaotaoResult;
import taotao.com.pojo.TbItem;
import taotao.com.service.ItemService;
/**
 * 
 * 商品管理Controller
 * @author liurong
 *
 */
@Controller
public class ItemController {
	@Autowired
	private ItemService itemServie;

	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getTbItemById(@PathVariable long itemId){
		TbItem item=this.itemServie.getItemById(itemId);
		return item;
	}
	
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public TaotaoResult deleteTbItemById(String ids){
		long itemId=Long.parseLong(ids.split(",")[0]);
		TaotaoResult taotaoResult=this.itemServie.deleteItem(itemId);
		return taotaoResult;
	}
	
	
	
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		System.out.println("****hello****");
		EasyUIDataGridResult result=itemServie.getItemList(page, rows);
		return result;
	}
	
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	TaotaoResult addItem(TbItem item,String desc){
		return this.itemServie.addItem(item, desc);
	}
	
}	
