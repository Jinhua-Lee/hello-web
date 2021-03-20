package com.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/**
 * 用户实体类
 * @author Jinhua
 */
@Data
@NoArgsConstructor
public class User {

    /**
     * 自增主键
     */
    private Integer id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 性别
     */
    private String sex;
    /**
     * 家乡
     */
    private String home;
    /**
     * 备注信息
     */
    private String info;

    public User(String name, String pwd, String sex, String home, String info) {
        this.name = name;
        this.pwd = pwd;
        this.sex = sex;
        this.home = home;
        this.info = info;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }
}