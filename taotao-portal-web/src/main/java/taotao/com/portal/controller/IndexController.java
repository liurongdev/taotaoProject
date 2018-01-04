package taotao.com.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import taotao.com.common.utils.JsonUtils;
import taotao.com.content.service.ContentService;
import taotao.com.pojo.TbContent;
import taotao.com.portal.pojo.AD1Node;

@Controller
public class IndexController {
   
	@Autowired
	private ContentService contentService;
	
	@Value("${AD1_CATEGORY_ID}")
	private long AD1_CATEGORY_ID;
	
	@Value("${AD1_WIDTH}")
	private int AD1_WIDTH;
	@Value("${AD1_WIDTH_B}")
	private int AD1_WIDTH_B;
	
	@Value("${AD1_HEIGHT}")
	private int AD1_HEIGHT;
	@Value("${AD1_HEIGHT_B}")
	private int AD1_HEIGHT_B;
	
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		//获取TbContent List对象
		List<TbContent>list=this.contentService.getContentByCid(AD1_CATEGORY_ID);
		List<AD1Node>result=new ArrayList<AD1Node>();
		for(TbContent content:list){
			AD1Node node=new AD1Node();
			node.setAlt(content.getTitle());
			node.setWidth(AD1_WIDTH);
			node.setWidthB(AD1_WIDTH_B);
			node.setHeigth(AD1_HEIGHT);
			node.setHeightB(AD1_HEIGHT_B);
			node.setHref(content.getUrl());
			node.setSrc(content.getPic());
			node.setSrcB(content.getPic2());
			result.add(node);
		}
		String ad1=JsonUtils.objectToJson(result);
		model.addAttribute("ad1", ad1);
		return "index";
	}
}








