package com.shenrenye.gmall.user.service.impl;

import com.shenrenye.gmall.beans.UmsMember;
import com.shenrenye.gmall.user.mapper.UmsMemberMapper;
import com.shenrenye.gmall.user.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsMemberServiceImpl implements UmsMemberService{

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Override
    public List<UmsMember> getUserAll() {
        return umsMemberMapper.selectAll();
    }

    @Override
    public UmsMember getUserById(String id) {
        UmsMember umsMember = new UmsMember();
        umsMember.setId(id);
        return umsMemberMapper.selectOne(umsMember);
    }

    @Override
    public Integer editUserById(String id) {
        UmsMember umsMember = new UmsMember();
        umsMember.setId(id);
        umsMember.setPhone("15678940623");
        return umsMemberMapper.updateByPrimaryKeySelective(umsMember);
    }
}
