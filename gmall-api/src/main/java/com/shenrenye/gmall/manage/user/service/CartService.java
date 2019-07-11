package com.shenrenye.gmall.manage.user.service;

import com.shenrenye.gmall.beans.OmsCartItem;

import java.util.List;

public interface CartService {

    OmsCartItem isCartExist(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemExist);

    void insertCart(OmsCartItem omsCartItem);

    List<OmsCartItem> getCartCache(String memberId);

    void updateCartCheck(OmsCartItem omsCartItem);
}
