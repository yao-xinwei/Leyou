package com.java.item.client;

import com.java.item.pojo.SpecGroup;
import com.java.item.pojo.SpecParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface SpecApi {
    //查询规格组
    @GetMapping("spec/groups/{cid}")
    public List<SpecGroup> querySpecGroups(@PathVariable("cid") Long cid);
    //查询规格组对应的规格参数
    @GetMapping("spec/params")
    public List<SpecParam> querySpecParams(@RequestParam(value = "gid",required = false) Long gid,
                                                           @RequestParam(value = "cid",required = false) Long cid,
                                                           @RequestParam(value = "searching",required = false) Boolean searching,
                                                           @RequestParam(value = "generic",required = false) Boolean generic);

}
