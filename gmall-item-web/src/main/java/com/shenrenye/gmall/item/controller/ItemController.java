package com.shenrenye.gmall.item.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.dubbo.config.annotation.Reference;
import com.shenrenye.gmall.beans.*;
import com.shenrenye.gmall.manage.user.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;

    @RequestMapping("/{skuId}.html")
    public String item(@PathVariable String skuId , ModelMap map){

        // 查询一个包含sku信息的skuInfo对象
        PmsSkuInfo pmsSkuInfo =skuService.getSkuById(skuId);
        map.put("skuInfo",pmsSkuInfo);

        // 查询当前sku的spu对应的销售属性列表,用户点击销售属性值用
        List<PmsProductSaleAttr> pmsProductSaleAttrs = skuService.spuSaleAttrListCheckedBySkuId(pmsSkuInfo.getProductId(),skuId);
        map.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);

        // 隐藏的当前sku所在的spu下的销售属性值组合对应skuId的hash表，用户点击后，找到对应的skuId用
        List<PmsSkuInfo> PmsSkuInfos = skuService.checkSkuBySpuId(pmsSkuInfo.getProductId());
        // 用销售属性值的组合当作key，用skuId当作value制作一个hash表
        HashMap<String,String> skuSaleAttrMap =  new HashMap<String,String>();
        for (PmsSkuInfo skuInfo : PmsSkuInfos) {
            String skuIdForHashMap = skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            String valueIds = "";
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                valueIds= valueIds +"|"+pmsSkuSaleAttrValue.getSaleAttrValueId();

            }
            skuSaleAttrMap.put(valueIds,skuIdForHashMap);
        }
        map.put("skuSaleAttrMap", JSON.toJSONString(skuSaleAttrMap));

        map.put("currentSkuId",skuId);

        return "item";
    }


    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public String getUserAll(@PathVariable("itemId") String itemId){
        return "item"+itemId;
    }


}
