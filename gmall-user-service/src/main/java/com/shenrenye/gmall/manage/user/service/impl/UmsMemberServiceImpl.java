package com.shenrenye.gmall.manage.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.shenrenye.gmall.beans.UmsMember;
import com.shenrenye.gmall.manage.user.mapper.UmsMemberMapper;
import com.shenrenye.gmall.manage.user.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Override
    public List<UmsMember> getUserAll() {

        return umsMemberMapper.selectAll();
    }

    @Override
    public UmsMember getUserById(String id) {
        return null;
    }

    @Override
    public Integer editUserById(String id) {
        return null;
    }
}
