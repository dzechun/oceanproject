package com.fantechs.provider.wanbao.api.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.wanbao.api.entity.MiddleOutDeliveryOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MiddleOutDeliveryOrderMapper extends MyMapper<MiddleOutDeliveryOrder> {

    void setPolicy();

    /**
     * 万宝-出库订单信息查询
     * @return
     */
    List<MiddleOutDeliveryOrder> findOutDeliveryData(Map<String, Object> map);

    /**
     * 万宝-查询内销出库订单
     * @return
     */
    List<MiddleOutDeliveryOrder> findOutDeliveryDataFormIMS(Map<String, Object> map);

    /**
     * 插入中间库
     * @param order
     * @return
     */
    int save(MiddleOutDeliveryOrder order);
}