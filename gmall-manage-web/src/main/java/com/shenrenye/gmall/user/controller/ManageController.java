package com.shenrenye.gmall.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ManageController {

    @ResponseBody
    @RequestMapping("index")
    public String index(){
        return "index";
    }
}
