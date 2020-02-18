package com.java.item.client;

import com.java.item.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;


public interface CategoryApi {
    //查询分类信息
    @GetMapping("category/list") //相当于@RequestMapping(value="",method="get")
    public List<Category> queryByParenId(@RequestParam("pid") Integer id);
    //根据品牌id查找分类信息（这个品牌下面的所有分类）
    @GetMapping("category/bid/{bid}") //http://api.leyou.com/api/item/category/bid/325401
    public List<Category> queryByBrandId(@PathVariable("bid") Long bid);
    //根据分类id查询分类名称
    @GetMapping("category/names")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);
}
