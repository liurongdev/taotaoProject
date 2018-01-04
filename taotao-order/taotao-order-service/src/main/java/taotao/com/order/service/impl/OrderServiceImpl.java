package taotao.com.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import taotao.com.common.pojo.TaotaoResult;
import taotao.com.jedis.JedisClient;
import taotao.com.mapper.TbOrderItemMapper;
import taotao.com.mapper.TbOrderMapper;
import taotao.com.mapper.TbOrderShippingMapper;
import taotao.com.order.pojo.OrderInfo;
import taotao.com.order.service.OrderService;
import taotao.com.pojo.TbOrderItem;
import taotao.com.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService {
    
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderShippingMapper shippingMapper;
	@Autowired
	private TbOrderItemMapper itemMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	@Value("${ORDER_ID_BEGIN_VALUE}")
	private String ORDER_ID_BEGIN_VALUE;
	
	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	private String ORDER_ITEM_ID_GEN_KEY;
	
	@Override
	public TaotaoResult createOrder(OrderInfo orderInfo) {
		//生成订单号
		if(!jedisClient.exists(ORDER_ID_GEN_KEY)){
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_BEGIN_VALUE);
		}
		//获取orderId
		String orderId=jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		orderInfo.setOrderId(orderId);
		//设置邮费,这里免邮费
		orderInfo.setPostFee("0");
		//设置状态，1-未付款，2-已付款，3-未发货，4-已发货，5-交易成功，6-系统关闭
		orderInfo.setStatus(1);
		orderInfo.setUpdateTime(new Date());
		orderInfo.setCreateTime(new Date());
		//插入订单表
		this.orderMapper.insert(orderInfo);
		//订单详情表中插入数据
		List<TbOrderItem> orderItems=orderInfo.getOrderItems();
		for(TbOrderItem orderItem:orderItems){
			String oid=jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
			orderItem.setId(oid);
			orderItem.setOrderId(orderId);
			itemMapper.insert(orderItem);
		}
		//向物流表中插入数据
		TbOrderShipping shipping=orderInfo.getOrderShipping();
		shipping.setOrderId(orderId);
		shipping.setCreated(new Date());
		shipping.setUpdated(new Date());
		shippingMapper.insert(shipping);
		return TaotaoResult.ok(orderId);
	}

}
