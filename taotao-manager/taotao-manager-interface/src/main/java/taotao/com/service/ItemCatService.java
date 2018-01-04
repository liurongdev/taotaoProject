package taotao.com.service;

import java.util.List;

import taotao.com.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	public List<EasyUITreeNode> getItemCatList(long parentId); 
}
