package com.java.item.controller;

import com.java.item.pojo.Category;

import com.java.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 相当于@ResponseBody 和 @Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    //查询分类信息
    @GetMapping("list") //相当于@RequestMapping(value="",method="get")
    public ResponseEntity<List<Category>> queryByParenId(@RequestParam("pid") Integer id){
        List<Category> list = categoryService.queryByParenId(id);
        if(list != null && list.size()>0){
            return ResponseEntity.ok(list); //返回两个部分，一个是数据list，一个是响应码 200 ，ok就是200
        }
        //没有查到数据则返回，返回一个响应码
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    //根据品牌id查找分类信息（这个品牌下面的所有分类）
    @GetMapping("bid/{bid}") //http://api.leyou.com/api/item/category/bid/325401
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid){
        List<Category> categories = categoryService.queryByBrandId(bid);
        if(categories != null && categories.size()>0){
            return ResponseEntity.ok(categories);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    //根据分类id查询分类名称
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids") List<Long> ids){
        List<String> nameByIds = categoryService.queryNameByIds(ids);
        if(nameByIds != null && nameByIds.size()>0){
            return ResponseEntity.ok(nameByIds);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
