package com.shenrenye.gmall.manage.user.service;

import com.shenrenye.gmall.beans.PmsProductImage;
import com.shenrenye.gmall.beans.PmsProductSaleAttr;

import java.util.List;

public interface SpuService {

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);
}
