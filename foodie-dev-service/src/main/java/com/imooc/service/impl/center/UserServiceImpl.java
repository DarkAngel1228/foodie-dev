package com.imooc.service.impl.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public boolean queryUsernameIsExist(String username) {
        return false;
    }

    @Override
    public Users createUser(UserBO userBO) {
        return null;
    }

    @Override
    public Users queryUserForLogin(String username, String password) {
        return null;
    }
}
