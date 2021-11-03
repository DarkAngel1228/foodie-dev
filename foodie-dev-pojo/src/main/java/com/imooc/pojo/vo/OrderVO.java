package com.imooc.pojo.vo;

import com.imooc.pojo.bo.ShopcartBO;

import java.util.List;

public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;
    private List<ShopcartBO> toBeRemovedShopCatList;


    public List<ShopcartBO> getToBeRemovedShopCatList() {
        return toBeRemovedShopCatList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }
    public void setToBeRemovedShopCatList(List<ShopcartBO> toBeRemovedShopCatList) {
        this.toBeRemovedShopCatList = toBeRemovedShopCatList;
    }
}