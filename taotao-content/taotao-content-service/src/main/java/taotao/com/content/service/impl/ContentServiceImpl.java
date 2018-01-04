package taotao.com.content.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.omg.PortableServer.THREAD_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.tools.classfile.Annotation.element_value;

import taotao.com.common.pojo.EasyUIDataGridResult;
import taotao.com.common.pojo.TaotaoResult;
import taotao.com.common.utils.JsonUtils;
import taotao.com.content.service.ContentService;
import taotao.com.jedis.JedisClient;
import taotao.com.mapper.TbContentMapper;
import taotao.com.pojo.TbContent;
import taotao.com.pojo.TbContentExample;
import taotao.com.pojo.TbContentExample.Criteria;


@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient JedisClient;

	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;
	@Override
	public TaotaoResult addContent(TbContent content) {
		//补全pojo内容
		content.setUpdated(new Date());
		content.setCreated(new Date());
	    //执行插入
		contentMapper.insert(content);
		JedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
		return TaotaoResult.ok();
	}

	@Override
	public EasyUIDataGridResult getContentist(int page, int rows,long categoryId) {
		// TODO Auto-generated method stub
				//设置分页信息
				PageHelper.startPage(page, rows);
				
				//没有设置条件则是查询全部
				TbContentExample example=new TbContentExample();
				Criteria criteria=example.createCriteria();
				criteria.andCategoryIdEqualTo(categoryId);
				List<TbContent>list=contentMapper.selectByExample(example);
				
				PageInfo<TbContent>pageInfo=new PageInfo<>(list);
				EasyUIDataGridResult result=new EasyUIDataGridResult();
				result.setRows(list);
				result.setTotal(pageInfo.getTotal());
				return result;
	}

	@Override
	public TaotaoResult deleteContent(String ids) {
		System.out.println(ids);
		String[] str=ids.split(",");
		long[] id=new long[str.length];
		for(int i=0;i<str.length;i++){
			id[i]=Long.parseLong(str[i]);
		}
		TbContent content=this.contentMapper.selectByPrimaryKey(id[0]);
		List<Long>values=new ArrayList<Long>();
		for(int i=0;i<id.length;i++){
			values.add(id[i]);
		}
		TbContentExample example=new TbContentExample();
		Criteria criteria=example.createCriteria();
		criteria.andIdIn(values);
		this.contentMapper.deleteByExample(example);
		JedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult updateContent(TbContent content, long id) {
		System.out.println(content.getContent());
		System.out.println("id=="+id);
		TbContent content2=this.contentMapper.selectByPrimaryKey(id);
		System.out.println("content2.content"+content2.getContent());
		content.setId(id);
		content.setUpdated(new Date());
		content.setCreated(content2.getCreated());
		this.contentMapper.updateByPrimaryKeyWithBLOBs(content);
		return TaotaoResult.ok(content);
	}

	@Override
	public List<TbContent> getContentByCid(long cid) {
		//先到缓存中查找，如果没有再到数据库中查找，缓存不能影响正常的业务逻辑
		try{
			//查找缓存，如果找到就放回List结构内容
			String json=JedisClient.hget(INDEX_CONTENT, cid+"");
			if(StringUtils.isNotBlank(json)){
				List<TbContent>result=JsonUtils.jsonToList(json, TbContent.class);
				System.out.println("缓存中找到数据："+result	);
				return result;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("缓存中没有数据，请到数据库中查询");
		}
		System.out.println("数据库中查找数据！");
		TbContentExample example=new TbContentExample();
		Criteria criteria=example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent>list=this.contentMapper.selectByExample(example);
		try{
			JedisClient.hset(INDEX_CONTENT, cid+"", JsonUtils.objectToJson(list));
			System.out.println("数据插入缓存成功！");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据插入缓存失败！");
		}
		return list;
	}
}











