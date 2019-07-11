package com.shenrenye.gmall.manage.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shenrenye.gmall.beans.*;
import com.shenrenye.gmall.manage.user.service.PmsBaseCatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin  //允许跨域访问
public class ManageController {

    @Reference
    private PmsBaseCatalogService pmsBaseCatalog1Service;

    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getPmsBaseCatalog1List(){
        List<PmsBaseCatalog1> pmsBaseCatalog1s = pmsBaseCatalog1Service.getPmsBaseCatalog1List();
        return pmsBaseCatalog1s;
    }

    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getPmsBaseCatalog2List(String catalog1Id){
        List<PmsBaseCatalog2> pmsBaseCatalog2s = pmsBaseCatalog1Service.getPmsBaseCatalog2List(catalog1Id);
        return pmsBaseCatalog2s;
    }

    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getPmsBaseCatalog3List(String catalog2Id){
        List<PmsBaseCatalog3> pmsBaseCatalog3s = pmsBaseCatalog1Service.getPmsBaseCatalogL3ist(catalog2Id);
        return pmsBaseCatalog3s;
    }

    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> getPmsBaseAttrInfoList(String catalog3Id){
        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = pmsBaseCatalog1Service.getPmsBaseAttrInfoList(catalog3Id);
        return pmsBaseAttrInfoList;
    }

    @ResponseBody
    @RequestMapping("getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseCatalog1Service.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }


    @ResponseBody
    @RequestMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        pmsBaseCatalog1Service.savePmsBaseAttrInfo(pmsBaseAttrInfo);
        return "redirect:attrInfoList";
    }



}
