package com.java.item.mapper;

import com.java.item.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

                                                        //可以生成where id in(1,2,3)
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
    //根据品牌id查找分类信息（这个品牌下面的所有分类）
    @Select("select c.* from tb_category c,tb_category_brand cd where c.id = cd.category_id and brand_id = #{bid}")
    List<Category> queryByBrandId(@Param("bid") Long bid);


}
