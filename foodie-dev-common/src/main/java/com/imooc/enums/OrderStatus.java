package com.imooc.enums;

public enum OrderStatus {
    WAIT_PAY(10, "待付款"),
    WAIT_DELIVER(20, "已付款，待发货"),
    WAIT_RECEIVE(30, "已发货，待收货"),
    SUCCESS(40, "交易成功"),
    CLOSE(50, "交易关闭");

    public final Integer type;
    public final String value;

    OrderStatus(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
