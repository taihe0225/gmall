package com.shenrenye.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.shenrenye.gmall.beans.OmsCartItem;
import com.shenrenye.gmall.beans.PmsSkuInfo;
import com.shenrenye.gmall.manage.user.service.CartService;
import com.shenrenye.gmall.manage.user.service.SkuService;
import com.shenrenye.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@Controller
public class CartController {


    @Reference
    CartService cartService;

    @Reference
    SkuService skuService;

    @RequestMapping("addToCart")
    public String addToCart(HttpSession session, HttpServletRequest request, HttpServletResponse response, OmsCartItem omsCartItem){
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(omsCartItem.getProductSkuId());
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        omsCartItem.setCreateDate(new Date());
        omsCartItem.setIsChecked("1");
        omsCartItem.setPrice(pmsSkuInfo.getPrice());// 商品单价
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setProductId(pmsSkuInfo.getProductId());
        omsCartItem.setProductName(pmsSkuInfo.getSkuName());
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuId(pmsSkuInfo.getId());
        omsCartItem.setTotalPrice(pmsSkuInfo.getPrice().multiply(omsCartItem.getQuantity()));// 购物车价格(商品单价*添加数量)

        String memberId = "1";

        // 添加购物车的业务逻辑
        if(StringUtils.isBlank(memberId)){

            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);// cookie工具类
            if(StringUtils.isBlank(cartListCookie)){
                omsCartItems.add(omsCartItem);
            }else{
                // 判断cookie中的购物车数据和当前添加的购物车数据是否有重复
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                // 更新或者添加购物车
                boolean b = if_new_cart(omsCartItems,omsCartItem);
                if(b){
                    // 新车则添加
                    omsCartItems.add(omsCartItem);
                }else{
                    // 老车则更新
                    for (OmsCartItem cartItem : omsCartItems) {
                        if(cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())){
                            BigDecimal add = cartItem.getQuantity().add(omsCartItem.getQuantity());
                            cartItem.setQuantity(add);
                            BigDecimal multiply = cartItem.getPrice().multiply(cartItem.getQuantity());
                            cartItem.setTotalPrice(multiply);
                            break;
                        }
                    }
                }
            }
            // 添加cookie到浏览器
            String cookieJSON = JSON.toJSONString(omsCartItems);
            System.out.println(cookieJSON);
            CookieUtil.setCookie(request,response,"cartListCookie",cookieJSON,1000*60*60*24,true);//cookie工具类
        }else{
            omsCartItem.setMemberId(memberId);
            omsCartItem.setMemberNickname("windir");
            // 查询db
            OmsCartItem omsCartItemExist = cartService.isCartExist(omsCartItem);

            if(omsCartItemExist!=null){
                // 更新数据库
                System.out.println("更新");
                omsCartItemExist.setQuantity(omsCartItemExist.getQuantity().add(omsCartItem.getQuantity()));
                omsCartItemExist.setTotalPrice(omsCartItemExist.getPrice().multiply(omsCartItemExist.getQuantity()));
                cartService.updateCart(omsCartItemExist);
            }else{
                // 添加数据库
                System.out.println("添加");
                cartService.insertCart(omsCartItem);
            }
        }
        return "redirect:/success.html";
    }

    private boolean if_new_cart(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {

        boolean b = true;

        for (OmsCartItem cartItem : omsCartItems) {
            if(cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())){
                b = false;
                break;
            }
        }
        return b;
    }


    @RequestMapping("cartList")
    public String cartList(ModelMap map, HttpSession session, HttpServletRequest request, HttpServletResponse response){

        List<OmsCartItem> omsCartItems = new ArrayList<>();

        String memberId = "1";

        if (StringUtils.isBlank(memberId)){
            //获取cookie中的数据、
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isBlank(cartListCookie)){
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            }
        }else{
            omsCartItems = cartService.getCartCache(memberId);
        }

        map.put("cartList",omsCartItems);
        map.put("cartSumPrice",getCartSumPrice(omsCartItems));
        return "cartList";

    }

    /**
     * 获取购物车总价格
     * @param omsCartItems
     * @return
     */
    private Object getCartSumPrice(List<OmsCartItem> omsCartItems) {
        BigDecimal sum = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            String isChecked = omsCartItem.getIsChecked();
            if(isChecked.equals("1")){
                sum = sum.add(omsCartItem.getTotalPrice());
            }
        }

        return sum;
    }


    @RequestMapping("checkCart")
    public String checkCart(HttpSession session, HttpServletRequest request, HttpServletResponse response, ModelMap map,OmsCartItem omsCartItem) {

        List<OmsCartItem> omsCartItems = new ArrayList<>();

        // 用户是否登录
        String memberId = "1";

        if(StringUtils.isBlank(memberId)){
            //没有登录，cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookie)){
                omsCartItems = JSON.parseArray(cartListCookie,OmsCartItem.class);
                // 修改被选中的购物车状态
                for (OmsCartItem cartItem : omsCartItems) {
                    if(cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())){
                        cartItem.setIsChecked(omsCartItem.getIsChecked());
                    }
                }
            }

            // 覆盖cookie
            String cookieJSON = JSON.toJSONString(omsCartItems);
            System.out.println(cookieJSON);
            CookieUtil.setCookie(request,response,"cartListCookie",cookieJSON,1000*60*60*24,true);//cookie工具类

        }else{
            //已经登录，db或者缓存
            omsCartItem.setMemberId(memberId);
            cartService.updateCartCheck(omsCartItem);
            omsCartItems = cartService.getCartCache(memberId);
        }

        map.put("cartList",omsCartItems);
        map.put("cartSumPrice",getCartSumPrice(omsCartItems));
        return "cartListInner";
    }

}
