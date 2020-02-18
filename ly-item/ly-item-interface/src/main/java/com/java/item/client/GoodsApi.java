package com.java.item.client;

import com.java.item.bo.SpuBo;
import com.java.item.pojo.Sku;
import com.java.item.pojo.Spu;
import com.java.item.pojo.SpuDetail;
import com.java.po.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GoodsApi {
    //显示商品列表 spu/page?key=&saleable=true&page=1&rows=5
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(@RequestParam(value = "key",required = false) String key,
                                                            @RequestParam(value = "saleable",required = false) Boolean saleable,
                                                            @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                            @RequestParam(value = "rows",defaultValue = "5") Integer rows);

    //根据spuid查询商品详请表（spu_detail）
    @GetMapping("spu/detail/{spuid}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuid") Long spuid);
    //根据spuid查询sku表
    @GetMapping("sku/list")
    public List<Sku> querySkuBYSpuId(@RequestParam("id") Long id);
    //根据spuId查询spu数据 item/88.html
    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long spuId);

}
