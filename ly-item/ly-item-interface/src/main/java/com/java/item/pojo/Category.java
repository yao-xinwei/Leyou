package com.java.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_category")
@Data //自动生成Get、Set方法，不再写Get、Set方法
public class Category {
    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //产生方式
    private Long id;//` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类目id',
    private String name;//` varchar(32) NOT NULL COMMENT '类目名称',
    private Integer parentId;//` bigint(20) NOT NULL COMMENT '父类目id,顶级类目填0',
    private Integer isParent;//` tinyint(1) NOT NULL COMMENT '是否为父节点，0为否，1为是',
    private Integer sort;//` int(4) NOT NULL COMMENT '排序指数，越小越靠前',
}
