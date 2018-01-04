package taotao.com.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
/**
 * 展实登录和注册的页面
 * @author liurong
 *
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class PageController {
   
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	
	@RequestMapping("/page/login")
	public String showLogin(String url,Model model){
        model.addAttribute("redirect", url);
		return "login";
	}
}



