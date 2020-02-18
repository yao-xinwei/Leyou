package com.java.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class UploadService {
    //上传图片 存到本地
   /* public String uploadImage(MultipartFile file) {
        //保存到本地
        //创建一个file
        File file1 = new File("D:\\img");
        if(!file1.exists()){ //判断是否存在
            file1.mkdir(); //没有就创建文件夹
        }
        //保存图片到文件夹
        try {                     //放的位置（父文件夹） 图片的原始名称（文件名）
            file.transferTo(new File(file1,file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "http://image.leyou.com/"+file.getOriginalFilename();
    }*/
    @Autowired
    FastFileStorageClient storageClient;
    public String uploadImage(MultipartFile file){
        String url=null;
        // 上传
       /* StorePath storePath = this.storageClient.uploadFile(
                new FileInputStream(file), file.length(), "png", null);*/
       //获取浏览器传来的图片的后缀
        String originalFilename = file.getOriginalFilename();//获取文件的名字
        String ext = StringUtils.substringAfter(originalFilename, ".");//截取最后一个以.结尾的后缀
        //开始上传
        try {                           //文件输入流         文件的大小   文件的后缀  是否是集合
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            //获取上传后的地址
            String fullPath = storePath.getFullPath();
            url = "http://image.leyou.com/"+fullPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
