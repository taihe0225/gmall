package com.shenrenye.gmall.manage.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shenrenye.gmall.beans.PmsBaseSaleAttr;
import com.shenrenye.gmall.beans.PmsProductImage;
import com.shenrenye.gmall.beans.PmsProductInfo;
import com.shenrenye.gmall.beans.PmsProductSaleAttr;
import com.shenrenye.gmall.manage.user.service.PmsBaseCatalogService;
import com.shenrenye.gmall.manage.user.service.SpuService;
import com.shenrenye.gmall.util.MyUploadUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin  //允许跨域访问
public class SpuController {

    @Reference
    private PmsBaseCatalogService pmsBaseCatalog1Service;
    @Reference
    private SpuService spuService;

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
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        pmsBaseCatalog1Service.savePmsProductInfo(pmsProductInfo);
       return "success";
    }


    @ResponseBody
    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file")MultipartFile multipartFile){
        System.out.println("MultipartFile= " + multipartFile);
        String imgUrl = MyUploadUtil.uploadImage(multipartFile);
        return imgUrl;
    }

    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){

        List<PmsProductSaleAttr>  pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }


    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){
        List<PmsProductImage>  pmsProductImages = spuService.spuImageList(spuId);
        return pmsProductImages;
    }


}
