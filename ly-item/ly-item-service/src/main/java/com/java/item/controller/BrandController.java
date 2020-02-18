package com.java.item.controller;

import com.java.item.pojo.Brand;
import com.java.item.service.BrandService;
import com.java.po.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    BrandService brandService;

    //查询品牌信息
    //http://api.leyou.com/api/item/brand/page?page=1&rows=5&sortBy=id&desc=false&key=
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> pageQuery(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                       @RequestParam(value = "rows",defaultValue = "5") Integer rows,
                                                       @RequestParam( value = "sortBy",required = false) String sortBy,
                                                       @RequestParam(value = "desc",required = false) Boolean desc,
                                                       @RequestParam(value = "key",required = false) String  key){
        PageResult<Brand> pageList = brandService.pageQuery(page,rows,sortBy,desc,key);
        if(pageList!=null && pageList.getItems().size()>0){
            return ResponseEntity.ok(pageList);//返回两个部分，一个是数据list，一个是响应码 200 ，ok就是200
        }
        //没有查到数据则返回，返回一个响应码
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    //添加品牌信息 http://api.leyou.com/api/item/brand
    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand,@RequestParam("cids") List<Long> cids){
        brandService.addBrand(brand,cids);
        //返回一个响应码
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //编辑的提交 http://api.leyou.com/api/item/brand
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand,@RequestParam("cids") List<Long> cids){
        brandService.updateBrand(brand,cids);
        //返回一个响应码
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //根据cid查询品牌信息 brand/cid/76
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategory(@PathVariable("cid") Long cid){
        List<Brand> brandList= brandService.queryBrandByCategory(cid);
        if(brandList!=null && brandList.size()>0){
            return ResponseEntity.ok(brandList);//返回两个部分，一个是数据list，一个是响应码 200 ，ok就是200
        }
        //没有查到数据则返回，返回一个响应码
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
