package taotao.com.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr.Item;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import taotao.com.common.pojo.EasyUIDataGridResult;
import taotao.com.common.pojo.TaotaoResult;
import taotao.com.common.utils.IDUtils;
import taotao.com.common.utils.JsonUtils;
import taotao.com.jedis.JedisClient;
import taotao.com.mapper.TbItemDescMapper;
import taotao.com.mapper.TbItemMapper;
import taotao.com.pojo.TbItem;
import taotao.com.pojo.TbItemDesc;
import taotao.com.pojo.TbItemExample;
import taotao.com.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private JmsTemplate JmsTemplate;
	
	@Resource(name="itemAddTopic")
	private Destination destination;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JedisClient JedisClient;
	
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${TIME_EXPIRE}")
	private Integer TIME_EXPIRE;
	@Override
	public TbItem getItemById(long itemId) {
		//在查询数据库之前先到缓存中查找
		try{
		   String json=JedisClient.get(ITEM_INFO+":"+itemId+":BASE");
		   if(StringUtils.isNotBlank(json)){
			   TbItem tbItem=JsonUtils.jsonToPojo(json, TbItem.class);
			   return tbItem;
		   }
		}catch(Exception e){
			e.printStackTrace();
		}
		//缓存中要是没有则到数据库中查找
		TbItem item=itemMapper.selectByPrimaryKey(itemId);
		try{
			//把数据库中的数据插入到缓存之中
			JedisClient.set(ITEM_INFO+":"+itemId+":BASE", JsonUtils.objectToJson(item));
			//设置缓存的过期时间
			JedisClient.expire(ITEM_INFO+":"+itemId+":BASE", TIME_EXPIRE);
		}catch(Exception e){
			e.printStackTrace();
		}
		return item;
	}
	
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// TODO Auto-generated method stub
		//设置分页信息
		PageHelper.startPage(page, rows);
		
		//没有设置条件则是查询全部
		TbItemExample example=new TbItemExample();
		
		List<TbItem>list=itemMapper.selectByExample(example);
		
		PageInfo<TbItem>pageInfo=new PageInfo<>(list);
		EasyUIDataGridResult result=new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		//创建商品id
		final long itemId=IDUtils.genItemId();
		//补全item信息
		item.setId(itemId);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//商品描述 1-正常  2-删除  3-下架
		item.setStatus((byte)1);
		itemMapper.insert(item);
		
		//向商品表插入数据
		TbItemDesc itemDesc=new TbItemDesc();
		//不全商品描述表
		itemDesc.setItemId(itemId);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDesc.setItemDesc(desc);
		//向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		//返回结果之前向ActiveMQ发送消息
		JmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message=session.createTextMessage(itemId+"");
				return message;
			}
		});
		//返回结果
		return TaotaoResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//在查询数据库之前先到缓存中查找
		try{
			String json=JedisClient.get(ITEM_INFO+":"+itemId+":DESC");
			if(StringUtils.isNotBlank(json)){
			   TbItemDesc tbItemDesc=JsonUtils.jsonToPojo(json, TbItemDesc.class);
			   return tbItemDesc;
			}
	    }catch(Exception e){
	    	e.printStackTrace();
		}
		//缓存中没找到就到数据库中查找
		TbItemDesc result=itemDescMapper.selectByPrimaryKey(itemId);
		try{
			//把数据库中的数据插入到缓存之中
			JedisClient.set(ITEM_INFO+":"+itemId+":DESC", JsonUtils.objectToJson(result));
			//设置缓存的过期时间
			JedisClient.expire(ITEM_INFO+":"+itemId+":DESC", TIME_EXPIRE);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public TaotaoResult deleteItem(long itemId) {
		System.out.println("delete item-->itemId="+itemId);
		this.itemMapper.deleteByPrimaryKey(itemId);
		return TaotaoResult.ok();
	}
	
	
}
