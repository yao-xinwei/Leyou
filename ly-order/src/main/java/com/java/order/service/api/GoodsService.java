package com.java.order.service.api;

import com.java.item.client.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;



@FeignClient(value = "api-gateway", path = "/api/item")
public interface GoodsService extends GoodsApi {

}
