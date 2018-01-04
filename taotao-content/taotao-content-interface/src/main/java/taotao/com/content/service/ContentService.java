package taotao.com.content.service;

import java.util.List;

import taotao.com.common.pojo.EasyUIDataGridResult;
import taotao.com.common.pojo.TaotaoResult;
import taotao.com.pojo.TbContent;


public interface ContentService {
	
	 TaotaoResult addContent(TbContent content);
	 EasyUIDataGridResult getContentist(int page,int rows,long categoryId);
	 TaotaoResult deleteContent(String ids);
	 TaotaoResult updateContent(TbContent content,long id);
	 
	 List<TbContent> getContentByCid(long cid);
}
