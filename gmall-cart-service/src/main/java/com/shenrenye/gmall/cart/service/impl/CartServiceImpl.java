package com.shenrenye.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.shenrenye.gmall.beans.OmsCartItem;
import com.shenrenye.gmall.cart.mapper.OmsCartItemMapper;
import com.shenrenye.gmall.manage.user.service.CartService;
import com.shenrenye.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    OmsCartItemMapper omsCartItemMapper;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 判断是否存在购物车
     * @param omsCartItem
     * @return
     */
    @Override
    public OmsCartItem isCartExist(OmsCartItem omsCartItem) {

        OmsCartItem omsCartItem1 = new OmsCartItem();

        omsCartItem1.setProductSkuId(omsCartItem.getProductSkuId());
        omsCartItem1.setMemberId(omsCartItem.getMemberId());
        OmsCartItem omsCartItem2 = omsCartItemMapper.selectOne(omsCartItem1);

        return omsCartItem2;
    }

    /**
     * 更新购物车
     * @param omsCartItemExist
     */
    @Override
    public void updateCart(OmsCartItem omsCartItemExist) {

        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setTotalPrice(omsCartItemExist.getTotalPrice());
        omsCartItem.setQuantity(omsCartItemExist.getQuantity());

        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("id",omsCartItemExist.getId());

        omsCartItemMapper.updateByExampleSelective(omsCartItem,example);

        // 同步购物车缓存
        flushCartCache(omsCartItemExist.getMemberId());
    }

    // 同步购物车缓存
    private void flushCartCache(String memberId) {
        Jedis jedis = null;

        try {
            jedis = redisUtil.getJedis();
            String cartCachekey = "user:" + memberId + ":cart";
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setMemberId(memberId);
            List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);
            Map<String,String> hmmap = new HashMap<>();
            for (OmsCartItem cartItem : omsCartItems) {
                hmmap.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
            }
            jedis.hmset(cartCachekey,hmmap);
        } finally {
            jedis.close();
        }


    }

    @Override
    public void insertCart(OmsCartItem omsCartItem) {
        omsCartItemMapper.insertSelective(omsCartItem);
    }

    /**
     * 获取购物车缓存数据
     * @param memberId
     * @return
     */
    @Override
    public List<OmsCartItem> getCartCache(String memberId) {

        ArrayList<OmsCartItem> omsCartItems = new ArrayList<>();

        Jedis jedis = redisUtil.getJedis();

        List<String> hvals = jedis.hvals("user:" + memberId + ":cart");

        if (hvals != null){
            for (String cartJson : hvals) {
                OmsCartItem omsCartItem = new OmsCartItem();
                omsCartItem = JSON.parseObject(cartJson,OmsCartItem.class);
                omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
                omsCartItems.add(omsCartItem);
            }
        }

        jedis.close();

        return omsCartItems;
    }

    @Override
    public void updateCartCheck(OmsCartItem omsCartItem) {
        OmsCartItem omsCartItemForUpdate = new OmsCartItem();
        omsCartItemForUpdate.setIsChecked(omsCartItem.getIsChecked());
        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("productSkuId",omsCartItem.getProductSkuId()).andEqualTo("memberId",omsCartItem.getMemberId());
        omsCartItemMapper.updateByExampleSelective(omsCartItemForUpdate,e);

        // 同步购物车缓存
        flushCartCache(omsCartItem.getMemberId());
    }


}
