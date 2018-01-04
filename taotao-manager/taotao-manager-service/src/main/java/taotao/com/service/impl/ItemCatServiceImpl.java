package taotao.com.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import taotao.com.common.pojo.EasyUITreeNode;
import taotao.com.mapper.TbItemCatMapper;
import taotao.com.pojo.TbItemCat;
import taotao.com.pojo.TbItemCatExample;
import taotao.com.pojo.TbItemCatExample.Criteria;
import taotao.com.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		TbItemCatExample example=new TbItemCatExample();
		//设置查询条件
		Criteria criteria=example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat>list=itemCatMapper.selectByExample(example);
		List<EasyUITreeNode> resultList=new ArrayList<>();
		for(TbItemCat tbItemCat:list){
			EasyUITreeNode node=new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		return resultList;
	}

}
