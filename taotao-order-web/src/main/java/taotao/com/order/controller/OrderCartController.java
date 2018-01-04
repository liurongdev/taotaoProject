package taotao.com.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import taotao.com.common.pojo.TaotaoResult;
import taotao.com.common.utils.CookieUtils;
import taotao.com.common.utils.JsonUtils;
import taotao.com.order.pojo.OrderInfo;
import taotao.com.order.service.OrderService;
import taotao.com.pojo.TbItem;
import taotao.com.pojo.TbUser;

@Controller
public class OrderCartController {
  
	@Autowired
	private OrderService orderService;
	
	@Value("${CART_KEY}")
	private String CART_KEY;
	
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		//用户必须是登录状态
		//获取用户id
		TbUser user=(TbUser) request.getAttribute("user");
		System.out.println("username="+user.getUsername());
		//根据用户信息取出收货地址列表
		//从cookie中去到商品列表
		List<TbItem> cartList=getCartList(request);
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "order-cart";
	}
	
	public List<TbItem> getCartList(HttpServletRequest request){
		String json=CookieUtils.getCookieValue(request, CART_KEY, true);
		if(StringUtils.isBlank(json)){
			return new ArrayList<TbItem>();
		}
		List<TbItem> list=JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,Model model){
		TaotaoResult result=this.orderService.createOrder(orderInfo);
		//设置订单号和钱数
		model.addAttribute("orderId", result.getData().toString());
		model.addAttribute("payment", orderInfo.getPayment());
		//设置收货时间三天后
		DateTime dateTime=new DateTime();
		dateTime.plusDays(3);
		model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));
		return "success";
	}
}
