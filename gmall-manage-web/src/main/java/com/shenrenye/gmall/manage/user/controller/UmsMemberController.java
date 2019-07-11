package com.shenrenye.gmall.manage.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shenrenye.gmall.beans.UmsMember;
import com.shenrenye.gmall.manage.user.service.UmsMemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class UmsMemberController {

    @Reference
    private UmsMemberService umsMemberService;

    /**
     * 查询所有umsmember
     * @return json
     */
    @RequestMapping("/get/umsmember/all")
    @ResponseBody
    public List<UmsMember> getUserAll(){
        List<UmsMember> users = umsMemberService.getUserAll();
        return users;
    }

    @RequestMapping("/get/umsmember/by/id/{id}")
    @ResponseBody
    public UmsMember getUserById(@PathVariable("id") String id){
        UmsMember umsMember = umsMemberService.getUserById(id);
        return umsMember;
    }

    @RequestMapping("/edit/umsmember/by/id/{id}")
    @ResponseBody
    public UmsMember editUserById(@PathVariable("id") String id){
        Integer i = umsMemberService.editUserById(id);
        return umsMemberService.getUserById(id);
    }

}
