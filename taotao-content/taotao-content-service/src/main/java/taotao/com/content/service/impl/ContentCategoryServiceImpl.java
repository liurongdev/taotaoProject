package taotao.com.content.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import taotao.com.common.pojo.EasyUITreeNode;
import taotao.com.common.pojo.TaotaoResult;
import taotao.com.content.service.ContentCategoryService;
import taotao.com.mapper.TbContentCategoryMapper;
import taotao.com.pojo.TbContentCategory;
import taotao.com.pojo.TbContentCategoryExample;
import taotao.com.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria criteria=example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory>list=contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode>resultList=new ArrayList<EasyUITreeNode>();
		for(TbContentCategory tbContentCategory:list){
			EasyUITreeNode node=new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		return resultList;
	}
	@Override
	public TaotaoResult addContentCategory(long parentId, String name) {
		TbContentCategory contentCategory=new TbContentCategory();
		contentCategory.setIsParent(false);
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		//设置排序的顺序，默认的全部为1
		contentCategory.setSortOrder(1);
		//设置状态，1正常，2删除
		contentCategory.setStatus(1);
		contentCategory.setUpdated(new Date());
		contentCategory.setCreated(new Date());
		
		//查询父节点的状态
		TbContentCategory parent=this.contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			this.contentCategoryMapper.updateByPrimaryKey(parent);
		}
		this.contentCategoryMapper.insert(contentCategory);
		return TaotaoResult.ok(contentCategory);
	}
	@Override
	public TaotaoResult updateContentCategory(long id, String name) {
		TbContentCategory contentCategory=this.contentCategoryMapper.selectByPrimaryKey(id);
		contentCategory.setName(name);
		this.contentCategoryMapper.updateByPrimaryKey(contentCategory);
		return TaotaoResult.ok(contentCategory);
	}
	
	@Override
	public TaotaoResult deleteContentCategory(long id) {
		TbContentCategory contentCategory=this.contentCategoryMapper.selectByPrimaryKey(id);
		TbContentCategory parentContentCategory=this.contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria criteria=example.createCriteria();
		criteria.andParentIdEqualTo(contentCategory.getParentId());
		List<TbContentCategory>list=this.contentCategoryMapper.selectByExample(example);
		if(!contentCategory.getIsParent()){
			this.contentCategoryMapper.deleteByPrimaryKey(id);
			if(list.size()==1){
				parentContentCategory.setIsParent(false);
				this.contentCategoryMapper.updateByPrimaryKey(parentContentCategory);
			}
		}else{
			TaotaoResult result=new TaotaoResult();
			result.setData(contentCategory);
			result.setMsg("删除错误，父节点不能删除,请先删除父节点的叶子节点！");
			result.setStatus(400);
			return result;
		}
		return TaotaoResult.ok(contentCategory);
	}
}
