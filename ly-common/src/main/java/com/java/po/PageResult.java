package com.java.po;

import lombok.Data;

import java.util.List;
@Data //自动生成Get、Set方法
public class PageResult<T> {
    private Long total;//一共有多少条数据
    private Long totalPage;//一共多少页
    private List<T> items;//每页显示的数据

    public PageResult() {
    }

    public PageResult(Long total, Long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
    public PageResult(Long total,  List<T> items) {
        this.total = total;
        this.items = items;
    }
}
