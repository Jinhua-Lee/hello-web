package com.dao;

import com.domain.PageBean;
import com.domain.User;

/**
 * 用户访问Dao层
 *
 * @author Jinhua
 */
public interface UserDao {

    /**
     * 返回所有用户信息
     *
     * @return 返回所有用户信息
     */
    PageBean<User> getUserAll();

    /**
     * 根据用户实体添加到用户Usr表
     *
     * @param user 用户实体
     * @return 添加结果
     */
    boolean addUser(User user);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    int deleteUserById(int id);

    /**
     * 根据ID更新用户信息
     *
     * @param user 带ID的用户信息
     * @return 更新结果
     */
    int updateUserById(User user);

    /**
     * 通过用户名和密码找到用户
     *
     * @param name     用户名
     * @param password 密码
     * @return 用户
     */
    User findUserByNamePwd(String name, String password);

    /**
     * 多条件组合查询用户
     *
     * @param user 用户查询条件
     * @return 分页查询用户
     */
    PageBean<User> multiConditionQuery(User user);

    /**
     * 分页查询用户
     *
     * @param pageCurrent 当前页数
     * @param pageSize    页大小
     * @param user        用户查询条件
     * @return 分页查询结果
     */
    PageBean<User> findAll(int pageCurrent, int pageSize, User user);
}
