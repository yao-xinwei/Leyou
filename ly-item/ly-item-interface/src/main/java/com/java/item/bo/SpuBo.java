package com.java.item.bo;

import com.java.item.pojo.Sku;
import com.java.item.pojo.Spu;
import com.java.item.pojo.SpuDetail;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;


@Data
public class SpuBo extends Spu {
    @Transient //表中不存在的字段加此注解
    private String cname;//分类名称
    @Transient
    private String bname;//品牌名称
    @Transient
    private List<Sku> skus;//sku数据(和前端(Json中)传来的字段保持一致，不然接收不到)
    @Transient
    private SpuDetail spuDetail;//和前端(Json中)传来的字段保持一致，不然接收不到
}
