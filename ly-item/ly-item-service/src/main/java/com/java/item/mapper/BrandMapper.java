package com.java.item.mapper;

import com.java.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    //向tb_category_brand表中添加数据
    @Insert("insert into tb_category_brand values(#{i},#{id})")
    void insertBrandCategory(@Param("i") Long i, @Param("id") Long id);
    //先删除tb_category_brand中的对应数据
    @Delete("delete from tb_category_brand where brand_id=#{id}")
    void deleteBrandCategory(@Param("id") Long id);
    //根据cid查询品牌信息
    @Select("select b.* from tb_brand b join tb_category_brand cb on(b.id = cb.brand_id) where cb.category_id=#{cid}")
    List<Brand> queryBrandByCategory(@Param("cid") Long cid);
}
