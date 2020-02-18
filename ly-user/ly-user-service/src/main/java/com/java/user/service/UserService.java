package com.java.user.service;

import com.java.user.mapper.UserMapper;
import com.java.user.pojo.User;
import com.java.user.utils.CodecUtils;
import com.java.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //唯一标识
    private static final String KEY = "user:code:phone:";
    //用户数据的校验，主要包括对：手机号、用户名的唯一性校验
    public Boolean check(String data, Integer type) {
        User user = new User();
        //判断要校验的数据类型：1，用户名；2，手机
        switch (type){
            case 1://1，用户名
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
        }
        //返回 true可用
        int i = userMapper.selectCount(user);
        System.out.println("验证"+i);
        Boolean bo = userMapper.selectCount(user) != 1;
        return bo;//查询总条数 为0是可用 不可能为1 数据库以规定
    }
    //发送短信功能
    public Boolean sendVertfyCode(String phone) {
        //产生验证码
        String s = NumberUtils.generateCode(5);//产生5位的验证码,随机产生
        //把验证码放到Redis中           第三个参数是：数据Redis中存活的时间,四：代表分钟
        stringRedisTemplate.opsForValue().set(KEY+phone,s,5, TimeUnit.MINUTES);
        //Redis中保存的是 user：code：phone:手机号（key） s（value）
        //发送短信(需要调用第三方接口)，需要传俩个数据手机号和验证码 此方法略
        return true;
    }
    //注册用户信息
    public Boolean createUser(User user, String code) {
        //校验验证码
        String s = stringRedisTemplate.opsForValue().get(KEY + user.getPhone());//现获取Redis中的key
        //验证是否取到值
        if(s == null){
            return false;
        }
        //如果取到数据, （若填的验证码不正确）
        if(!code.equals(s)){
            return false;
        }
        //数据添加到数据库中
        //验证用户名是否在数据库中已存在

        //数据库中没有查到数据则添加到数据库中
        //生成盐,随机生成
        String salt = CodecUtils.generateSalt();
        //加密 对密码加密
        String newpassword = CodecUtils.md5Hex(user.getPassword(), salt);//加密后的密码保存到数据库中
        //加密后的密码
        user.setPassword(newpassword);
        //时间
        user.setCreated(new Date());
        //盐
        user.setSalt(salt);
        boolean bool = userMapper.insertSelective(user)==1;
        if(bool){//true
            //删除Redis中的数据
            stringRedisTemplate.delete(KEY+user.getPhone());
        }
        return bool;
    }
    //查询用户名和密码
    public User queryUser(String username, String password) {
        //根据用户名查询用户
        User user = new User();
        user.setUsername(username);
        User user1 = userMapper.selectOne(user);
        if(user1 == null){//用户为空
            return null;
        }
        //查到进行加密，先取出盐
        String salt = user1.getSalt();
        //传来的密码进行加密
        String newpassword = CodecUtils.md5Hex(password, salt);
        //判断密码
        if(!user1.getPassword().equals(newpassword)){
            return null;
        }
        return user1;
    }
}
