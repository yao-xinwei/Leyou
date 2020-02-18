package com.java.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.java.item.bo.SpuBo;
import com.java.item.mapper.*;
import com.java.item.pojo.*;
import com.java.po.PageResult;
import com.netflix.discovery.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;//String和amqp集成提供一个模板类
    //显示商品列表
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        //开启分页
        PageHelper.startPage(page,rows);
        //select * from tb_spu where title like "%"+key+"%" and saleable = saleable;
        //查询对象
        Example example = new Example(Spu.class);
        //获取criteria对象
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){//判断不为空
            criteria.andLike("title","%"+key+"%");//模糊查询 like "%"+key+"%"
        }
        //是否上下架
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);//and saleable = saleable
        }
        //查询的结果
        Page<Spu> spuPage = (Page<Spu>)spuMapper.selectByExample(example);
        //分页后查询的结果
        List<Spu> result = spuPage.getResult();
        //新建一个spubo对象集合
        List<SpuBo> spuBos = new ArrayList<>();
        result.forEach(t->{ //取出result中的值
            SpuBo spuBo = new SpuBo();
            //拷贝
            BeanUtils.copyProperties(t,spuBo);//将t对象中的值拷贝给spuBo对象
            //根据分类id查询分类的名字                        把数组变成一个list集合
            List<String> names = categoryService.queryNameByIds(Arrays.asList(t.getCid1(),t.getCid2(),t.getCid3()));
            //将names中的集合以/分隔（页面要求）
            String join = StringUtils.join(names, "/");
            //封装到spuBo中（分类名称）
            spuBo.setCname(join);
            //封装到spuBo中（品牌名称）
            Brand brand = brandMapper.selectByPrimaryKey(t.getBrandId());
            spuBo.setBname(brand.getName());
            //将spuBo中的值放到List集合中
            spuBos.add(spuBo);
        });
        return new PageResult<>(spuPage.getTotal(),new Long(spuPage.getPages()),spuBos);
    }
    //添加商品信息
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        //spu中前端没有传的值
        spuBo.setSaleable(true);//是否上架
        spuBo.setValid(true);//是否有效
        spuBo.setCreateTime(new Date());//创建时间
        spuBo.setLastUpdateTime(new Date());//
        //保存数据到spu表中
        spuMapper.insertSelective(spuBo);//可以返回主键 主键的值在实体中（对应的字段）添加后返回
        //获取spu返回的主键
        Long id = spuBo.getId();
        //获取商品详请
        SpuDetail spuDetail = spuBo.getSpuDetail();
        //spuDetail中前端没传的数据
        spuDetail.setSpuId(id);
        //保存数据到spuDetail表中
        spuDetailMapper.insert(spuDetail);
        //获取sku数据(是个集合)
        List<Sku> suks = spuBo.getSkus();
        saveSkus(spuBo,suks);//保存sku和stock(库存表)数据
        //发送消息
        sendMessage(id,"insert");
    }
    //保存sku和scoke(库存表)数据
    @Transactional
    public void saveSkus(SpuBo spuBo, List<Sku> suks) {
        suks.forEach(t->{
            //sku中前端没传的数据
            t.setSpuId(spuBo.getId());
            t.setCreateTime(new Date());
            t.setLastUpdateTime(new Date());
            //保存数据到sku表中
            skuMapper.insertSelective(t);
            //sku中有stock需要的数据
            Stock stock = new Stock();
            stock.setSkuId(t.getId());
            stock.setStock(t.getStock());
            //保存数据到stock表中
            stockMapper.insert(stock);
        });
    }
    //根据spuid查询商品详请表（spu_detail）
    public SpuDetail querySpuDetailBySpuId(Long spuid) {
        return spuDetailMapper.selectByPrimaryKey(spuid);
    }
    //根据spuid查询sku表
    public List<Sku> querySkuBYSpuId(Long id) {
        //select * from th_sku where spu_id = id
        Sku sku = new Sku();
        sku.setSpuId(id);
        //return skuMapper.select(sku);
        List<Sku> skuList = skuMapper.select(sku);
        //查询库存
        skuList.forEach(t->{
            //获取skuid
            Long id1 = t.getId();
            Stock stock = stockMapper.selectByPrimaryKey(id1);
            t.setStock(stock.getStock());//将stock中的库存值放到sku里面
        });
        return skuList;
    }
    //修改商品
    @Transactional
    public void udpateGoods(SpuBo spuBo) {
        spuBo.setLastUpdateTime(new Date());//修改的时间
        //修改spu表
        spuMapper.updateByPrimaryKey(spuBo);
        //修改spuDetail表 spubo中保存的有spuDetail所需的值
        spuDetailMapper.updateByPrimaryKey(spuBo.getSpuDetail());
        //先删除sku和库存表数据
        Long id = spuBo.getId();
        //先根据id查询sku数据
        Sku sku = new Sku();
        sku.setSpuId(id);
        // select * from tb_sku where spu_id = id;
        List<Sku> skuList = skuMapper.select(sku);
        //删除
        skuList.forEach(t->{
            stockMapper.deleteByPrimaryKey(t.getId());
            skuMapper.delete(t);
        });
        //再添加
        saveSkus(spuBo,spuBo.getSkus());
        //发送消息
        sendMessage(id,"update");
    }
    //根据spuId查询spu数据
    public Spu querySpuById(Long spuId) {
        return spuMapper.selectByPrimaryKey(spuId);
    }
    //发送消息的方法（当添加，修改，删除时）
    //                        商品的id   操作类型(添加，修改，删除)
    private void sendMessage(Long id, String type){
        //                            发送类型
        amqpTemplate.convertAndSend("item."+type,id);
    }
}
