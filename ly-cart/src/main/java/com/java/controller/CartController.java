package com.java.controller;

import com.java.pojo.Cart;
import com.java.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;
    //添加到购物车
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //显示购物车
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts(){
        List<Cart> cartList = cartService.queryCarts();
        if(cartList != null && cartList.size()>0){
            return ResponseEntity.ok(cartList);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //修改商品数量
    @PutMapping("increment")
    public ResponseEntity<Void> updateIncrementCart(@RequestBody Cart cart){
        cartService.updateIncrementCart(cart);
        return ResponseEntity.ok().build();
    }
    //删除购物车的商品信息
    @DeleteMapping("{skuid}")
    public ResponseEntity<Void> deleteCart0(@PathVariable("skuid") Long skuid){
        cartService.deleteCart(skuid);
        return ResponseEntity.ok().build();
    }

}
