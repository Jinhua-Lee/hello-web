package com.dao.impl;

import com.domain.PageBean;
import com.domain.User;
import com.dao.UserDao;
import com.util.C3P0Utils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * 用户访问Dao实现
 *
 * @author Jinhua
 */
public class UserDaoImpl implements UserDao {
    private final QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());

    /**
     * 从数据库获取所有用户并返回构造的实体集合
     *
     * @return 返回所有用户的集合
     */
    @Override
    public PageBean<User> getUserAll() {
        PageBean<User> pageBean = new PageBean<>();
        pageBean.setPageCurrent(1);
        pageBean.setPageSize(1);

        // 先构造一个空的用户集合
        List<User> users;
        String sql = "Select * From Usr";
        String countSql = "Select Count(*) From Usr";

        try {
            // 获取初始总记录数和所有记录
            users = qr.query(sql, new BeanListHandler<>(User.class));
            Number num = qr.query(countSql, new ScalarHandler<>());
            pageBean.setTotalRecord(num.intValue());
            pageBean.setBeanList(users);
            pageBean.setTotalPages();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pageBean;
    }

    /**
     * 根据表单数据进行用户注册
     *
     * @param user 表单数据封装为用户实体，进行注册
     * @return 返回添加结果
     */
    @Override
    public boolean addUser(User user) {
        boolean flag = false;
        int row = 0;
        String sql = "Insert into Usr(Name, Pwd, Sex, Home, Info) values(?, ?, ?, ?, ?);";
//		row = MyDbUtils.executeUpdate(sql,
//				user.getName(), user.getPwd(), user.getSex(), user.getHome(), user.getInfo());
        try {
            row = qr.update(sql, user.getName(), user.getPwd(), user.getSex(), user.getHome(), user.getInfo());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (row > 0) {
            flag = true;
        }

        return flag;
    }

    /**
     * 根据Id值删除用户
     *
     * @param id 传入的参数id
     * @return 返回受影响的行数, 即删除用户的个数
     */
    @Override
    public int deleteUserById(int id) {
        int row;
        String sql = "Delete From Usr Where ID = ?";

        try {
            row = qr.update(sql, id);
        } catch (SQLException e) {
            throw new RuntimeException("删除用户失败");
        }

        return row;
    }

    /**
     * 通过id值查找，更新用户信息
     *
     * @param user 要更新到的用户实体
     * @return 返回受影响的行数
     */
    @Override
    public int updateUserById(User user) {
        int row = 0;
        String sql = "Update Usr set " +
                "Name = ?, Pwd = ?, Sex = ?, Home = ?, Info = ? Where id = ?";
        try {
            row = qr.update(sql, user.getName(), user.getPwd(),
                    user.getSex(), user.getHome(), user.getInfo(),
                    user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return row;
    }

    /**
     * 通过用户名和密码来找到用户
     *
     * @param name     用户名
     * @param password 密码
     * @return 返回一个用户实体
     */
    @Override
    public User findUserByNamePwd(String name, String password) {
        User user;
        String sql = "select * from usr where name = ? and pwd = ?";
        try {
            user = qr.query(sql, new BeanHandler<>(User.class), name, password);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("用户不存在");
        }
    }

    /**
     * 给出用户实体条件，获得条件查询的SQL语句
     *
     * @param user 用户实体的条件
     * @return 返回一个SQL语句的String对象，后面可以加limit
     */
    private String getWhere(User user, StringBuilder sb) {
        if (!user.getName().trim().isEmpty() && user.getName() != null) {
            sb.append(" And Name = '").append(user.getName()).append("' ");
        }
        if (!user.getSex().trim().isEmpty() && user.getSex() != null) {
            sb.append(" And Sex = '").append(user.getSex()).append("' ");
        }
        if (!user.getHome().trim().isEmpty() && user.getHome() != null) {
            sb.append(" And Home = '").append(user.getHome()).append("' ");
        }
        if (!user.getInfo().trim().isEmpty() && user.getInfo() != null) {
            sb.append(" And Info = '").append(user.getInfo()).append("' ");
        }
        return sb.toString();
    }


    /**
     * 多条件组合查询：
     * 初始进入页面，页面参数默认，每条一页记录，不带条件
     * 1. 给出SQL语句前缀
     * 2. 条件组合，判断是否为空，不为空则拼接
     *
     * @param user 条件组合的用户实体
     * @return 返回符合条件的用户集合
     */
    @Override
    public PageBean<User> multiConditionQuery(User user) {
        PageBean<User> pageBean = new PageBean<>();
        pageBean.setPageCurrent(1);
        pageBean.setPageSize(1);
        pageBean.setTotalPages();
        System.out.println(user.toString());
        String sql = getWhere(user, new StringBuilder("Select * From Usr Where 1 = 1 "));

        try {
            List<User> users = qr.query(sql, new BeanListHandler<>(User.class));
            pageBean.setBeanList(users);
            return pageBean;
        } catch (SQLException e) {
            throw new RuntimeException("多条件组合查询出错" + e);
        }
    }

    /**
     * 分页 + 多条件组合查询
     *
     * @param pageCurrent 当前页
     * @param pageSize    每页的记录数
     * @param user        用户条件实体
     * @return 返回一个PageBean实体对象
     */
    @Override
    public PageBean<User> findAll(int pageCurrent, int pageSize, User user) {
        // 1. 设置PageBean对象的基本属性
        PageBean<User> pb = new PageBean<>();
        pb.setPageCurrent(pageCurrent);
        pb.setPageSize(pageSize);

        // 2. 得到总记录数
        String countSql = getWhere(user, new StringBuilder("Select Count(*) From Usr Where 1 = 1 "));
        try {
            Number num = qr.query(countSql, new ScalarHandler<>());
            int totalRecord = num.intValue();
            pb.setTotalRecord(totalRecord);

            // 设置总页数，根据对象中的totalRecord属性和pageSize自动设置，不需要参数
            pb.setTotalPages();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 3. 得到BeanList
        StringBuilder limitSql = new StringBuilder(getWhere(user,
                new StringBuilder("Select * From Usr Where 1 = 1 "))).append(" limit ?, ?");
        List<User> users = null;
        try {
            users = qr.query(limitSql.toString(), new BeanListHandler<>(User.class),
                    (pageCurrent - 1) * pageSize, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pb.setBeanList(users);
        return pb;
    }

    @Override
    public User findUserByName(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            return null;
        }
        String byNameSql = "select * from usr where name = ?";
        try {
            return qr.query(byNameSql, new BeanHandler<>(User.class), name);
        } catch (SQLException se) {
            throw new RuntimeException("按姓名查询用户出错！！！ " + se);
        }
    }
}
