package com.domain;

import lombok.Data;

import java.util.List;

/**
 * 分页结果封装
 *
 * @author Jinhua
 */
@Data
public class PageBean<T> {

    /**
     * 总页数
     */
    private int totalPages;
    /**
     * 当前页
     */
    private int pageCurrent;
    /**
     * 总记录数
     */
    private int totalRecord;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 当前页的数据
     */
    private List<T> beanList;
    /**
     * 查询的条件
     */
    private String url;

    /**
     * 每次请求时需要设置
     * 计算总页数 = 总记录数 / 每页记录数，有余数加一
     */
    public void setTotalPages() {
        int totalPages = totalRecord / pageSize;
        this.totalPages = totalRecord % pageSize == 0 ? totalPages : totalPages + 1;
    }
}
