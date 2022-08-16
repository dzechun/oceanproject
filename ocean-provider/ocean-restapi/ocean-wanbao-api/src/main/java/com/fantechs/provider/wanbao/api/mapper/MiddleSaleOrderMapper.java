package com.fantechs.provider.wanbao.api.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.wanbao.api.entity.MiddleSaleOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MiddleSaleOrderMapper extends MyMapper<MiddleSaleOrder> {

    void setPolicy();

    /**
     * 万宝-销售订单信息查询
     * @return
     */
    List<MiddleSaleOrder> findSaleOrderData(Map<String, Object> map);

    /**
     * 万宝-IMS销售订单信息查询
     * @return
     */
    List<MiddleSaleOrder> findSaleOrderFormIMS(Map<String, Object> map);

    /**
     * 插入中间库
     * @param saleOrder
     * @return
     */
    int save(MiddleSaleOrder saleOrder);
}