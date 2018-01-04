package taotao.com.order.service;

import taotao.com.common.pojo.TaotaoResult;
import taotao.com.order.pojo.OrderInfo;


public interface OrderService {
	
	TaotaoResult createOrder(OrderInfo orderInfo);
}
