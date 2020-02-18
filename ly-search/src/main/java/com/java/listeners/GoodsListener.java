package com.java.listeners;

import com.java.dao.GoodsDao;
import com.java.service.IndexService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {
    @Autowired
    private IndexService indexService;

    //消息的消费者
    @RabbitListener(bindings = @QueueBinding(
            //  队列名称随便写，但不能和其他的重名
            value = @Queue(value = "ly.item.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",//交换机的名字
                    type = ExchangeTypes.TOPIC
            ),
            //接受发送的消息（要和前面的发送的信息一致）
            key = {"item.insert","item.update"}))
    //                  接受的前面穿传来的商品id
    public void listen(Long id){
        //更新索引库
        indexService.createIndex(id);
    }
    //消息的消费者
    @RabbitListener(bindings = @QueueBinding(
            //  队列名称随便写，但不能和其他的重名
            value = @Queue(value = "ly.item.queue1", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",//交换机的名字
                    type = ExchangeTypes.TOPIC
            ),
            //接受发送的消息（要和前面的发送的信息一致）
            key = {"item.delete"}))
    //                  接受的前面穿传来的商品id
    public void delete(Long id){
        //更新索引库
        indexService.deleteIndex(id);
    }
}
