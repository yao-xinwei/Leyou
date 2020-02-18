package com.java.item.client;

import com.java.item.pojo.Brand;
import com.java.po.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BrandApi {
    //查询品牌信息
    //http://api.leyou.com/api/item/brand/page?page=1&rows=5&sortBy=id&desc=false&key=
    @GetMapping("brand/page")
    public PageResult<Brand> pageQuery(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                       @RequestParam(value = "rows",defaultValue = "5") Integer rows,
                                                       @RequestParam( value = "sortBy",required = false) String sortBy,
                                                       @RequestParam(value = "desc",required = false) Boolean desc,
                                                       @RequestParam(value = "key",required = false) String  key);


    //根据cid查询品牌信息 brand/cid/76
    @GetMapping("brand/cid/{cid}")
    public List<Brand> queryBrandByCategory(@PathVariable("cid") Long cid);
}
