package com.shenrenye.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shenrenye.gmall.beans.UmsMember;
import com.shenrenye.gmall.user.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UmsMemberController {

    @Reference
    private UmsMemberService umsMemberService;

    @RequestMapping("/hello/idea")
    @ResponseBody
    public List<UmsMember> getUserAll(){
        List<UmsMember> users = umsMemberService.getUserAll();
        return users;
    }

}
