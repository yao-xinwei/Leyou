package com.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * 用于产生静态文件
 */
@Service
public class FileService {
    @Value("${ly.thymeleaf.destPath}")
    private String path;// = D:/html/item
    @Autowired
    private PageService pageService;
    @Autowired
    private TemplateEngine templateEngine;//模板引擎
    //判断路径（ D:/html/item）下面有没有spuId.HTML文件
    public boolean exists(Long spuId) {
        File file = new File(path);
        if(!file.exists()){//判断文件是否存在(不存在)
            //不存在就创建文件夹
            file.mkdirs();
        }
        //在路径下有没有HTML文件
        return new File(file,spuId+".html").exists();//是否有文件
    }
    //创建文件
    public void syncCreateHtml(Long spuId) {
        //创建上下文对象
        Context context = new Context();
        //往上下文中放入数据
        context.setVariables(pageService.loadData(spuId));//（查询的数据放到里面）
        //创建一个文件对象
        File file = new File(path,spuId+".html");
        try {
            PrintWriter printWriter = new PrintWriter(file,"utf-8");//输出流,字符集编码
            //                  模板名（就是要变成静态页面的HTML） 需要上下文中的数据  输入流
            templateEngine.process("item",context,printWriter);//产生静态文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //删除静态页面
    public void deleteHtml(Long id) {
        File file = new File(path, id + ".html");
        file.deleteOnExit();//如果存在就删除
    }
}
