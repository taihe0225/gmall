package com.shenrenye.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shenrenye.gmall.manage.user.service.PmsBaseCatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {


    @RequestMapping("index")
    public String index(){

        return "index";
    }

}
