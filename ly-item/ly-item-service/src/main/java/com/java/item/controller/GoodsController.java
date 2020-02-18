package com.java.item.controller;

import com.java.item.bo.SpuBo;
import com.java.item.pojo.Sku;
import com.java.item.pojo.Spu;
import com.java.item.pojo.SpuDetail;
import com.java.item.service.GoodsService;
import com.java.po.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    //显示商品列表 spu/page?key=&saleable=true&page=1&rows=5
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(@RequestParam(value = "key",required = false) String key,
                                                            @RequestParam(value = "saleable",required = false) Boolean saleable,
                                                            @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                            @RequestParam(value = "rows",defaultValue = "5") Integer rows){
        PageResult<SpuBo> pageResult = goodsService.querySpuByPage(key,saleable,page,rows);
        if(pageResult != null && pageResult.getItems().size()>0){
            return ResponseEntity.ok(pageResult); //返回两个部分，一个是数据list，一个是响应码 200 ，ok就是200
        }
        //没有查到数据则返回，返回一个响应码
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    //添加商品信息
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        goodsService.saveGoods(spuBo);
        //返回一个响应码
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //根据spuid查询商品详请表（spu_detail）
    @GetMapping("spu/detail/{spuid}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuid") Long spuid){
        SpuDetail  spuDetail= goodsService.querySpuDetailBySpuId(spuid);
        if(spuDetail != null){
            return ResponseEntity.ok(spuDetail); //返回两个部分，一个是数据list，一个是响应码 200 ，ok就是200
        }
        //没有查到数据则返回，返回一个响应码
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    //根据spuid查询sku表
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBYSpuId(@RequestParam("id") Long id){
        List<Sku> skuList = goodsService.querySkuBYSpuId(id);
        if(skuList != null && skuList.size()>0){
            return ResponseEntity.ok(skuList); //返回两个部分，一个是数据list，一个是响应码 200 ，ok就是200
        }
        //没有查到数据则返回，返回一个响应码
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    //修改商品
    @PutMapping("goods")
    public ResponseEntity<Void> udpateGoods(@RequestBody SpuBo spuBo){
        goodsService.udpateGoods(spuBo);
        //返回一个响应码
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //根据spuId查询spu数据
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = goodsService.querySpuById(id);
        if(spu != null){
            return ResponseEntity.ok(spu); //返回两个部分，一个是数据list，一个是响应码 200 ，ok就是200
        }
        //没有查到数据则返回，返回一个响应码
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
