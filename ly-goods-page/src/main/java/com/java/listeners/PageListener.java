package com.java.listeners;


import com.java.service.FileService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageListener {
    @Autowired
    private FileService fileService;

    //消息的消费者
    @RabbitListener(bindings = @QueueBinding(
            //  队列名称随便写，但不能和其他的重名
            value = @Queue(value = "ly.page.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",//交换机的名字
                    type = ExchangeTypes.TOPIC
            ),
            //接受发送的消息（要和前面的发送的信息一致）
            key = {"item.insert","item.update"}))
    //                  接受的前面穿传来的商品id
    public void listenPage(Long id){
        //创建新的静态页面
        fileService.syncCreateHtml(id);
    }
    //消息的消费者
    @RabbitListener(bindings = @QueueBinding(
            //  队列名称随便写，但不能和其他的重名
            value = @Queue(value = "ly.page.queue1", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",//交换机的名字
                    type = ExchangeTypes.TOPIC
            ),
            //接受发送的消息（要和前面的发送的信息一致）
            key = {"item.delete"}))
    //                  接受的前面穿传来的商品id
    public void deletePage(Long id){
        //删除静态页面
        fileService.deleteHtml(id);
    }
}
