package taotao.com.sso.service;

import taotao.com.common.pojo.TaotaoResult;
import taotao.com.pojo.TbUser;

public interface UserService {
    TaotaoResult checkUserData(String data,int type);
    TaotaoResult registerUser(TbUser user);
    TaotaoResult login(String username,String password);
    TaotaoResult getUserByToken(String token);
    TaotaoResult deleteUserByToken(String token);
}
