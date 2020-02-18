package com.java.dao;

import com.java.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//                                                      指定实体类  实体中主键id类型
public interface GoodsDao extends ElasticsearchRepository<Goods,Long> {
}
