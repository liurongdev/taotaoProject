package taotao.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import taotao.com.common.pojo.EasyUIDataGridResult;
import taotao.com.common.pojo.TaotaoResult;
import taotao.com.content.service.ContentService;
import taotao.com.pojo.TbContent;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult addContent(TbContent content){
		TaotaoResult result=this.contentService.addContent(content);
		return result;
	}
	
	@RequestMapping("/content/delete")
	@ResponseBody
	public TaotaoResult deleteContent(String ids){
		System.out.println("/content/delete");
		TaotaoResult result=this.contentService.deleteContent(ids);
		return result;
	}
	
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public TaotaoResult updateContent(TbContent content,long id){
		TaotaoResult result=this.contentService.updateContent(content, id);
		return result;
	}
	

	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(Integer page,Integer rows,long categoryId){
		System.out.println("****list-content****");
		EasyUIDataGridResult result=contentService.getContentist(page, rows, categoryId);
		return result;
	}
	
}

