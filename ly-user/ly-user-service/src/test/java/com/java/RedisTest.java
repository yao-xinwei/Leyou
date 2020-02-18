package com.java;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)//spring和junit集成
@SpringBootTest(classes = LyUserService.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;//spring和Redis集成的模板类

    @Test
    public void testRedis() {
        // 存储数据         一个对象
        // redis数据库命令：set key1 value1
        this.redisTemplate.opsForValue().set("key1", "value1");
        // 获取数据
        //redis数据库命令：get key1
        String val = this.redisTemplate.opsForValue().get("key1");
        System.out.println("val = " + val);
    }

    @Test
    public void testRedis2() {
        // 存储数据，并指定剩余生命时间,5小时，第三个参数是存活时间
        this.redisTemplate.opsForValue().set("key2", "value2",
                1, TimeUnit.MINUTES);
    }

    @Test
    public void testHash(){
        //Redis中的数据类型对应java数据类型Map<String,Map<String,String>>
        //使用key，user操作对应map  Map<user,Map<key,value>>
        BoundHashOperations<String, Object, Object> hashOps =
                this.redisTemplate.boundHashOps("user");//key
        // 操作hash数据
        hashOps.put("name", "jack");//Map<name,jack>
        hashOps.put("age", "22");

        // 获取单个数据
        Object name = hashOps.get("name");
        System.out.println("name = " + name);

        // 获取所有数据
        //entries是获取map中的map（Map<String,String>）
        Map<Object, Object> map = hashOps.entries();
        for (Map.Entry<Object, Object> me : map.entrySet()) {
            System.out.println(me.getKey() + " : " + me.getValue());
        }
    }
}