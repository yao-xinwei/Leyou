package com.java.item.controller;

import com.java.item.pojo.SpecGroup;
import com.java.item.pojo.SpecParam;
import com.java.item.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecController {
    @Autowired
    private SpecService specService;

    //查询规格组
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid){
        List<SpecGroup> specGroupList = specService.querySpecGroups(cid);
        if(specGroupList != null && specGroupList.size()>0){
            return ResponseEntity.ok(specGroupList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    //查询规格组对应的规格参数
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParams(@RequestParam(value = "gid",required = false) Long gid,
                                                           @RequestParam(value = "cid",required = false) Long cid,
                                                           @RequestParam(value = "searching",required = false) Boolean searching,
                                                           @RequestParam(value = "generic",required = false) Boolean generic){
        List<SpecParam> specParamList = specService.querySpecParams(gid,cid,searching,generic);
        if(specParamList != null && specParamList.size()>0){
            return ResponseEntity.ok(specParamList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    //新增规格组
    @PostMapping("group")
    public ResponseEntity<Void> addGroup(@RequestBody SpecGroup specGroup){
        System.out.println("前端传来的cid"+specGroup.getCid());
        System.out.println("前端传来的Name"+specGroup.getName());
        specService.addGroup(specGroup);
        //返回一个响应码
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //删除规格组
    @DeleteMapping("group/{gid}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("gid") Long gid){
        specService.deleteGroup(gid);
        //返回一个响应码
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //修改规格组
    @PutMapping("group")
    public ResponseEntity<Void> upateGroup(@RequestBody SpecGroup specGroup){
        specService.upateGroup(specGroup);
        //返回一个响应码
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //添加规格组下面的规格参数
    @PostMapping("param")
    public ResponseEntity<Void> addParam(@RequestBody SpecParam specParam){
        specService.addParam(specParam);
        //返回一个响应码
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //删除规格组下的规格参数
    @DeleteMapping("param/{pid}")
    public ResponseEntity<Void> deleteParam(@PathVariable("pid") Long pid){
        specService.deleteParam(pid);
        //返回一个响应码
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //修改规格组下面的规格参数
    @PutMapping("param")
    public ResponseEntity<Void> updateParam(@RequestBody SpecParam specParam){
        specService.updateParam(specParam);
        //返回一个响应码
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
