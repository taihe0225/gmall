package com.shenrenye.gmall.user.service;


import com.shenrenye.gmall.beans.*;

import java.util.List;

public interface PmsBaseCatalog1Service {


    List<PmsBaseCatalog1> getPmsBaseCatalog1List();
    List<PmsBaseCatalog2> getPmsBaseCatalog2List(String id);
    List<PmsBaseCatalog3> getPmsBaseCatalogL3ist(String id);

    List<PmsBaseAttrInfo> getPmsBaseAttrInfoList(String catalog3Id);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    void savePmsBaseAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsProductInfo> getSpuList(String catalog3Id);

    List<PmsBaseSaleAttr> getaBseSaleAttrList();

    void savePmsProductInfo(PmsProductInfo pmsProductInfo);
}
