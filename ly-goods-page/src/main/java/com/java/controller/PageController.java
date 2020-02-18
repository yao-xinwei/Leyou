package com.java.controller;

import com.java.service.FileService;
import com.java.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {
    @Autowired
    private PageService pageService;
    @Autowired
    private FileService fileService;
    //查询商品的详情
    @GetMapping("item/{spuId}.html")
    public String toPage(@PathVariable("spuId") Long spuId, Model model){
        //将数据放入到model中，括号是一个map集合
        model.addAllAttributes(pageService.loadData(spuId));
        if(!fileService.exists(spuId)){//如果不存在，则创建文文件
            fileService.syncCreateHtml(spuId);//创建文件
        }
        return "item";
    }
}
