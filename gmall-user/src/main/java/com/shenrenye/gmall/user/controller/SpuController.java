package com.shenrenye.gmall.user.controller;

import com.shenrenye.gmall.beans.PmsBaseAttrInfo;
import com.shenrenye.gmall.beans.PmsBaseSaleAttr;
import com.shenrenye.gmall.beans.PmsProductInfo;
import com.shenrenye.gmall.user.service.PmsBaseCatalog1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin  //允许跨域访问
public class SpuController {

    @Autowired
    private PmsBaseCatalog1Service pmsBaseCatalog1Service;

    @ResponseBody
    @RequestMapping("spuList")
    public List<PmsProductInfo> getSpuList(String catalog3Id){
        List<PmsProductInfo> pmsProductInfos = pmsBaseCatalog1Service.getSpuList(catalog3Id);
        return pmsProductInfos;
    }


    @ResponseBody
    @RequestMapping("baseSaleAttrList")
    public List<PmsBaseSaleAttr> getaBseSaleAttrList(){
        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = pmsBaseCatalog1Service.getaBseSaleAttrList();
        return pmsBaseSaleAttrs;
    }


    @ResponseBody
    @RequestMapping("saveSpuInfo")
    public List<PmsBaseSaleAttr> saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        pmsBaseCatalog1Service.savePmsProductInfo(pmsProductInfo);
       return null;
    }


    @ResponseBody
    @RequestMapping(" fileUpload")
    public String fileUpload(@RequestParam("file")MultipartFile MultipartFile){
        System.out.println("MultipartFile= " + MultipartFile);
        return "http://photocdn.sohu.com/20150625/Img415612078.jpg";
    }
}
