package com.hang.reiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.reiji.domain.User;

public interface UserService extends IService<User> {
    User mySave(String phone);
}
