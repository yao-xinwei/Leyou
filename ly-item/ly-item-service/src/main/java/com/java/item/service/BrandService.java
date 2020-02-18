package com.java.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.java.item.mapper.BrandMapper;
import com.java.item.pojo.Brand;
import com.java.po.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    BrandMapper brandMapper;
    //查询品牌信息
    public PageResult<Brand> pageQuery(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //查询多少条分页助手给(利用反射机制)
        //开启分页
        PageHelper.startPage(page,rows);
        Example example = new Example(Brand.class); //相当于where 后面的条件
        // 模糊查询 相当于select * from brand  where name like %key%;
        if(StringUtils.isNoneBlank(key)){//判断不为空
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name","%"+key+"%");
        }
        //排序 select * from brand  where name like %key% order by sortBy desc
        if(StringUtils.isNoneBlank(sortBy)){//判断不为空
            example.setOrderByClause(sortBy + (desc?" desc":" asc"));
        }
        //查询数据库
        Page<Brand> brands = (Page<Brand>)brandMapper.selectByExample(example);

        //分页需要的数据
        //    private Long total;//一共有多少条数据
        //    private Long totalPage;//一共多少页
        //    private List<T> items;//每页显示的数据  new Long()返回的是一个int类型，我们定义的是Long类型
        return new PageResult<>(brands.getTotal(),new Long(brands.getPages()),brands.getResult());
    }
    //添加品牌信息
    @Transactional //开启事务
    public void addBrand(Brand brand, List<Long> cids) {
        //把数据添加到品牌表
        //可以返回主键 主键的值在实体中（对应的字段）添加后返回
        brandMapper.insertSelective(brand);
        //把分类添加到tb_category_brand表中
        for (Long i:cids) {
            brandMapper.insertBrandCategory(i,brand.getId());
        }
    }
    //编辑的提交
    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {
        //更新tb_brand
        brandMapper.updateByPrimaryKey(brand);
        //先删除tb_category_brand中的对应数据
        brandMapper.deleteBrandCategory(brand.getId());
        //再添加tb_category_brand
        cids.forEach(t->{
            brandMapper.insertBrandCategory(t,brand.getId());
        });
    }
    //根据cid查询品牌信息
    public List<Brand> queryBrandByCategory(Long cid) {
        return brandMapper.queryBrandByCategory(cid);
    }
}
