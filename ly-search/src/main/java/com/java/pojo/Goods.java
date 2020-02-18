package com.java.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data //自动生成Get、Set方法
@Document(indexName = "goods",type = "docs",replicas = 0,shards = 1) //标记实体类为文档对象（索引库名称(数据库)，索引库中的类型(数据表)，副本数量，分片数量）
public class Goods {
    @Id
    private Long id;//spuId 主键
    @Field(type = FieldType.Text,analyzer = "ik_max_word")//对应索引库中的字段(对应索引库中的类型（可分词），分词器：最大分词)
    private String all;//所有需要被搜索的信息，包含标题，分类
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;// 卖点（副标题）

    private Long brandId;// 品牌id

    private Long cid1;// 1级分类id

    private Long cid2;// 2级分类id

    private Long cid3;// 3级分类id

    public List<Long> price;// 价格

    private Date createTime;// 商品的创建时间

    @Field(type = FieldType.Keyword, index = false)//对应索引库中的字段(对应索引库中的类型（不可分词，数据作为完整字段进行匹配，可以参与聚合），不对其建索引（不写 会建索引）)
    private String skus;// sku信息的json结构 （skuId、标题、价格、图片）

    private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
}
