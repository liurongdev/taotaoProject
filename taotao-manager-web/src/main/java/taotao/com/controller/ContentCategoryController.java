package taotao.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import taotao.com.common.pojo.EasyUITreeNode;
import taotao.com.common.pojo.TaotaoResult;
import taotao.com.content.service.ContentCategoryService;
/**
 * 
 * 商品管理Controller
 * @author liurong
 *
 */
@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList( @RequestParam(name="id" ,defaultValue="0") long parentId){
		List<EasyUITreeNode> result=this.contentCategoryService.getContentCategoryList(parentId);
		return result;
	}
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult addContentCategory(long parentId,String name){
		System.out.println("11111111111");
		TaotaoResult result=contentCategoryService.addContentCategory(parentId, name);
		System.out.println("22222222222");
		return result;
	}
	
	
	@RequestMapping("/content/category/update")
	@ResponseBody
	public TaotaoResult updateContentCategory(long id,String name){
		System.out.println("333333");
		TaotaoResult result=contentCategoryService.updateContentCategory(id, name);
		System.out.println("4444");
		return result;
	}
	
	
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public TaotaoResult deleteContentCategory(long id){
		System.out.println("5555");
		TaotaoResult result=contentCategoryService.deleteContentCategory(id);
		System.out.println("666666");
		return result;
	}
}
