package com.java.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.java.client.CategoryClient;
import com.java.client.GoodsClient;
import com.java.client.SpecClient;
import com.java.dao.GoodsDao;
import com.java.item.bo.SpuBo;
import com.java.item.pojo.Sku;
import com.java.item.pojo.SpecParam;
import com.java.item.pojo.Spu;
import com.java.item.pojo.SpuDetail;
import com.java.pojo.Goods;
import com.java.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用作查询的spubo数据变成goods数据
 */
@Service
public class IndexService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private GoodsDao goodsDao;
    //将查询的结果变成goods需要的数据
    public Goods buildGoods(SpuBo spuBo) {
        //将spubo数据拷贝到goods中
        Goods goods = new Goods();
        BeanUtils.copyProperties(spuBo,goods);//根据属性名相同进行拷贝
        //goods中没有拷贝到的值 all(标题加分类)
        String title = spuBo.getTitle();//获取标题
        //获取到分类名称                   把数组变成一个list集合
        List<String> names = categoryClient.queryNamesByIds(Arrays.asList(spuBo.getCid1(), spuBo.getCid2(), spuBo.getCid3()));
        //将获取的名称切割 以空格进行切割
        String join = StringUtils.join(names, " ");
        //保存到all中
        goods.setAll(title+" "+join);//得到的数据：标题 类型名称

        //goods中没有拷贝到的值 skus(sku信息的json结构 （skuId、标题、价格、图片）)
        List<Sku> skuList = goodsClient.querySkuBYSpuId(spuBo.getId());//根据spuid查询sku
        //goods中没有拷贝到的值 price
        List<Long> prices = new ArrayList<>();
        //将skuList结果变成json字符串
        List listMap= new ArrayList<Map<String,Object>>();
        skuList.forEach(t->{
            //获取到的价格
            prices.add(t.getPrice());
            //skus(sku信息的json结构 （skuId、标题、价格、图片）)
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id",t.getId());//key不能乱写，要根据前端要求
            map.put("title",t.getTitle());
            map.put("price",t.getPrice());
            map.put("image",StringUtils.isBlank(t.getImages())?"":t.getImages().split(",")[0]);//判断是否为空,不为空取第一张图片
            listMap.add(map);
        });
        //保存到price
        goods.setPrice(prices);
        //保存到sku    JsonUtils工具的作用是把listMap变成一个字符串
        goods.setSkus(JsonUtils.serialize(listMap));
        //goods中没有拷贝到的值 specs 可搜索的规格参数，key是参数名，值是参数值
        Map<String, Object> specs = getSpecs(spuBo);
        //保存到specs中
        goods.setSpecs(specs);
        return goods;
    }
    //规格参数
    private Map<String, Object> getSpecs(SpuBo spuBo) {

        //key规格参数的名称，值为对应的规格参数的值（取决于商品本身）
        Map<String, Object> specs = new HashMap<>();//操作系统：android

        //1,获取到所有的可搜索的规格参数
        List<SpecParam> searchingParams = this.specClient.querySpecParams(null, spuBo.getCid3(), true,null);

        //2,循环可搜索的规格参数集合，判断通用还是特有，通用从通用规格中取值，特有从特有规格中取值

        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuBo.getId());

        //由于要取值，为了方便我们把通用规格和特有规格都转换为Map
        //通用规格键值对集合map
        Map<Long, Object> genericMap = JsonUtils.nativeRead(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        //特有规格键值对集合map
        Map<Long, List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        //3,通用和特有的值来自于spuDetail
        searchingParams.forEach(specParam -> {
            Long id = specParam.getId();//对应取值，规格参数的id就是通用规格和特有规格保存时map的key，当key一致可以直接取值
            String name = specParam.getName();//具体的key的值

            //通用参数
            Object value = null;
            if (specParam.getGeneric()) {
                //通用参数
                value = genericMap.get(id);

                if (null != value && specParam.getNumeric()) {
                    //数值类型可能需要加分段,以及单位
                    value = this.chooseSegment(value.toString(), specParam);
                }
            } else {//特有参数
                value = specialMap.get(id);

            }
            if (null == value) {
                value = "其他";
            }
            specs.put(name, value);
        });
        return  specs;

    }
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {//segment:1000-2000
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();//添加单位
                }
                break;
            }
        }
        return result;
    }
    //更新索引库
    public void createIndex(Long id) {
        //查询数据库中的spu
        Spu spu = goodsClient.querySpuById(id);
        //将查到的数据拷贝给Spubo
        SpuBo spuBo = new SpuBo();
        BeanUtils.copyProperties(spu,spuBo);
        //再将spuBo数据给索引库
        Goods goods = this.buildGoods(spuBo);
        //开始更新
        goodsDao.save(goods);
    }
    //删除索引
    public void deleteIndex(Long id){
        goodsDao.deleteById(id);
    }
}
