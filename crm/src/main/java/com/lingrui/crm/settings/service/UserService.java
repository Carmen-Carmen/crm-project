package com.lingrui.crm.settings.service;

import com.lingrui.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-20 20:24
 * @ Version 1.0
 */
public interface UserService {
    User queryUserByLoginActAndPwd(Map<String, Object> map);

    List<User> queryAllUsers();

}
