package com.java.item.service;

import com.java.item.mapper.CategoryMapper;
import com.java.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    //查询分类信息
    public List<Category> queryByParenId(Integer id) {
        // 以下相当于 select * from tb_category where parent_id = id;
        Category category = new Category();
        category.setParentId(id);
        return categoryMapper.select(category);
    }
    //根据品牌id查找分类信息（这个品牌下面的所有分类）
    public List<Category> queryByBrandId(Long bid) {
        return categoryMapper.queryByBrandId(bid);
    }
    //查询分类
    public List<String> queryNameByIds(List<Long> asList) {
        //根据cid查询分类名称
        List<String> strings = new ArrayList<>(); //保存查到的分类的名字
        //select * from tb_category where id in(asList)
        List<Category> categories = categoryMapper.selectByIdList(asList);
        //只要名字
        categories.forEach(t->{
            strings.add(t.getName());
        });
        return strings;
    }
}
