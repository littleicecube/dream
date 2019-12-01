package com.palace.dream.simple.service;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dream.common.Result;
import com.palace.dream.simple.dao.UserDao;

@Service
public class UserService {

	@Resource
	UserDao userDao;
	
	public Result addUser(Map<String,Object> map){
		long ll = userDao.addUser(map);
		if(ll > 0 ) {
			return Result.succ();
		}
		return  Result.err("用户添加失败");
	}
}
