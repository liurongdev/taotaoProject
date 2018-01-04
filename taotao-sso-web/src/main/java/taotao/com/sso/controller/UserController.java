package taotao.com.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import taotao.com.common.pojo.TaotaoResult;
import taotao.com.common.utils.CookieUtils;
import taotao.com.common.utils.JsonUtils;
import taotao.com.pojo.TbUser;
import taotao.com.sso.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
    public TaotaoResult checkData(@PathVariable String param,@PathVariable Integer type){
		TaotaoResult result=userService.checkUserData(param, type);
		return result;
	}
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
    public TaotaoResult register(TbUser user){
		TaotaoResult result=userService.registerUser(user);
		System.out.println("用户注册成功！");
		return result;
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
    public TaotaoResult login(String username,String password,
    		HttpServletResponse response,HttpServletRequest request){
		TaotaoResult result=userService.login(username, password);
		//把tooken写入cookie
		if(result.getStatus()==200){
			CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
			System.out.println("用户登录成功！");
		}
		return result;
	}
	
	/**
	 * 
	 *第一种方法
	
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,
			     produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
    public String  getUserToken(@PathVariable String token,String callback){
		TaotaoResult result=userService.getUserByToken(token);
		if(StringUtils.isNotBlank(callback)){
			return callback+"("+JsonUtils.objectToJson(result)+");";
		}
		System.out.println("查找用户的token成功！");
		return JsonUtils.objectToJson(result);
	}
	 */
	//使用的spring版本要求4.1以上
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET)
	@ResponseBody
	public Object  getUserToken(@PathVariable String token,String callback){
		TaotaoResult result=userService.getUserByToken(token);
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue value=new MappingJacksonValue(result);
			value.setJsonpFunction(callback);
			return value;
	}
	System.out.println("查找用户的token成功！");
	return result;
}
	
	@RequestMapping(value="/user/logout/{token}",method=RequestMethod.GET)
	@ResponseBody
    public TaotaoResult logout(@PathVariable String token){
		TaotaoResult result=userService.deleteUserByToken(token);
		System.out.println("删除用户token成功！");
		return result;
	}
}
