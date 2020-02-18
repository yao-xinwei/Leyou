package com.java.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
@Data
@Table(name = "tb_spec_group")
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;

    @Transient //映射其他表，在本表中不存在，一对多关系
    private List<SpecParam> specParams; // 规格参数

   // getter和setter ...
}