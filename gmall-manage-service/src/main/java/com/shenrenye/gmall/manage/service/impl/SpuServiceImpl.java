package com.shenrenye.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.shenrenye.gmall.beans.PmsProductImage;
import com.shenrenye.gmall.beans.PmsProductSaleAttr;
import com.shenrenye.gmall.beans.PmsProductSaleAttrValue;
import com.shenrenye.gmall.manage.mapper.PmsProductImageMapper;
import com.shenrenye.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.shenrenye.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.shenrenye.gmall.manage.user.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService{

    @Autowired
    private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    private PmsProductImageMapper pmsProductImageMapper;
    @Autowired
    private PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {

            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            pmsProductSaleAttrValue.setProductId(spuId);
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);

            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }
        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }
}
