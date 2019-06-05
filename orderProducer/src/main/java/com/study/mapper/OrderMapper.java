package com.study.mapper;

import com.study.model.OrderInfoModel;

/**
 * @author mengfanzhu
 * @Description:
 * @date 2019/6/4 21:56
 * @Version 1.0
 */
public interface OrderMapper {

    /**
     * 插入订单信息
     *
     * @param orderInfoModel
     * @return
     */
    Long insert(OrderInfoModel orderInfoModel);

    /**
     * 按照订单号查询订单信息
     *
     * @param orderNo
     * @return
     */
    OrderInfoModel getByOrderNo(String orderNo);
}
