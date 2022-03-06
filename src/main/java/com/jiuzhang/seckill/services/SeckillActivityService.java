package com.jiuzhang.seckill.services;

import com.alibaba.fastjson.JSON;
import com.jiuzhang.seckill.db.dao.OrderDao;
import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.po.Order;
import com.jiuzhang.seckill.db.po.SeckillActivity;
import com.jiuzhang.seckill.mq.RocketMQService;
import com.jiuzhang.seckill.util.RedisService;
import com.jiuzhang.seckill.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class SeckillActivityService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private RocketMQService rocketMQService;
    @Autowired
    private OrderDao orderDao;

    /**
     * datacenterId;  数据中心
     * machineId;     机器标识
     * 在分布式环境中可以从机器配置上读取
     * 单机开发环境中先写死
     */
    private SnowFlake snowFlake = new SnowFlake(1, 1);

    /**
     * 判断秒杀库存
     *
     * @param activityId
     * @return
     */
    public boolean seckillStockValidator(long activityId) {
        String key = "stock:" + activityId;
        return redisService.stockDeductValidator(key);
    }

    /**
     * 创建订单
     *
     * @param seckillActivityId
     * @param userId
     * @return
     * @throws Exception
     */
    public Order createOrder(long seckillActivityId, long userId) throws Exception {
        /*
         * 1.创建订单
         */
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        Order order = new Order();
        //采用雪花算法生成订单ID
        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setSeckillActivityId(seckillActivity.getId());
        order.setUserId(userId);
        order.setOrderAmount(seckillActivity.getSeckillPrice().longValue());
        /*
         *2.发送创建订单消息
         */
        rocketMQService.sendMessage("seckill_order", JSON.toJSONString(order));

        return order;
    }

    /**
     * 订单支付完成处理
     * @param orderNo
     */
    public void payOrderProcess(String orderNo) {
        log.info("完成支付订单 订单号：" + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        boolean deductStockResult = seckillActivityDao.deductStock(order.getSeckillActivityId());
        if (deductStockResult) {
            order.setPayTime(new Date());
            // 订单状态 0、没有可用库存，无效订单  1、已创建等待支付  2、完成支付
            order.setOrderStatus(2);
            orderDao.updateOrder(order);
        }
    }

}
