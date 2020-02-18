package com.java.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.java.auth.entity.UserInfo;
import com.java.interceptors.LoginInterceptor;
import com.java.pojo.Cart;
import com.java.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;//Redis模板

    private static final String PEX="ly:cart:uid:";
    //添加到购物车
    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //System.out.println("用户id"+userInfo.getId()+"用户名："+userInfo.getUsername());
        //获取key
        BoundHashOperations<String, Object, Object> bos = stringRedisTemplate.boundHashOps(PEX + userInfo.getId());
        //根据商品的id取值
        Object o = bos.get(cart.getSkuId().toString());//判断Redis中有没有重复，第一次为空
        System.out.println("打印结果："+cart.getSkuId().toString());
        System.out.println("打印结果bos："+bos);
        System.out.println("打印结果object："+o);
        if(o != null){//不为空
            //购物车中当前的商品已存在
            //首先从Redis中取出来，取出来的是一个json格式的字符串，要变成一个对象
            Cart cart1= JsonUtils.nativeRead(o.toString(), new TypeReference<Cart>() {
            });
            //redis中数量和页面的数量进行相加，得到总数量
            cart1.setNum(cart.getNum()+cart1.getNum());
            //页面中商品和Redis中的商品都要相加
            bos.put(cart.getSkuId().toString(), JsonUtils.serialize(cart1));
        }else{
            //开始放入购物车,Redis中              将数据变成字符串（Redis中只能放入字符串）
            bos.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
        }
    }
    //显示购物车
    public List<Cart> queryCarts() {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //获取key
        BoundHashOperations<String, Object, Object> bos = stringRedisTemplate.boundHashOps(PEX + userInfo.getId());
        //根据key获取值
        List<Object> values = bos.values();//从Redis中取数据
        List<Cart> cartList = new ArrayList<>();
        //将Object对象变成cart对象
        values.forEach(t->{
            Cart cart = JsonUtils.nativeRead(t.toString(), new TypeReference<Cart>() {
            });
            cartList.add(cart);
        });
        return cartList;
    }
    //修改商品数量
    public void updateIncrementCart(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //获取key
        BoundHashOperations<String, Object, Object> bos = stringRedisTemplate.boundHashOps(PEX + userInfo.getId());
        //根据商品的id取值
        Object o = bos.get(cart.getSkuId().toString());//取出的是json字符串，变成对象
        Cart cart1 = JsonUtils.nativeRead(o.toString(), new TypeReference<Cart>() {
        });
        //开始修改
        cart1.setNum(cart1.getNum()+1);
        //放入Redis中
        bos.put(cart.getSkuId().toString(),JsonUtils.serialize(cart1));
    }
    //删除购物车的商品信息
    public void deleteCart(Long skuid) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //获取key
        BoundHashOperations<String, Object, Object> bos = stringRedisTemplate.boundHashOps(PEX + userInfo.getId());
        bos.delete(skuid.toString());
    }
}
