package com.shenrenye.gmall.user.service;

import com.shenrenye.gmall.beans.UmsMember;

import java.util.List;

public interface UmsMemberService {

    List<UmsMember> getUserAll();

    UmsMember getUserById(String id);

    Integer editUserById(String id);
}
