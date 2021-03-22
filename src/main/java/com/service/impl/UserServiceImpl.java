package com.service.impl;

import com.domain.PageBean;
import com.domain.User;
import com.dao.DaoFactory;
import com.dao.UserDao;
import com.service.UserService;

import java.util.Objects;

/**
 * 用户服务实现类
 *
 * @author Jinhua
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao = DaoFactory.getUserDao();

    @Override
    public boolean login(String name, String pwd) {
        User user = userDao.findUserByNamePwd(name, pwd);
        return user != null;
    }

    @Override
    public boolean register(User user) {
        return userDao.addUser(user);
    }

    @Override
    public PageBean<User> getUserAll() {
        return userDao.getUserAll();
    }

    @Override
    public boolean deleteUser(int id) {
        boolean flag = false;
        int row = userDao.deleteUserById(id);
        if (row > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updateUserInfo(User user) {
        boolean flag = false;
        int row = userDao.updateUserById(user);
        if (row > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public PageBean<User> multiConditionQuery(User user) {
        return userDao.multiConditionQuery(user);
    }

    @Override
    public PageBean<User> findAll(int pageCurrent, int pageSize, User user) {
        return userDao.findAll(pageCurrent, pageSize, user);
    }

    @Override
    public User findUserByName(String name) {
        return userDao.findUserByName(name);
    }
}
