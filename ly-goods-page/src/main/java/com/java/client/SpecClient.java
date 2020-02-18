package com.java.client;

import com.java.item.client.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service") //fegin客户端,调用其他服务
public interface SpecClient extends SpecApi {

}
