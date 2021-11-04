package com.imooc.config;

import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {
    @Autowired
    private OrderService orderService;

    /**
     * 使用定时任务关闭超时支付订单，会存在的弊端
     * 1. 会有时间差，程序不严谨
     * 2. 不支持集群
     * 3. 会对数据库全表扫描，极其影响数据库性能
     *
     * 定时任务，仅仅只适用于小型轻量级项目或传统项目
     *
     * 后续课程会涉及到MQ -> RabbitMQ, RocketMQ, Kafka, ZeroMQ 。。。
     *
     * 延时任务（队列）
     *
     */




    //@Scheduled(cron = "0/3 * * * * ?")
    public void autoCloseOrder() {
        orderService.closeOrder();
        System.out.println("执行定时任务，当前时间为：" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN));
    }
}
