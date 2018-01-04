package taotao.com.service;

import taotao.com.common.pojo.EasyUIDataGridResult;
import taotao.com.common.pojo.TaotaoResult;
import taotao.com.pojo.TbItem;
import taotao.com.pojo.TbItemDesc;

public interface ItemService {
	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page,int rows);
	TaotaoResult addItem(TbItem item,String desc);
	TbItemDesc getItemDescById(long itemId);
	TaotaoResult deleteItem(long itemId);
}
