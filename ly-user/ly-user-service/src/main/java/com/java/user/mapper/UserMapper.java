package com.java.user.mapper;

import com.java.user.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    //验证用户名是否在数据库中已存在
    @Select("select count(*) from tb_user where username = #{username}")
    public int queryUserName(@Param("username") String username);
}
