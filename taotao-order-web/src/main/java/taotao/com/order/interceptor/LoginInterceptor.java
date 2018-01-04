package taotao.com.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import taotao.com.common.pojo.TaotaoResult;
import taotao.com.common.utils.CookieUtils;
import taotao.com.pojo.TbUser;
import taotao.com.sso.service.UserService;

public class LoginInterceptor implements HandlerInterceptor {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private UserService UserService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// ModelAndView 返回之后

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		//执行handler之后，ModelAndView 返回之前

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		//执行handler之前执行此方法 ，
		//判断用户是否登录，从token中取得用户信息，可能没有取到，传递当前的url到sso系统，登录之后可以成功返回
		String token=CookieUtils.getCookieValue(request, TOKEN_KEY);
		System.out.println("token="+token);
		if(StringUtils.isBlank(token)){
			//取当前请求的url
			String requsetUrl=request.getRequestURL().toString();
			System.out.println("requestUrl="+requsetUrl);
			response.sendRedirect(SSO_URL+"/page/login?url="+requsetUrl);
			return false;
		}
		//取到token
		TaotaoResult result=UserService.getUserByToken(token);
		if(result.getStatus()!=200){
			//取当前请求的url
			System.out.println("00000000000000");
			String requsetUrl=request.getRequestURL().toString();
			System.out.println("requestUrl2="+requsetUrl);
			response.sendRedirect(SSO_URL+"/page/login?url="+requsetUrl);
			return false;
		}
		//取到用户信息放到Request之中
		TbUser user=(TbUser) result.getData();
		request.setAttribute("user", user);
		//放行
		return true;
	}

}
