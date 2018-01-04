package taotao.com.car.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import taotao.com.common.pojo.TaotaoResult;
import taotao.com.common.utils.CookieUtils;
import taotao.com.common.utils.JsonUtils;
import taotao.com.pojo.TbItem;
import taotao.com.service.ItemService;

@Controller
public class CartController {

	@Autowired
	private ItemService ItemService;
	
	@Value("${CART_KEY}")
	private String CART_KEY;
	@Value("${CART_EXPIER}")
	private Integer CART_EXPIER;
	 
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,
			@RequestParam(defaultValue="1")Integer num,
			HttpServletRequest request,HttpServletResponse response){
		
		List<TbItem> list=getCartList(request);
		//判断购物车中是否有商品，没有的话则添加，有的话这数量加上num
		boolean flag=false;
		for(TbItem item:list){
			if(item.getId()==itemId.longValue()){
				flag=true;
				item.setNum(item.getNum()+num);
				break;
			}
		}
		//如果没有
		if(!flag){
			TbItem item=ItemService.getItemById(itemId);
			item.setNum(num);
			String images=item.getImage();
			if(StringUtils.isNotBlank(images)){
				String[] image=images.split(",");
				item.setImage(image[0]);
			}
			list.add(item);
		}
		
		CookieUtils.setCookie(request, response, CART_KEY,
				JsonUtils.objectToJson(list),CART_EXPIER,true);
		return "cartSuccess";
	}
	
	public List<TbItem> getCartList(HttpServletRequest request){
		String json=CookieUtils.getCookieValue(request, CART_KEY, true);
		if(StringUtils.isBlank(json)){
			return new ArrayList<TbItem>();
		}
		List<TbItem> list=JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	@RequestMapping("/cart/cart")
	public String showCart(HttpServletRequest request){
		//从Cookie中取得购物车的列表
     	List<TbItem> cartList=getCartList(request);
     	request.setAttribute("cartList", cartList);
     	return "cart";
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult showCart(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//从Cookie中取得购物车的列表
     	List<TbItem> cartList=getCartList(request);
     	for(TbItem item:cartList){
     		if(item.getId()==itemId.longValue()){
     			item.setNum(num);
     			break;
     		}
     	}
    	CookieUtils.setCookie(request, response, CART_KEY,
				JsonUtils.objectToJson(cartList),CART_EXPIER,true);
    	System.out.println("cookie中的商品信息更改成功！");
     	return TaotaoResult.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,
			HttpServletRequest request,HttpServletResponse response){
		List<TbItem> list=getCartList(request);
		for(TbItem item:list){
			if(item.getId()==itemId.longValue()){
				list.remove(item);
				break;
			}
		}
		CookieUtils.setCookie(request, response, CART_KEY,
				JsonUtils.objectToJson(list),CART_EXPIER,true);
		System.out.println("商品删除成功！");
		return "redirect:/cart/cart.html";
	}
}
