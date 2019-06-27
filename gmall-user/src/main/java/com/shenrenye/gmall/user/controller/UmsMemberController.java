package com.shenrenye.gmall.user.controller;

import com.shenrenye.gmall.beans.UmsMember;
import com.shenrenye.gmall.user.service.UmsMemberService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UmsMemberController {

    @Autowired
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
