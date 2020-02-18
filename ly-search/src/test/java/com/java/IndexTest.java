package com.java;

import com.java.client.GoodsClient;
import com.java.dao.GoodsDao;
import com.java.item.bo.SpuBo;
import com.java.po.PageResult;
import com.java.pojo.Goods;
import com.java.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)//spring和junit集成
@SpringBootTest(classes = LySearchService.class)//要测试的类
public class IndexTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;//一个模板类(spring提供)，操作其就相当于操作索引库
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private IndexService indexService;
    @Test
    public void init(){
        //建库
        elasticsearchTemplate.createIndex(Goods.class);
        //建表(映射关系)
        elasticsearchTemplate.putMapping(Goods.class);
    }
    //添加数据
    @Test
    public void loadDate(){
        int page = 1;
        while (true){//不断查询数据库
            //使用feignCilent 调用商品微服务
            PageResult<SpuBo> pageResult = goodsClient.querySpuByPage(null, null, page, 50);
            if(pageResult == null){//说明数据查询完毕
                break;
            }
            page++;//第二页......
            //查询的数据
            List<SpuBo> items = pageResult.getItems();

            List<Goods> list = new ArrayList<>();
            //将查询的spubo数据变成goods数据
            items.forEach(t->{
                Goods goods = indexService.buildGoods(t);//转变后的结果
                list.add(goods);//保存
            });
            //保存到索引库
            goodsDao.saveAll(list);// 接收对象集合，实现批量新增
        }
    }

}
