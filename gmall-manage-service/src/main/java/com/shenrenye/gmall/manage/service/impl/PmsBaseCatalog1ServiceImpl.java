package com.shenrenye.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.shenrenye.gmall.beans.*;
import com.shenrenye.gmall.manage.mapper.*;
import com.shenrenye.gmall.manage.user.service.PmsBaseCatalogService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Service
public class PmsBaseCatalog1ServiceImpl implements PmsBaseCatalogService {

    @Autowired
    private PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;
    @Autowired
    private PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;
    @Autowired
    private PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;
    @Autowired
    private PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Autowired
    private PmsBaseAttrValueMapper pmsBaseAttrValueMapper;
    @Autowired
    private PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    private PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;
    @Autowired
    private PmsProductImageMapper pmsProductImageMapper;
    @Autowired
    private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    private PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Override
    public List<PmsBaseCatalog1> getPmsBaseCatalog1List() {
        return pmsBaseCatalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getPmsBaseCatalog2List(String id) {
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(id);
        return pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
    }

    @Override
    public List<PmsBaseCatalog3> getPmsBaseCatalogL3ist(String id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(id);
        return pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
    }

    @Override
    public List<PmsBaseAttrInfo> getPmsBaseAttrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        return pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        return pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
    }

    @Override
    public void savePmsBaseAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {

        pmsBaseAttrInfoMapper.delete(pmsBaseAttrInfo);

        pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
        String id = pmsBaseAttrInfo.getId();
        List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();

        PmsBaseAttrValue pmsBaseAttrValue2 = new PmsBaseAttrValue();
        pmsBaseAttrValue2.setAttrId(id);
        pmsBaseAttrValueMapper.delete(pmsBaseAttrValue2);

        for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
            pmsBaseAttrValue.setAttrId(id);
            pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
        }
    }

    @Override
    public List<PmsProductInfo> getSpuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        return pmsProductInfoMapper.select(pmsProductInfo);
    }

    @Override
    public List<PmsBaseSaleAttr> getaBseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public void savePmsProductInfo(PmsProductInfo pmsProductInfo) {

        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        String spuId = pmsProductInfo.getId();

        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(spuId);
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }

        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttrs: spuSaleAttrList) {
            pmsProductSaleAttrs.setProductId(spuId);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttrs);

            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttrs.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList){
                pmsProductSaleAttrValue.setProductId(spuId);
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
        }

    }


}
