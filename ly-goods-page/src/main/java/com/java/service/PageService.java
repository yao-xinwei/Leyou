package com.java.service;

import com.java.client.GoodsClient;
import com.java.client.SpecClient;
import com.java.item.pojo.SpecGroup;
import com.java.item.pojo.SpecParam;
import com.java.item.pojo.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecClient specClient;
    //查询商品的详情
    public Map<String,Object> loadData(Long spuId) {
        Map<String,Object> map = new HashMap<String,Object>();
        //前端页面需要的值spu
        Spu spu = goodsClient.querySpuById(spuId);
        map.put("spu",spu);
        //前端页面需要的值spuDetail
        map.put("spuDetail",goodsClient.querySpuDetailBySpuId(spuId));
        //前端页面需要的值skus
        map.put("skus",goodsClient.querySkuBYSpuId(spuId));
        //前端页面需要的值specParams
        List<SpecParam> specParamList = specClient.querySpecParams(null, spu.getCid3(), null, null);
        //要需要的数据
        Map<Long,Object> specMap = new HashMap<Long,Object>();
        specParamList.forEach(t->{
            specMap.put(t.getId(),t.getName());
        });
        map.put("specParams",specMap);
        //前端页面需要的值specGroups
        List<SpecGroup> specGroupList = specClient.querySpecGroups(spu.getCid3());
        map.put("specGroups",specGroupList);
        return map;
    }
}
