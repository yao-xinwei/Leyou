package com.java.service;


import com.java.dao.GoodsDao;
import com.java.po.PageResult;
import com.java.pojo.Goods;
import com.java.utils.SearchRequest;

import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    @Autowired
    private GoodsDao goodsDao;


    public PageResult<Goods> search(SearchRequest searchRequest) {
        //用户搜索关键字
        String key = searchRequest.getKey();
        //第几页
        Integer page = searchRequest.getPage();
        //创建查询对象NativeSearchQueryBuilder 原生的查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //查询条件                                                      查询字段   传过来的值  操作符
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("all",key).operator(Operator.AND));
        //分页                                                索引库从0开始查  每页显示的条数 20
        nativeSearchQueryBuilder.withPageable(PageRequest.of(page-1,searchRequest.getSize()));
       //过滤                                                                       前端需要的字段
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));
        //搜索
        Page<Goods> goodsPage = goodsDao.search(nativeSearchQueryBuilder.build());
        //                               总条数                    总页数                             每页显示多少条
        return new PageResult<>(goodsPage.getTotalElements(),new Long(goodsPage.getTotalPages()),goodsPage.getContent());
    }
}
