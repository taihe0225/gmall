package com.shenrenye.gmall.manage.user.service;

import com.shenrenye.gmall.beans.PmsProductSaleAttr;
import com.shenrenye.gmall.beans.PmsSkuAttrValue;
import com.shenrenye.gmall.beans.PmsSkuInfo;
import com.shenrenye.gmall.beans.PmsSkuSaleAttrValue;

import java.util.List;

public interface SkuService {

    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuAttrValue> getSkuAttrValueList(String skuId);

    List<PmsSkuSaleAttrValue> getSkuSaleAttrValueList(String skuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckedBySkuId(String productId, String skuId);

    List<PmsSkuInfo> checkSkuBySpuId(String productId);

    List<PmsSkuInfo> getSkuInfoAll();
}
