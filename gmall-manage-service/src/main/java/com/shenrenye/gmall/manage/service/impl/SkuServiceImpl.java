package com.shenrenye.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.shenrenye.gmall.beans.*;
import com.shenrenye.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.shenrenye.gmall.manage.mapper.PmsSkuImageMapper;
import com.shenrenye.gmall.manage.mapper.PmsSkuInfoMapper;
import com.shenrenye.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.shenrenye.gmall.manage.user.service.SkuService;
import com.shenrenye.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

@Service
public class SkuServiceImpl implements SkuService{
    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String sku_id = pmsSkuInfo.getId();

        // 保存图片
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(sku_id);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }

        // 保存平台属性
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(sku_id);
            skuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        // 保存销售属性
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(sku_id);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);

        }
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        PmsSkuInfo pmsSkuInfo = null ;
        //获取jedis连接
        Jedis jedis = redisUtil.getJedis();
        try {
            //获取redis缓存中获取数据
            String jedisStr = jedis.get("sku:" + skuId + ":info");
            //为空则说明缓存没有数据
            if (StringUtils.isBlank(jedisStr)){
                String vskuIp = UUID.randomUUID().toString();
                //设置锁以及过期时间，避免死锁 OK则说明没有锁，可以操作数据库
                //String OK = jedis.set("sku:lock","12131313");
                String OK = jedis.set("sku:"+skuId+":lock",vskuIp,"nx","px",2000);
                //判断
                if (StringUtils.isNotBlank(OK) && "OK".equals(OK)){
                    //调用方法查询数据库
                    pmsSkuInfo = getSkuByIdFromDB(skuId);
                    if (pmsSkuInfo != null){
                        //将数据库中查询的数据存放如jedis缓存中
                        jedis.set("sku:"+skuId+":info", JSON.toJSONString(pmsSkuInfo));
                    }
                    //删除锁
                    String v = jedis.get("sku:" + skuId + ":lock");
                    //判断锁是否为同一把锁，防止误删
                    if (StringUtils.isBlank(v) && v.equals(vskuIp)){
                        jedis.del("sku:"+skuId+":lock");
                    }
                }else{
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //自旋再次执行操作
                    return getSkuById(skuId);
                }
            }else {
                //将缓存中的json数据转换成对应的对象
                pmsSkuInfo = JSON.parseObject(jedisStr,PmsSkuInfo.class);
            }
        } finally {
            jedis.close();
        }
        return pmsSkuInfo;
    }


    public PmsSkuInfo getSkuByIdFromDB(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo1.setSkuImageList(pmsSkuImages);
        return pmsSkuInfo1;
    }


    @Override
    public List<PmsSkuAttrValue> getSkuAttrValueList(String skuId) {
        PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
        pmsSkuAttrValue.setSkuId(skuId);
        return skuAttrValueMapper.select(pmsSkuAttrValue);
    }

    @Override
    public List<PmsSkuSaleAttrValue> getSkuSaleAttrValueList(String skuId) {
        PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
        pmsSkuSaleAttrValue.setSkuId(skuId);
        return pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckedBySkuId(String productId, String skuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrList= pmsSkuSaleAttrValueMapper.selectSpuSaleAttrListCheckedBySkuId(productId,skuId);
        return pmsProductSaleAttrList;
    }

    @Override
    public List<PmsSkuInfo> checkSkuBySpuId(String spuId) {
        List<PmsSkuInfo> PmsSkuInfos = pmsSkuSaleAttrValueMapper.selectCheckSkuBySpuId(spuId);
        return PmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getSkuInfoAll() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            List<PmsSkuAttrValue> select = skuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(select);
        }
        return pmsSkuInfos;
    }


}
