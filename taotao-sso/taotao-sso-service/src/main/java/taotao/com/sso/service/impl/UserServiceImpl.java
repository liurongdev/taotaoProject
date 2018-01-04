package taotao.com.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User;

import taotao.com.common.pojo.TaotaoResult;
import taotao.com.common.utils.JsonUtils;
import taotao.com.jedis.JedisClient;
import taotao.com.mapper.TbUserMapper;
import taotao.com.pojo.TbUser;
import taotao.com.pojo.TbUserExample;
import taotao.com.pojo.TbUserExample.Criteria;
import taotao.com.sso.service.UserService;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private TbUserMapper TbUserMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public TaotaoResult checkUserData(String data, int type) {
		TbUserExample example=new TbUserExample();
		Criteria criteria=example.createCriteria();
		if(type==1){
			//username
			criteria.andUsernameEqualTo(data);
		}else if(type==2){
			//phone
			criteria.andPhoneEqualTo(data);
		}else if(type==3){
			criteria.andEmailEqualTo(data);
		}else{
			return TaotaoResult.build(400, "要查询的数据类型错误");
		}
		List<TbUser>list=this.TbUserMapper.selectByExample(example);
		if(list!=null && list.size()>0){
			return TaotaoResult.ok(false);
		}
		return TaotaoResult.ok(true);
	}

	@Override
	public TaotaoResult registerUser(TbUser user) {
		//用户名不能为为空！
		if(StringUtils.isBlank(user.getUsername())){
			return TaotaoResult.build(400, "用户名不能为空");
		}
		//判断用户名是否重复
		TaotaoResult result=checkUserData(user.getUsername(), 1);
		if(!(boolean) result.getData()){
			return TaotaoResult.build(400, "用户名不能重复");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return TaotaoResult.build(400, "用户密码不能为空！");
		}
		//用户邮箱，电话可以为空，但是不能重复
		if(StringUtils.isNotBlank(user.getPhone())){
			TaotaoResult phoneResult=checkUserData(user.getPhone(),2);
			if(!(boolean) phoneResult.getData()){
				return TaotaoResult.build(400, "用户手机不能重复");
			}
		}
		if(StringUtils.isNotBlank(user.getEmail())){
			TaotaoResult emailResult=checkUserData(user.getPhone(),3);
			if(!(boolean) emailResult.getData()){
				return TaotaoResult.build(400, "用户邮箱不能重复");
			}
		}
		user.setCreated(new Date());
		user.setUpdated(new Date());
		String md5Pass=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		this.TbUserMapper.insert(user);
		System.out.println("用户注册成功！");
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult login(String username, String password) {
		//效验用户数据
		TbUserExample example=new TbUserExample();
		Criteria criteria=example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list=this.TbUserMapper.selectByExample(example);
		if(list==null || list.size()==0){
			return TaotaoResult.build(400, "用户民或密码不正确");
		}
		TbUser user=list.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			System.out.println("用户密码不正确");
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		//生成token 使用uuid
		String token=UUID.randomUUID().toString();
		//redis中插入token信息
		user.setPassword(null);
		jedisClient.set(USER_SESSION+":"+token, JsonUtils.objectToJson(user));
		jedisClient.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
		System.out.println("向redis中插入token成功：token.key="+USER_SESSION+":"+token);
		//返回结果
		return TaotaoResult.ok(token);
		
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		String json=jedisClient.get(USER_SESSION+":"+token);
		System.out.println("json:"+json);
		if(StringUtils.isBlank(json)){
			return TaotaoResult.build(400, "用户登录已经过期！");
		}
		//重置json过期时间
		jedisClient.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
		//把json转换成对象
		TbUser user=JsonUtils.jsonToPojo(json, TbUser.class);
		return TaotaoResult.ok(user);
		//return TaotaoResult.ok(json);
	}

	@Override
	public TaotaoResult deleteUserByToken(String token) {
		if(!jedisClient.exists(USER_SESSION+":"+token)){
			System.out.println("==删除的token不在redis之中===");
		}
		String json=jedisClient.get(USER_SESSION+":"+token);
		if(StringUtils.isBlank(json)){
			return TaotaoResult.build(400, "要删除的用户token不存在！");
		}
		jedisClient.expire(USER_SESSION+":"+token, 20);
		return TaotaoResult.ok();
	}

}



