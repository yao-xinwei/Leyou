package com.java.item.service;

import com.java.item.mapper.SpecGroupMapper;
import com.java.item.mapper.SpecParamMapper;
import com.java.item.pojo.SpecGroup;
import com.java.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;
    //查询规格组和规格组里面对应的规格参数
    public List<SpecGroup> querySpecGroups(Long cid) {
        //select * from tb_spec_group where cid = #{cid}
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        //通过分类的id查询规格参数
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        //查询规格组里面的规格参数  f是list中的一个对象
       /* for (SpecGroup a:specGroupList) {
            下面的循环就这这个，只是用了不一样的方法
        }*/
       specGroupList.forEach(f->{
            //获取规格组里面的id
            Long id = f.getId(); // id多个
            //查询规格参数
            SpecParam specParam = new SpecParam();
            specParam.setGroupId(id);
            //通过规格组id查询规格参数
            // select * form tb_spec_param where group_id = id;
            List<SpecParam> specParamList = specParamMapper.select(specParam);
            //再给规格组（封装）
            f.setSpecParams(specParamList);
        });
        return specGroupList;
    }
    //查询规格组对应的规格参数
    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        return specParamMapper.select(specParam);
    }
    //新增规格组
    @Transactional
    public void addGroup(SpecGroup specGroup) {
        specGroupMapper.insertSelective(specGroup);
    }
    //删除规格组
    @Transactional
    public void deleteGroup(Long gid) {
        specGroupMapper.deleteByPrimaryKey(gid);
    }
    //修改规格组
    @Transactional
    public void upateGroup(SpecGroup specGroup) {
        //可以返回主键 主键的值在实体中（对应的字段）添加后返回
        specGroupMapper.updateByPrimaryKey(specGroup);
    }
    //添加规格组下面的规格参数
    @Transactional
    public void addParam(SpecParam specParam) {
        //可以返回主键 主键的值在实体中（对应的字段）添加后返回
        specParamMapper.insertSelective(specParam);
    }
    //删除规格组下的规格参数
    @Transactional
    public void deleteParam(Long pid) {
        specParamMapper.deleteByPrimaryKey(pid);
    }
    //修改规格组下面的规格参数
    @Transactional
    public void updateParam(SpecParam specParam) {
        //可以返回主键 主键的值在实体中（对应的字段）添加后返回
        specParamMapper.updateByPrimaryKey(specParam);
    }
}
