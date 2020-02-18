package com.java.client;

import com.java.item.client.SpecApi;
import com.java.item.pojo.SpecParam;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient("item-service") //fegin客户端,调用其他服务
public interface SpecClient extends SpecApi {

}
