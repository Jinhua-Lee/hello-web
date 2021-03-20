package com.service;

import com.domain.PageBean;
import com.domain.User;

/**
 * 用户服务接口
 *
 * @author Jinhua
 */
public interface UserService {

    /**
     * 通过匹配数据库和输入框中的用户名和密码登录
     *
     * @param name 用户名
     * @param pwd  密码
     * @return 登录结果
     */
    boolean login(String name, String pwd);

    /**
     * 通过传入用户实体信息进行注册
     *
     * @param user 用户实体
     * @return 注册结果
     */
    boolean register(User user);

    /**
     * 分页返回用户信息
     *
     * @return 用户信息
     */
    PageBean<User> getUserAll();

    /**
     * 通过用户ID删除用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    public boolean deleteUser(int id);

    /**
     * 通过ID编号更新用户信息
     *
     * @param user 带ID的用户
     * @return 用户信息
     */
    boolean updateUserInfo(User user);

    /**
     * 多条件组合查询
     *
     * @param user 用户查询条件
     * @return 用户信息
     */
    PageBean<User> multiConditionQuery(User user);

    /**
     * 分页查询用户信息
     *
     * @param pageCurrent 页数
     * @param pageSize    页大小
     * @param user        用户查询条件
     * @return 分页用户信息
     */
    PageBean<User> findAll(int pageCurrent, int pageSize, User user);
}
