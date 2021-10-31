package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.OrdersMapperCustom;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyOrdersServiceImpl extends BaseService implements MyOrdersService {

    @Autowired
    public OrdersMapperCustom ordersMapperCustom;

    @Autowired
    public OrdersMapper ordersMapper;

    @Autowired
    public OrderStatusMapper orderStatusMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(map);

        return setterPageGrid(list, page);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        orderStatus.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER);
        orderStatusMapper.updateByExampleSelective(orderStatus, example);
    }

    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        return null;
    }

    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        return false;
    }

    @Override
    public boolean deleteOrder(String userId, String orderId) {
        return false;
    }

    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPayCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", YesOrNo.YES.type);
        int waitCommentCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        OrderStatusCountsVO countsVO = new OrderStatusCountsVO(waitPayCounts, waitDeliverCounts, waitReceiveCounts,
                waitCommentCounts);

        return countsVO;

    }

    @Override
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize) {
        return null;
    }
}
