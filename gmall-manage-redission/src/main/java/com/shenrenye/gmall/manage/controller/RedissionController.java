package com.shenrenye.gmall.manage.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.shenrenye.gmall.util.RedisUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

@Controller
public class RedissionController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping("testRedission")
    @ResponseBody
    public String testRedission(){
        Jedis jedis = redisUtil.getJedis();
        RLock lock = redissonClient.getLock("redis-lock");
        String v = "";
        lock.lock();
        try{
            v = jedis.get("k");
            if (StringUtils.isBlank(v)){
                v = "1";
            }
            int num = Integer.parseInt(v);
            jedis.set("k",num + 1 +"");

        }finally{
            jedis.close();
            lock.unlock();
        }

        return v;
    }

}
