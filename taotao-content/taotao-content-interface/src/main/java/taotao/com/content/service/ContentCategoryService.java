package taotao.com.content.service;

import java.util.List;

import taotao.com.common.pojo.EasyUITreeNode;
import taotao.com.common.pojo.TaotaoResult;

public interface ContentCategoryService {
	List<EasyUITreeNode> getContentCategoryList(long parentId);
	TaotaoResult addContentCategory(long parentId,String name);
	TaotaoResult updateContentCategory(long id,String name);
	TaotaoResult deleteContentCategory(long id);
	
}
